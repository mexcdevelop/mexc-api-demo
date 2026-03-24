# tests/test_market_api.py
"""
Comprehensive test suite for MexcMarketDataApi
"""
import os
import sys
import time
import pytest
from typing import Generator, Dict, Any

# Add project root to Python path
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from src.market_api import MexcMarketDataApi

# Test constants
TEST_SYMBOL = "BTC_USDT"
TEST_INVALID_SYMBOL = "INVALID_SYMBOL"
TEST_INTERVAL_MS = "Min15"
TEST_INTERVAL_S = "Min15"
TEST_PAGE_NUM = 1
TEST_PAGE_SIZE = 20
TEST_LIMIT = 10
TEST_LARGE_LIMIT = 1000


@pytest.fixture
def client() -> Generator[MexcMarketDataApi, None, None]:
    """Create MexcMarketDataApi client instance"""
    client = MexcMarketDataApi()
    yield client


# =========================================================================
# 1. BASIC MARKET ENDPOINTS TESTS
# =========================================================================

class TestBasicMarketEndpoints:
    """Test basic market endpoints"""
    
    def test_ping(self, client: MexcMarketDataApi):
        """Test ping - get server time"""
        response = client.ping()
        assert response is not None
        assert response.get("success") is True
        assert response.get("data") is not None
        print(f"\nServer timestamp: {response.get('data')}")
    
    def test_get_contract_detail_all(self, client: MexcMarketDataApi):
        """Test get contract detail - all contracts"""
        response = client.get_contract_detail(None)
        assert response is not None
        assert response.get("success") is True
        data = response.get("data")
        assert isinstance(data, list)
        assert len(data) > 0
        print(f"\nRetrieved {len(data)} contracts")
    
    def test_get_contract_detail_by_symbol(self, client: MexcMarketDataApi):
        """Test get contract detail - by symbol"""
        response = client.get_contract_detail(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True
        data = response.get("data")
        assert isinstance(data, dict)
        assert data.get("symbol") == TEST_SYMBOL
        print(f"\nContract detail for {TEST_SYMBOL}:")
        print(f"  Base Coin: {data.get('baseCoin')}")
        print(f"  Quote Coin: {data.get('quoteCoin')}")
        print(f"  Max Leverage: {data.get('maxLeverage')}")
    
    def test_get_support_currencies(self, client: MexcMarketDataApi):
        """Test get support currencies"""
        response = client.get_support_currencies()
        assert response is not None
        assert response.get("success") is True
        currencies = response.get("data", [])
        assert isinstance(currencies, list)
        assert len(currencies) > 0
        print(f"\nSupported {len(currencies)} currencies")
        print(f"First 10: {currencies[:10]}")


# =========================================================================
# 2. ORDER BOOK ENDPOINTS TESTS
# =========================================================================

class TestOrderBookEndpoints:
    """Test order book endpoints"""
    
    def test_get_depth_default(self, client: MexcMarketDataApi):
        """Test get depth - default limit"""
        response = client.get_depth(TEST_SYMBOL, None)
        assert response is not None
        assert response.get("success") is True
        data = response.get("data", {})
        assert "bids" in data
        assert "asks" in data
        print(f"\nOrder book version: {data.get('version')}")
        print(f"Bids: {len(data.get('bids', []))} levels")
        print(f"Asks: {len(data.get('asks', []))} levels")
    
    def test_get_depth_with_limit(self, client: MexcMarketDataApi):
        """Test get depth - with limit"""
        response = client.get_depth(TEST_SYMBOL, TEST_LIMIT)
        assert response is not None
        assert response.get("success") is True
        data = response.get("data", {})
        bids = data.get('bids', [])
        asks = data.get('asks', [])
        assert len(bids) <= TEST_LIMIT
        assert len(asks) <= TEST_LIMIT
        print(f"\nTop {TEST_LIMIT} bids: {bids}")
        print(f"Top {TEST_LIMIT} asks: {asks}")
    
    def test_get_depth_commits(self, client: MexcMarketDataApi):
        """Test get depth commits"""
        limit = 5
        response = client.get_depth_commits(TEST_SYMBOL, limit)
        assert response is not None
        assert response.get("success") is True
        snapshots = response.get("data", [])
        assert len(snapshots) <= limit
        print(f"\nRetrieved {len(snapshots)} depth snapshots")
        if snapshots:
            print(f"First snapshot version: {snapshots[0].get('version')}")


# =========================================================================
# 3. PRICE INDEX ENDPOINTS TESTS
# =========================================================================

class TestPriceIndexEndpoints:
    """Test price index endpoints"""
    
    def test_get_index_price(self, client: MexcMarketDataApi):
        """Test get index price"""
        response = client.get_index_price(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True
        data = response.get("data", {})
        assert data.get("symbol") == TEST_SYMBOL
        assert data.get("indexPrice") is not None
        print(f"\n{TEST_SYMBOL} Index Price: {data.get('indexPrice')}")
    
    def test_get_fair_price(self, client: MexcMarketDataApi):
        """Test get fair price"""
        response = client.get_fair_price(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True
        data = response.get("data", {})
        assert data.get("symbol") == TEST_SYMBOL
        assert data.get("fairPrice") is not None
        print(f"\n{TEST_SYMBOL} Fair Price: {data.get('fairPrice')}")


# =========================================================================
# 4. FUNDING RATE ENDPOINTS TESTS
# =========================================================================

class TestFundingRateEndpoints:
    """Test funding rate endpoints"""
    
    def test_get_funding_rate(self, client: MexcMarketDataApi):
        """Test get funding rate"""
        response = client.get_funding_rate(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True
        data = response.get("data", {})
        assert data.get("symbol") == TEST_SYMBOL
        print(f"\n{TEST_SYMBOL} Funding Rate: {data.get('fundingRate')}")
        print(f"Next Settlement: {data.get('nextSettleTime')}")
    
    def test_get_funding_rate_history(self, client: MexcMarketDataApi):
        """Test get funding rate history"""
        response = client.get_funding_rate_history(TEST_SYMBOL, 10)
        assert response is not None
        assert response.get("success") is True
        print(f"\nFunding rate history retrieved")


# =========================================================================
# 5. KLINE ENDPOINTS TESTS
# =========================================================================

class TestKlineEndpoints:
    """Test kline endpoints"""
    
    def test_get_kline(self, client: MexcMarketDataApi):
        """Test get kline data"""
        end_time = int(time.time())
        start_time = end_time - 7 * 24 * 60 * 60# 7 days ago
        
        response = client.get_kline(TEST_SYMBOL, TEST_INTERVAL_MS, 
                                     start_time, end_time, 100)
        assert response is not None
        assert response.get("success") is True
        print(f"\nKline data retrieved")
    
    def test_get_index_price_kline(self, client: MexcMarketDataApi):
        """Test get index price kline"""
        end_time = int(time.time())
        start_time = end_time - 7 * 24 * 60 * 60  # 7 days ago in seconds
        
        response = client.get_index_price_kline(TEST_SYMBOL, TEST_INTERVAL_S,
                                                 start_time, end_time)
        assert response is not None
        assert response.get("success") is True
        print(f"\nIndex price kline retrieved")
    
    def test_get_fair_price_kline(self, client: MexcMarketDataApi):
        """Test get fair price kline"""
        end_time = int(time.time())
        start_time = end_time - 7 * 24 * 60 * 60  # 7 days ago in seconds
        
        response = client.get_fair_price_kline(TEST_SYMBOL, TEST_INTERVAL_S,
                                                start_time, end_time)
        assert response is not None
        assert response.get("success") is True
        print(f"\nFair price kline retrieved")


# =========================================================================
# 6. TRADE ENDPOINTS TESTS
# =========================================================================

class TestTradeEndpoints:
    """Test trade endpoints"""
    
    def test_get_recent_deals(self, client: MexcMarketDataApi):
        """Test get recent deals"""
        response = client.get_recent_deals(TEST_SYMBOL, 20)
        assert response is not None
        assert response.get("success") is True
        deals = response.get("data", [])
        print(f"\nRetrieved {len(deals)} recent deals")
        if deals:
            print(f"Most recent: {deals[0]}")
    
    def test_get_recent_deals_default_limit(self, client: MexcMarketDataApi):
        """Test get recent deals with default limit"""
        response = client.get_recent_deals(TEST_SYMBOL, None)
        assert response is not None
        assert response.get("success") is True


# =========================================================================
# 7. TICKER ENDPOINTS TESTS
# =========================================================================

class TestTickerEndpoints:
    """Test ticker endpoints"""
    
    def test_get_all_tickers(self, client: MexcMarketDataApi):
        """Test get all tickers"""
        response = client.get_all_tickers()
        assert response is not None
        assert response.get("success") is True
        tickers = response.get("data", [])
        assert len(tickers) > 0
        print(f"\nRetrieved tickers for {len(tickers)} symbols")
        
        # Verify BTC ticker exists
        btc_ticker = None
        for ticker in tickers:
            if ticker.get('symbol') == TEST_SYMBOL:
                btc_ticker = ticker
                break
        assert btc_ticker is not None
        print(f"\n{TEST_SYMBOL}:")
        print(f"  Last: {btc_ticker.get('lastPrice')}")
        print(f"  Volume: {btc_ticker.get('volume24')}")


# =========================================================================
# 8. RISK REVERSE ENDPOINTS TESTS
# =========================================================================

class TestRiskReverseEndpoints:
    """Test risk reverse endpoints"""
    
    def test_get_risk_reverse_by_symbol(self, client: MexcMarketDataApi):
        """Test get risk reverse by symbol"""
        response = client.get_risk_reverse_by_symbol(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True
        data = response.get("data", {})
        print(f"\n{TEST_SYMBOL} Risk Reverse:")
        print(f"  Amount: {data.get('available')}")
        print(f"  Update Time: {data.get('timestamp')}")
    
    def test_get_risk_reverse_history(self, client: MexcMarketDataApi):
        """Test get risk reverse history"""
        response = client.get_risk_reverse_history(TEST_SYMBOL, 
                                                    TEST_PAGE_NUM, 
                                                    TEST_PAGE_SIZE)
        assert response is not None
        assert response.get("success") is True
        print(f"\nRisk reverse history retrieved")


# =========================================================================
# 9. PARAMETERIZED TESTS
# =========================================================================

class TestParameterized:
    """Parameterized tests for market endpoints"""
    
    @pytest.mark.parametrize("symbol", [
        "BTC_USDT", 
        "ETH_USDT", 
        "BNB_USDT"
    ])
    def test_get_contract_detail_for_different_symbols(self, client: MexcMarketDataApi, 
                                                        symbol: str):
        """Test get contract detail for different symbols"""
        response = client.get_contract_detail(symbol)
        assert response is not None
        assert response.get("success") is True
        data = response.get("data", {})
        assert data.get("symbol") == symbol
        print(f"\n✓ {symbol} contract detail retrieved")
    
    @pytest.mark.parametrize("limit", [5, 10, 20, 50])
    def test_get_depth_with_different_limits(self, client: MexcMarketDataApi, 
                                               limit: int):
        """Test get depth with different limits"""
        response = client.get_depth(TEST_SYMBOL, limit)
        assert response is not None
        assert response.get("success") is True
        data = response.get("data", {})
        bids = data.get('bids', [])
        asks = data.get('asks', [])
        assert len(bids) <= limit
        assert len(asks) <= limit
        print(f"\n✓ Depth with limit {limit} retrieved")
    
    @pytest.mark.parametrize("interval", [
        "Min1", "Min5", "Min15", "Hour4", "Day1"
    ])
    def test_get_index_price_kline_different_intervals(self, client: MexcMarketDataApi,
                                                        interval: str):
        """Test get index price kline with different intervals"""
        end_time = int(time.time())
        start_time = end_time - 24 * 60 * 60  # 1 day ago
        
        response = client.get_index_price_kline(TEST_SYMBOL, interval,
                                                 start_time, end_time)
        assert response is not None
        assert response.get("success") is True
        print(f"\n✓ Index price kline with interval {interval} retrieved")



# =========================================================================
# 10. ERROR HANDLING TESTS
# =========================================================================

class TestErrorHandling:
    """Test error handling scenarios"""
    
    def test_get_contract_detail_invalid_symbol(self, client: MexcMarketDataApi):
        """Test get contract detail with invalid symbol"""
        response = client.get_contract_detail(TEST_INVALID_SYMBOL)
        assert response is not None
        # Should return success=False with error code
        print(f"\nInvalid symbol response: {response}")
    
    def test_get_depth_invalid_symbol(self, client: MexcMarketDataApi):
        """Test get depth with invalid symbol"""
        response = client.get_depth(TEST_INVALID_SYMBOL, 10)
        assert response is not None
        print(f"\nInvalid depth response: {response}")
    
    def test_get_index_price_invalid_symbol(self, client: MexcMarketDataApi):
        """Test get index price with invalid symbol"""
        response = client.get_index_price(TEST_INVALID_SYMBOL)
        assert response is not None
    
    def test_get_funding_rate_invalid_symbol(self, client: MexcMarketDataApi):
        """Test get funding rate with invalid symbol"""
        response = client.get_funding_rate(TEST_INVALID_SYMBOL)
        assert response is not None
    
    def test_get_kline_invalid_interval(self, client: MexcMarketDataApi):
        """Test get kline with invalid interval"""
        end_time = int(time.time())
        start_time = end_time - 7 * 24 * 60 * 60
        
        response = client.get_kline(TEST_SYMBOL, "INVALID_INTERVAL", 
                                     start_time, end_time, 10)
        assert response is not None


# =========================================================================
# 11. EDGE CASE TESTS
# =========================================================================

class TestEdgeCases:
    """Test edge cases"""
    
    def test_get_depth_commits_zero_limit(self, client: MexcMarketDataApi):
        """Test get depth commits with zero limit"""
        response = client.get_depth_commits(TEST_SYMBOL, 0)
        assert response is not None
        print(f"\nZero limit response: {response}")
    
    def test_get_recent_deals_large_limit(self, client: MexcMarketDataApi):
        """Test get recent deals with large limit"""
        response = client.get_recent_deals(TEST_SYMBOL, TEST_LARGE_LIMIT)
        assert response is not None
    
    def test_get_kline_future_time_range(self, client: MexcMarketDataApi):
        """Test get kline with future time range"""
        future_time = int(time.time() * 1000) + 24 * 60 * 60
        past_time = future_time - 7 * 24 * 60 * 60
        
        response = client.get_kline(TEST_SYMBOL, "Min30", past_time, future_time, 100)
        assert response is not None


# =========================================================================
# 12. PERFORMANCE TESTS
# =========================================================================

class TestPerformance:
    """Test performance of API calls"""
    
    @pytest.mark.slow
    def test_multiple_consecutive_calls(self, client: MexcMarketDataApi):
        """Test multiple consecutive API calls"""
        start_time = time.time()
        
        # Make several API calls
        for _ in range(5):
            client.ping()
            client.get_index_price(TEST_SYMBOL)
            client.get_funding_rate(TEST_SYMBOL)
        
        end_time = time.time()
        duration = end_time - start_time
        print(f"\nCompleted 15 API calls in {duration:.2f} seconds")
        assert duration < 30  # Should complete within 30 seconds


# =========================================================================
# 13. DATA VALIDATION TESTS
# =========================================================================

class TestDataValidation:
    """Test data validation and structure"""
    
    def test_depth_data_structure(self, client: MexcMarketDataApi):
        """Test depth data structure"""
        response = client.get_depth(TEST_SYMBOL, 5)
        assert response is not None
        assert response.get("success") is True
        
        data = response.get("data", {})
        assert "version" in data
        assert "timestamp" in data
        assert "bids" in data
        assert "asks" in data
        
        bids = data.get("bids", [])
        asks = data.get("asks", [])
        
        # Verify bid/ask format
        if bids:
            first_bid = bids[0]
            assert isinstance(first_bid, list)
            assert len(first_bid) >= 2  # [price, quantity, ...]
    
    def test_contract_detail_structure(self, client: MexcMarketDataApi):
        """Test contract detail data structure"""
        response = client.get_contract_detail(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True
        
        data = response.get("data", {})
        required_fields = ["symbol", "baseCoin", "quoteCoin", 
                          "contractSize", "maxLeverage"]
        
        for field in required_fields:
            assert field in data, f"Missing field: {field}"
            print(f"✓ {field}: {data.get(field)}")

    def test_get_funding_rate(self, client: MexcMarketDataApi):
        """Test get funding rate"""
        response = client.get_funding_rate(TEST_SYMBOL)
        assert response is not None
        assert response.get("success") is True
        data = response.get("data", {})
        assert data.get("symbol") == TEST_SYMBOL
        print(f"\n{TEST_SYMBOL} Funding Rate: {data.get('fundingRate')}")
        print(f"Next Settlement: {data.get('nextSettleTime')}")



if __name__ == "__main__":
    """Run tests directly"""
    pytest.main([__file__, "-v", "--capture=no"])