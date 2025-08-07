package com.mexc.example.spot.websocket;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mxc.push.common.protobuf.PushDataV3ApiWrapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class WebsocketV3Protobuf extends WebSocketListener {


    private WebSocket privateClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();
        Map<String, String> params = new HashMap<>();
        params.put("recWindow", "60000");
        //String listenKey = CreateListenKey.postUserDataStream(params).get("listenKey");
        String listenKey = "1955374b3a1e64a47fe177678229d1238539a746d4a62ea9475cc4a394230425";

        Request request = new Request.Builder()
                .url("wss://wbs-api.mexc.com/ws?listenKey=" + listenKey)
                .build();
        WebSocket webSocket = client.newWebSocket(request, this);

        //Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown();
        return webSocket;
    }

    private WebSocket publicClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url("wss://wbs-api.mexc.com/ws")
                .build();
        WebSocket webSocket = client.newWebSocket(request, this);

        //Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown();
        return webSocket;
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        log.info("MEXC-WS CONNECTED ....");
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        System.out.println("TEXT MESSAGE: " + text);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, ByteString bytes) {
        try {
            PushDataV3ApiWrapper pushDataV3ApiWrapper = PushDataV3ApiWrapper.parseFrom(bytes.toByteArray());
            String channel = pushDataV3ApiWrapper.getChannel();
            switch (channel) {
                case "spot@public.aggre.deals.v3.api.pb@100ms@BTCUSDT":
                    log.info("Trade Streams:{}", pushDataV3ApiWrapper.getPublicAggreDeals());
                    break;

                case "spot@public.kline.v3.api.pb@BTCUSDT@Min15":
                    log.info("K-line Streams:{}", pushDataV3ApiWrapper.getPublicSpotKline());
                    break;

                case "spot@public.aggre.depth.v3.api.pb@100ms@BTCUSDT":
                    log.info("Diff.Depth Stream:{}", pushDataV3ApiWrapper.getPublicAggreDepths());
                    break;

                case "spot@public.increase.depth.batch.v3.api.pb@BTCUSDT":
                    log.info("Diff.Depth Stream(Batch):{}", pushDataV3ApiWrapper.getPublicIncreaseDepthsBatch());
                    break;

                case "spot@public.limit.depth.v3.api.pb@BTCUSDT@5":
                    log.info("Partial Book Depth Streams:{}", pushDataV3ApiWrapper.getPublicLimitDepths());
                    break;

                case "spot@public.aggre.bookTicker.v3.api.pb@100ms@BTCUSDT":
                    log.info("Individual Symbol Book Ticker Streams:{}", pushDataV3ApiWrapper.getPublicAggreBookTicker());
                    break;
                case "spot@public.bookTicker.batch.v3.api.pb@BTCUSDT":
                    log.info("Individual Symbol Book Ticker Streams(Batch):{}", pushDataV3ApiWrapper.getPublicBookTickerBatch());
                    break;

                case "spot@private.account.v3.api.pb":
                    log.info("Spot Account Update:{}", pushDataV3ApiWrapper.getPrivateAccount());
                    break;

                case "spot@private.deals.v3.api.pb":
                    log.info("Spot Account Deals:{}", pushDataV3ApiWrapper.getPrivateDeals());
                    break;

                case "spot@private.orders.v3.api.pb":
                    log.info("Spot Account Orders:{}", pushDataV3ApiWrapper.getPrivateOrders());
                    break;

                default:
                    break;
            }
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(1000, null);
        System.out.println("CLOSE: " + code + " " + reason);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
    }

    public static void main(String... args) {
        WebsocketV3Protobuf websocketV3 = new WebsocketV3Protobuf();
        //sub public channel
        WebSocket publicClient = websocketV3.publicClient();
        publicClient.send(new SubscriptionCommand("spot@public.aggre.deals.v3.api.pb@100ms@BTCUSDT").toJsonString());
        publicClient.send(new SubscriptionCommand("spot@public.kline.v3.api.pb@BTCUSDT@Min15").toJsonString());
        publicClient.send(new SubscriptionCommand("spot@public.aggre.depth.v3.api.pb@100ms@BTCUSDT").toJsonString());
        publicClient.send(new SubscriptionCommand("spot@public.increase.depth.batch.v3.api.pb@BTCUSDT").toJsonString());
        publicClient.send(new SubscriptionCommand("spot@public.limit.depth.v3.api.pb@BTCUSDT@5").toJsonString());
        publicClient.send(new SubscriptionCommand("spot@public.aggre.bookTicker.v3.api.pb@100ms@BTCUSDT").toJsonString());
        publicClient.send(new SubscriptionCommand("spot@public.bookTicker.batch.v3.api.pb@BTCUSDT").toJsonString());

        //sub private channel
        WebSocket privateClient = websocketV3.privateClient();
        privateClient.send(new SubscriptionCommand("spot@private.account.v3.api.pb").toJsonString());
        privateClient.send(new SubscriptionCommand("spot@private.deals.v3.api.pb").toJsonString());
        privateClient.send(new SubscriptionCommand("spot@private.orders.v3.api.pb").toJsonString());

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(
                () -> privateClient.send("{\"method\":\"PING\"}"), 10, 20, TimeUnit.SECONDS
        );
    }
}
