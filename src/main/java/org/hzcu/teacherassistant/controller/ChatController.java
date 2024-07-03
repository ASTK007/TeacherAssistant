package org.hzcu.teacherassistant.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/chat")
@Tag(name = "智能AI助手")
public class ChatController {
    private static final String hostUrl = "https://spark-api.xf-yun.com/v4.0/chat";
    @Value("${xf.appid}")
    private String appid;
    @Value("${xf.apiSecret}")
    private String apiSecret;
    @Value("${xf.apiKey}")
    private String apiKey;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private List<RoleContent> historyList = new ArrayList<>();

    @PostMapping("/ask")
    @Operation(summary = "智能问答")
    public String askQuestion(@RequestParam String question) throws Exception {
        // Build authentication URL
        String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时时间
                .readTimeout(60, TimeUnit.SECONDS) // 设置读取超时时间
                .build();

        String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();

        // Initialize WebSocket and wait for completion
        WebSocketResponseHandler responseHandler = new WebSocketResponseHandler(question);
        WebSocket webSocket = client.newWebSocket(request, responseHandler);
        responseHandler.awaitCompletion(); // 等待 WebSocket 连接结束

        return responseHandler.getTotalAnswer();
    }

    private class WebSocketResponseHandler extends WebSocketListener {
        private String newQuestion;
        private String totalAnswer = "";
        private boolean completed = false;
        private final CountDownLatch latch = new CountDownLatch(1);

        public WebSocketResponseHandler(String newQuestion) {
            this.newQuestion = newQuestion;
        }

        public String getTotalAnswer() {
            return totalAnswer;
        }

        public void awaitCompletion() throws InterruptedException {
            latch.await();
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            try {
                ObjectNode requestJson = objectMapper.createObjectNode();

                ObjectNode header = requestJson.putObject("header");
                header.put("app_id", appid);
                header.put("uid", UUID.randomUUID().toString().substring(0, 10));

                ObjectNode parameter = requestJson.putObject("parameter");
                ObjectNode chat = parameter.putObject("chat");
                chat.put("domain", "4.0Ultra");
                chat.put("temperature", 0.5);
                chat.put("max_tokens", 4096);

                ObjectNode payload = requestJson.putObject("payload");
                ObjectNode message = payload.putObject("message");
                ArrayNode text = message.putArray("text");

                for (RoleContent tempRoleContent : historyList) {
                    text.add(objectMapper.valueToTree(tempRoleContent));
                }

                RoleContent roleContent = new RoleContent("user", newQuestion);
                text.add(objectMapper.valueToTree(roleContent));
                historyList.add(roleContent);

                webSocket.send(requestJson.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            try {
                JsonNode myJsonParse = objectMapper.readTree(text);
                JsonNode header = myJsonParse.get("header");
                if (header.get("code").asInt() != 0) {
                    System.out.println("Error occurred, error code: " + header.get("code").asInt());
                    webSocket.close(1000, "");
                    return;
                }
                JsonNode textList = myJsonParse.get("payload").get("choices").get("text");
                for (JsonNode temp : textList) {
                    totalAnswer += temp.get("content").asText();
                }
                if (header.get("status").asInt() == 2) {
                    if (canAddHistory()) {
                        RoleContent roleContent = new RoleContent("assistant", totalAnswer);
                        historyList.add(roleContent);
                    } else {
                        historyList.remove(0);
                        RoleContent roleContent = new RoleContent("assistant", totalAnswer);
                        historyList.add(roleContent);
                    }
                    completed = true;
                    latch.countDown(); // 释放 latch，标记连接已完成
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            if (response != null) {
                int code = response.code();
                System.out.println("onFailure code:" + code);
                try {
                    System.out.println("onFailure body:" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (code != 101) {
                    System.exit(0);
                }
            }
            latch.countDown(); // 处理失败情况，释放 latch
        }
    }

    private boolean canAddHistory() {
        int historyLength = historyList.stream().mapToInt(temp -> temp.content.length()).sum();
        if (historyLength > 12000) {
            for (int i = 0; i < 5; i++) {
                historyList.remove(0);
            }
            return false;
        } else {
            return true;
        }
    }

    private static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());

        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";

        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        String sha = Base64.getEncoder().encodeToString(hexDigits);

        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"",
                apiKey, "hmac-sha256", "host date request-line", sha);

        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath()))
                .newBuilder()
                .addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8)))
                .addQueryParameter("date", date)
                .addQueryParameter("host", url.getHost())
                .build();

        return httpUrl.toString();
    }

    static class RoleContent {
        String role;
        String content;

        public RoleContent() {}

        public RoleContent(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
