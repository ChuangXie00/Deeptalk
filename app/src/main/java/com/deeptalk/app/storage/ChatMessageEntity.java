package com.deeptalk.app.storage;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.deeptalk.app.model.ChatMessage;

// message storage entity for Room DB

@Entity(tableName = "chat_messages")
public class ChatMessageEntity {

    @PrimaryKey(autoGenerate = true)
    public int id; // 自动递增的主键

    public String content;  // 聊天内容
    public boolean isUser;  // 是否为用户
    public long timestamp;  // 排序用

    public String model;    // 所属模型(Deep / ChatGPT)

    public ChatMessageEntity() {}
    public ChatMessageEntity(String content, boolean isUser, long timestamp, String model) {
        this.content = content;
        this.isUser = isUser;
        this.timestamp = timestamp;
        this.model = model;
    }
}
