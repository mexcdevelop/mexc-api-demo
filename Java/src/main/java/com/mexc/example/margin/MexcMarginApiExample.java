package com.mexc.example.margin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.margin.pojo.*;
import com.mexc.example.spot.api.v3.pojo.Order;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MexcMarginApiExample {

    public static TradeModeResult changeTradeMode(Map<String, String> params) {
        return UserDataClient.get("/api/v3/margin/tradeMode", params, new TypeReference<TradeModeResult>() {
        });
    }

    public static PlaceOrderResult placeOrder(Map<String, String> params) {
        return UserDataClient.post("/api/v3/margin/order", params, new TypeReference<PlaceOrderResult>() {
        });
    }

    public static TranIdResult loan(Map<String, String> params) {
        return UserDataClient.post("/api/v3/margin/loan", params, new TypeReference<TranIdResult>() {
        });
    }

    public static TranIdResult repay(Map<String, String> params) {
        return UserDataClient.post("/api/v3/margin/repay", params, new TypeReference<TranIdResult>() {
        });
    }

    //todo
    public static List<OrderResult> cancelBySymbol(Map<String, String> params) {
        return UserDataClient.delete("/api/v3/margin/openOrders", params, new TypeReference<List<OrderResult>>() {
        });
    }

    public static OrderResult cancelOrder(Map<String, String> params) {
        return UserDataClient.delete("/api/v3/margin/order", params, new TypeReference<OrderResult>() {
        });
    }

    public static RowsResult<LoanListItem> loanRecords(Map<String, String> params) {
        return UserDataClient.get("/api/v3/margin/loan", params, new TypeReference<RowsResult<LoanListItem>>() {
        });
    }

    public static RowsResult<RepayListItem> repayRecords(Map<String, String> params) {
        return UserDataClient.get("/api/v3/margin/repay", params, new TypeReference<RowsResult<RepayListItem>>() {
        });
    }

    public static List<OrderResult> allOrders(Map<String, String> params) {
        return UserDataClient.get("/api/v3/margin/allOrders", params, new TypeReference<List<OrderResult>>() {
        });
    }

    public static List<OrderResult> openOrders(Map<String, String> params) {
        return UserDataClient.get("/api/v3/margin/openOrders", params, new TypeReference<List<OrderResult>>() {
        });
    }

    public static List<MyTradesResult> myTrades(Map<String, String> params) {
        return UserDataClient.get("/api/v3/margin/myTrades", params, new TypeReference<List<MyTradesResult>>() {
        });
    }


    public static MaxTransferableResult maxTransferable(Map<String, String> params) {
        return UserDataClient.get("/api/v3/margin/maxTransferable", params, new TypeReference<MaxTransferableResult>() {
        });
    }

    public static IndexPriceResult priceIndex(Map<String, String> params) {
        return UserDataClient.get("/api/v3/margin/priceIndex", params, new TypeReference<IndexPriceResult>() {
        });
    }


    public static OrderResult getOrder(Map<String, String> params) {
        return UserDataClient.get("/api/v3/margin/order", params, new TypeReference<OrderResult>() {
        });
    }


    public static IsolatedAccountResult isolatedAccount(Map<String, String> params) {
        return UserDataClient.get("/api/v3/margin/isolated/account", params, new TypeReference<IsolatedAccountResult>() {
        });
    }


    public static MaxBorrowableResult maxBorrowable(Map<String, String> params) {
        return UserDataClient.get("/api/v3/margin/maxBorrowable", params, new TypeReference<MaxBorrowableResult>() {
        });
    }


    public static IsolatedPairResult isolatedPair(Map<String, String> params) {
        return UserDataClient.get("/api/v3/margin/isolated/pair", params, new TypeReference<IsolatedPairResult>() {
        });
    }

    public static ForceLiquidationRecResult forceLiquidationRec(Map<String, String> params) {
        return UserDataClient.get("/api/v3/margin/forceLiquidationRec", params, new TypeReference<ForceLiquidationRecResult>() {
        });
    }

    public static List<IsolatedMarginDataResult> isolatedMarginData(Map<String, String> params) {
        return UserDataClient.get("/api/v3/margin/isolatedMarginData", params, new TypeReference<List<IsolatedMarginDataResult>>() {
        });
    }

    public static IsolatedMarginTierResult isolatedMarginTier(Map<String, String> params) {
        return UserDataClient.get("/api/v3/margin/isolatedMarginTier", params, new TypeReference<IsolatedMarginTierResult>() {
        });
    }

    public static void main(String[] args) {

//        //切换杠杆模式
        TradeModeResult TradeModeResult = changeTradeMode(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("tradeMode", "0")
                .put("symbol", "BTCUSDT")
                .build()));
        log.info("==>>TradeModeResult:{}", JsonUtil.toJson(TradeModeResult));

//        //下单
        PlaceOrderResult placeOrderResult = placeOrder(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .put("side", "BUY")
                .put("type", "LIMIT")
                .put("price", "20000")
                .put("quantity", "0.001")
                .build()));
        log.info("==>>placeOrderResult:{}", JsonUtil.toJson(placeOrderResult));
//
//        //杠杆订单查询
        OrderResult order = getOrder(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .put("orderId", "150751023827259392")
                .build()));
        log.info("==>>order:{}", JsonUtil.toJson(order));

//        //借贷
        TranIdResult loanResult = loan(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("asset", "BTC")
                .put("amount", "100")
                .put("symbol", "BTCUSDT")
                .build()));
        log.info("==>>tranIdResult:{}", JsonUtil.toJson(loanResult));


//        //归还借贷
        TranIdResult repayResult = repay(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("asset", "BTC")
                .put("amount", "100")
                .put("symbol", "BTCUSDT")
                .put("borrowId", "14235346546345345")
                .build()));
        log.info("==>>repayResult:{}", JsonUtil.toJson(repayResult));

//        //撤销订单
        OrderResult OrderResult = cancelOrder(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .put("orderId", "324365834583")
                .build()));
        log.info("==>>OrderResult:{}", JsonUtil.toJson(OrderResult));

          //当前挂单记录
        List<OrderResult> openOrdersResult = openOrders(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .put("isIsolated", "TRUE")
                .build()));
        log.info("==>>OrderResult:{}", JsonUtil.toJson(openOrdersResult));

        //查询借贷记录
        RowsResult<LoanListItem> loanRecordsResult = loanRecords(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("asset", "BTC")
                .put("symbol", "BTCUSDT")
                .build()));
        log.info("==>>RowsResult:{}", JsonUtil.toJson(loanRecordsResult));

         //查询最大可转出额
        MaxTransferableResult maxTransferableResult =maxTransferable(Maps.newHashMap(ImmutableMap.<String, String>builder()
               .put("symbol", "BTCUSDT")
                .put("asset", "BTC")
                .build()));
        log.info("==>>MaxTransferableResult:{}", JsonUtil.toJson(maxTransferableResult));

        //查询逐仓杠杆交易对
        IsolatedPairResult isolatedPairResult =isolatedPair(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .build()));
        log.info("==>>IsolatedPairResult:{}", JsonUtil.toJson(isolatedPairResult));

        //查询逐仓杠杆利率及限额
        List<IsolatedMarginDataResult> isolatedMarginDataResult =isolatedMarginData(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .build()));
        log.info("==>>IsolatedPairResult:{}", JsonUtil.toJson(isolatedMarginDataResult));

    }
}
