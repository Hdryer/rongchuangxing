package com.bindada.syscourse.interceptors;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bindada.syscourse.dto.UserDTO;
import com.bindada.syscourse.util.Jwtutil;
import com.bindada.syscourse.util.RedisUtil;
import com.bindada.syscourse.util.UserThreadLocal;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        try{
            String token = request.getHeader("token");
            log.info("token:{}",token);
            Jwtutil.verify(token);//验证令牌
            if(!RedisUtil.redisTemplate.hasKey(token)){
                throw new NullPointerException("无token信息，请先登录");
            }
            Object userJson = RedisUtil.redisTemplate.opsForValue().get(token);
            UserDTO userDTO = JSON.parseObject((String) userJson, UserDTO.class);
            UserThreadLocal.put(userDTO);
            return true;          //放行请求
        }catch (SignatureVerificationException e){
            map.put("message","token签名无效");
            map.put("code",401);
        }catch (AlgorithmMismatchException e){
            map.put("message","算法不一致");
            map.put("code",402);
        }catch (TokenExpiredException e){
            map.put("message","token已过期，请重新登录");
            map.put("code",403);
        }catch (NullPointerException e){
            map.put("message","无token信息，请先登录");
            map.put("code",404);
        }catch (Exception e){
            map.put("message","无效token");
            map.put("code",405);
        }
        map.put("state",false);   //设置状态

        //将map转为 json -- jackson（spring默认的json ObjectMapper().writeValue）;
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);      //返回给前端
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
