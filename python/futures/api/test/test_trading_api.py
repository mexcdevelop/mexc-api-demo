# tests/test_trading_api.py
"""
Comprehensive test suite for MexcAccountTradingApi
"""
import os
import sys
import time
import pytest
from typing import Generator
from dotenv import load_dotenv

# Add project root to Python path
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from src.trading_api import MexcAccountTradingApi

# Load environment variables
load_dotenv()

# Test constants
TEST_SYMBOL = "BTC_USDT"
TEST_CURRENCY = "USDT"
TEST_ORDER_ID = "123456789"
TEST_CLIENT_ORDER_ID = f"test-order-{int(time.time())}"
TEST_POSITION_ID = "123456789"
TEST_EXTERNAL_OID = f"ext-{int(time.time())}"
TEST_PAGE_NUM = 1
TEST_PAGE_SIZE = 20
TEST_LEVERAGE = 10
TEST_PRICE = "50000"
TEST_VOLUME = "1"
TEST_AMOUNT = "100"
TEST_STOP_PRICE = "20000"
TEST_ACTIVATION_PRICE = "55000"
TEST_BACK_VALUE = "0.2"


@pytest.fixture(scope="session")
def api_credentials() -> tuple:
    """Get API credentials from environment variables"""
    api_key = os.getenv("MEXC_API_KEY")
    secret_key = os.getenv("MEXC_SECRET_KEY")
    if not api_key or not secret_key:
        pytest.skip("MEXC_API_KEY and MEXC_SECRET_KEY must be set in environment")
    
    return api_key, secret_key


@pytest.fixture
def client(api_credentials: tuple) -> Generator[MexcAccountTradingApi, None, None]:
    """Create MexcAccountTradingApi client instance"""
    api_key, secret_key = api_credentials
    client = MexcAccountTradingApi(api_key, secret_key)
    yield client


# =========================================================================
# 1. ACCOUNT INFORMATION & ASSETS TESTS
# =========================================================================

class TestAccountInfo:
    """Test account information and assets endpoints"""
    
    def test_get_all_account_assets(self, client: MexcAccountTradingApi):
        """Test get all account assets"""
        response = client.get_all_account_assets()
        assert response is not None
        assert response.get("success") is True
        print(f"\nAll account assets: {response}")
    
    def test_get_single_asset(self, client: MexcAccountTradingApi):
        """Test get single currency asset"""
        response = client.get_single_asset(TEST_CURRENCY)
        assert response is not None
        assert response.get("success") is True
        print(f"\n{TEST_CURRENCY} asset: {response}")
    
    def test_get_transfer_record(self, client: MexcAccountTradingApi):
        """Test get asset transfer records"""
        response = client.get_transfer_record(None, None, None, TEST_PAGE_NUM, TEST_PAGE_SIZE)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_transfer_record_with_filters(self, client: MexcAccountTradingApi):
        """Test get asset transfer records with filters"""
        response = client.get_transfer_record(TEST_CURRENCY, "SUCCESS", "IN", 
                                               TEST_PAGE_NUM, TEST_PAGE_SIZE)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_profit_rate_daily(self, client: MexcAccountTradingApi):
        """Test get personal profit rate - daily"""
        response = client.get_profit_rate(1)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_profit_rate_weekly(self, client: MexcAccountTradingApi):
        """Test get personal profit rate - weekly"""
        response = client.get_profit_rate(2)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_account_config(self, client: MexcAccountTradingApi):
        """Test get account configuration"""
        response = client.get_account_config()
        assert response is not None
        assert response.get("success") is True
    
    def test_get_account_discount_type(self, client: MexcAccountTradingApi):
        """Test get account discount type"""
        response = client.get_account_discount_type()
        assert response is not None
        assert response.get("success") is True


# =========================================================================
# 2. ASSET ANALYSIS & PNL TESTS
# =========================================================================

