package com.deeptalk.app.network;

// 接口类
// Retrofit 会自动生成它的实现类。
// 所有要请求的 API 都会在这个接口中定义


import com.deeptalk.app.model.ChatRequest;
import com.deeptalk.app.model.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    // 向 DeepSeek API 发送 POST 请求
    @POST("v1/chat/completions")
    Call<ChatResponse> sendPrompt(@Body ChatRequest request);
}
