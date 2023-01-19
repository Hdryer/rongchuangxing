package com.bindada.syscourse.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weixin")
@Data
public class WeixinProperties {
    private String appID;
    private String appsecret;
    private String token;
    private String aesKey;

    @Override
    public String toString() {
        return String.format("AppID: %s, AppSecret: %s, Token: %s, AESKey: %s", getAppID(), getAppsecret(), getToken(), getAesKey());
    }
}
