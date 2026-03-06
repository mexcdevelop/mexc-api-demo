package com.mexc.example.futures.websocket;

import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.SignatureUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.ByteString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Getter
@Slf4j
public final class FuturesWebsocket extends WebSocketListener {

    private WebSocket privateClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url("wss://contract.mexc.co/edge")
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
        System.out.println(Instant.now().toEpochMilli() + "MESSAGE: " + text);
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
        FuturesWebsocket websocketV3 = new FuturesWebsocket();
        //sub private channel
        WebSocket privateClient = websocketV3.privateClient();

        Map<String, Object> params = new HashMap<>();
        String apiKey = "";
        String secret = "";
        Long timestamp = Instant.now().toEpochMilli();
        String signStr = apiKey + timestamp;
        params.put("apiKey", apiKey);
        params.put("reqTime", timestamp);
        params.put("signature", SignatureUtil.actualSignature(signStr, secret));
        LoginVo vo = new LoginVo();
        vo.setParam(params);
        String command = JsonUtil.toJson(vo);
        privateClient.send(command);


        privateClient.send("{\n" +
                "    \"method\":\"personal.filter\",\n" +
                "    \"param\":{\n" +
                "        \"filters\":[\n" +
                "            {\n" +
                "                \"filter\":\"order\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}");
    }


    @Getter
    @Setter
    public static class LoginVo {
        private String method = "login";
        private boolean subscribe = true;
        private Map<String, Object> param;
    }

    private static List<String> createCommands(String symbol) {

        List<String> args = new ArrayList<>();
        args.add("{\"method\": \"sub.depth.full\", \"param\": {\"symbol\":\"AGI_USDT\"}}");
        args.add("{\"method\": \"sub.depth\", \"param\": {\"symbol\":\"AGI_USDT\"}}");
        args.add("{\"method\": \"sub.funding.rate\", \"param\": {\"symbol\":\"AGI_USDT\"}}");
        args.add("{\"method\": \"sub.index.price\", \"param\": {\"symbol\":\"AGI_USDT\"}}");
        args.add("{\"method\": \"sub.deal\", \"param\": {\"symbol\":\"AGI_USDT\"}}");
        return args;
    }


}