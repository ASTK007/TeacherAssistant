package org.hzcu.teacherassistant.controller;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import okhttp3.HttpUrl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/tts")
@Tag(name = "语音合成助手")
public class TtsController {

    // 地址与鉴权信息
    private static final String hostUrl = "https://tts-api.xfyun.cn/v2/tts";
    @Value("${xf.appid}")
    private String appid;
    @Value("${xf.apiSecret}")
    private String apiSecret;
    @Value("${xf.apiKey}")
    private String apiKey;
    private static final String TTE = "UTF8";
    private static final String VCN = "xiaoyan";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static boolean wsCloseFlag = false;

    @PostMapping("/synthesize")
    @Operation(summary = "语音合成")
    public ResponseEntity<byte[]> synthesize(@RequestParam String text) throws Exception {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("输入需要转换的文本");
        }
        String wsUrl = getAuthUrl(hostUrl, apiKey, apiSecret).replace("https://", "wss://");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        websocketWork(wsUrl, outputStream, text);

        byte[] audioBytes = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/mpeg"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"output.mp3\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(audioBytes);
    }

    private void websocketWork(String wsUrl, ByteArrayOutputStream outputStream, String text) {
        try {
            URI uri = new URI(wsUrl);
            WebSocketClient webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    System.out.println("WebSocket connection established...");
                }

                @Override
                public void onMessage(String message) {
                    try {
                        JsonParse myJsonParse = objectMapper.readValue(message, JsonParse.class);
                        if (myJsonParse.code != 0) {
                            System.out.println("Error occurred, code: " + myJsonParse.code);
                            System.out.println("Error occurred, message: " + myJsonParse.message);
                            System.out.println("Session ID: " + myJsonParse.sid);
                        }
                        if (myJsonParse.data != null) {
                            byte[] textBase64Decode = Base64.getDecoder().decode(myJsonParse.data.audio);
                            outputStream.write(textBase64Decode);
                            if (myJsonParse.data.status == 2) {
                                wsCloseFlag = true;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    System.out.println("WebSocket connection closed...");
                }

                @Override
                public void onError(Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            };
            webSocketClient.connect();
            while (!webSocketClient.isOpen()) {
                Thread.sleep(100);
            }
            MyThread webSocketThread = new MyThread(webSocketClient, text, appid);
            webSocketThread.start();
            while (!wsCloseFlag) {
                Thread.sleep(200);
            }
            webSocketClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class MyThread extends Thread {
        WebSocketClient webSocketClient;
        String text;
        private String appid;

        public MyThread(WebSocketClient webSocketClient, String text, String appid) {
            this.webSocketClient = webSocketClient;
            this.text = text;
            this.appid = appid;
        }

        public void run() {
            String requestJson;
            try {
                Map<String, Object> requestMap = new HashMap<>();
                Map<String, String> common = new HashMap<>();
                common.put("app_id", appid);
                requestMap.put("common", common);

                Map<String, Object> business = new HashMap<>();
                business.put("aue", "lame");
                business.put("sfl", 1);
                business.put("tte", TTE);
                business.put("vcn", VCN);
                business.put("pitch", 50);
                business.put("speed", 50);
                requestMap.put("business", business);

                Map<String, Object> data = new HashMap<>();
                data.put("status", 2);
                data.put("text", Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8)));
                requestMap.put("data", data);

                requestJson = objectMapper.writeValueAsString(requestMap);
                webSocketClient.send(requestJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
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
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder()
                .addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8)))
                .addQueryParameter("date", date)
                .addQueryParameter("host", url.getHost())
                .build();

        return httpUrl.toString();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class JsonParse {
        public int code;
        public String sid;
        public Data data;
        public String message;

        static class Data {
            public int status;
            public String audio;
            public String ced;
        }
    }
}
