package com.kdanmobile.demoJava.config;

import com.kdanmobile.demoJava.tokenManage.AuthTokenManage;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author ComPDFKit-WPH 2022/9/15
 * <p>
 * 请求客户端拦截器，用于添加token令牌
 */
public class TokenHeaderInterceptor implements Interceptor {

    private final AuthTokenManage tokenManage = new AuthTokenManage();

    private String token;

    @NotNull
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request originalRequest;
        if (StringUtils.isEmpty(token)) {
            token = tokenManage.getToken(chain.request().url().host());
        }
        originalRequest = chain.request().newBuilder().header("Authorization", token).build();
        Response response = chain.proceed(originalRequest);
        if (response.code() == 401) {
            token = tokenManage.updateToken(chain.request().url().host());
            originalRequest = chain.request().newBuilder().header("Authorization", token).build();
            response = chain.proceed(originalRequest);
        }
        return response;
    }

}
