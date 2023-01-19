package com.bindada.syscourse.common.aliyun;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.bindada.syscourse.config.AliyunProperties;
import org.slf4j.Logger;

public class SmsService {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SmsService.class);
    private static Client client = null;

    private static Client createClient(String keyId, String keySecret) {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(keyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(keySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";

        Client client = null;
        try {
            client = new com.aliyun.dysmsapi20170525.Client(config);
        } catch (Exception e) {
            logger.error("创建短信客户端失败", e);
        }
        return client;
    }

    private static Client getClient() {
        if (client == null) {
            SmsService.client = createClient(AliyunProperties.getSmsAccessKeyId(), AliyunProperties.getSmsAccessKeySecret());
        }
        return SmsService.client;
    }

    public static boolean SendVerificationCode(String phoneNumber, String code) {
        SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName("阿里云短信测试")
                .setTemplateCode("SMS_154950909")
                .setPhoneNumbers(phoneNumber)
                .setTemplateParam(String.format("{\"code\":\"%s\"}", code));

        try {
            getClient().sendSms(sendSmsRequest);
            logger.info(String.format("【阿里云验证码】给 %s 发送短信成功", phoneNumber));
        } catch (Exception e) {
            logger.error(String.format("【阿里云验证码】给 %s 发送短信失败：", phoneNumber), e);
            return false;
        }
        return true;
    }
}
