package com.bindada.syscourse.common.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.bindada.syscourse.common.PredefinedMediaType;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.util.HashMap;

public class TemplateMessage {
    /**
     * 用于装载模板消息的数据
     */
    public static class TemplateData {
        @JSONField(name = "value")
        public String value;
        @JSONField(name = "color")
        public String color;

        public TemplateData(String value, String color) {
            this.value = value;
            this.color = color;
        }

        public TemplateData(String value) {
            this(value, "#173177");
        }
    }

    /**
     * 模板消息的基类
     */
    public static class BaseMessage {

        @JSONField(name = "touser")
        public String receiverOpenID;

        @JSONField(name = "template_id")
        public String templateID;

        @JSONField(name = "url")
        public String url;

        @JSONField(name = "client_msg_id")
        public String messageID;

        @JSONField(name = "data")
        public HashMap<String, TemplateData> data;

        public BaseMessage(String toUser, String templateID, String detailUrl, HashMap<String, TemplateData> data) {
            this.receiverOpenID = toUser;
            this.templateID = templateID;
            this.url = detailUrl;
            this.data = data;
        }

        public BaseMessage(String toUser, String templateID, HashMap<String, TemplateData> data) {
            this(toUser, templateID, null, data);
        }

        public RequestBody toRequestBody() {
            this.generateMessageID();
            return RequestBody.create(JSON.toJSONBytes(this), MediaType.parse(PredefinedMediaType.APPLICATION_JSON));
        }

        private void generateMessageID() {
            String templateIDHash = Integer.toHexString(templateID.hashCode());
            String dataHash = Integer.toHexString(data.hashCode());
            this.messageID = Integer.toHexString((templateIDHash + dataHash).hashCode());
        }
    }

    public static class FinishClassMessage extends BaseMessage {
        /**
         * 完成课程（消课）的模板消息
         *
         * @param toUser      接收者的 OpenID
         * @param detailUrl   详情页的 URL
         * @param classType   课程类型
         * @param classDetail 课程详情
         * @param classTime   课程时间，24小时制时间格式（支持+年月日），支持填时间段，两个时间点之间用“~”符号连接，例如：15:01，或：2019年10月1日 15:01
         * @param teacher     教师
         * @param remains     剩余课时
         */
        public FinishClassMessage(String toUser, String detailUrl, String classType, String classDetail, String classTime, String teacher, int remains) {
            this(toUser, detailUrl, classType, classDetail, classTime, "您好，您的孩子完成了一节课", teacher, remains);
        }

        /**
         * 完成课程（消课）的模板消息，该构造函数提供了标题下第一句话的自定义功能，20 字以内
         *
         * @param toUser       接收者的 OpenID
         * @param detailUrl    详情页的 URL
         * @param classType    课程类型
         * @param classDetail  课程详情
         * @param classTime    课程时间，24小时制时间格式（支持+年月日），支持填时间段，两个时间点之间用“~”符号连接，例如：15:01，或：2019年10月1日 15:01
         * @param introduction 标题下第一句话，20 字以内
         * @param teacher      教师
         * @param remains      剩余课时
         */
        public FinishClassMessage(String toUser, String detailUrl, String classType, String classDetail, String classTime, String introduction, String teacher, int remains) {
            super(toUser, "t4ytQt9GQOuVMPlik5wMOz4qIbUNrMQvZv99zWklgGE", detailUrl, null);

            HashMap<String, TemplateData> data = new HashMap<>();
            data.put("thing01", new TemplateData(introduction));
            data.put("thing02", new TemplateData(classType));
            data.put("thing03", new TemplateData(classDetail));
            data.put("thing04", new TemplateData(teacher));
            data.put("time", new TemplateData(classTime));
            data.put("number", new TemplateData(String.valueOf(remains)));

            this.data = data;
        }
    }

    protected static class Response {
        @JSONField(name = "errcode")
        public int errorCode;
        @JSONField(name = "errmsg")
        public String errorMessage;
        @JSONField(name = "msgid")
        public String messageID;
    }
}
