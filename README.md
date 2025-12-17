# FeishuBot

这个示例项目演示如何使用 Maven 在 Java 16 环境下调用自定义机器人 webhook 发送飞书消息。

## 运行示例

1. 确保已经安装 JDK 16 及以上版本，并在项目根目录执行编译：

   ```bash
   mvn compile
   ```

2. 运行示例主类，向 webhook 发送文本消息。你可以在命令末尾追加自定义文本，未提供时默认发送“示例消息：request example”。

   ```bash
   mvn exec:java -Dexec.args="需要推送的内容"
   ```

示例主类位于 `org.example.FeishuWebhookSender`，内置的 webhook 地址为：

```
https://open.feishu.cn/open-apis/bot/v2/hook/36fdfe62-0c1f-40ea-9bb6-b0ba0360c781
```

如需推送到其他机器人，请修改 `FeishuWebhookSender` 中的 `WEBHOOK_URL` 常量。
