package com.deeptalk.app.model;

import java.util.List;

// 接收 API 返回的数据结构
public class ChatResponse {
    private List<Choice> choices;

    public List<Choice> getChoices() {
        return choices;
    }

    public static class Choice {
        private Message message;

        public Message getMessage() {
            return message;
        }
        public void setMessage(Message message) {
            this.message = message;
        }
    }

    public static class Message {
        private String role;
        private String content;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
