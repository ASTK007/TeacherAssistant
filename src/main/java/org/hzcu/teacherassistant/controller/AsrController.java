package org.hzcu.teacherassistant.controller;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import okhttp3.HttpUrl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/asr")
@Tag(name = "语音识别助手")
public class AsrController {

    private static final String ASR_HOST_URL = "https://iat-api.xfyun.cn/v2/iat";
    @Value("${xf.appid}")
    private String appid;
    @Value("${xf.apiSecret}")
    private String apiSecret;
    @Value("${xf.apiKey}")
    private String apiKey;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static boolean wsCloseFlag = false;

    @PostMapping("/recognize")
    @Operation(summary = "语音识别")
    public ResponseEntity<String> recognize(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        String wsUrl = getAuthUrl(ASR_HOST_URL, apiKey, apiSecret).replace("https://", "wss://");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        websocketAsrWork(wsUrl, outputStream, file);

        String recognitionResult = outputStream.toString(StandardCharsets.UTF_8);

        return ResponseEntity.ok(recognitionResult);
    }

    private void websocketAsrWork(String wsUrl, ByteArrayOutputStream outputStream, MultipartFile file) {
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
                        JsonParse parsedMessage = objectMapper.readValue(message, JsonParse.class);
                        if (parsedMessage.code != 0) {
                            System.out.println("Error occurred, code: " + parsedMessage.code);
                            System.out.println("Error occurred, message: " + parsedMessage.message);
                            System.out.println("Session ID: " + parsedMessage.sid);
                        }
                        if (parsedMessage.data != null) {
                            for (JsonParse.Ws ws : parsedMessage.data.result.ws) {
                                for (JsonParse.Cw cw : ws.cw) {
                                    outputStream.write(cw.w.getBytes(StandardCharsets.UTF_8));
                                }
                            }
                            if (parsedMessage.data.status == 2) {
                                wsCloseFlag = true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("WebSocket connection closed...");
                }

                @Override
                public void onError(Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            };
            webSocketClient.connect();
            while (!webSocketClient.isOpen()) {
                Thread.sleep(100);
            }

            byte[] audioBytes = file.getBytes();
            int frameSize = 1280;
            int interval = 40;
            int status = 0;
            for (int i = 0; i < audioBytes.length; i += frameSize) {
                int toIndex = Math.min(i + frameSize, audioBytes.length);
                byte[] frame = Arrays.copyOfRange(audioBytes, i, toIndex);
                Map<String, Object> requestMap = createRequestMap(appid, frame, status);
                String requestJson = objectMapper.writeValueAsString(requestMap);
                webSocketClient.send(requestJson);
                status = 1;
                Thread.sleep(interval);
            }

            // Sending end frame
            Map<String, Object> endRequestMap = createRequestMap(appid, new byte[0], 2);
            String endRequestJson = objectMapper.writeValueAsString(endRequestMap);
            webSocketClient.send(endRequestJson);

            while (!wsCloseFlag) {
                Thread.sleep(200);
            }
            webSocketClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> createRequestMap(String appid, byte[] frame, int status) {
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> commonMap = new HashMap<>();
        commonMap.put("app_id", appid);

        Map<String, Object> businessMap = new HashMap<>();
        businessMap.put("language", "zh_cn");
        businessMap.put("domain", "iat");
        businessMap.put("accent", "mandarin");
        businessMap.put("vad_eos", 10000);
        businessMap.put("dwa", "wpgs");

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("status", status);
        dataMap.put("format", "audio/L16;rate=16000");
        dataMap.put("encoding", "lame");
        dataMap.put("audio", Base64.getEncoder().encodeToString(frame));

        requestMap.put("common", commonMap);
        requestMap.put("business", businessMap);
        requestMap.put("data", dataMap);

        return requestMap;
    }

    private String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        String date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US).format(new Date());
        String signatureOrigin = String.format("host: %s\ndate: %s\nGET %s HTTP/1.1", url.getHost(), date, url.getPath());
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(signatureOrigin.getBytes(StandardCharsets.UTF_8));
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"",
                apiKey, "hmac-sha256", "host date request-line", sha);
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(url.getHost())
                .addPathSegments(url.getPath().substring(1))
                .addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8)))
                .addQueryParameter("date", date)
                .addQueryParameter("host", url.getHost())
                .build();
        return httpUrl.toString();
    }

    static class JsonParse {
        public int code;
        public String sid;
        public Data data;
        public String message;

        static class Data {
            public int status;
            public Result result;
            public String ced;
        }

        static class Result {
            public String ls;
            public String bg;
            public String ed;
            public List<Ws> ws;
            public String sn;
        }

        static class Ws {
            public String bg;
            public List<Cw> cw;
        }
        @JsonIgnoreProperties
        static class Cw {
            public String w;
            public String wp;
            public String sc;
        }
    }
}
