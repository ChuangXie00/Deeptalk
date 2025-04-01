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

    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private Button buttonSend;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    @Override
    // 生命周期入口
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 绑定控件到自定义的布局格式
        recyclerView = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        // 初始化聊天列表
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        // 发送按钮逻辑
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

        // test
        testSendMockRequest();
    }

    private void testSendMockRequest() {
        ApiService apiService = ApiClient.getApiService();

        apiService.getMockResponse().enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String reply = response.body().getChoices().get(0).getMessage().getContent();
                    Log.d("API_SUCCESS", "AI 回复内容：" + reply);
                } else {
                    Log.e("API_ERROR", "请求失败，响应码: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                Log.e("API_FAILURE", "请求异常: " + t.getMessage(), t);
            }
        });
    }
}
