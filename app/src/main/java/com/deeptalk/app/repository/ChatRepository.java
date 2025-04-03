// ChatRepository.java
package com.deeptalk.app.repository;

import com.deeptalk.app.model.ChatRequest;
import com.deeptalk.app.model.ChatResponse;
import com.deeptalk.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;

public class ChatRepository {

    private final ApiService apiService;

    public ChatRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    // 封装 Retrofit 异步请求逻辑
    public void sendMessage(ChatRequest request, Callback<ChatResponse> callback) {
        Call<ChatResponse> call = apiService.sendPrompt(request);
        call.enqueue(callback);
    }
}
