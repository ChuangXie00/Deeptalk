// com.deeptalk.app.viewmodel.ChatViewModel.java

package com.deeptalk.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.deeptalk.app.model.ChatMessage;
import com.deeptalk.app.model.ChatRequest;
import com.deeptalk.app.model.ChatResponse;
import com.deeptalk.app.network.ApiClient;
import com.deeptalk.app.network.ApiService;
import com.deeptalk.app.repository.ChatRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// 存储和管理聊天相关数据
public class ChatViewModel extends ViewModel {

    // 使用 LiveData 存储聊天消息列表, 便于通知界面刷新（观察 UI 变化）
    private final MutableLiveData<List<ChatMessage>> messages
            = new MutableLiveData<>(new ArrayList<>());

    // 数据访问层
    private final ChatRepository repository;

    public ChatViewModel() {
        // 初始化 Repository，注入 Retrofit 接口
        this.repository = new ChatRepository(ApiClient.getDeepSeekApiService());
    }

    // 暴露LiveData，供Activity/Fragment观察数据变化
    public LiveData<List<ChatMessage>> getMessages() {
        return messages;
    }

    // 处理用户发消息，先加入用户输入
    // 再发起 API 调用，通过 Repository 发起网络请求，然后将 AI 回复加入消息流中
    // 默认使用 DeepSeek
    public void sendMessage(String userText) {
        sendMessage(userText, ChatRepository.ApiType.DEEPSEEK);
    }

    // 支持选择API（DeepSeek/OpenAI）
    public void sendMessage(String userText, ChatRepository.ApiType type) {
        final List<ChatMessage> currentList = new ArrayList<>();
        List<ChatMessage> oldList = messages.getValue();
        if (oldList != null) {
            currentList.addAll(oldList);
        }

        // 添加用户消息
        ChatMessage userMessage = new ChatMessage(userText, true);
        currentList.add(userMessage);
        messages.setValue(currentList);

        // ✅ 根据类型选择模型名
        String modelName = (type == ChatRepository.ApiType.OPENAI) ? "gpt-3.5-turbo" : "deepseek-chat";

        // 构造 API 请求体
        ChatRequest request = ChatRequest.fromUserText(userText, modelName);

        // 调用 Repository 发起 API 请求（根据类型）
        repository.sendMessage(request, type, new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 构造 AI 回复消息
                    String reply = "No response";
                    if (response.body().getChoices() != null
                            && !response.body().getChoices().isEmpty()) {
                        reply = response.body().getChoices().get(0).getMessage().getContent();
                    }

                    ChatMessage aiMessage = new ChatMessage(reply, false);
                    currentList.add(aiMessage);
                    messages.postValue(currentList);  // 刷新 UI
                } else {
                    // 错误响应处理
                    ChatMessage errorMsg = new ChatMessage("AI response error", false);
                    currentList.add(errorMsg);
                    messages.postValue(currentList);
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                // 网络请求失败
                ChatMessage errorMsg = new ChatMessage("Network error: " + t.getMessage(), false);
                currentList.add(errorMsg);
                messages.postValue(currentList);
            }
        });
    }
}
