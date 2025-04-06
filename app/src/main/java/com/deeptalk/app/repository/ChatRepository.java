// ChatRepository.java
package com.deeptalk.app.repository;

import com.deeptalk.app.model.ChatMessage;
import com.deeptalk.app.model.ChatRequest;
import com.deeptalk.app.model.ChatResponse;
import com.deeptalk.app.network.ApiClient;
import com.deeptalk.app.network.ApiService;
import com.deeptalk.app.storage.ChatDao;
import com.deeptalk.app.storage.ChatMessageEntity;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;

public class ChatRepository {

    public enum ApiType {
        DEEPSEEK,
        OPENAI
    }

    private final ApiService defaultApiService;

    private final ChatDao chatDao;

    public ChatRepository(ApiService apiService, ChatDao chatDao) {
        this.defaultApiService = apiService;
        this.chatDao = chatDao;
    }

    // (old method, only DeepSeek)封装Retrofit异步请求逻辑
    public void sendMessage(ChatRequest request, Callback<ChatResponse> callback) {
        Call<ChatResponse> call = defaultApiService.sendDeepSeekPrompt(request);
        call.enqueue(callback);
    }

    // Retrofit异步请求逻辑 with api type
    public void sendMessage(ChatRequest request, ApiType type, Callback<ChatResponse> callback) {
        ApiService selectedService;
        if (type == ApiType.OPENAI) {
            selectedService = ApiClient.getOpenAIApiService();
            selectedService.sendOpenAIPrompt(request).enqueue(callback);
        } else if (type == ApiType.DEEPSEEK) {
            selectedService = ApiClient.getDeepSeekApiService();
            selectedService.sendDeepSeekPrompt(request).enqueue(callback);
        }
    }

    public void saveMessage(ChatMessageEntity entity) {
        new Thread(() -> chatDao.insert(entity)).start();
    }
    public void getAllMessages(Consumer<List<ChatMessageEntity>> callback){
        new Thread(() -> {
            List<ChatMessageEntity> result = chatDao.getAllMessages();
            callback.accept(result);
        }).start();
    }
}
