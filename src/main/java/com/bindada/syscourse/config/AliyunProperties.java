package com.bindada.syscourse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AliyunProperties {
    private static String smsAccessKeyId;
    private static String smsAccessKeySecret;

    public static String getSmsAccessKeyId() {
        return smsAccessKeyId;
    }

    @Value("${aliyun.sms.AccessKeyId}")
    public void setSmsAccessKeyId(String smsAccessKeyId) {
        AliyunProperties.smsAccessKeyId = smsAccessKeyId;
    }

    public static String getSmsAccessKeySecret() {
        return smsAccessKeySecret;
    }

    @Value("${aliyun.sms.AccessKeySecret}")
    public void setSmsAccessKeySecret(String smsAccessKeySecret) {
        AliyunProperties.smsAccessKeySecret = smsAccessKeySecret;
    }
}
