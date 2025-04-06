package com.deeptalk.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deeptalk.app.R;
import com.deeptalk.app.adapter.ChatAdapter;
import com.deeptalk.app.model.ChatMessage;
import com.deeptalk.app.repository.ChatRepository;
import com.deeptalk.app.storage.ChatDao;
import com.deeptalk.app.viewmodel.ChatViewModel;
import com.deeptalk.app.MainApplication;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;          // 聊天列表 UI
    private EditText editTextMessage;
    private Button buttonSend;
    private Spinner spinnerApi;                 // 选择模型的spinner

    private ChatAdapter chatAdapter;            // RecyclerView 适配器
    private ChatViewModel chatViewModel;        // 引入 ViewModel

    // current api(default:DeepSeek)
    private ChatRepository.ApiType selectedApiType = ChatRepository.ApiType.DEEPSEEK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        spinnerApi = findViewById(R.id.spinner_api);

        // 手动创建ViewModel, 传入ChatDAO
        ChatDao dao = MainApplication.chatDatabase.chatDao();
        chatViewModel = new ChatViewModel(dao);

        // 初始化适配器（先传空列表）
        chatAdapter = new ChatAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        // 设置spinner的数据源
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.api_type_list,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerApi.setAdapter(adapter);

        // 监听Spinner的选择变化
        spinnerApi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedApiType = (position == 0)
                        ? ChatRepository.ApiType.DEEPSEEK
                        : ChatRepository.ApiType.OPENAI;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 默认使用 DeepSeek
                selectedApiType = ChatRepository.ApiType.DEEPSEEK;
            }
        });


        // 监听ViewModel中的消息LiveData，更新 RecyclerView(UI)
        chatViewModel.getMessages().observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> updatedMessages) {
                chatAdapter.submitList(new ArrayList<>(updatedMessages)); // 创建副本替换旧数据, 防止diff错乱
                recyclerView.scrollToPosition(updatedMessages.size() - 1); // 滚动到底部
            }
        });

        // init loading history messages
        chatViewModel.loadHistory();

        // 发送按钮监听，调用 ViewModel 处理发送逻辑
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput = editTextMessage.getText().toString().trim();
                if (!userInput.isEmpty()) {
                    chatViewModel.sendMessage(userInput, selectedApiType); // 交给 ViewModel 处理
                    editTextMessage.setText("");          // 清空输入框
                }
            }
        });
    }
}
