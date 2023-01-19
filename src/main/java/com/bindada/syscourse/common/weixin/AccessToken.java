package com.bindada.syscourse.common.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

// 这个类用于管理微信的 access_token
@Component
public class AccessToken {
    private static final Logger logger = LoggerFactory.getLogger(AccessToken.class);

    private static String configAppID;
    private static String configAppSecret;

    @Value("${weixin.appID}")
    public void setAppID(String appID) {
        configAppID = appID;
    }

    public static String getAppID() {
        return configAppID;
    }

    @Value("${weixin.appsecret}")
    public void setAppSecret(String appsecret) {
        configAppSecret = appsecret;
    }

    public static String getAppSecret() {
        return configAppSecret;
    }

    private static OkHttpClient httpClient = null;
    private static HttpUrl url = null;

    private static HttpUrl getRequestURL() {
        if (url == null) {
            url = Objects.requireNonNull(HttpUrl.parse("https://api.weixin.qq.com/cgi-bin/token"))
                    .newBuilder()
                    .addQueryParameter("grant_type", "client_credential")
                    .addQueryParameter("appid", configAppID)
                    .addQueryParameter("secret", configAppSecret)
                    .build();
            logger.info("完成初始化请求地址：{}", url);
        }
        return url;
    }

    private static String accessToken = "";
    private static LocalDateTime expires = null;

    private static OkHttpClient getHttpClient() {
        if (httpClient == null) {
            // 懒加载
            httpClient = new OkHttpClient();
        }
        return httpClient;
    }

    // 验证当前 token 是否已过期，过期返回 false，反之
    private static boolean validateTokenExpires() {
        if (accessToken.isEmpty() || expires == null) {
            logger.info("access_token 与 expires 均为空，需要初始化。");
            return false;
        }

        if (expires.isBefore(LocalDateTime.now())) {
            logger.info("access_token 已过期。");
            return false;
        }
        return true;
    }

    private static void fetchAccessToken() throws Exception {
        Request request = new Request.Builder()
                .url(getRequestURL())
                .build();

        try (Response response = getHttpClient().newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            byte[] data = Objects.requireNonNull(response.body()).bytes();
            AccessTokenMessage message = JSON.parseObject(data, AccessTokenMessage.class);

            if (message.errorCode != 0) {
                throw new IOException(String.format("错误码：%d - 错误信息：%s", message.errorCode, message.errorMessage));
            }
            if (message.accessToken.isEmpty() || message.expiresIn == 0) {
                throw new IOException("序列化错误，接收到了不合理的响应。");
            }

            accessToken = message.accessToken;
            expires = LocalDateTime.now().plusSeconds(message.expiresIn);
            logger.info("获取成功 - access_token={}，expires={}", message.accessToken, message.expiresIn);
        }
    }

    /**
     * 刷新/获取微信接口 access_token，通过内部计时保证有效。
     *
     * @param forceRefresh 是否强制刷新 access_token，是为 true
     * @return 返回时效内的 access_token
     */
    public static String getAccessToken(boolean forceRefresh) {
        if (forceRefresh || !validateTokenExpires()) {
            try {
                fetchAccessToken();
            } catch (Exception e) {
                logger.error("刷新 access_token 错误：", e);
            }
        }
        return accessToken;
    }

    /**
     * 刷新/获取微信接口 access_token，通过内部计时保证有效。
     * 该方法是 {@link AccessToken#getAccessToken(boolean)} 强制刷新为 false 的快捷方式。
     *
     * @return 返回时效内的 access_token
     */
    public static String getAccessToken() {
        return getAccessToken(false);
    }

    protected static class AccessTokenMessage {
        @JSONField(name = "access_token")
        public String accessToken;

        @JSONField(name = "expires_in")
        public int expiresIn;

        @JSONField(name = "errcode")
        public int errorCode;

        @JSONField(name = "errmsg")
        public String errorMessage;
    }
}
