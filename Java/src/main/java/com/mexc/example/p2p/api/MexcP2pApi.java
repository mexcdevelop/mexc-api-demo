package com.mexc.example.p2p.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mexc.example.common.UserDataClient;

import java.util.Map;

public class MexcP2pApi {


    public static Object myAds(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/fiat/merchant/ads/pagination",
                params,
                new TypeReference<Object>() {
                }
        );
    }


    public static Object marketAds(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/fiat/market/ads/pagination",
                params,
                new TypeReference<Object>() {
                }
        );
    }


    public static Object makerOrders(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/fiat/merchant/order/paginationV2",
                params,
                new TypeReference<Object>() {
                }
        );
    }


    public static Object allOrders(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/fiat/market/order/paginationV2",
                params,
                new TypeReference<Object>() {
                }
        );
    }


    public static Object orderDetail(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/fiat/order/detail",
                params,
                new TypeReference<Object>() {
                }
        );
    }


    public static Object createOrder(Map<String, String> params) {
        return UserDataClient.post(
                "/api/v3/fiat/merchant/order/deal",
                params,
                new TypeReference<Object>() {
                }
        );
    }


    public static Object confirmPaid(Map<String, String> params) {
        return UserDataClient.post(
                "/api/v3/fiat/confirm_paid",
                params,
                new TypeReference<Object>() {
                }
        );
    }

    public static Object releaseCoin(Map<String, String> params) {
        return UserDataClient.post(
                "/api/v3/fiat/release_coin",
                params,
                new TypeReference<Object>() {
                }
        );
    }


    public static Object saveOrUpdateAd(Map<String, String> params) {
        return UserDataClient.post(
                "/api/v3/fiat/merchant/ads/save_or_update",
                params,
                new TypeReference<Object>() {
                }
        );
    }


    public static Object merchantServiceSwitch(Map<String, String> params) {
        return UserDataClient.post(
                "/api/v3/fiat/merchant/service/switch",
                params,
                new TypeReference<Object>() {
                }
        );
    }



    public static Map<String, String> generateListenKey(Map<String, String> params) {
        return UserDataClient.post(
                "/api/v3/userDataStream",
                params,
                new TypeReference<Map<String, String>>() {
                }
        );
    }


    public static Object getListenKey(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/userDataStream",
                params,
                new TypeReference<Object>() {
                }
        );
    }


    public static Object retrieveChatConversation(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/fiat/retrieveChatConversation",
                params,
                new TypeReference<Object>() {
                }
        );
    }


    public static Object retrieveChatMessages(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/fiat/retrieveChatMessageWithPagination",
                params,
                new TypeReference<Object>() {
                }
        );
    }


    public static Object downloadFile(Map<String, String> params) {
        return UserDataClient.get(
                "/api/v3/fiat/downloadFile",
                params,
                new TypeReference<Object>() {
                }
        );
    }

}