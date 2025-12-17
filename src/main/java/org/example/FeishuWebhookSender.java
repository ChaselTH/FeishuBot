package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * A minimal example that sends a text message to a Feishu custom bot webhook.
 * <p>
 * The webhook URL comes from https://open.feishu.cn/open-apis/bot/v2/hook/36fdfe62-0c1f-40ea-9bb6-b0ba0360c781.
 * Update the constant if you want to target a different bot.
 */
public class FeishuWebhookSender {
    private static final String WEBHOOK_URL = "https://open.feishu.cn/open-apis/bot/v2/hook/36fdfe62-0c1f-40ea-9bb6-b0ba0360c781";

    public static void main(String[] args) throws IOException, InterruptedException {
        String text = args.length > 0 ? String.join(" ", args) : "示例消息：request example";
        sendTextMessage(text);
    }

    /**
     * Sends a text message to the configured webhook URL.
     *
     * @param text message body to deliver to the Feishu group
     */
    public static void sendTextMessage(String text) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String payload = buildTextPayload(text);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(WEBHOOK_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.printf("HTTP %d%n%s%n", response.statusCode(), response.body());
    }

    private static String buildTextPayload(String text) {
        String escaped = escapeForJson(text);
        return String.format("{\"msg_type\":\"text\",\"content\":{\"text\":\"%s\"}}", escaped);
    }

    private static String escapeForJson(String value) {
        StringBuilder builder = new StringBuilder();
        for (char c : value.toCharArray()) {
            switch (c) {
                case '\\' -> builder.append("\\\\");
                case '"' -> builder.append("\\\"");
                case '\n' -> builder.append("\\n");
                case '\r' -> builder.append("\\r");
                case '\t' -> builder.append("\\t");
                default -> builder.append(c);
            }
        }
        return builder.toString();
    }
}
