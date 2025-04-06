package com.deeptalk.app;

import android.app.Application;

import androidx.room.Room;
import com.deeptalk.app.storage.ChatDatabase;
public class MainApplication extends Application{
    public static ChatDatabase chatDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        // init Room DB
        chatDatabase = Room.databaseBuilder(
                getApplicationContext(),
                ChatDatabase.class,
                "chat_database"
        ).build();
    }
}
