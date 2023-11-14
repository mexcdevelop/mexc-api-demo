package com.mexc.example.spot.websocket;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

@Getter
@Slf4j
public final class WebsocketV2 extends WebSocketListener {
    private WebSocket run() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url("wss://wbs.mexc.com/raw/ws")
                .build();
        WebSocket webSocket = client.newWebSocket(request, this);

        //Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown();
        return webSocket;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        log.info("MEXC-WS CONNECTED ....");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        System.out.println("MESSAGE: " + text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        System.out.println("MESSAGE: " + bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(1000, null);
        System.out.println("CLOSE: " + code + " " + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
    }

    public static void main(String... args) {
        WebsocketV2 spotRawWs = new WebsocketV2();
        WebSocket webSocket = spotRawWs.run();

        //subscribe depth
        webSocket.send("{\"op\":\"sub.limit.depth\",\"symbol\":\"BTC_USDT\",\"depth\": 5}");

        //subscribe kline
        //webSocket.send("{\"op\":\"sub.kline\",\"symbol\":\"BTC_USDT\",\"interval\":\"Min30\"}");

        //subscribe trade
        //webSocket.send("{\"op\":\"sub.symbol\",\"symbol\":\"BTC_USDT\"}}");

        //subscribe private message
        //签名规则 把api_key、req_time以及及op用MD5私钥做一个签名  Use MD5 private key to sign api_key, req_time, and op
        //api_key=api_key&req_time=req_time&op=sub.personal&api_secret=api_secret

        String apiKey = "";
        String secretKey = "";

        Map<String, String> params = new HashMap<>();
        params.put("api_key", apiKey);
        params.put("req_time", Instant.now().toEpochMilli() + "");
        params.put("op", "sub.personal");
        params.put("sign", sign(generateSignStr(params, secretKey)));
        Gson gson = new Gson();
        webSocket.send(gson.toJson(params));

        //webSocket.close(1000, null);
    }

    private static String sign(String target) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("Fail to get MD5 instance");
            return null;
        }
        md.update(target.getBytes());
        byte[] dg = md.digest();
        StringBuilder output = new StringBuilder(dg.length * 2);
        for (byte dgByte : dg) {
            int current = dgByte & 0xff;
            if (current < 16) {
                output.append("0");
            }
            output.append(Integer.toString(current, 16));
        }
        return output.toString();
    }


    private static String generateSignStr(Map<String, String> params, String key) {
        StringBuilder sb = new StringBuilder();
        params.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            if (sb.length() > 0) {
                sb.append('&');
            }
            sb.append(entry.getKey()).append('=');
            sb.append(entry.getValue());
        });
        sb.append('&').append("api_secret")
                .append('=').append(key);
        return sb.toString();
    }
}