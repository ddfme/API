package com.kdanmobile.demoJava.tokenManage;

import com.alibaba.fastjson.JSONObject;
import com.kdanmobile.demoJava.base.R;
import com.kdanmobile.demoJava.exception.CommonException;
import okhttp3.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author ComPDFKit-WPH 2022/9/15
 * <p>
 * 管理token
 */
public class AuthTokenManage {
    // 认证凭证
    private static final String tenantName = "kdan";
    // 认证密码
    private static final String tenantPassword = "123456";

    /**
     * 获取token并放入缓存
     *
     * @return token
     */
    @Cacheable(value = "token")
    public String getToken(String serverUrl) {
        return authToken(serverUrl);
    }

    /**
     * 更新token并更新缓存
     *
     * @return token
     */
    @CacheEvict(value = "token")
    public String updateToken(String serverUrl) {
        return authToken(serverUrl);
    }

    /**
     * 发送请求获取token
     *
     * @return token
     */
    private String authToken(String serverUrl) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String data = "{\"tenantName\":\"" + tenantName + "\",\"tenantPassword\":\"" + tenantPassword + "\"}";
        RequestBody body = RequestBody.create(mediaType, data);
        Request request = new Request.Builder()
                .url("http://" + serverUrl + ":7000/v1/oauth/token")
                .method("POST", body)
                .build();
        R<Map<String, String>> r = null;
        try {
            Response response = client.newCall(request).execute();
            r = JSONObject.parseObject(Objects.requireNonNull(response.body()).string(), R.class);
            if (!"200".equals(r.getCode())) {
                throw new CommonException(r.getCode(), r.getMsg());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        assert r != null;
        return "Bearer " + r.getData().get("accessToken");
    }
}