class TestAssetAnalysis:
    """Test asset analysis and PnL endpoints"""
    
    def test_get_asset_analysis_this_week(self, client: MexcAccountTradingApi):
        """Test get asset analysis - this week"""
        response = client.get_asset_analysis(1, TEST_CURRENCY, None, None)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_asset_analysis_this_month(self, client: MexcAccountTradingApi):
        """Test get asset analysis - this month"""
        response = client.get_asset_analysis(2, TEST_CURRENCY, None, None)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_asset_analysis_all_time(self, client: MexcAccountTradingApi):
        """Test get asset analysis - all time"""
        response = client.get_asset_analysis(3, TEST_CURRENCY, None, None)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_asset_analysis_custom(self, client: MexcAccountTradingApi):
        """Test get asset analysis - custom range"""
        end_time = int(time.time() * 1000)
        start_time = end_time - 30 * 24 * 60 * 60 * 1000
        response = client.get_asset_analysis(4, TEST_CURRENCY, start_time, end_time)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_yesterday_pnl(self, client: MexcAccountTradingApi):
        """Test get yesterday's PnL"""
        response = client.get_yesterday_pnl()
        assert response is not None
        assert response.get("success") is True
    
    def test_post_asset_analysis_v3(self, client: MexcAccountTradingApi):
        """Test post asset analysis V3"""
        end_time = int(time.time() * 1000)
        start_time = end_time - 30 * 24 * 60 * 60 * 1000
        response = client.post_asset_analysis_v3(start_time, end_time, 1, 1, TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True
    
    def test_post_calendar_daily_v3(self, client: MexcAccountTradingApi):
        """Test post daily calendar analysis V3"""
        end_time = int(time.time() * 1000)
        start_time = end_time - 30 * 24 * 60 * 60 * 1000
        response = client.post_calendar_daily_v3(start_time, end_time, 1, 1)
        assert response is not None
        assert response.get("success") is True
    
    def test_post_calendar_monthly_v3(self, client: MexcAccountTradingApi):
        """Test post monthly calendar analysis V3"""
        response = client.post_calendar_monthly_v3(1, 1)
        assert response is not None
        assert response.get("success") is True
    
    def test_post_recent_analysis_v3(self, client: MexcAccountTradingApi):
        """Test post recent analysis V3"""
        response = client.post_recent_analysis_v3(1, 1, TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_today_pnl(self, client: MexcAccountTradingApi):
        """Test get today's PnL"""
        response = client.get_today_pnl()
        assert response is not None
        assert response.get("success") is True
    
    def test_export_asset_analysis(self, client: MexcAccountTradingApi):
        """Test export asset analysis"""
        end_time = int(time.time() * 1000)
        start_time = end_time - 30 * 24 * 60 * 60 * 1000
        response = client.export_asset_analysis(start_time, end_time, TEST_SYMBOL, 1, 1)
        assert response is not None
        assert response.get("success") is True


# =========================================================================
# 3. FEE & RATE INFORMATION TESTS
# =========================================================================

class TestFeeRateInfo:
    """Test fee and rate information endpoints"""
    
    def test_get_fee_deduct_configs(self, client: MexcAccountTradingApi):
        """Test get fee deduction configurations"""
        response = client.get_fee_deduct_configs()
        assert response is not None
        assert response.get("success") is True
    
    def test_get_tiered_fee_rate_v2(self, client: MexcAccountTradingApi):
        """Test get tiered fee rate V2"""
        response = client.get_tiered_fee_rate_v2()
        assert response is not None
        assert response.get("success") is True
    
    def test_get_tiered_fee_rate_by_symbol(self, client: MexcAccountTradingApi):
        """Test get tiered fee rate by symbol"""
        response = client.get_tiered_fee_rate_by_symbol(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_contract_fee_discount_config(self, client: MexcAccountTradingApi):
        """Test get contract fee discount configuration"""
        response = client.get_contract_fee_discount_config()
        assert response is not None
        assert response.get("success") is True
    
    def test_get_order_fee_details(self, client: MexcAccountTradingApi):
        """Test get order fee details"""
        response = client.get_order_fee_details(TEST_SYMBOL, TEST_ORDER_ID)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_total_order_deal_fee(self, client: MexcAccountTradingApi):
        """Test get total order deal fee"""
        end_time = int(time.time() * 1000)
        start_time = end_time - 30 * 24 * 60 * 60 * 1000
        response = client.get_total_order_deal_fee(start_time, end_time, TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_contract_fee_rate(self, client: MexcAccountTradingApi):
        """Test get contract fee rate"""
        response = client.get_contract_fee_rate(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_zero_fee_rate_contracts(self, client: MexcAccountTradingApi):
        """Test get zero fee rate contracts"""
        response = client.get_zero_fee_rate_contracts()
        assert response is not None
        assert response.get("success") is True
    
    def test_get_account_risk_limit(self, client: MexcAccountTradingApi):
        """Test get account risk limit"""
        response = client.get_account_risk_limit()
        assert response is not None
        assert response.get("success") is True


# =========================================================================
# 4. POSITION MANAGEMENT TESTS
# =========================================================================

class TestPositionManagement:
    """Test position management endpoints"""
    
    def test_get_history_positions(self, client: MexcAccountTradingApi):
        """Test get history positions"""
        end_time = int(time.time() * 1000)
        start_time = end_time - 30 * 24 * 60 * 60 * 1000
        response = client.get_history_positions(TEST_SYMBOL, start_time, end_time, 
                                                 TEST_PAGE_NUM, TEST_PAGE_SIZE)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_open_positions(self, client: MexcAccountTradingApi):
        """Test get open positions"""
        response = client.get_open_positions(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_funding_records(self, client: MexcAccountTradingApi):
        """Test get funding records"""
        end_time = int(time.time() * 1000)
        start_time = end_time - 30 * 24 * 60 * 60 * 1000
        response = client.get_funding_records(TEST_SYMBOL, start_time, end_time,
                                               TEST_PAGE_NUM, TEST_PAGE_SIZE)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_position_leverage(self, client: MexcAccountTradingApi):
        """Test get position leverage"""
        response = client.get_position_leverage(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_position_mode(self, client: MexcAccountTradingApi):
        """Test get position mode"""
        response = client.get_position_mode(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True
    
    def test_change_position_margin_add(self, client: MexcAccountTradingApi):
        """Test change position margin - ADD"""
        response = client.change_position_margin(TEST_POSITION_ID, TEST_AMOUNT, "ADD")
        assert response is not None
        assert response.get("success") is True
    
    def test_change_position_margin_sub(self, client: MexcAccountTradingApi):
        """Test change position margin - SUB"""
        response = client.change_position_margin(TEST_POSITION_ID, TEST_AMOUNT, "SUB")
        assert response is not None
        assert response.get("success") is True
    
    def test_change_auto_add_margin_enable(self, client: MexcAccountTradingApi):
        """Test change auto add margin - enable"""
        response = client.change_auto_add_margin(TEST_POSITION_ID, True)
        assert response is not None
        assert response.get("success") is True
    
    def test_change_auto_add_margin_disable(self, client: MexcAccountTradingApi):
        """Test change auto add margin - disable"""
        response = client.change_auto_add_margin(TEST_POSITION_ID, False)
        assert response is not None
        assert response.get("success") is True
    
    def test_change_leverage(self, client: MexcAccountTradingApi):
        """Test change leverage"""
        response = client.change_leverage(position_id=TEST_POSITION_ID, symbol=TEST_SYMBOL,
                                           leverage=TEST_LEVERAGE, open_type="2", 
                                           position_type="2")
        assert response is not None
        assert response.get("success") is True
    
    def test_reverse_position(self, client: MexcAccountTradingApi):
        """Test reverse position"""
        response = client.reverse_position(TEST_SYMBOL, TEST_POSITION_ID, TEST_VOLUME)
        assert response is not None
        assert response.get("success") is True
    
    def test_close_all_positions(self, client: MexcAccountTradingApi):
        """Test close all positions"""
        response = client.close_all_positions(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True


# =========================================================================
# 5. ORDER PLACEMENT TESTS
# =========================================================================

class TestOrderPlacement:
    """Test order placement endpoints"""
    
    def test_place_order(self, client: MexcAccountTradingApi):
        """Test place order"""
        response = client.place_order(
            TEST_SYMBOL, "1", "1", "1",
            TEST_LEVERAGE, TEST_PRICE, TEST_VOLUME, "1", False
        )
        assert response is not None
        assert response.get("success") is True
    
    def test_chase_limit_order(self, client: MexcAccountTradingApi):
        """Test chase limit order"""
        response = client.chase_limit_order(TEST_ORDER_ID)
        assert response is not None
        assert response.get("success") is True
    
    def test_change_limit_order(self, client: MexcAccountTradingApi):
        """Test change limit order"""
        response = client.change_limit_order(TEST_ORDER_ID, "51000", "2")
        assert response is not None
        assert response.get("success") is True


# =========================================================================
# 6. ORDER CANCELLATION TESTS
# =========================================================================

class TestOrderCancellation:
    """Test order cancellation endpoints"""
    
    def test_cancel_order(self, client: MexcAccountTradingApi):
        """Test cancel order"""
        order_ids = [TEST_ORDER_ID]
        response = client.cancel_order(order_ids)
        assert response is not None
        assert response.get("success") is True
    
    def test_cancel_all_orders(self, client: MexcAccountTradingApi):
        """Test cancel all orders"""
        response = client.cancel_all_orders(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True


# =========================================================================
# 7. ORDER QUERY TESTS
# =========================================================================

class TestOrderQuery:
    """Test order query endpoints"""
    
    def test_get_order_by_id(self, client: MexcAccountTradingApi):
        """Test get order by ID"""
        response = client.get_order_by_id(TEST_ORDER_ID)
        assert response is not None
        assert response.get("success") is True
    
    def test_batch_query_orders(self, client: MexcAccountTradingApi):
        """Test batch query orders"""
        response = client.batch_query_orders(TEST_ORDER_ID)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_list_open_orders(self, client: MexcAccountTradingApi):
        """Test get all open orders"""
        response = client.get_list_open_orders(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_history_orders(self, client: MexcAccountTradingApi):
        """Test get history orders"""
        end_time = int(time.time() * 1000)
        start_time = end_time - 30 * 24 * 60 * 60 * 1000
        response = client.get_history_orders(TEST_SYMBOL, start_time, end_time,
                                              TEST_PAGE_NUM, TEST_PAGE_SIZE, None)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_order_deals_v3(self, client: MexcAccountTradingApi):
        """Test get order deals V3"""
        response = client.get_order_deals_v3(TEST_SYMBOL, TEST_ORDER_ID,
                                              TEST_PAGE_NUM, TEST_PAGE_SIZE)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_deal_details(self, client: MexcAccountTradingApi):
        """Test get deal details"""
        response = client.get_deal_details(TEST_ORDER_ID)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_close_orders(self, client: MexcAccountTradingApi):
        """Test get close orders"""
        end_time = int(time.time() * 1000)
        start_time = end_time - 30 * 24 * 60 * 60 * 1000
        response = client.get_close_orders(TEST_SYMBOL, start_time, end_time,
                                            TEST_PAGE_NUM, TEST_PAGE_SIZE)
        assert response is not None
        assert response.get("success") is True


# =========================================================================
# 8. PLAN ORDER TESTS
# =========================================================================

class TestPlanOrders:
    """Test plan order endpoints"""
    
    def test_get_plan_orders(self, client: MexcAccountTradingApi):
        """Test get plan orders"""
        response = client.get_plan_orders(TEST_SYMBOL, TEST_PAGE_NUM, TEST_PAGE_SIZE)
        assert response is not None
        assert response.get("success") is True
    
    def test_place_plan_order_v2(self, client: MexcAccountTradingApi):
        """Test place plan order V2"""
        response = client.place_plan_order_v2(
            TEST_SYMBOL, "1", "1", TEST_LEVERAGE,
            "2", "50000", "5", "50000", TEST_VOLUME, "1", "1"
        )
        assert response is not None
        assert response.get("success") is True
    
    def test_cancel_all_plan_orders(self, client: MexcAccountTradingApi):
        """Test cancel all plan orders"""
        response = client.cancel_all_plan_orders(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True


# =========================================================================
# 9. STOP ORDER TESTS
# =========================================================================

class TestStopOrders:
    """Test stop order endpoints"""
    
    def test_place_stop_order(self, client: MexcAccountTradingApi):
        """Test place stop order"""
        response = client.place_stop_order(
            TEST_SYMBOL, 1, 1, TEST_POSITION_ID, 1, 1, 
            TEST_STOP_PRICE, TEST_STOP_PRICE
        )
        assert response is not None
        assert response.get("success") is True
    
    def test_get_stop_orders_list(self, client: MexcAccountTradingApi):
        """Test get stop orders list"""
        response = client.get_stop_orders_list(TEST_SYMBOL, TEST_PAGE_NUM, TEST_PAGE_SIZE)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_open_stop_orders(self, client: MexcAccountTradingApi):
        """Test get open stop orders"""
        response = client.get_open_stop_orders(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True


# =========================================================================
# 10. TRAILING STOP ORDER TESTS
# =========================================================================

class TestTrailingStopOrders:
    """Test trailing stop order endpoints"""
    
    def test_place_trailing_stop_order(self, client: MexcAccountTradingApi):
        """Test place trailing stop order"""
        response = client.place_trailing_stop_order(
            TEST_SYMBOL, 3, 1, TEST_VOLUME, 1, 1, 
            TEST_ACTIVATION_PRICE, 1, TEST_BACK_VALUE, 1, True
        )
        assert response is not None
        assert response.get("success") is True
    
    def test_get_trailing_stop_orders_list(self, client: MexcAccountTradingApi):
        """Test get trailing stop orders list"""
        response = client.get_trailing_stop_orders_list(TEST_SYMBOL, TEST_PAGE_NUM, TEST_PAGE_SIZE)
        assert response is not None
        assert response.get("success") is True


# =========================================================================
# 11. PARAMETERIZED TESTS
# =========================================================================

class TestParameterized:
    """Parameterized tests for various endpoints"""
    
    @pytest.mark.parametrize("currency", ["USDT", "BTC", "ETH"])
    def test_get_single_asset_parameterized(self, client: MexcAccountTradingApi, currency: str):
        """Test get single asset with different currencies"""
        response = client.get_single_asset(currency)
        assert response is not None
        assert response.get("success") is True
    
    @pytest.mark.parametrize("profit_rate_type", [1, 2])
    def test_get_profit_rate_parameterized(self, client: MexcAccountTradingApi, profit_rate_type: int):
        """Test get profit rate with different types"""
        response = client.get_profit_rate(profit_rate_type)
        assert response is not None
        assert response.get("success") is True
    
    @pytest.mark.parametrize("analysis_type", [1, 2, 3])
    def test_get_asset_analysis_parameterized(self, client: MexcAccountTradingApi, analysis_type: int):
        """Test get asset analysis with different types"""
        end_time = int(time.time() * 1000)
        start_time = end_time - 30 * 24 * 60 * 60 * 1000
        response = client.get_asset_analysis(
            analysis_type, TEST_CURRENCY,
            start_time if analysis_type == 4 else None,
            end_time if analysis_type == 4 else None
        )
        assert response is not None
        assert response.get("success") is True
    
    @pytest.mark.parametrize("symbol", ["BTC_USDT", "ETH_USDT", "BNB_USDT"])
    def test_get_contract_fee_rate_parameterized(self, client: MexcAccountTradingApi, symbol: str):
        """Test get contract fee rate with different symbols"""
        response = client.get_contract_fee_rate(symbol)
        assert response is not None
        assert response.get("success") is True


# =========================================================================
# 12. ERROR HANDLING TESTS
# =========================================================================

class TestErrorHandling:
    """Test error handling scenarios"""
    
    def test_get_single_asset_invalid_currency(self, client: MexcAccountTradingApi):
        """Test get single asset with invalid currency"""
        response = client.get_single_asset("INVALID_CURRENCY")
        assert response is not None
    
    def test_get_transfer_record_invalid_page_size(self, client: MexcAccountTradingApi):
        """Test get transfer record with invalid page size"""
        response = client.get_transfer_record(None, None, None, 1, 1000)
        assert response is not None
    
    def test_place_order_invalid_symbol(self, client: MexcAccountTradingApi):
        """Test place order with invalid symbol"""
        response = client.place_order(
            "INVALID_SYMBOL", "LIMIT", "BUY", "ISOLATED",
            TEST_LEVERAGE, TEST_PRICE, TEST_VOLUME, "ONE_WAY", False
        )
        assert response is not None
    
    def test_get_order_detail_invalid_order_id(self, client: MexcAccountTradingApi):
        """Test get order detail with invalid order ID"""
        response = client.get_order_by_id("invalid-order-id")
        assert response is not None


# =========================================================================
# 13. NULL PARAMETER TESTS
# =========================================================================

class TestNullParameters:
    """Test endpoints with null parameters"""
    
    def test_get_open_orders_null_symbol(self, client: MexcAccountTradingApi):
        """Test get open orders with null symbol"""
        response = client.get_list_open_orders(None)
        assert response is not None
        assert response.get("success") is True
    
    def test_get_open_positions_null_symbol(self, client: MexcAccountTradingApi):
        """Test get open positions with null symbol"""
        response = client.get_open_positions(None)
        assert response is not None
        assert response.get("success") is True
    
    def test_cancel_all_orders_null_symbol(self, client: MexcAccountTradingApi):
        """Test cancel all orders with null symbol"""
        response = client.cancel_all_orders(None)
        assert response is not None
        assert response.get("success") is True


# =========================================================================
# 14. BATCH ORDER TESTS
# =========================================================================

class TestBatchOrders:
    """Test batch order operations"""
    
    def test_place_batch_orders(self, client: MexcAccountTradingApi):
        """Test place batch orders"""
        order = {
            "symbol": TEST_SYMBOL,
            "price": TEST_PRICE,
            "vol": TEST_VOLUME,
            "leverage": TEST_LEVERAGE,
            "side": "1",
            "type": "2",
            "openType": "2",
            "externalOid": f"batch-{int(time.time())}"
        }
        orders = [order]
        response = client.place_batch_orders(orders)
        assert response is not None
        assert response.get("success") is True
    
    def test_batch_cancel_with_external(self, client: MexcAccountTradingApi):
        """Test batch cancel with external IDs"""
        params = [{
            "symbol": TEST_SYMBOL,
            "externalOid": TEST_EXTERNAL_OID
        }]
        response = client.batch_cancel_with_external(params)
        assert response is not None
        assert response.get("success") is True
    
    def test_cancel_with_external(self, client: MexcAccountTradingApi):
        """Test cancel with external ID"""
        params = {
            "symbol": TEST_SYMBOL,
            "externalOid": TEST_EXTERNAL_OID
        }
        response = client.cancel_with_external(params)
        assert response is not None
        assert response.get("success") is True
    
    def test_batch_query_with_external(self, client: MexcAccountTradingApi):
        """Test batch query with external IDs"""
        params = [{
            "symbol": TEST_SYMBOL,
            "externalOid": TEST_EXTERNAL_OID
        }]
        response = client.batch_query_with_external(params)
        assert response is not None
        assert response.get("success") is True

    if __name__ == "__main__":
        """Run tests directly"""
    pytest.main([__file__, "-v", "--capture=no"])       