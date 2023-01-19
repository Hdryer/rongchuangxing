package com.bindada.syscourse.common.weixin;

import java.util.HashMap;
import java.util.Map;

public class EventHandler {
    static final Map<String, EventHandlerInterface> eventHandlers = new HashMap<>();

    public static void registerEventHandler(String eventType, EventHandlerInterface eventHandler) {
        eventHandlers.put(eventType, eventHandler);
    }

    public static EventHandlerInterface getEventHandler(String eventType) {
        return eventHandlers.get(eventType);
    }

    public static class Events {
        public static final String QRCODE_SCAN = "QRCODE_SCAN";
    }

    public interface EventHandlerInterface {
        void onEventReceived(Map<String, String> data);
    }

    public static abstract class QRCodeScanEventHandler implements EventHandlerInterface {
        @Override
        public void onEventReceived(Map<String, String> data) {
            String event = data.get("Event");
            String eventKey = data.get("EventKey");
            if (event.equals("subscribe")) {
                eventKey = eventKey.substring("qrscene_".length());
            }
            this.onScan(data.get("FromUserName"), eventKey, data.get("Ticket"));
        }

        /**
         * 该方法在用户扫描带参数的二维码时被调用
         *
         * @param openID   用户的 OpenID
         * @param sceneKey 二维码的场景值
         * @param ticket   换取二维码图片的 ticket
         */
        public abstract void onScan(String openID, String sceneKey, String ticket);
    }

}
