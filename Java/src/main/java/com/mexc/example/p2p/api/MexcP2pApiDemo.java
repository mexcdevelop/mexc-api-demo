package com.mexc.example.p2p.api;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;

import java.util.HashMap;
import java.util.Map;

public class MexcP2pApiDemo {

    public static void main(String[] args) {

        demoMyAds();

        demoMarketAds();

        demoMakerOrders();

        demoAllOrders();

        demoOrderDetail();

        demoCreateOrder();

        demoConfirmPaid();

        demoReleaseCoin();

        demoSaveOrUpdateAd();

        demoMerchantServiceSwitch();

        demoGenerateListenKey();

        demoGetListenKey();

        demoRetrieveChatConversation();

        demoRetrieveChatMessages();

        demoDownloadFile();
    }

    /**
     * 我的广告
     */
    private static void demoMyAds() {

        Map<String, String> params = Maps.newHashMap(
                ImmutableMap.<String, String>builder()
                        .put("page", "1")
                        .put("limit", "10")
                        .build());

        Object resp = MexcP2pApi.myAds(params);

        System.out.println(JsonUtil.toJson(resp));
    }

    /**
     * 市场广告
     */
    private static void demoMarketAds() {

        Map<String, String> params = new HashMap<>();

        params.put("coinId", "USDT");
        params.put("fiatUnit", "USD");
        params.put("page", "1");

        Object resp = MexcP2pApi.marketAds(params);

        System.out.println(JsonUtil.toJson(resp));
    }

    /**
     * Maker订单
     */
    private static void demoMakerOrders() {

        Map<String, String> params = new HashMap<>();

        params.put("lastId", "1");
        params.put("limit", "10");

        Object resp = MexcP2pApi.makerOrders(params);

        System.out.println(JsonUtil.toJson(resp));
    }

    /**
     * 全部订单
     */
    private static void demoAllOrders() {

        Map<String, String> params = new HashMap<>();

        params.put("lastId", "1");
        params.put("limit", "10");

        Object resp = MexcP2pApi.allOrders(params);

        System.out.println(JsonUtil.toJson(resp));
    }

    /**
     * 订单详情
     */
    private static void demoOrderDetail() {

        Map<String, String> params = new HashMap<>();

        params.put("advOrderNo", "123456789");

        Object resp = MexcP2pApi.orderDetail(params);

        System.out.println(JsonUtil.toJson(resp));
    }

    /**
     * 创建订单
     */
    private static void demoCreateOrder() {

        Map<String, String> params = new HashMap<>();

        params.put("advNo", "10001");
        params.put("amount", "100");
        params.put("userConfirmPaymentId", "1001111");

        Object resp = MexcP2pApi.createOrder(params);

        System.out.println(JsonUtil.toJson(resp));
    }

    /**
     * 已付款
     */
    private static void demoConfirmPaid() {

        Map<String, String> params = new HashMap<>();

        params.put("advOrderNo", "123456789");
        params.put("userConfirmPaymentId", "123456789");

        Object resp = MexcP2pApi.confirmPaid(params);

        System.out.println(JsonUtil.toJson(resp));
    }

    /**
     * 放币
     */
    private static void demoReleaseCoin() {

        Map<String, String> params = new HashMap<>();

        params.put("advOrderNo", "123456789");

        Object resp = MexcP2pApi.releaseCoin(params);

        System.out.println(JsonUtil.toJson(resp));
    }

    /**
     * 创建/修改广告
     */
    private static void demoSaveOrUpdateAd() {

        Map<String, String> params = new HashMap<>();

        params.put("price", "7.0");
        params.put("coinId", "USDT");
        params.put("payTimeLimit", "30");
        params.put("initQuantity", "1000");
        params.put("side", "SELL");
        params.put("fiatUnit", "USD");
        params.put("payMethod", "1678");
        params.put("minSingleTransAmount", "100");
        params.put("maxSingleTransAmount", "1000000");
        params.put("userAllTradeCountMin", "1");
        params.put("userAllTradeCountMax", "10");

        Object resp = MexcP2pApi.saveOrUpdateAd(params);

        System.out.println(JsonUtil.toJson(resp));
    }

    /**
     * 商家营业状态
     */
    private static void demoMerchantServiceSwitch() {

        Map<String, String> params = new HashMap<>();

        params.put("open", "true");

        Object resp = MexcP2pApi.merchantServiceSwitch(params);

        System.out.println(JsonUtil.toJson(resp));
    }

    /**
     * 创建listenKey
     */
    private static void demoGenerateListenKey() {

        Object resp = MexcP2pApi.generateListenKey(new HashMap<>());

        System.out.println(JsonUtil.toJson(resp));
    }

    /**
     * 获取listenKey
     */
    private static void demoGetListenKey() {

        HashMap<String, String> params = new HashMap<>();
        params.put("recvWindow", "60000");
        Object resp = MexcP2pApi.getListenKey(params);

        System.out.println(JsonUtil.toJson(resp));
    }

    /**
     * 获取聊天会话
     */
    private static void demoRetrieveChatConversation() {

        Map<String, String> params = new HashMap<>();

        params.put("orderId", "123456789");

        Object resp = MexcP2pApi.retrieveChatConversation(params);

        System.out.println(JsonUtil.toJson(resp));
    }

    /**
     * 获取聊天记录
     */
    private static void demoRetrieveChatMessages() {

        Map<String, String> params = new HashMap<>();

        params.put("conversationId", "10001");
        params.put("page", "1");
        params.put("limit", "20");

        Object resp = MexcP2pApi.retrieveChatMessages(params);

        System.out.println(JsonUtil.toJson(resp));
    }

    /**
     * 下载聊天附件
     */
    private static void demoDownloadFile() {

        Map<String, String> params = new HashMap<>();

        params.put("fileId", "xxxxxx");

        Object resp = MexcP2pApi.downloadFile(params);

        System.out.println(JsonUtil.toJson(resp));
    }

}