package com.bindada.syscourse.common.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.bindada.syscourse.common.PredefinedMediaType;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.util.HashMap;
import java.util.Map;

public class QRCode {
    /**
     * 创建二维码的数据，分临时和永久
     *
     * <p>
     * 临时二维码的有效期为30天，最大值为2592000（即30天）。
     * 永久二维码目前有两种类型，分别为永久的整型参数值和永久的字符串参数值。
     * 场景参数可以是 int 也可以是 String，但是 int 类型的场景参数值不能大于100000（目前参数只支持1--100000）
     * </p>
     * <p>
     * <p>
     * 临时二维码见 {@link CreateData#CreateData(int, int)} 和 {@link CreateData#CreateData(int, String)}
     * </p>
     * <p>
     * 永久二维码见 {@link CreateData#CreateData(int)} 和 {@link CreateData#CreateData(String)}
     * </p>
     */
    public static class CreateData {
        @JSONField(name = "expire_seconds")
        public int expires;
        @JSONField(name = "action_name")
        public final String actionName;
        @JSONField(name = "action_info")
        public final Map<String, Map<String, Object>> actionInfo = new HashMap<>();

        public RequestBody toRequestBody() {
            return RequestBody.create(JSON.toJSONBytes(this), MediaType.parse(PredefinedMediaType.APPLICATION_JSON));
        }

        /**
         * 临时二维码
         *
         * @param expires 有效期，单位秒，最大值为2592000（即30天）
         * @param sceneId 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
         */
        public CreateData(int expires, int sceneId) {
            this.expires = expires;
            this.actionName = "QR_SCENE";
            this.actionInfo.put("scene", new HashMap<String, Object>() {{
                put("scene_id", sceneId);
            }});
        }

        /**
         * 临时二维码
         *
         * @param expires  有效期，单位秒，最大值为2592000（即30天）
         * @param sceneStr 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
         */
        public CreateData(int expires, String sceneStr) {
            this.expires = expires;
            this.actionName = "QR_STR_SCENE";
            this.actionInfo.put("scene", new HashMap<String, Object>() {{
                put("scene_str", sceneStr);
            }});
        }

        /**
         * 永久二维码，是无过期时间的，但数量较少（目前为最多10万个）。永久二维码主要用于适用于账号绑定、用户来源统计等场景。
         *
         * @param sceneId 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
         */
        public CreateData(int sceneId) {
            this.actionName = "QR_LIMIT_SCENE";
            this.actionInfo.put("scene", new HashMap<String, Object>() {{
                put("scene_id", sceneId);
            }});
        }

        /**
         * 永久二维码，是无过期时间的，但数量较少（目前为最多10万个）。永久二维码主要用于适用于账号绑定、用户来源统计等场景。
         *
         * @param sceneStr 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
         */
        public CreateData(String sceneStr) {
            this.actionName = "QR_LIMIT_STR_SCENE";
            this.actionInfo.put("scene", new HashMap<String, Object>() {{
                put("scene_str", sceneStr);
            }});
        }
    }
}
