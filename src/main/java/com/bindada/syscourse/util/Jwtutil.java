package com.bindada.syscourse.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

/**
*   @Description  JwtUtil的工具类
*   @Author  bindada  2022/7/10 21:12
*/
public class Jwtutil {

    private static final String SING = "!AFGYO8*$@%";    //服务器签名，自定义

    private static final long EXPIRE_TIME = 120*60*1000; //毫秒级，2个小时

    /**
    *   生成token   格式：header.payload.sing
    */
    public static String getToken(Map<String,String> map){
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);        //过期时间
        JWTCreator.Builder builder = JWT.create();
        //payload
        map.forEach((k,v)->{
            builder.withClaim(k,v);    //遍历存储payload信息
        });
        String token = builder.withExpiresAt(date).sign(Algorithm.HMAC256(SING));  //设置令牌时间和签名加密
        return token;
    }


    /**
    *   验证token合法性
    */
    public static DecodedJWT verify(String token){
        //注意：验证时候一定要和生成时的 算法 和 签名SING 一致。
            return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }

    /**
    *   获得token中的信息
    */
    public static DecodedJWT getTokenInfo(String token){
        DecodedJWT decode = JWT.decode(token);
        return decode;
    }
}