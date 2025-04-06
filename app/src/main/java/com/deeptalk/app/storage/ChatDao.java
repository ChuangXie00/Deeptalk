package com.deeptalk.app.storage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.deeptalk.app.model.ChatMessage;

import java.util.List;


//  Interface: Data Access Object, 聊天消息的数据库操作接口

@Dao
public interface ChatDao {
    @Insert
    void insert(ChatMessageEntity message);

    // Query all messages by timestamp(升序 ASC)
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    List<ChatMessageEntity> getAllMessages();

    // Delete all messages record
    @Query("DELETE FROM chat_messages")
    void clearAll();
}
