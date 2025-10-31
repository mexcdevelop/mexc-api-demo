package com.mexc.example.spot.api.v2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.SignatureUtil;
import com.mexc.example.spot.api.v2.pojo.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang.time.DateUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class MexcApiV2Example {
    private static final int CONN_TIMEOUT = 20;
    private static final int READ_TIMEOUT = 20;
    private static final int WRITE_TIMEOUT = 20;

    private static final String REQUEST_HOST = "https://www.mexc.com";

    private final String secretKey;
    private final String accessKey;

    public MexcApiV2Example(String secretKey, String accessKey) {
        this.secretKey = secretKey;
        this.accessKey = accessKey;
    }

    private static final OkHttpClient OK_HTTP_CLIENT = createOkHttpClient();

    public Result<List<SymbolInfo>> symbols() {
        return get("/open/api/v2/market/symbols", null, false, new TypeReference<Result<List<SymbolInfo>>>() {
        });
    }

    public Result<Object> timestamp() {
        return get("/open/api/v2/common/timestamp", null, false, new TypeReference<Result<Object>>() {
        });
    }

    public Result<Object> ping() {
        return get("/open/api/v2/common/ping", null, false, new TypeReference<Result<Object>>() {
        });
    }

    public Result<Map<String, List<String>>> defaultSymbols() {
        return get("/open/api/v2/market/api_default_symbols", null, false, new TypeReference<Result<Map<String, List<String>>>>() {
        });
    }

    public Result<List<TickerInfo>> ticker(Map<String, String> params) {
        return get("/open/api/v2/market/ticker", params, false, new TypeReference<Result<List<TickerInfo>>>() {
        });
    }

    public Result<MarketDepthWrapper> depth(Map<String, String> params) {
        return get("/open/api/v2/market/depth", params, false, new TypeReference<Result<MarketDepthWrapper>>() {
        });
    }

    public Result<List<TradeHistory>> deals(Map<String, String> params) {
        return get("/open/api/v2/market/deals", params, false, new TypeReference<Result<List<TradeHistory>>>() {
        });
    }

    public Result<List<Object[]>> kline(Map<String, String> params) {
        return get("/open/api/v2/market/kline", params, false, new TypeReference<Result<List<Object[]>>>() {
        });
    }

    public Result<List<CoinList>> coinList(Map<String, String> params) {
        return get("/open/api/v2/market/coin/list", params, false, new TypeReference<Result<List<CoinList>>>() {
        });
    }


    public Result<Map<String, Asset>> balance() {
        return get("/open/api/v2/account/info", null, true, new TypeReference<Result<Map<String, Asset>>>() {
        });
    }

    public Result<Map<String, List<String>>> apiSymbols() {
        return get("/open/api/v2/market/api_symbols", null, true, new TypeReference<Result<Map<String, List<String>>>>() {
        });
    }


    public Result<String> placeOrder(PlaceOrderRequest request) {
        return post("/open/api/v2/order/place", request, new TypeReference<Result<String>>() {
        });
    }

    public Result<Object> batchPlaceOrder(List<PlaceOrderRequest> batchOrders) {
        return post("/open/api/v2/order/place_batch", batchOrders, new TypeReference<Result<Object>>() {
        });
    }

    public Result<Map<String, String>> cancel(Map<String, String> params) {
        return delete("/open/api/v2/order/cancel", params, true, new TypeReference<Result<Map<String, String>>>() {
        });
    }


    public Result<List<OpenOrderResp>> openOrders(Map<String, String> params) {
        return get("/open/api/v2/order/open_orders", params, true, new TypeReference<Result<List<OpenOrderResp>>>() {
        });
    }

    public Result<List<OrderResp>> orderList(Map<String, String> params) {
        return get("/open/api/v2/order/list", params, true, new TypeReference<Result<List<OrderResp>>>() {
        });
    }

    public Result<List<OrderResp>> orderQuery(Map<String, String> params) {
        return get("/open/api/v2/order/query", params, true, new TypeReference<Result<List<OrderResp>>>() {
        });
    }

    public Result<List<OrderDealsResp>> orderDeals(Map<String, String> params) {
        return get("/open/api/v2/order/deals", params, true, new TypeReference<Result<List<OrderDealsResp>>>() {
        });
    }

    public Result<List<OrderDealsResp>> orderDealDetail(Map<String, String> params) {
        return get("/open/api/v2/order/deal_detail", params, true, new TypeReference<Result<List<OrderDealsResp>>>() {
        });
    }

    public Result<List<Map<String, String>>> cancelBySymbol(Map<String, String> params) {
        return delete("/open/api/v2/order/cancel_by_symbol", params, true, new TypeReference<Result<List<Map<String, String>>>>() {
        });
    }

    public Result<DepositAddressDto> depositAddressList(Map<String, String> params) {
        return get("/open/api/v2/asset/deposit/address/list", params, true, new TypeReference<Result<DepositAddressDto>>() {
        });
    }

    public Result<Pagination<DepositRecordDto>> depositRecordList(Map<String, String> params) {
        return get("/open/api/v2/asset/deposit/list", params, true, new TypeReference<Result<Pagination<DepositRecordDto>>>() {
        });
    }

    public Result<Pagination<WithdrawAddress>> withdrawAddressList(Map<String, String> params) {
        return get("/open/api/v2/asset/address/list", params, true, new TypeReference<Result<Pagination<WithdrawAddress>>>() {
        });
    }

    public Result<Pagination<WithdrawRecord>> withdrawList(Map<String, String> params) {
        return get("/open/api/v2/asset/withdraw/list", params, true, new TypeReference<Result<Pagination<WithdrawRecord>>>() {
        });
    }

    public Result<Map<String, String>> withdrawApply(WithdrawApply withdrawApply) {
        return post("/open/api/v2/asset/withdraw", withdrawApply, new TypeReference<Result<Map<String, String>>>() {
        });
    }

    public Result<Map<String, String>> cancelWithdraw(String withdrawId) {
        Map<String, String> params = new HashMap<>();
        params.put("withdraw_id", withdrawId);
        return delete("/open/api/v2/asset/withdraw", params, true, new TypeReference<Result<Map<String, String>>>() {
        });
    }

    public Result<InternalTransferResult> internalTransfer(Map<String, String> params) {
        return post("/open/api/v2/asset/internal/transfer", params, new TypeReference<Result<InternalTransferResult>>() {
        });
    }

    public Result<Pagination<InternalTransferRecord>> internalTransferRecord(Map<String, String> params) {
        return get("/open/api/v2/asset/internal/transfer/record", params, true, new TypeReference<Result<Pagination<InternalTransferRecord>>>() {
        });
    }

    public Result<List<Balance>> accountBalance(Map<String, String> params) {
        return get("/open/api/v2/account/balance", params, true, new TypeReference<Result<List<Balance>>>() {
        });
    }

    public Result<InternalTransferRecord> internalTransferInfo(Map<String, String> params) {
        return get("/open/api/v2/asset/internal/transfer/info", params, true, new TypeReference<Result<InternalTransferRecord>>() {
        });
    }

    private <T> T get(String uri, Map<String, String> params, boolean needSign, TypeReference<T> ref) {
        if (params == null) {
            params = new HashMap<>();
        }
        return call("GET", uri, null, params, needSign, ref);
    }

    private <T> T delete(String uri, Map<String, String> params, boolean needSign, TypeReference<T> ref) {
        if (params == null) {
            params = new HashMap<>();
        }
        return call("DELETE", uri, null, params, needSign, ref);
    }

    private <T> T post(String uri, Object object, TypeReference<T> ref) {
        return call("POST", uri, object, new HashMap<>(), true, ref);
    }

    private String createSignature(String method, String uri, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(1024);
        sb.append(method.toUpperCase()).append('\n')
                .append(uri).append('\n');
        SortedMap<String, String> map = new TreeMap<>(params);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append('=').append(SignatureUtil.urlEncode(value)).append('&');
        }
        sb.deleteCharAt(sb.length() - 1);

        return SignatureUtil.actualSignature(sb.toString(), secretKey);
    }

    private <T> T call(String method, String uri, Object object, Map<String, String> params, boolean needSign,
                       TypeReference<T> ref) {
        try {
            params.put("api_key", this.accessKey);
            if (needSign) {
                params.put("req_time", Instant.now().getEpochSecond() + "");
                //params.put("recv_window", "60");
                params.put("sign", createSignature(method, uri, params));
            }
            Request.Builder builder;
            if ("POST".equals(method)) {
                RequestBody body = RequestBody.create(JsonUtil.writeValue(object), MediaType.parse("application/json"));
                builder = new Request.Builder().url(REQUEST_HOST + uri + "?" + toQueryString(params)).post(body);
            } else if ("DELETE".equals(method)) {
                builder = new Request.Builder().url(REQUEST_HOST + uri + "?" + toQueryString(params)).delete();
            } else {
                builder = new Request.Builder().url(REQUEST_HOST + uri + "?" + toQueryString(params)).get();
            }
            Request request = builder.build();
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            assert response.body() != null;
            String content = response.body().string();
            if (200 != response.code()) {
                throw new RuntimeException(content);
            }
            return JsonUtil.readValue(content, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String toQueryString(Map<String, String> params) {
        return params.entrySet().stream().map((entry) -> entry.getKey() + "=" + SignatureUtil.urlEncode(entry.getValue())).collect(Collectors.joining("&"));
    }

    private static OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    public static void main(String[] args) {
        String accessKey = "";
        String secretKey = "";
        Gson gson = new Gson();
        MexcApiV2Example example = new MexcApiV2Example(secretKey, accessKey);

        //depth
        Result<MarketDepthWrapper> depth = example.depth(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "ETH_USDT")
                .put("depth", "10")
                .build()));
        log.info("==>>Trading pair depth:{}", gson.toJson(depth));

        //deals
        Result<List<TradeHistory>> deals = example.deals(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "ETH_USDT")
                .put("limit", "10")
                .build()));
        log.info("==>>交易对成交历史查询:{}", gson.toJson(deals));

        //ticker(24h)
        Result<List<TickerInfo>> ticker = example.ticker((Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "ETH_USDT")
                .build())));
        log.info("市场行情(24h):{}", gson.toJson(ticker));

        // kline
        Result<List<Object[]>> kline = example.kline((Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "ETH_USDT")
                .put("interval", "1m")
                .put("start_time", Instant.now().getEpochSecond() - 55 + "")
                .put("limit", "100")
                .build())));
        log.info("获取交易对K线:{}", gson.toJson(kline));


        //place order
        Result<String> order = example.placeOrder(buildPlaceOrderRequest());
        log.info("==>>下单:{}", gson.toJson(order));

        Result<Map<String, String>> cancelResult = example.cancel((Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("order_ids", order.getData())
                .build())));
        log.info("cancelResult:{}", gson.toJson(cancelResult));

        //batch place order
        List<PlaceOrderRequest> placeOrderReqList = new ArrayList<>();
        placeOrderReqList.add(buildPlaceOrderRequest());
        placeOrderReqList.add(buildPlaceOrderRequest());
        Result<Object> batchResult = example.batchPlaceOrder(placeOrderReqList);
        log.info("==>>批量下单:{}", gson.toJson(batchResult));

        //order deal detail
        Result<List<OrderDealsResp>> dealDetail = example.orderDealDetail((Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("order_id", "a39ea6b7afcf4f5cbba1e515210ff827")
                .build())));

        log.info("订单成交明细查询:{}", gson.toJson(dealDetail));


        log.info("server time ->> " + new Date(Long.parseLong(example.timestamp().getData().toString())));

        //open orders
        Result<List<OpenOrderResp>> openOrders = example.openOrders((Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "MX_USDT")
                .put("start_time", "1572076703000")
                .put("trade_type", "BID")
                .put("limit", "1000")
                .build())));
        log.info("查询当前委托单:{}", gson.toJson(openOrders));

        //get order list by condition
        Result<List<OrderResp>> listResult = example.orderList((Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "MX_USDT")
                .put("start_time", "1573198722444")
                .put("trade_type", "BID")
                .put("states", "NEW")
                .put("limit", "100")
                .build())));
        log.info("按条件查询订单列表:{}", gson.toJson(listResult));

        //get order by ids
        Result<List<OrderResp>> orderQueryList = example.orderQuery((Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("order_ids", "467e0920fae94ba6a90f6b40df0ad169")
                .build())));
        log.info("根据Ids查询订单:{}", gson.toJson(orderQueryList));


        //get order deals
        Result<List<OrderDealsResp>> orderDeals = example.orderDeals((Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "MX_USDT")
                .put("start_time", "1571872481022")
                .put("limit", "100")
                .build())));
        log.info("用户成交记录查询:{}", gson.toJson(orderDeals));


        //get balance
        Result<Map<String, Asset>> balance = example.balance();
        log.info("balance:{}", gson.toJson(balance));

        //get default symbols
        Result<Map<String, List<String>>> defaultSymbols = example.defaultSymbols();
        log.info("defaultSymbols:{}", gson.toJson(defaultSymbols));

        //get coinlist
        Result<List<CoinList>> coinList = example.coinList(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("currency", "BTC")
                .build()));
        log.info("coinList:{}", gson.toJson(coinList));

        //get apisymbols
        Result<Map<String, List<String>>> apiSymbols = example.apiSymbols();
        log.info("apiSymbols:{}", gson.toJson(apiSymbols));

        //cancel order by symbol
        Result<List<Map<String, String>>> cancelOrderResult = example.cancelBySymbol(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "MX_USDT")
                .build()));
        log.info("cancelResult:{}", gson.toJson(cancelOrderResult));

        //get deposit address
        Result<DepositAddressDto> addressList = example.depositAddressList(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("currency", "USDT")
                .build()));
        log.info("=>>addressList:{}", gson.toJson(addressList));

        //get deposit record list
        Date now = new Date();
        Date start = DateUtils.addDays(now, -30);
        Result<Pagination<DepositRecordDto>> depositRecordList = example.depositRecordList(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("currency", "USDT-TRX")
                .put("start_time", start.getTime() + "")
                .put("end_time", now.getTime() + "")
                .build()));
        log.info("=>>depositRecordList:{}", gson.toJson(depositRecordList));

        //get withdraw address
        Result<Pagination<WithdrawAddress>> withdrawAddressList = example.withdrawAddressList(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("currency", "USDT")
                .build()));
        log.info("withdrawAddressList:{}", gson.toJson(withdrawAddressList));

        //get withdraw record list
        Result<Pagination<WithdrawRecord>> withdrawList = example.withdrawList(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("currency", "USDT")
                .build()));
        log.info("withdrawList:{}", gson.toJson(withdrawList));

        //withdraw
        Result<Map<String, String>> withdrawResult = example.withdrawApply(WithdrawApply.builder()
                .currency("LUNA")
                .chain("BEP20(BSC)")
                .amount("1")
                .address("0x8D8F1dEBB8F6c92062D4D9Ba3ea44EF4b7104424")
                .build());
        log.info("withdrawResult:{}", gson.toJson(withdrawResult));

        //cencel withdraw
        Result<Map<String, String>> cancelWithdrawResult = example.cancelWithdraw("test_cancel1");
        log.info("cancelWithdrawResult:{}", gson.toJson(cancelWithdrawResult));

        //internal transfer
        Result<InternalTransferResult> transferResult = example.internalTransfer(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("amount", "1")
                .put("currency", "USDT")
                .put("from", "MAIN")
                .put("to", "CONTRACT")
                .build()));
        log.info("transferResult:{}", gson.toJson(transferResult));

        //get transfer record
        Result<Pagination<InternalTransferRecord>> transferRecord = example.internalTransferRecord(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("currency", "USDT")
                .build()));
        log.info("transferRecord:{}", gson.toJson(transferRecord));

        //get account balance
        Result<List<Balance>> accountBalance = example.accountBalance(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("currency", "USDT")
                .build()));
        log.info("accountBalance:{}", gson.toJson(accountBalance));

        //get transfer record detail
        Result<InternalTransferRecord> transferInfo = example.internalTransferInfo(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("transact_id", "bc7a78ca643e4c0992128e30797dd648")
                .build()));
        log.info("transferInfo:{}", gson.toJson(transferInfo));

    }

    private static PlaceOrderRequest buildPlaceOrderRequest() {
        PlaceOrderRequest req = new PlaceOrderRequest();
        //req.setClientOrderId(UUID.randomUUID().toString().replaceAll("_", ""));
        req.setOrderType("LIMIT_ORDER");
        req.setTradeType("BID");
        req.setSymbol("MX_USDT");
        req.setPrice("1");
        req.setQuantity("10");
        return req;
    }
}
