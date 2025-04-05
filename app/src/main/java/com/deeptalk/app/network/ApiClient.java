package com.deeptalk.app.network;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ApiClient {

    // config of DeepSeek
    private static final String DEEPSEEK_BASE_URL = "https://api.deepseek.com/";
    private static final String DEEPSEEK_API_KEY = "";
    public static Retrofit deepSeekRetrofit = null;

    // config of OpenAI
    private static final String OPENAI_BASE_URL = "https://api.openai.com/";
    private static final String OPENAI_API_KEY = "";
    public static Retrofit openAIRetrofit = null;

    // 创建DeepSeek的Retrofit 实例
    public static ApiService getDeepSeekApiService() {
        if (deepSeekRetrofit == null) {
            OkHttpClient client = createClient(DEEPSEEK_API_KEY);
            deepSeekRetrofit = new Retrofit.Builder()
                    .baseUrl(DEEPSEEK_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return deepSeekRetrofit.create(ApiService.class);
    }

    // OpenAI的Retrofit 实例
    public static ApiService getOpenAIApiService() {
        if (openAIRetrofit == null) {
            OkHttpClient client = createClient(OPENAI_API_KEY);
            openAIRetrofit = new Retrofit.Builder()
                    .baseUrl(OPENAI_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return openAIRetrofit.create(ApiService.class);
    }

    // 通用Header拦截器的生成方法
    private static OkHttpClient createClient(String apiKey) {
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("Authorization", "Bearer " + apiKey)
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                }).build();
    }
}
