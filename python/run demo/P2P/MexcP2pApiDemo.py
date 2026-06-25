from pprint import pprint
from python.spot import mexc_spot_v3


p2p = mexc_spot_v3.mexc_p2p()

params = {
    "page": "1",
    "limit": "10"
}

myAds = p2p.my_ads(params)
print("my_ads:", myAds)


params = {
    "coinId": "USDT",
    "fiatUnit": "USD",
    "page": "1"
}

marketAds = p2p.market_ads(params)
print("market_ads:", marketAds)


params = {
    "lastId": "1",
    "limit": "10"
}

makerOrders = p2p.maker_orders(params)
print("maker_orders:", makerOrders)


params = {
    "lastId": "1",
    "limit": "10"
}

allOrders = p2p.all_orders(params)
print("all_orders:", allOrders)


params = {
    "advOrderNo": "123456789"
}

orderDetail = p2p.order_detail(params)
print("order_detail:", orderDetail)


params = {
    "advNo": "10001",
    "amount": "100",
    "userConfirmPaymentId": "1001111"
}

createOrder = p2p.create_order(params)
print("create_order:", createOrder)


params = {
    "advOrderNo": "123456789",
    "userConfirmPaymentId": "123456789"
}

confirmPaid = p2p.confirm_paid(params)
print("confirm_paid:", confirmPaid)


params = {
    "advOrderNo": "123456789"
}

releaseCoin = p2p.release_coin(params)
print("release_coin:", releaseCoin)


params = {
    "price": "7.0",
    "coinId": "USDT",
    "payTimeLimit": "30",
    "initQuantity": "1000",
    "side": "SELL",
    "fiatUnit": "USD",
    "payMethod": "1678",
    "minSingleTransAmount": "100",
    "maxSingleTransAmount": "1000000"
}

saveAd = p2p.save_or_update_ad(params)
print("save_or_update_ad:", saveAd)


params = {
    "open": "true"
}

switch = p2p.merchant_service_switch(params)
print("merchant_service_switch:", switch)


params = {
    "orderId": "123456789"
}

chatConv = p2p.retrieve_chat_conversation(params)
print("chat_conversation:", chatConv)


params = {
    "conversationId": "10001",
    "page": "1",
    "limit": "20"
}

chatMsg = p2p.retrieve_chat_messages(params)
print("chat_messages:", chatMsg)


params = {
    "fileId": "xxxxxx"
}

fileResp = p2p.download_file(params)
print("download_file:", fileResp)