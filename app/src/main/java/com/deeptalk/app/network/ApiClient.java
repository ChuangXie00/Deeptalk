package com.deeptalk.app.network;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ApiClient {
    private static Retrofit retrofit = null;

    // ⚠️ 替换成你自己的 API Key
    private static final String API_KEY = "sk-65b8584834a04d878285b4a39b49f576";  // ←←← 在这里替换你的 key

    // 获得retrofit的 单例对象
    public static ApiService getApiService() {
        if (retrofit == null) {
            // 创建 OkHttpClient，添加拦截器以加入 Header
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request request = original.newBuilder()
                                    .header("Authorization", "Bearer " + API_KEY)
                                    .method(original.method(), original.body())
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.deepseek.com/") // ✅ DeepSeek API 域名
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(ApiService.class);
    }
}
