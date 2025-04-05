// ChatRepository.java
package com.deeptalk.app.repository;

import com.deeptalk.app.model.ChatRequest;
import com.deeptalk.app.model.ChatResponse;
import com.deeptalk.app.network.ApiClient;
import com.deeptalk.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;

public class ChatRepository {

    public enum ApiType {
        DEEPSEEK,
        OPENAI
    }


    private final ApiService defaultApiService;

    public ChatRepository(ApiService apiService) {
        this.defaultApiService = apiService;
    }

    // (old method, only DeepSeek)封装 Retrofit 异步请求逻辑
    public void sendMessage(ChatRequest request, Callback<ChatResponse> callback) {
        Call<ChatResponse> call = defaultApiService.sendDeepSeekPrompt(request);
        call.enqueue(callback);
    }

    //
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
}
