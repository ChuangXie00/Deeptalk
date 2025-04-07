package com.deeptalk.app.network;

import android.content.Context;

import com.deeptalk.app.utils.PreferenceUtils;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

import java.io.IOException;

public class ApiClient {

    // config of DeepSeek
    private static final String DEEPSEEK_BASE_URL = "https://api.deepseek.com/";
    public static Retrofit deepSeekRetrofit = null;

    // config of OpenAI
    private static final String OPENAI_BASE_URL = "https://api.openai.com/";
    private static final String OPENAI_API_KEY = "";
    public static Retrofit openAIRetrofit = null;

    // 创建DeepSeek的Retrofit 实例, 改为动态读取 API Key
    public static ApiService getDeepSeekApiService(Context context) {
        if (deepSeekRetrofit == null) {
            OkHttpClient client = createClient(context);
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
            OkHttpClient client = createStaticClient(OPENAI_API_KEY);
            openAIRetrofit = new Retrofit.Builder()
                    .baseUrl(OPENAI_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return openAIRetrofit.create(ApiService.class);
    }

    // DeepSeek用的Header拦截器的生成方法
    private static OkHttpClient createClient(Context context) {
        return new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS) // 增加连接超时时间 10 -> 20
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        String apiKey = PreferenceUtils.getApiKey(context);
                        if (apiKey == null || apiKey.isEmpty()) {
                            apiKey = "Bearer empty"; // fallback, avoid crash
                        }

                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("Authorization", "Bearer " + apiKey)
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                }).build();
    }

    // 静态 Key 用于 OpenAI（可选）
    private static OkHttpClient createStaticClient(String apiKey) {
        return new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
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
