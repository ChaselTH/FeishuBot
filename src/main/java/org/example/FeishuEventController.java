package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/feishu", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeishuEventController {

    private static final Logger log = LoggerFactory.getLogger(FeishuEventController.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FeishuWebhookClient webhookClient;

    public FeishuEventController(FeishuWebhookClient webhookClient) {
        this.webhookClient = webhookClient;
    }

    @PostMapping("/events")
    public Map<String, Object> onEvent(@RequestBody JsonNode payload) {
        log.debug("Received Feishu payload: {}", payload);

        JsonNode challenge = payload.get("challenge");
        if (challenge != null) {
            return Collections.singletonMap("challenge", challenge.asText());
        }

        JsonNode eventTypeNode = payload.at("/header/event_type");
        if (!eventTypeNode.isMissingNode() && "im.message.receive_v1".equals(eventTypeNode.asText())) {
            handleMessageEvent(payload.path("event"));
        }

        Map<String, Object> ack = new HashMap<>();
        ack.put("code", 0);
        ack.put("msg", "success");
        return ack;
    }

    private void handleMessageEvent(JsonNode eventNode) {
        JsonNode messageNode = eventNode.path("message");
        String content = messageNode.path("content").asText("");
        String messageType = messageNode.path("message_type").asText("");

        if (!StringUtils.hasText(content)) {
            return;
        }

        String textContent = extractTextContent(content, messageType);
        if (StringUtils.hasText(textContent) && textContent.contains("<at")) {
            log.info("Detected @ mention in message, replying with confirmation.");
            webhookClient.sendText("test success");
        }
    }

    private String extractTextContent(String content, String messageType) {
        if ("text".equals(messageType)) {
            try {
                JsonNode textNode = objectMapper.readTree(content);
                return textNode.path("text").asText("");
            } catch (IOException e) {
                log.warn("Failed to parse message content: {}", content, e);
                return content;
            }
        }
        return content;
    }
}
