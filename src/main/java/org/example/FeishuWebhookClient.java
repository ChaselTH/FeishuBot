package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class FeishuWebhookClient {

    private static final Logger log = LoggerFactory.getLogger(FeishuWebhookClient.class);

    private final RestTemplate restTemplate;
    private final String webhookUrl;

    public FeishuWebhookClient(@Value("${feishu.webhook}") String webhookUrl) {
        this.webhookUrl = webhookUrl;
        this.restTemplate = new RestTemplate();
    }

    public void sendText(String text) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("msg_type", "text");

            Map<String, Object> content = new HashMap<>();
            content.put("text", text);
            payload.put("content", content);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, request, String.class);
            log.info("Sent message to Feishu webhook. Status: {} Body: {}", response.getStatusCode(), response.getBody());
        } catch (Exception ex) {
            log.error("Failed to send message to Feishu webhook", ex);
        }
    }
}
