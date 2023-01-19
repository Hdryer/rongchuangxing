package com.bindada.syscourse.common;

<<<<<<< HEAD
import com.bindada.syscourse.common.GlobalConstants;
=======
>>>>>>> e153035 (融创星项目服务端code第一版)
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {
    private long code;
    private String message;
    private T data;

    public ApiResponse(int code) {
        this.code = code;
    }

    /**
     * 请求成功
     */
    public static ApiResponse success(String message) {
        return new ApiResponse(200, message, null);
    }

    /**
     * 请求成功
     */
    public static ApiResponse success(Object data) {
        return new ApiResponse(200, "成功", data);
    }

    /**
     * 请求成功
     */
    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(200, message, data);
    }

    /**
     * 请求错误
     */
    public static ApiResponse error(String message) {
        return new ApiResponse(500, message, null);
    }

    /**
     * 请求失败
     */
    public static ApiResponse error(long code, String message) {
        return new ApiResponse(code, message, null);
    }

    /**
     * 请求失败
     */
    public static ApiResponse fail(String message) {
<<<<<<< HEAD
        return new ApiResponse(GlobalConstants.FAILD, message, null);
=======
        return new ApiResponse(1000, message, null);
>>>>>>> e153035 (融创星项目服务端code第一版)
    }

    /**
     * 请求失败
     */
    public static ApiResponse fail(int code) {
        return new ApiResponse(code);
    }

    /**
     * 请求失败
     */
    public static ApiResponse fail(int code, String message) {
        return new ApiResponse(code, message, null);
    }

    /**
     * 请求失败
     */
    public static ApiResponse fail(Object data) {
<<<<<<< HEAD
        return new ApiResponse(GlobalConstants.FAILD, "失败", data);
=======
        return new ApiResponse(1000, "失败", data);
>>>>>>> e153035 (融创星项目服务端code第一版)
    }
}
