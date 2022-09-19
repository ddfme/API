## ComPDFKit-SaaS API 参考 

### 介绍
ComPDFKit-SaaS API 是围绕REST标准组织的，通过HTTP 方法具有可预测的面向资源的 URL，其中以JSON格式 获取信息。

我们支持跨域资源共享，让您可以从客户端安全地与我们的 API 交互。尽管您永远不应该在客户端代码中公开您的秘密 API 密钥。

要开始开发，请联系工作人员：XXXX

### 请求工作流
使用 ComPDFKit-SaaS API 的 PDF 处理工作流程非常简单，由 4 个基本请求指令组成：获取任务、上传文件、执行任务和获取文件信息并下载文件。一旦 API 执行了这四个步骤，您的 PDF 文件将使用您所需的工具进行处理并返回下载链接。


下面您将看到对这 4 个请求和每个步骤的参数的完整参考。

### 前置准备
对于 `{serverUrl}` 请求服务器地址，建议登录[控制台]()进行查看选择<br> 
对于 `{executeTypeUrl}` 目前支持文件处理功能，建议登录[控制台]()进行查看选择
### 验证
我们正在使用一种非常简单但有效的身份验证方法：JWT、 JSON Web Tokens。它包括在每个请求中发送一个不记名标头，并使用您在 ComPDFKit-SaaS API 开发人员帐户中提供的密钥签名令牌。您的密钥可能永远不会暴露，但签名的令牌可以暴露，您必须在除认证之外的每个标头请求参数中发送它`Authorization: Bearer {signed_token}`

#### 从我们的身份验证服务器请求签名令牌
当您向 /auth 资源发送请求时，您将收到 Authorization: Bearer {signed_token}要在每个请求中发送的令牌（/task、/upload、/execute、/taskInfo）。请记住，令牌有过期日期，必须再次请求。
- 认证（POST）参数详情见 [API接口文档](https://www.showdoc.cc/2033153860430948?page_id=9190236630534089)
- 您需要设置属于您的 `认证凭证` 以及 `认证密码`
- 建议您使用统一的拦截器为每一个请求添加`Authorization`请求头
```java
// token管理器
AuthTokenManage authTokenManage = new AuthTokenManage();
// token
String accessToken = authTokenManage.getToken();
```
### 主要资源
#### 开始
- 获取任务ID(GET) 参数详情见 [API接口文档](https://www.showdoc.cc/2033153860430948?page_id=9190236630534089)
- 要对文件进行处理，必须要先获得任务
- 每个任务所支持上传的文件阈值为 `5` 个
- 任务开始之后 `60分钟` 不进行执行动作，任务将过期
```java
@Autowired
private FileConvertAPI fileConvertAPI;
// 创建一个任务并且获得任务id
String taskId = fileConvertAPI.getTask("pdf/docx");
```

#### 上传文件
- 上传文件（POST）参数详情见 [API接口文档](https://www.showdoc.cc/2033153860430948?page_id=9190236630534089)
- 上传的文件格式必须符合本次执行功能要求的原格式
- 下一步是通过将这些文件添加到任务中来分配您（或您的用户）要上传的所有文件，如以下代码示例所示：
```java
File file1 = new File("/1.pdf");
File file2 = new File("/2.pdf");
File file3 = new File("/3-加密文件.pdf");
String fileKey1 = fileConvertAPI.fileUpload(taskId, file1, "");
String fileKey2 = fileConvertAPI.fileUpload(taskId, file2, "");
String fileKey3 = fileConvertAPI.fileUpload(taskId, file3, "123");
```

#### 执行任务
- 执行任务（GET） 参数详情见 [API接口文档](https://www.showdoc.cc/2033153860430948?page_id=9190236630534089)
- 任务状态必须是 `TaskStart` 状态
- 现在是时候执行您的任务了：
```java
fileConvertAPI.executeTask(taskId);
```

#### 获取任务信息并获得下载链接
- 获取任务信息（GET）参数详情见 [API接口文档](https://www.showdoc.cc/2033153860430948?page_id=9190236630534089)
- 只有任务状态为 `TaskFinish` 并且文件状态为 `success` 才意味着文件处理执行成功
- 示例代码如下
```java
// TaskInfoDTO 字段详见接口文档
TaskInfoDTO taskInfo = fileConvertAPI.getTaskInfo(taskId);
```

### 完整代码
这是完整代码的样子
```java
@Autowired
private FileConvertAPI fileConvertAPI;

@Test
void contextLoads() {
    // 创建新任务并获得任务id
    String task = fileConvertAPI.getTask("pdf/docx");
    // 上传本次任务处理的文件
    String fileKey = fileConvertAPI.fileUpload(task, new File("C:\\Users\\00\\Desktop\\wangPH\\2207.00061.pdf"), null);
    // 执行任务
    fileConvertAPI.executeTask(task);
    // 获得任务信息
    TaskInfoDTO taskInfo = fileConvertAPI.getTaskInfo(task);
    // 判断任务是否执行完毕
    while (!"TaskFinish".equals(taskInfo.getTaskStatus())){
        try {
            // 间隔1秒查询一次（轮询）
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        taskInfo = fileConvertAPI.getTaskInfo(task);
    }
    // 获取任务中执行处理的所有文件信息
    List<FileInfoDTO> fileInfoDTOList = taskInfo.getFileInfoDTOList();
    for (FileInfoDTO fileInfoDTO : fileInfoDTOList) {
        // 判断文件是否转换成功
        if ("success".equals(fileInfoDTO.getStatus())){
            try {
                // 获得文件下载链接并下载
                URL url = new URL(fileInfoDTO.getDownloadUrl());
                URLConnection conn = url.openConnection();
                InputStream inStream = conn.getInputStream();
                FileOutputStream fs = new FileOutputStream("C:/Users/00/Desktop/" + fileInfoDTO.getDownloadUrl().substring(fileInfoDTO.getDownloadUrl().indexOf("@")+1));
                IOUtils.copy(inStream, fs);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```
