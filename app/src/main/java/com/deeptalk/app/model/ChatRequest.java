package com.deeptalk.app.model;

import java.util.List;

// 构建发送给 API 的 JSON 请求体
public class ChatRequest {

    private String model;
    private List<Message> messages;

    public ChatRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    public static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
