package com.mexc.example.p2p.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mexc.example.common.UserDataClient;

import java.util.Map;

public class MexcP2pApi {

    /**
     * 获取我的广告列表
     */
    public static Object myAds(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/fiat/merchant/ads/pagination",
                params,
                new TypeReference<Object>() {
                }
        );
    }

    /**
     * 获取广告市场列表
     */
    public static Object marketAds(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/fiat/market/ads/pagination",
                params,
                new TypeReference<Object>() {
                }
        );
    }

    /**
     * 获取我的订单列表 Maker 视角
     */
    public static Object makerOrders(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/fiat/merchant/order/paginationV2",
                params,
                new TypeReference<Object>() {
                }
        );
    }

    /**
     * 获取所有订单列表 Maker + Taker
     */
    public static Object allOrders(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/fiat/market/order/paginationV2",
                params,
                new TypeReference<Object>() {
                }
        );
    }

    /**
     * 获取订单详情
     */
    public static Object orderDetail(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/fiat/order/detail",
                params,
                new TypeReference<Object>() {
                }
        );
    }

    /**
     * 创建订单
     */
    public static Object createOrder(Map<String, String> params) {
        return UserDataClient.post(
                "/api/v3/fiat/merchant/order/deal",
                params,
                new TypeReference<Object>() {
                }
        );
    }

    /**
     * 标注订单已付款
     */
    public static Object confirmPaid(Map<String, String> params) {
        return UserDataClient.post(
                "/api/v3/fiat/confirm_paid",
                params,
                new TypeReference<Object>() {
                }
        );
    }

    /**
     * 释放虚拟币
     */
    public static Object releaseCoin(Map<String, String> params) {
        return UserDataClient.post(
                "/api/v3/fiat/release_coin",
                params,
                new TypeReference<Object>() {
                }
        );
    }

    /**
     * 创建或更新广告
     */
    public static Object saveOrUpdateAd(Map<String, String> params) {
        return UserDataClient.post(
                "/api/v3/fiat/merchant/ads/save_or_update",
                params,
                new TypeReference<Object>() {
                }
        );
    }

    /**
     * 商家营业/歇业
     */
    public static Object merchantServiceSwitch(Map<String, String> params) {
        return UserDataClient.post(
                "/api/v3/fiat/merchant/service/switch",
                params,
                new TypeReference<Object>() {
                }
        );
    }


    /**
     * 生成 listenKey
     */
    public static Map<String, String> generateListenKey(Map<String, String> params) {
        return UserDataClient.post(
                "/api/v3/userDataStream",
                params,
                new TypeReference<Map<String, String>>() {
                }
        );
    }

    /**
     * 获取 listenKey
     */
    public static Object getListenKey(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/userDataStream",
                params,
                new TypeReference<Object>() {
                }
        );
    }

    /**
     * 获取聊天会话 ID
     */
    public static Object retrieveChatConversation(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/fiat/retrieveChatConversation",
                params,
                new TypeReference<Object>() {
                }
        );
    }

    /**
     * 分页获取聊天消息
     */
    public static Object retrieveChatMessages(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/fiat/retrieveChatMessageWithPagination",
                params,
                new TypeReference<Object>() {
                }
        );
    }


    /**
     * 下载文件
     */
    public static Object downloadFile(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/fiat/downloadFile",
                params,
                new TypeReference<Object>() {
                }
        );
    }

}