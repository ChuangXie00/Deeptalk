package com.deeptalk.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deeptalk.app.R;
import com.deeptalk.app.adapter.ChatAdapter;
import com.deeptalk.app.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

// network request
import com.deeptalk.app.network.ApiClient;
import com.deeptalk.app.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// model
import com.deeptalk.app.model.ChatRequest;
import com.deeptalk.app.model.ChatRequest.Message;
import com.deeptalk.app.model.ChatResponse;

// log
import android.util.Log;

// 控制用户输入 and 聊天消息的显示逻辑
public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;          // 聊天列表 UI
    private EditText editTextMessage;
    private Button buttonSend;
    private ChatAdapter chatAdapter;            // RecyclerView 适配器
    private List<ChatMessage> chatMessages;     // 聊天消息列表

    @Override
    // 生命周期入口
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 初始化视图 绑定控件到自定义的布局格式
        recyclerView = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        // 初始化聊天数据和列表
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        // 发送按钮点击事件
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput = editTextMessage.getText().toString().trim();
                if (!userInput.isEmpty()) {
                    // 添加用户消息到聊天界面
                    chatMessages.add(new ChatMessage(userInput, true));

                    // 调用 DeepSeek API 发送请求
                    sendMessageToDeepSeek(userInput);

                    // 清空输入框
                    editTextMessage.setText("");

                    // 添加假装 AI 回复
//                    chatMessages.add(new ChatMessage("AI: Hello! I'm DeepTalk 🤖", false));
//
//                    // 通知刷新列表
//                    chatAdapter.notifyItemRangeInserted(chatMessages.size() - 2, 2);
//                    recyclerView.scrollToPosition(chatMessages.size() - 1);
//
//                    // 清空输入框
//                    editTextMessage.setText("");
                }
            }
        });

    }

    // 添加消息 and 刷新UI
    private void addChatMessage(ChatMessage message) {
        chatMessages.add(message);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        recyclerView.scrollToPosition(chatMessages.size() - 1);
    }

    private void sendMessageToDeepSeek(String userInput) {
        List<ChatRequest.Message> messages = new ArrayList<>();
        messages.add(new ChatRequest.Message("user", userInput));
        ChatRequest request = new ChatRequest("deepseek-chat", messages);

        ApiService apiService = ApiClient.getApiService(); // 获得 retrofit2 的单例示例

        Call<ChatResponse> call = apiService.getChatResponse(request);
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body()!= null) {
                    String aiReply = response.body().getChoices()
                            .get(0).getMessage().getContent();
                    addChatMessage(new ChatMessage(aiReply, false));
                } else {
                    Log.e("API","Response error or empty");
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                Log.e("","Requset failed:" + t.getMessage());
            }
        });
    }
}
