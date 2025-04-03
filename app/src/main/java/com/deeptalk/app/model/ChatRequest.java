package com.deeptalk.app.model;

import java.util.ArrayList;
import java.util.List;

// 构建发送给 API 的 JSON 请求体
public class ChatRequest {

    private String model;
    private List<Message> messages;

    public ChatRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    // 静态工厂方法，用于ViewModel中的快速狗仔请求
    public static ChatRequest fromUserText(String userText) {
        List<Message> messageList = new ArrayList<>();
        messageList.add(new Message("user", userText));

        return new ChatRequest("deepseek-chat", messageList);
    }

    public String getModel() {
        return model;
    }
    public List<Message> getMessages() {
        return messages;
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
