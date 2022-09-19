package com.kdanmobile.demoJava.kdanAPIs;

import com.alibaba.fastjson.JSONObject;
import com.kdanmobile.demoJava.base.R;
import com.kdanmobile.demoJava.entity.FileInfoDTO;
import com.kdanmobile.demoJava.entity.TaskInfoDTO;
import com.kdanmobile.demoJava.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author ComPDFKit-WPH 2022/9/19
 * <p>
 * 文件转换API调用
 */
@Component
@Slf4j
public class FileConvertAPI {
    // 请求客户端
    private final OkHttpClient client;
    // 请求服务器地址
    @Value("${request.serverUrl}")
    private String serverUrl;

    public FileConvertAPI(OkHttpClient client) {
        this.client = client;
    }

    /**
     * 获取任务id
     *
     * @param executeTypeUrl 使用功能
     * @return 任务id
     */
    public String getTask(String executeTypeUrl) {
        // 生成请求并设置请求地址URL
        Request request = new Request.Builder()
                .url("http://" + serverUrl + ":8080/server/v1/task/" + executeTypeUrl)
                .build();
        // 发送请求获得响应
        Response response = null;
        R<Map<String, String>> r = null;
        try {
            response = client.newCall(request).execute();
            // 获得响应中的body数据
            String body = Objects.requireNonNull(response.body()).string();
            r = JSONObject.parseObject(body, R.class);
            if (!"200".equals(r.getCode())) {
                throw new CommonException(r.getCode(), r.getMsg());
            }
            return r.getData().get("taskId");
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonException("500", "未知异常");
        }
    }

    /**
     * 上传文件
     *
     * @param taskId   任务id
     * @param file     文件
     * @param password 文件打开密码
     * @return 文件唯一标识 fileKey
     */
    public String fileUpload(String taskId, File file, String password) {
        // 设置FORM形式传参
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                // 添加上传的文件
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                file))
                // 添加任务id
                .addFormDataPart("taskId", taskId)
                // 添加文件打开密码（没有可不设置）
                .addFormDataPart("password", password == null ? "" : password)
                .build();
        // 生成请求并设置请求地址URL以及请求参数body
        Request request = new Request.Builder()
                .url("http://" + serverUrl + ":8080/server/v1/file/upload")
                // 设置请求方式和参数body
                .method("POST", body)
                .build();
        // 发送请求获得响应
        try {
            Response response = client.newCall(request).execute();
            // 获得响应中的body数据
            String bodyString = Objects.requireNonNull(response.body()).string();
            R<Map<String, String>> r = JSONObject.parseObject(bodyString, R.class);
            if (!"200".equals(r.getCode())) {
                throw new CommonException(r.getCode(), r.getMsg());
            }
            return r.getData().get("fileKey");
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonException("500", "请求执行错误");
        }
    }

    /**
     * 执行任务
     *
     * @param taskId 任务id
     */
    public void executeTask(String taskId) {
        // 生成请求并设置请求地址URL
        // GET请求传参 taskId
        Request request = new Request.Builder()
                .url("http://" + serverUrl + ":8080/server/v1/convert/start?taskId=" + taskId)
                .build();
        // 发送请求获得响应
        Response response;
        try {
            response = client.newCall(request).execute();
            // 获得响应中的body数据
            String bodyString = Objects.requireNonNull(response.body()).string();
            R<Map<String, String>> r = JSONObject.parseObject(bodyString, R.class);
            if (!"200".equals(r.getCode())) {
                throw new CommonException(r.getCode(), r.getMsg());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonException("500", "未知异常");
        }
    }

    /**
     * 获取任务信息
     *
     * @param taskId 任务id
     * @return 任务信息
     */
    public TaskInfoDTO getTaskInfo(String taskId) {
        Request request = new Request.Builder()
                .url("http://" + serverUrl + ":8080/server/v1/task/taskInfo?taskId=" + taskId)
                .build();
        // 获得响应中的body数据
        String bodyString = null;
        try {
            // 发送请求获得响应
            Response response = client.newCall(request).execute();
            bodyString = Objects.requireNonNull(response.body()).string();
            // TaskInfoDTO实体类参考 接口文档返回数据格式
            R r = JSONObject.parseObject(bodyString, R.class);
            if (!"200".equals(r.getCode())) {
                log.error(r.getMsg());
                throw new CommonException(r.getCode(), r.getMsg());
            }
            return JSONObject.parseObject(r.getData().toString(), TaskInfoDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonException("500", "未知异常");
        }
    }

    /**
     * 获取单个文件转换信息
     *
     * @param fileKey 文件唯一标识
     * @return 文件转换信息
     */
    public FileInfoDTO getFileInfo(String fileKey) {
        Request request = new Request.Builder()
                .url("http://" + serverUrl + ":8080/server/v1/file/fileInfo?fileKey=" + fileKey)
                .build();
        // 获得响应中的body数据
        try {
            // 发送请求获得响应
            Response response = client.newCall(request).execute();
            String bodyString = Objects.requireNonNull(response.body()).string();
            // TaskInfoDTO实体类参考 接口文档返回数据格式
            R r = JSONObject.parseObject(bodyString, R.class);
            if (!"200".equals(r.getCode())) {
                log.error(r.getMsg());
                throw new CommonException(r.getCode(), r.getMsg());
            }
            return JSONObject.parseObject(r.getData().toString(), FileInfoDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonException("500", "未知异常");
        }
    }
}
