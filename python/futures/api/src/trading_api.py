from typing import Optional, Dict, Any, List
import time

from .mexc_client import MexcHttpClient, logger


class MexcAccountTradingApi(MexcHttpClient):
    
    def __init__(self, api_key: str, secret_key: str):
        super().__init__(api_key, secret_key)
    
    # ==================== Account Asset Endpoints ====================
    
    def get_all_account_assets(self) -> dict:
        logger.info("\n=== Get All Account Assets ===")
        return self.get_signed("/api/v1/private/account/assets", None)
    
    def get_single_asset(self, currency: str) -> dict:
        logger.info(f"\n=== Get Single Currency Asset: {currency} ===")
        return self.get_signed(f"/api/v1/private/account/asset/{currency}", None)
    
    def get_transfer_record(self, currency: Optional[str] = None, state: Optional[str] = None,
                            type_: Optional[str] = None, page_num: int = 1, page_size: int = 20) -> dict:
        logger.info("\n=== Get Asset Transfer Records ===")
        params = {}
        if currency:
            params['currency'] = currency
        if state:
            params['state'] = state
        if type_:
            params['type'] = type_
        params['page_num'] = str(page_num)
        params['page_size'] = str(page_size)
        return self.get_signed("/api/v1/private/account/transfer_record", params)
    
    def get_profit_rate(self, type_: int) -> dict:
        logger.info(f"\n=== Get Personal Profit Rate (type={type_}) ===")
        return self.get_signed(f"/api/v1/private/account/profit_rate/{type_}", None)
    
    def get_asset_analysis(self, type_: int, currency: str, 
                           start_time: Optional[int] = None, end_time: Optional[int] = None) -> dict:
        logger.info(f"\n=== Get Asset Analysis (type={type_}) ===")
        params = {'currency': currency}
        if start_time:
            params['startTime'] = str(start_time)
        if end_time:
            params['endTime'] = str(end_time)
        return self.get_signed(f"/api/v1/private/account/asset/analysis/{type_}", params)
    
    def get_fee_deduct_configs(self) -> dict:
        logger.info("\n=== Get Fee Deduction Configurations ===")
        return self.get_signed("/api/v1/private/account/feeDeductConfigs", None)
    
    def get_yesterday_pnl(self) -> dict:
        logger.info("\n=== Get Yesterday's PnL ===")
        return self.get_signed("/api/v1/private/account/asset/analysis/yesterday_pnl", None)
    
    def post_asset_analysis_v3(self, start_time: int, end_time: int, 
                                reverse: Optional[int] = None,
                                include_unrealised_pnl: Optional[int] = None,
                                symbol: Optional[str] = None) -> dict:
        logger.info("\n=== User Asset Analysis V3 ===")
        params = {
            'startTime': str(start_time),
            'endTime': str(end_time)
        }
        if reverse is not None:
            params['reverse'] = str(reverse)
        if include_unrealised_pnl is not None:
            params['includeUnrealisedPnl'] = str(include_unrealised_pnl)
        if symbol:
            params['symbol'] = symbol
        return self.post_signed("/api/v1/private/account/asset/analysis/v3", params)
    
    def post_calendar_daily_v3(self, start_time: int, end_time: int,
                                reverse: Optional[int] = None,
                                include_unrealised_pnl: Optional[int] = None) -> dict:
        logger.info("\n=== Daily Calendar Analysis V3 ===")
        params = {
            'startTime': str(start_time),
            'endTime': str(end_time)
        }
        if reverse is not None:
            params['reverse'] = str(reverse)
        if include_unrealised_pnl is not None:
            params['includeUnrealisedPnl'] = str(include_unrealised_pnl)
        return self.post_signed("/api/v1/private/account/asset/analysis/calendar/daily/v3", params)
    
    def post_calendar_monthly_v3(self, reverse: Optional[int] = None,
                                  include_unrealised_pnl: Optional[int] = None) -> dict:
        logger.info("\n=== Monthly Calendar Analysis V3 ===")
        params = {}
        if reverse is not None:
            params['reverse'] = str(reverse)
        if include_unrealised_pnl is not None:
            params['includeUnrealisedPnl'] = str(include_unrealised_pnl)
        return self.post_signed("/api/v1/private/account/asset/analysis/calendar/monthly/v3", params)
    
    def post_recent_analysis_v3(self, reverse: Optional[int] = None,
                                 include_unrealised_pnl: Optional[int] = None,
                                 symbol: Optional[str] = None) -> dict:
        logger.info("\n=== Recent Asset Analysis V3 ===")
        params = {}
        if reverse is not None:
            params['reverse'] = str(reverse)
        if include_unrealised_pnl is not None:
            params['includeUnrealisedPnl'] = str(include_unrealised_pnl)
        if symbol:
            params['symbol'] = symbol
        return self.post_signed("/api/v1/private/account/asset/analysis/recent/v3", params)
    
    def get_today_pnl(self) -> dict:
        logger.info("\n=== Get Today's PnL ===")
        return self.get_signed("/api/v1/private/account/asset/analysis/today_pnl", None)

    def get_contract_fee_discount_config(self) -> dict:
        logger.info("\n=== Get Contract Fee Discount Configuration ===")
        return self.get_signed("/api/v1/private/account/config/contractFeeDiscountConfig", None)
    
    def get_order_fee_details(self, symbol: str, order_id: str) -> dict:
        logger.info(f"\n=== Get Order Fee Details: {order_id} ===")
        params = {
            'symbol': symbol,
            'orderId': order_id
        }
        return self.get_signed("/api/v1/private/order/fee_details", params)
    
    def get_account_discount_type(self) -> dict:
        logger.info("\n=== Get Account Discount Type ===")
        return self.get_signed("/api/v1/private/account/discountType", None)
    
    def export_asset_analysis(self, start_time: int, end_time: int, symbol: Optional[str] = None,
                               reverse: Optional[int] = None, 
                               include_unrealised_pnl: Optional[int] = None) -> dict:
        logger.info("\n=== Export Asset Analysis ===")
        params = {
            'startTime': str(start_time),
            'endTime': str(end_time)
        }
        if symbol:
            params['symbol'] = symbol
        if reverse is not None:
            params['reverse'] = str(reverse)
        if include_unrealised_pnl is not None:
            params['includeUnrealisedPnl'] = str(include_unrealised_pnl)
        return self.get_signed("/api/v1/private/account/asset/analysis/export", params)
    def get_total_order_deal_fee(self, start_time: int, end_time: int, symbol: Optional[str] = None) -> dict:    
        logger.info("\n=== Get Total Order Deal Fee ===")
        params = {
            'startTime': str(start_time),
            'endTime': str(end_time)
        }
        if symbol:
            params['symbol'] = symbol
        return self.get_signed("/api/v1/private/account/asset_book/order_deal_fee/total", params)
    
    def get_contract_fee_rate(self, symbol: str) -> dict:
        logger.info(f"\n=== Get Contract Fee Rate: {symbol} ===")
        params = {'symbol': symbol}
        return self.get_signed("/api/v1/private/account/contract/fee_rate", params)
    
    def get_zero_fee_rate_contracts(self) -> dict:
        logger.info("\n=== Get Zero Fee Rate Contracts ===")
        return self.get_signed("/api/v1/private/account/contract/zero_fee_rate", None)
    
    def get_history_positions(self, symbol: str, start_time: Optional[int] = None,
                               end_time: Optional[int] = None, page_num: Optional[int] = None,
                               page_size: Optional[int] = None) -> dict:
        logger.info(f"\n=== Get History Positions: {symbol} ===")
        params = {}
        if start_time:
            params['startTime'] = str(start_time)
        if end_time:
            params['endTime'] = str(end_time)
        if page_num:
            params['page_num'] = str(page_num)
        if page_size:
            params['page_size'] = str(page_size)
        return self.get_signed("/api/v1/private/position/list/history_positions", params)
    
    def get_open_positions(self, symbol: Optional[str] = None) -> dict:
        logger.info(f"\n=== Get Open Positions {symbol if symbol else ''} ===")
        params = {}
        if symbol:
            params['symbol'] = symbol
        return self.get_signed("/api/v1/private/position/open_positions", params)
    
    def get_funding_records(self, symbol: str, start_time: Optional[int] = None,
                             end_time: Optional[int] = None, page_num: Optional[int] = None,
                             page_size: Optional[int] = None) -> dict:
        logger.info(f"\n=== Get Funding Records: {symbol} ===")
        params = {'symbol': symbol}
        if start_time:
            params['startTime'] = str(start_time)
        if end_time:
            params['endTime'] = str(end_time)
        if page_num:
            params['page_num'] = str(page_num)
        if page_size:
            params['page_size'] = str(page_size)
        return self.get_signed("/api/v1/private/position/funding_records", params)
    
    def get_account_risk_limit(self) -> dict:
        logger.info("\n=== Get Account Risk Limit ===")
        return self.get_signed("/api/v1/private/account/risk_limit", None)
    
    def get_tiered_fee_rate_v2(self) -> dict:
        logger.info("\n=== Get Tiered Fee Rate V2 ===")
        return self.get_signed("/api/v1/private/account/tiered_fee_rate/v2", None)
    
    def get_tiered_fee_rate_by_symbol(self, symbol: str) -> dict:
        logger.info(f"\n=== Get Tiered Fee Rate for Symbol: {symbol} ===")
        params = {'symbol': symbol}
        return self.get_signed("/api/v1/private/account/tiered_fee_rate", params)
    
    # ==================== Position Management Endpoints ====================
    
    def change_position_margin(self, position_id: str, amount: str, type_: str) -> dict:
        logger.info(f"\n=== Change Position Margin: {position_id} {type_} {amount} ===")
        params = {
            'positionId': position_id,
            'amount': amount,
            'type': type_
        }
        return self.post_signed("/api/v1/private/position/change_margin", params)
    
    def change_auto_add_margin(self, position_id: str, auto_add: bool) -> dict:
        logger.info(f"\n=== Change Auto Add Margin: {position_id} {auto_add} ===")
        params = {
            'positionId': position_id,
            'isEnabled': str(auto_add).lower()
        }
        return self.post_signed("/api/v1/private/position/change_auto_add_im", params)
    
    def get_position_leverage(self, symbol: str) -> dict:
        logger.info(f"\n=== Get Position Leverage: {symbol} ===")
        params = {'symbol': symbol}
        return self.get_signed("/api/v1/private/position/leverage", params)
    
    def change_leverage(self, position_id: Optional[str] = None, symbol: Optional[str] = None,
                         leverage: int = 10, open_type: str = "2", position_type: str = "2") -> dict:
        logger.info(f"\n=== Change Leverage: {position_id or symbol} {leverage}x ===")
        params = {}
        if position_id:
            params['positionId'] = position_id
        elif symbol:
            params['symbol'] = symbol
        params['leverage'] = str(leverage)
        params['openType'] = open_type
        params['positionType'] = position_type
        return self.post_signed("/api/v1/private/position/change_leverage", params)
    
    def get_position_mode(self, symbol: Optional[str] = None) -> dict:
        logger.info(f"\n=== Get Position Mode {symbol if symbol else ''} ===")
        params = {}
        if symbol:
            params['symbol'] = symbol
        return self.get_signed("/api/v1/private/position/position_mode", params)
    
    def reverse_position(self, symbol: str, position_id: str, vol: str) -> dict:
        logger.info(f"\n=== Reverse Position: {symbol} {position_id} ===")
        params = {
            'symbol': symbol,
            'positionId': position_id,
            'vol': vol
        }
        return self.post_signed("/api/v1/private/position/reverse", params)
    
    def close_all_positions(self, symbol: Optional[str] = None) -> dict:
        logger.info(f"\n=== Close All Positions {symbol if symbol else ''} ===")
        params = {}
        if symbol:
            params['symbol'] = symbol
        return self.post_signed("/api/v1/private/position/close_all", params)
    
    def change_position_mode(self, position_mode: str) -> dict:
        logger.info(f"\n=== Change Position Mode: {position_mode} ===")
        params = {'positionMode': position_mode}
        return self.post_signed("/api/v1/private/position/change_position_mode", params)
    
    # ==================== Order Placement Endpoints ====================
    
    def place_order(self, symbol: str, type_: str, side: str, open_type: str,
                     leverage: int, price: Optional[str] = None, vol: str = "1",
                     position_mode: Optional[str] = None, reduce_only: Optional[bool] = None) -> dict:
        logger.info(f"\n=== Place Order: {symbol} {side} ===")
        params = {
            'symbol': symbol,
            'type': type_,
            'side': side,
            'openType': open_type,
            'leverage': str(leverage),
            'vol': vol
        }
        if price:
            params['price'] = price
        if position_mode:
            params['positionMode'] = position_mode
        if reduce_only is not None:
            params['reduceOnly'] = str(reduce_only).lower()
        return self.post_signed("/api/v1/private/order/submit", params)
    
    def place_batch_orders(self, orders: List[Any]) -> dict:
        logger.info(f"\n=== Place Batch Orders: {len(orders)} orders ===")
        return self.post_signed_with_array_body("/api/v1/private/order/submit_batch", orders)
    
    def chase_limit_order(self, order_id: str) -> dict:
        logger.info(f"\n=== Chase Limit Order: {order_id} ===")
        params = {'orderId': order_id}
        return self.post_signed("/api/v1/private/order/chase_limit_order", params)
    
    def change_limit_order(self, order_id: str, new_price: Optional[str] = None,
                            new_volume: Optional[str] = None) -> dict:
        logger.info(f"\n=== Change Limit Order: {order_id} ===")
        params = {'orderId': order_id}
        if new_price:
            params['price'] = new_price
        if new_volume:
            params['vol'] = new_volume
        return self.post_signed("/api/v1/private/order/change_limit_order", params)
    
    # ==================== Order Cancellation Endpoints ====================
    
    def cancel_order(self, order_ids: List[Any]) -> dict:
        logger.info(f"\n=== Cancel Order: {order_ids} ===")
        return self.post_signed_with_array_body("/api/v1/private/order/cancel", order_ids)
    
    def batch_cancel_with_external(self, params: List[Any]) -> dict:
        logger.info(f"\n=== Batch Cancel with External IDs: {params} ===")
        return self.post_signed_with_array_body("/api/v1/private/order/batch_cancel_with_external", params)
    
    def cancel_with_external(self, params: Dict[str, str]) -> dict:
        logger.info(f"\n=== Cancel with External ID: {params} ===")
        return self.post_signed("/api/v1/private/order/cancel_with_external", params)
    
    def cancel_all_orders(self, symbol: Optional[str] = None) -> dict:
        logger.info("\n=== Cancel All Orders ===")
        params = {}
        if symbol:
            params['symbol'] = symbol
        return self.post_signed("/api/v1/private/order/cancel_all", params)
    
    # ==================== Order Query Endpoints ====================
    
    def get_order_by_external_id(self, symbol: str, external_oid: str) -> dict:
        logger.info(f"\n=== Get Order by External ID: {external_oid} ===")
        endpoint = f"/api/v1/private/order/external/{symbol}/{external_oid}"
        return self.get_signed(endpoint, None)
    
    def get_order_by_id(self, order_id: str) -> dict:
        logger.info(f"\n=== Get Order by ID: {order_id} ===")
        endpoint = f"/api/v1/private/order/get/{order_id}"
        return self.get_signed(endpoint, None)
    
    def batch_query_orders(self, order_ids: str) -> dict:
        logger.info("\n=== Batch Query Orders ===")
        params = {'order_ids': order_ids}
        return self.get_signed("/api/v1/private/order/batch_query", params)
    
    def batch_query_with_external(self, params: List[Any]) -> dict:
        logger.info("\n=== Batch Query with External IDs ===")
        return self.post_signed_with_array_body("/api/v1/private/order/batch_query_with_external", params)
    
    def get_list_open_orders(self, symbol: Optional[str] = None) -> dict:
        logger.info("\n=== Get All Open Orders ===")
        params = {}
        if symbol:
            params['symbol'] = symbol
        return self.get_signed("/api/v1/private/order/list/open_orders", params)
    
    def get_history_orders(self, symbol: str, start_time: Optional[int] = None,
                            end_time: Optional[int] = None, page_num: Optional[int] = None,
                            page_size: Optional[int] = None, side: Optional[str] = None) -> dict:
        logger.info("\n=== Get History Orders ===")
        params = {'symbol': symbol}
        if start_time:
            params['startTime'] = str(start_time)
        if end_time:
            params['endTime'] = str(end_time)
        if page_num:
            params['page_num'] = str(page_num)
        if page_size:
            params['page_size'] = str(page_size)
        if side:
            params['side'] = side
        return self.get_signed("/api/v1/private/order/list/history_orders", params)
    
    def get_order_deals_v3(self, symbol: str, order_id: str, page_num: Optional[int] = None,
                            page_size: Optional[int] = None) -> dict:
        logger.info(f"\n=== Get Order Deals V3: {order_id} ===")
        params = {
            'symbol': symbol,
            'orderId': order_id
        }
        if page_num:
            params['page_num'] = str(page_num)
        if page_size:
            params['page_size'] = str(page_size)
        return self.get_signed("/api/v1/private/order/list/order_deals/v3", params)
    
    def get_deal_details_by_order_id(self, order_id: str) -> dict:
        logger.info(f"\n=== Get Deal Details by Order ID: {order_id} ===")
        endpoint = f"/api/v1/private/order/deal_details/{order_id}"
        return self.get_signed(endpoint, None)
    
    def get_deal_details(self, order_id: str) -> dict:
        logger.info(f"\n=== Get Deal Details: {order_id} ===")
        endpoint = f"/api/v1/private/order/deal_details/{order_id}"
        return self.get_signed(endpoint, None)
    
    def get_close_orders(self, symbol: str, start_time: Optional[int] = None,
                          end_time: Optional[int] = None, page_num: Optional[int] = None,
                          page_size: Optional[int] = None) -> dict:
        logger.info(f"\n=== Get Close Orders: {symbol} ===")
        params = {'symbol': symbol}
        if start_time:
            params['startTime'] = str(start_time)
        if end_time:
            params['endTime'] = str(end_time)
        if page_num:
            params['page_num'] = str(page_num)
        if page_size:
            params['page_size'] = str(page_size)
        return self.get_signed("/api/v1/private/order/list/close_orders", params)
    
    # ==================== Plan Order Endpoints ====================
    
    def get_plan_orders(self, symbol: str, page_num: Optional[int] = None,
                         page_size: Optional[int] = None) -> dict:
        logger.info(f"\n=== Get Plan Orders: {symbol} ===")
        params = {'symbol': symbol}
        if page_num:
            params['page_num'] = str(page_num)
        if page_size:
            params['page_size'] = str(page_size)
        return self.get_signed("/api/v1/private/planorder/list/orders", params)
    
    def place_plan_order_v2(self, symbol: str, side: str, open_type: str, leverage: int,
                              trigger_type: str, trigger_price: str, order_type: str,
                              price: str, volume: str, execute_cycle: str, trend: str) -> dict:
        logger.info(f"\n=== Place Plan Order V2: {symbol} {side} ===")
        params = {
            'symbol': symbol,
            'side': side,
            'openType': open_type,
            'leverage': str(leverage),
            'triggerType': trigger_type,
            'triggerPrice': trigger_price,
            'orderType': order_type,
            'price': price,
            'vol': volume,
            'executeCycle': execute_cycle,
            'trend': trend
        }
        return self.post_signed("/api/v1/private/planorder/place/v2", params)
    
    def change_plan_order_price(self, symbol: str, order_id: str, new_price: str,
                                  trigger_price: str, order_type: str) -> dict:
        logger.info(f"\n=== Change Plan Order Price: {order_id} ===")
        params = {
            'symbol': symbol,
            'orderId': order_id,
            'price': new_price,
            'triggerPrice': trigger_price,
            'orderType': order_type,
            'from': '1',
            'trend': '1'
        }
        return self.post_signed("/api/v1/private/planorder/change_price", params)
    
    def cancel_plan_order(self, params: List[Any]) -> dict:
        logger.info(f"\n=== Cancel Plan Order: {params} ===")
        return self.post_signed_with_array_body("/api/v1/private/planorder/cancel", params)
    
    def cancel_all_plan_orders(self, symbol: str) -> dict:
        logger.info(f"\n=== Cancel All Plan Orders: {symbol} ===")
        params = {'symbol': symbol}
        return self.post_signed("/api/v1/private/planorder/cancel_all", params)
    
    # ==================== Stop Order Endpoints ====================
    
    def place_stop_order(self, symbol: str, profit_trend: int, loss_trend: int,
                          position_id: str, vol: int, stop_loss_type: int,
                          stop_loss_order_price: str, stop_loss_price: str) -> dict:
        logger.info(f"\n=== Place Stop Order: {symbol} {stop_loss_type} ===")
        params = {
            'symbol': symbol,
            'profitTrend': str(profit_trend),
            'lossTrend': str(loss_trend),
            'positionId': position_id,
            'vol': str(vol),
            'stopLossType': str(stop_loss_type),
            'stopLossOrderPrice': stop_loss_order_price,
            'stopLossPrice': stop_loss_price
        }
        return self.post_signed("/api/v1/private/stoporder/place", params)
    
    def cancel_stop_order(self, symbol: str, order_id: str) -> dict:
        logger.info(f"\n=== Cancel Stop Order: {order_id} ===")
        params = {
            'symbol': symbol,
            'orderId': order_id
        }
        return self.post_signed("/api/v1/private/stoporder/cancel", params)
    
    def cancel_all_stop_orders(self, symbol: str) -> dict:
        logger.info(f"\n=== Cancel All Stop Orders: {symbol} ===")
        params = {'symbol': symbol}
        return self.post_signed("/api/v1/private/stoporder/cancel_all", params)
    
    def change_stop_order_price(self, order_id: str, new_stop_price: str) -> dict:
        logger.info(f"\n=== Change Stop Order Price: {order_id} ===")
        params = {
            'orderId': order_id,
            'takeProfitPrice': new_stop_price
        }
        return self.post_signed("/api/v1/private/stoporder/change_price", params)
    
    def change_stop_order_plan_price(self, symbol: str, order_id: str, new_plan_price: str) -> dict:
        logger.info(f"\n=== Change Stop Order Plan Price: {order_id} ===")
        params = {
            'stopPlanOrderId': order_id,
            'stopLossPrice': new_plan_price
        }
        return self.post_signed("/api/v1/private/stoporder/change_plan_price", params)
    
    def change_plan_order_stop_order(self, symbol: str, order_id: str, new_stop_price: Optional[str] = None) -> dict:
        logger.info(f"\n=== Change Plan Order Stop Order: {order_id} ===")
        params = {
            'symbol': symbol,
            'orderId': order_id
        }
        if new_stop_price:
            params['stopLossPrice'] = new_stop_price
        return self.post_signed("/api/v1/private/planorder/change_stop_order", params)
    
    def get_stop_orders_list(self, symbol: str, page_num: Optional[int] = None,
                               page_size: Optional[int] = None) -> dict:
        logger.info(f"\n=== Get Stop Orders List: {symbol} ===")
        params = {'symbol': symbol}
        if page_num:
            params['page_num'] = str(page_num)
        if page_size:
            params['page_size'] = str(page_size)
        return self.get_signed("/api/v1/private/stoporder/list/orders", params)
    
    def get_open_stop_orders(self, symbol: str) -> dict:
        logger.info(f"\n=== Get Open Stop Orders: {symbol} ===")
        params = {'symbol': symbol}
        return self.get_signed("/api/v1/private/stoporder/open_orders", params)
    
    # ==================== Trailing Stop Order Endpoints ====================
    
    def place_trailing_stop_order(self, symbol: str, leverage: int, side: int, vol: str,
                                    open_type: int, trend: int, active_price: Optional[str],
                                    back_type: int, back_value: str, position_mode: int,
                                    reduce_only: Optional[bool] = None) -> dict:
        logger.info(f"\n=== Place Trailing Stop Order: {symbol} ===")
        logger.info(f"Side: {side}, Volume: {vol}, Leverage: {leverage}")
        
        params = {
            'symbol': symbol,
            'leverage': str(leverage),
            'side': str(side),
            'vol': vol,
            'openType': str(open_type),
            'trend': str(trend),
            'backType': str(back_type),
            'backValue': back_value,
            'positionMode': str(position_mode)
        }
        if active_price:
            params['activePrice'] = active_price
        if reduce_only is not None:
            params['reduceOnly'] = str(reduce_only).lower()
        
        return self.post_signed("/api/v1/private/trackorder/place", params)
    
    def cancel_trailing_stop_order(self, symbol: str, order_id: str) -> dict:
        logger.info(f"\n=== Cancel Trailing Stop Order: {order_id} ===")
        params = {
            'symbol': symbol,
            'orderId': order_id
        }
        return self.post_signed("/api/v1/private/trackorder/cancel", params)
    
    def change_trailing_stop_order(self, symbol: str, order_id: str, new_activation_price: Optional[str] = None) -> dict:
        logger.info(f"\n=== Change Trailing Stop Order: {order_id} ===")
        params = {
            'symbol': symbol,
            'trackOrderId': order_id,
            'trend': '1',
            'backType': '1',
            'backValue': '0.3',
            'vol': '1'
        }
        if new_activation_price:
            params['activePrice'] = new_activation_price
        return self.post_signed("/api/v1/private/trackorder/change_order", params)
    
    def get_trailing_stop_orders_list(self, symbol: str, page_num: Optional[int] = None,
                                        page_size: Optional[int] = None) -> dict:
        logger.info(f"\n=== Get Trailing Stop Orders List: {symbol} ===")
        params = {
            'symbol': symbol,
            'states': '0'
        }
        if page_num:
            params['page_num'] = str(page_num)
        if page_size:
            params['page_size'] = str(page_size)
        return self.get_signed("/api/v1/private/trackorder/list/orders", params)
    
    def get_order_fee_details_by_client_id(self, symbol: str, client_order_id: str) -> dict:
        logger.info(f"\n=== Get Order Fee Details by Client ID: {client_order_id} ===")
        params = {
            'symbol': symbol,
            'clientOrderId': client_order_id
        }
        return self.get_signed("/api/v1/private/order/fee_details", params)