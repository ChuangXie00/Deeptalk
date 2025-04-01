package com.deeptalk.app.network;

import com.deeptalk.app.model.*;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface ApiService {
    @POST("0198a8f6-d05f-4a91-8d8c-002b57aa8ff0") // 模拟接口路径（后续替换为真实）
    Call<ChatResponse> getMockResponse();

}
