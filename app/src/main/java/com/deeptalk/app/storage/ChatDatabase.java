package com.deeptalk.app.storage;

import androidx.room.Database;
import androidx.room.RoomDatabase;


// Room 数据库类，数据库的主入口
// 连接 Entity（数据结构）和 DAO（操作接口）
@Database(entities = {ChatMessageEntity.class}, version = 1, exportSchema = false)
public abstract class ChatDatabase extends RoomDatabase{

    // DAO实例（Room 会自动实现）
    public abstract ChatDao chatDao();
}
