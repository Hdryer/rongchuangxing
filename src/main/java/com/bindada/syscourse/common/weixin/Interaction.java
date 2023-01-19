package com.bindada.syscourse.common.weixin;

import com.alibaba.fastjson.JSON;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Objects;

public class Interaction {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Interaction.class);
    private static final HttpUrl templateMessageUrl = HttpUrl.parse("https://api.weixin.qq.com/cgi-bin/message/template/send");
    private static final HttpUrl createQRCodeUrl = HttpUrl.parse("https://api.weixin.qq.com/cgi-bin/qrcode/create");
    private static final OkHttpClient httpClient = new OkHttpClient();

    private static HttpUrl getAccessTokenUrl(HttpUrl baseUrl) {
        return baseUrl.newBuilder().
                addQueryParameter("access_token", AccessToken.getAccessToken()).
                build();
    }

    /**
     * 发送模板消息
     *
     * @param data 模板消息类，包含了模板消息的所有信息，必须是继承自 {@link com.bindada.syscourse.common.weixin.TemplateMessage.BaseMessage} 的类
     * @return 返回发送消息的结果，如果发送成功，则返回 true，否则返回 false
     */
    public static <T extends TemplateMessage.BaseMessage> boolean NotifyTemplateMessage(@NotNull T data) {

        assert templateMessageUrl != null;
        HttpUrl url = getAccessTokenUrl(templateMessageUrl);

        Request request = new Request.Builder().
                url(url).
                post(data.toRequestBody()).
                build();

        try (Response response = httpClient.newCall(request).execute()) {
            byte[] body = Objects.requireNonNull(response.body()).bytes();
            TemplateMessage.Response messageResponse = JSON.parseObject(body, TemplateMessage.Response.class);
            if (messageResponse.errorCode == 0 && messageResponse.errorMessage.equals("ok")) {
                logger.info("给 {} 的微信模板消息发送成功：{}", data.receiverOpenID, messageResponse.messageID);
                return true;
            } else {
                logger.error("给 {} 的微信模板消息发送失败：[{}]{}", data.receiverOpenID, messageResponse.errorCode, messageResponse.errorMessage);
                return false;
            }
        } catch (Exception e) {
            logger.error("发送微信模板消息失败：", e);
            return false;
        }
    }

    /**
     * 创建二维码，获取二维码 ticket 后，开发者可用 ticket 换取二维码图片。使用方法如下：
     * <pre>
     *      HTTP GET请求（请使用 https 协议）https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET
     *      提醒：TICKET记得进行UrlEncode。请注意，本接口无须登录态即可调用。
     * </pre>
     *
     * @param data 二维码创建数据，见 {@link QRCode.CreateData}
     * @return 二维码 ticket，如果创建失败，则返回 null
     */
    public static String getQRCodeTicket(QRCode.CreateData data) {

        assert createQRCodeUrl != null;
        HttpUrl url = getAccessTokenUrl(createQRCodeUrl);

        Request request = new Request.Builder().
                url(url).
                post(data.toRequestBody()).
                build();

        try (Response response = httpClient.newCall(request).execute()) {
            byte[] body = Objects.requireNonNull(response.body()).bytes();
            Map<String, String> result = JSON.parseObject(body, Map.class);
            if (result.containsKey("ticket") || result.containsKey("url") || result.containsKey("expire_seconds")) {
                logger.info("创建二维码成功：{}", result);
                return result.get("ticket");
            }
            logger.error("创建二维码失败：{}", result);
        } catch (Exception e) {
            logger.error("创建二维码失败：", e);
        }
        return null;
    }


    private static final HttpUrl getOAuthOpenIDUrl = HttpUrl.parse("https://api.weixin.qq.com/sns/oauth2/access_token");

    public static String getOpenIDByOAuth(String code) {
        assert getOAuthOpenIDUrl != null;

        HttpUrl url = getOAuthOpenIDUrl.newBuilder().
                addQueryParameter("appid", AccessToken.getAppID()).
                addQueryParameter("secret", AccessToken.getAppSecret()).
                addQueryParameter("code", code).
                addQueryParameter("grant_type", "authorization_code").
                build();

        Request request = new Request.Builder().
                url(url).
                get().
                build();

        try (Response response = httpClient.newCall(request).execute()) {
            byte[] body = Objects.requireNonNull(response.body()).bytes();
            Map<String, String> result = JSON.parseObject(body, Map.class);
            if (result.containsKey("openid")) {
                logger.info("获取用户 OpenID 成功：{}", result);
                return result.get("openid");
            }
            logger.error("获取用户 OpenID 失败：{}", result);
        } catch (Exception e) {
            logger.error("获取用户 OpenID 失败：", e);
        }

        return null;
    }
}
