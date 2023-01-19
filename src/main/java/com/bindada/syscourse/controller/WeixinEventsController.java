package com.bindada.syscourse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bindada.syscourse.common.ApiResponse;
import com.bindada.syscourse.common.PredefinedMediaType;
import com.bindada.syscourse.common.aliyun.SmsService;
import com.bindada.syscourse.common.weixin.Crypto;
import com.bindada.syscourse.common.weixin.EventHandler;
import com.bindada.syscourse.common.weixin.Interaction;
import com.bindada.syscourse.common.weixin.TemplateMessage;
import com.bindada.syscourse.config.WeixinProperties;
import com.bindada.syscourse.entity.Student;
import com.bindada.syscourse.service.StudentService;
import com.bindada.syscourse.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;


// 这是给张小龙的亲妈上坟用的
@RestController
@RequestMapping("/wx")
@Api(tags = "微信模块")
public class WeixinEventsController {

    private static final Logger logger = LoggerFactory.getLogger(WeixinEventsController.class);

    private final WeixinProperties config;

    public WeixinEventsController(WeixinProperties config) {
        this.config = config;
    }

    @GetMapping(value = "/test", produces = PredefinedMediaType.TEXT_PLAIN)
    public String sayHello(@RequestParam(value = "to", required = false) String toUser,
                           @RequestParam(value = "tpl", required = false) String templateID,
                           @RequestParam(value = "url", required = false) String url) {
        HashMap<String, TemplateMessage.TemplateData> map = new HashMap<>();
        map.put("phrase", new TemplateMessage.TemplateData("李奇"));
        if (toUser == null) toUser = "o0DR_5-aaR-WnG7u2WTd-QZ1PEEk";
        if (templateID == null) templateID = "Ssgg0rd6VMSGMv6VDg3fQv1D1LwjGFTdB7C0wgvJUes";
        if (url == null) url = "https://www.baidu.com";
        return "张小龙什么时候死？\n啊？\n结果：" + Interaction.NotifyTemplateMessage(new TemplateMessage.BaseMessage(toUser, templateID, url, map));
    }

    @GetMapping(value = "/portal", produces = PredefinedMediaType.TEXT_PLAIN)
    public String authorizeWeixinToken(@RequestParam(name = "signature", required = false) String signature,
                                       @RequestParam(name = "timestamp", required = false) String timestamp,
                                       @RequestParam(name = "nonce", required = false) String nonce,
                                       @RequestParam(name = "echostr", required = false) String echostr) {

        if (Crypto.verifySignature(signature, timestamp, nonce, config.getToken())) {
            logger.info("【微信接口验签】成功验证签名：{}", signature);
            return echostr;
        }

        logger.warn("【微信接口验签】验证请求失败：");
        return "verification failed";
    }

    @PostMapping(value = "/portal", produces = PredefinedMediaType.TEXT_PLAIN)
    public String handleWeixinEvents(@RequestBody HashMap<String, String> map) {

        String event = map.get("Event");
        if (event == null) {
            logger.warn("【微信事件】未知事件：{}", map);
            return null;
        }
        logger.debug("【微信事件】收到事件：{}", map);

        switch (event) {
            case "subscribe":
            case "SCAN":
                EventHandler.EventHandlerInterface handler = EventHandler.getEventHandler(EventHandler.Events.QRCODE_SCAN);
                if (handler != null) {
                    handler.onEventReceived(map);
                }
                break;
            default:
                logger.warn("【微信事件】未定义处理方式的事件：{}", map);
                break;
        }

        return null;
    }

    private static final String smsCodePrefix = "scode";
    private static final String smsCountPrefix = "scount";

    private static final RedisUtil redis = new RedisUtil();

    @Autowired
    private StudentService studentService;

    private long getTonightEndSeconds() {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    @GetMapping(value = "/sms", produces = PredefinedMediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取手机验证码")
    public ApiResponse getSMSCode(@RequestParam(name = "phone") String phoneNumber) {
        if (!Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", phoneNumber)) {
            return ApiResponse.fail(400, "手机号码格式错误");
        }

        String countKey = smsCountPrefix + phoneNumber;
        Object rawCount = redis.get(countKey);
        int count = rawCount != null ? (int) rawCount : 0;
        if (count >= 3) {
            return ApiResponse.fail(400, "今日获取短信验证码次数已达上限");
        }

        String code = String.format("%06d", (int) (Math.random() * 1000000));
        if (!SmsService.SendVerificationCode(phoneNumber, code)) {
            return ApiResponse.fail(500, "短信发送失败");
        }
        redis.set(smsCodePrefix + phoneNumber, code, 60 * 5);
        if (count == 0) redis.set(countKey, count + 1, getTonightEndSeconds());
        else redis.incr(countKey, 1);
        return ApiResponse.success("短信验证码已发送");
    }

    @PostMapping(value = "/binding", produces = PredefinedMediaType.APPLICATION_JSON)
    @ApiOperation(value = "绑定手机号")
    public ApiResponse handleStudentBinding(@RequestParam(name = "phone") String phoneNumber,
                                            @RequestParam(name = "sms") String smsCode,
                                            @RequestParam(name = "openid") String openid) {
        String phoneKey = smsCodePrefix + phoneNumber;
        String smsCodeInRedis = String.valueOf(redis.get(phoneKey));
        if (!smsCodeInRedis.equals(smsCode)) {
            return ApiResponse.fail(400, "验证码错误");
        }

        redis.delete(phoneKey);
        if (studentService.bindAppID(phoneNumber, openid)) {
            return ApiResponse.success("绑定成功");
        } else {
            return ApiResponse.fail(500, "绑定失败");
        }
    }

    @GetMapping(value = "/binding-state")
    @ApiOperation(value = "判断家长是否绑定并进行跳转")
    public RedirectView getBindingState(@RequestParam(name = "code") String wxCode) {
        String openid = Interaction.getOpenIDByOAuth(wxCode);
        if (openid == null)
            return null;

        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Student::getAppId, openid);
        Student student = studentService.getOne(queryWrapper);
        if (student == null)
            return new RedirectView("http://rongchuangxing.cn:8080/binding?openid=" + openid);
        else
            return new RedirectView("http://rongchuangxing.cn:8080/bound");
    }
}
