package com.deeptalk.app.network;

import com.deeptalk.app.model.*;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.GET;

// 接口类
// Retrofit 会自动生成它的实现类。
// 所有要请求的 API 都会在这个接口中定义
public interface ApiService {

    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer sk-65b8584834a04d878285b4a39b49f576" // Bearer后接DeepSeek API Key
    })
    // 向 "v1/chat/completions" 发送一个 HTTP POST请求
    // 双引号内是请求的路径，会拼接在 ApiClient.java 的 BASE_URL 后面
    @POST("v1/chat/completions") // 模拟接口路径（后续替换为真实）
    // Call<T> 是 Retrofit 异步请求的响应体，这个方法的返回值是一个 异步网络请求的对象
    // T替换成我的模型类 ChatResponse, 接收JSON数据
    Call<ChatResponse> getChatResponse(@Body ChatRequest request);

}
