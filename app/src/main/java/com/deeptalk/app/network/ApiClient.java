package com.deeptalk.app.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 创建 Retrofit 单例对象，集中配置 baseUrl 和转换器
public class ApiClient  {
    // 全局只有一个静态的Retrofit对象（单例模式）
    private static Retrofit retrofit = null;

    // 公共的静态方法（可全局调用）, 返回的是（由Retrofit自动生成）的ApiService接口实现对象
    public static ApiService getApiService() {
        if (retrofit == null) {
            // new Retrofit.Builder() 是Retrofit构建器
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.deepseek.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        // 由Retrofit创建并返回ApiService的动态代理对象
        return retrofit.create(ApiService.class);
    }
}
