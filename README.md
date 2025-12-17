# FeishuBot

使用 Spring Boot 提供一个可长期运行的后端服务，监听飞书事件回调，当机器人被任何人 @ 时自动回复“test success”。示例默认使用提供的自定义机器人 webhook：

```
https://open.feishu.cn/open-apis/bot/v2/hook/36fdfe62-0c1f-40ea-9bb6-b0ba0360c781
```

## 快速开始

1. 安装 JDK 16+ 和 Maven。
2. 启动服务：

   ```bash
   mvn spring-boot:run
   ```

   服务默认监听 `http://localhost:8080/feishu/events`，支持飞书事件订阅的 `challenge` 验证。
3. 在飞书「事件订阅」中，将回调 URL 配置为上面的地址并开启 `im.message.receive_v1` 事件。任意群成员在群里 @ 机器人时，服务会检测到消息中的 `<at ...>` 标签并通过 webhook 回发“test success”。

> 如需使用其它 webhook，请在 `src/main/resources/application.properties` 中修改 `feishu.webhook`。需要监听其他事件或调整回复逻辑，可在 `FeishuEventController` 内扩展处理代码。
