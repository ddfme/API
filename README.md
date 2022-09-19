## ComPDFKit-SaaS API 参考 

### 介绍
ComPDFKit-SaaS API 是围绕REST标准组织的，通过HTTP 方法具有可预测的面向资源的 URL，其中以JSON格式 获取信息。

我们支持跨域资源共享，让您可以从客户端安全地与我们的 API 交互。尽管您永远不应该在客户端代码中公开您的秘密 API 密钥。

要开始开发，请联系工作人员：XXXX

### 请求工作流
使用 ComPDFKit-SaaS API 的 PDF 处理工作流程非常简单，由 4 个基本请求指令组成：获取任务、上传文件、执行任务和获取文件信息并下载文件。一旦 API 执行了这四个步骤，您的 PDF 文件将使用您所需的工具进行处理并返回下载链接。


下面您将看到对这 4 个请求和每个步骤的参数的完整参考。

### 前置准备
对于 `{serverUrl}` 请求服务器地址，建议登录[控制台]()进行查看选择
对于 `{executeTypeUrl}` 目前支持文件处理功能，建议登录[控制台]()进行查看选择
### 验证
我们正在使用一种非常简单但有效的身份验证方法：JWT、 JSON Web Tokens。它包括在每个请求中发送一个不记名标头，并使用您在 ComPDFKit-SaaS API 开发人员帐户中提供的密钥签名令牌。您的密钥可能永远不会暴露，但签名的令牌可以暴露，您必须在除认证之外的每个标头请求参数中发送它`Authorization: Bearer {signed_token}`

#### 从我们的身份验证服务器请求签名令牌
当您向 /auth 资源发送请求时，您将收到 Authorization: Bearer {signed_token}要在每个请求中发送的令牌（/task、/upload、/execute、/start、/taskInfo）。请记住，令牌有过期日期，必须再次请求。
- 认证（POST）参数详情见 [API接口文档](https://www.showdoc.cc/2033153860430948?page_id=9190236630534089)
> `http://{serverUrl}:8080/server/v1/oauth/token`

### 主要资源
#### 开始
- 获取任务ID