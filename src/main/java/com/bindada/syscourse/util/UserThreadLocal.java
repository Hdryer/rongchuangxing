package com.bindada.syscourse.util;

import com.bindada.syscourse.dto.UserDTO;

/**
 * threadLocal 工具类
 * */
public class UserThreadLocal {

    private UserThreadLocal(){

    }
    private static final ThreadLocal<UserDTO> threadLocal = new ThreadLocal<>();

    public static void put(UserDTO userDTO){
        threadLocal.set(userDTO);
    }

    public static UserDTO get(){
        return threadLocal.get();
    }

    public static void remove(){
        threadLocal.remove();
    }
}

