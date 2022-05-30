# mexc-java-demo

## Please refer to class MexcApiV2Example.java for utilizing the spot v2 APIs.

- ### MexcApiV2Example

```
        String accessKey = "api_key";
        String secretKey = "api_secret";
        Gson gson = new Gson();
        MexcApiV2Example example = new MexcApiV2Example(secretKey, accessKey);
        
        //depth
        Result<MarketDepthWrapper> depth = example.depth(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "ETH_USDT")
                .put("depth", "10")
                .build()));
        log.info("==>>depth:{}", gson.toJson(depth));

        // Kline
        Result<List<Object[]>> kline = example.kline((Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "ETH_USDT")
                .put("interval", "1m")
                .put("start_time", Instant.now().getEpochSecond() - 55 + "")
                .put("limit", "100")
                .build())));
        log.info("kline:{}", gson.toJson(kline));


        //placeOrder
        Result<String> order = example.placeOrder(buildPlaceOrderRequest());
        log.info("==>>placeOrder:{}", gson.toJson(order));

        Result<Map<String, String>> cancelResult = example.cancel((Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("order_ids", order.getData())
                .build())));
        log.info("cancelResult:{}", gson.toJson(cancelResult));
```

## Please refer to class MexcApiV3AuthExample.java and class MexcApiV3NoneAuthExample.java for utilizing the spot v3 APIs.

- ### MexcApiV3NoneAuthExample

```
        HashMap<String, String> symbolParams = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .build());
        //Kline
        symbolParams.put("interval", "1m");
        log.info("=>>klines:{}", gson.toJson(klines(symbolParams)));
        
        //ticker24hr
        log.info("=>>ticker24hr:{}", gson.toJson(ticker24hr(symbolParams)));
        
        //exchangeInfo
        log.info("=>>exchangeInfo:{}", gson.toJson(exchangeInfo(symbolParams)));

```

- ### MexcApiV3AuthExample

        //placeOrder
        OrderPlaceResp placeResp = placeOrder(params);
        log.info("==>>placeResp:{}", gson.toJson(placeResp));

        //cancelOrder
        OrderCancelResp cancelResp = cancelOrder(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .put("orderId", "150751023827259392")
                .put("recvWindow", "60000")
                .build()));
        log.info("==>>cancelResp:{}", gson.toJson(cancelResp));

## Please refer to class MexcSpotRawWs.java for utilizing the spot raw websocket.

- ### MexcSpotRawWs

```
        MexcSpotRawWs spotRawWs = new MexcSpotRawWs();
        WebSocket webSocket = spotRawWs.run();

        //sub depth
        webSocket.send("{\"op\":\"sub.limit.depth\",\"symbol\":\"BTC_USDT\",\"depth\": 5}");

        //sub kline
        webSocket.send("{\"op\":\"sub.kline\",\"symbol\":\"BTC_USDT\",\"interval\":\"Min30\"}");

        //sub private message
        //Signature Rules  Use MD5 private key to sign api_key, req_time, and op
        //api_key=api_key&req_time=req_time&op=sub.personal&api_secret=api_secret

```

### For access and secret keys, they can be create [`here`](https://www.mexc.com/zh-CN/user/openapi).


      
  