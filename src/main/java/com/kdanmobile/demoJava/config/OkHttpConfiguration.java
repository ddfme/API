package com.kdanmobile.demoJava.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author ComPDFKit-WPH 2022/9/15
 * <p>
 * 请求客户端配置
 */
@Configuration
public class OkHttpConfiguration {

    @Bean
    public OkHttpClient getOkHttpClient() {
        return new OkHttpClient().newBuilder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .callTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(new TokenHeaderInterceptor())
                .build();
    }

}
