package com.bindada.syscourse.controller;

import com.alibaba.fastjson.JSON;
import com.bindada.syscourse.common.ApiResponse;
import com.bindada.syscourse.dto.UserDTO;
import com.bindada.syscourse.entity.Teacher;
import com.bindada.syscourse.service.TeacherService;
import com.bindada.syscourse.util.Jwtutil;
import com.bindada.syscourse.util.RedisUtil;
import com.bindada.syscourse.util.UserThreadLocal;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Api(tags = "用户模块")
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/login")
    @ApiOperation("登录")
    public ApiResponse login(String account, String password){
        try {
            //判断登录
            Teacher teacher= teacherService.login(account, password);

            //生成token
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("accont",account);
            hashMap.put("password", DigestUtils.md5Hex(UUID.randomUUID().toString()+password));
            String token = Jwtutil.getToken(hashMap);

            //将登录信息存进redis
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(teacher,userDTO);
            String userJson = JSON.toJSONString(userDTO);
            redisUtil.set(token,userJson,7200);

            Map<String, Object> map = new HashMap<>();
            map.put("userDTO",userDTO);
            map.put("token",token);
            return ApiResponse.success("登录成功",map);
        } catch (Exception e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @PostMapping("/loginOut")
    @ApiOperation("注销")
    public ApiResponse loginOut(HttpServletRequest request){
        String token = request.getHeader("token");
        redisUtil.delete(token);
        return ApiResponse.success("注销成功");
    }

    @PutMapping("/updatePwd")
    @ApiOperation("修改密码")
    public ApiResponse updatePwd(HttpServletRequest request,String id,String password){
        String token = request.getHeader("token");
        teacherService.updatePwd(id,password);
        redisUtil.delete(token);
        return ApiResponse.success("修改密码成功,请重新登录");
    }

    @ApiOperation("获取所有教师ID")
    @GetMapping("/getTeacherId")
    public ApiResponse getAllTeacherID(){
        List<String> teacherIDS = teacherService.getTeacherAccountName();
        return ApiResponse.success(teacherIDS);
    }

    @ApiOperation(value = "获取本地user信息")
    @GetMapping("/getLocalUser")
    public ApiResponse getLocalUser(){
        return ApiResponse.success("获取成功",UserThreadLocal.get());
    }

}
