package com.deeptalk.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deeptalk.app.adapter.ChatAdapter;
import com.deeptalk.app.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private Button buttonSend;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 1. 绑定控件
        recyclerView = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        // 2. 初始化聊天列表
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        // 3. 发送按钮逻辑
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput = editTextMessage.getText().toString().trim();
                if (!userInput.isEmpty()) {
                    // 添加用户消息
                    chatMessages.add(new ChatMessage(userInput, true));
                    // 添加假装 AI 回复
                    chatMessages.add(new ChatMessage("AI: Hello! I'm DeepTalk 🤖", false));

                    // 通知刷新列表
                    chatAdapter.notifyItemRangeInserted(chatMessages.size() - 2, 2);
                    recyclerView.scrollToPosition(chatMessages.size() - 1);

                    // 清空输入框
                    editTextMessage.setText("");
                }
            }
        });
    }
}
