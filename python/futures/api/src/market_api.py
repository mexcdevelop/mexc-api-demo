from typing import Optional, List, Dict, Any
import time

from .mexc_client import MexcHttpClient, logger


class MexcMarketDataApi(MexcHttpClient):
    """
    MEXC Market Data API
    Corresponds to Java MexcMarketDataApi
    All endpoints are public and do not require authentication.
    """
    
    def __init__(self):
        """Initialize market data client (no API keys needed)"""
        super().__init__()
    
    # ==================== Basic Market Endpoints ====================
    
    def ping(self) -> Dict[str, Any]:
        """
        Get server time (ping)
        GET /api/v1/contract/ping
        
        Test connectivity and get server time.
        
        Returns:
            Dict containing server timestamp
        
        Example response:
        {
            "success": true,
            "code": 0,
            "data": 1761875313209
        }
        """
        logger.info("\n=== Ping - Get Server Time ===")
        response = self.get("/api/v1/contract/ping", None)
        if response and response.get('success'):
            logger.info(f"Server timestamp: {response.get('data')}")
        return response
    
    def get_contract_detail(self, symbol: Optional[str] = None) -> Dict[str, Any]:
        """
        Get contract information
        GET /api/v1/contract/detail
        
        Args:
            symbol: Optional contract symbol (e.g., "BTC_USDT"). Returns all if None.
        
        Returns:
            Dict containing contract information
        """
        logger.info(f"\n=== Get Contract Information {symbol if symbol else '(All)'} ===")
        
        params = None
        if symbol:
            params = {'symbol': symbol}
        
        response = self.get("/api/v1/contract/detail", params)
        
        if response and response.get('success'):
            data = response.get('data')
            if symbol and isinstance(data, dict):
                logger.info(f"Symbol: {data.get('symbol')}")
                logger.info(f"Base Coin: {data.get('baseCoin')}")
                logger.info(f"Quote Coin: {data.get('quoteCoin')}")
                logger.info(f"Contract Size: {data.get('contractSize')}")
                logger.info(f"Max Leverage: {data.get('maxLeverage')}")
            elif isinstance(data, list):
                logger.info(f"Retrieved {len(data)} contracts")
        
        return response
    
    def get_support_currencies(self) -> Dict[str, Any]:
        """
        Get transferable currencies
        GET /api/v1/contract/support_currencies
        
        Returns:
            Dict containing list of supported currencies
        """
        logger.info("\n=== Get Transferable Currencies ===")
        response = self.get("/api/v1/contract/support_currencies", None)
        
        if response and response.get('success'):
            currencies = response.get('data', [])
            logger.info(f"Supported currencies: {len(currencies)}")
            if currencies:
                logger.info(f"Sample: {currencies[:10]}")
        
        return response
    
    # ==================== Order Book Endpoints ====================
    
    def get_depth(self, symbol: str, limit: Optional[int] = None) -> Dict[str, Any]:
        """
        Get order book depth
        GET /api/v1/contract/depth/{symbol}
        
        Args:
            symbol: Contract symbol (required)
            limit: Number of depth levels (optional, default 100)
        
        Returns:
            Dict containing order book data with bids and asks
        """
        logger.info(f"\n=== Get Order Book Depth: {symbol} ===")
        
        endpoint = f"/api/v1/contract/depth/{symbol}"
        params = {'limit': limit} if limit else None
        
        response = self.get(endpoint, params)
        
        if response and response.get('success'):
            data = response.get('data', {})
            logger.info(f"Order Book Version: {data.get('version')}")
            logger.info(f"Timestamp: {data.get('timestamp')}")
            logger.info(f"Bids: {len(data.get('bids', []))} levels")
            logger.info(f"Asks: {len(data.get('asks', []))} levels")
            
            # Print top 3 bids and asks
            bids = data.get('bids', [])
            asks = data.get('asks', [])
            if bids:
                logger.info(f"Top 3 bids: {bids[:3]}")
            if asks:
                logger.info(f"Top 3 asks: {asks[:3]}")
        
        return response
    
    def get_depth_commits(self, symbol: str, limit: int) -> Dict[str, Any]:
        """
        Get last N depth snapshots
        GET /api/v1/contract/depth_commits/{symbol}/{limit}
        
        Args:
            symbol: Contract symbol (required)
            limit: Number of snapshots to return (required)
        
        Returns:
            Dict containing list of depth snapshots
        """
        logger.info(f"\n=== Get Last {limit} Depth Snapshots: {symbol} ===")
        
        endpoint = f"/api/v1/contract/depth_commits/{symbol}/{limit}"
        response = self.get(endpoint, None)
        
        if response and response.get('success'):
            snapshots = response.get('data', [])
            logger.info(f"Retrieved {len(snapshots)} snapshots")
            if snapshots:
                logger.info(f"First snapshot version: {snapshots[0].get('version')}")
        
        return response
    
    # ==================== Price Index Endpoints ====================
    
    def get_index_price(self, symbol: str) -> Dict[str, Any]:
        """
        Get index price
        GET /api/v1/contract/index_price/{symbol}
        
        Args:
            symbol: Contract symbol (required)
        
        Returns:
            Dict containing index price information
        """
        logger.info(f"\n=== Get Index Price: {symbol} ===")
        
        endpoint = f"/api/v1/contract/index_price/{symbol}"
        response = self.get(endpoint, None)
        
        if response and response.get('success'):
            data = response.get('data', {})
            logger.info(f"Index Price: {data.get('indexPrice')}")
            logger.info(f"Timestamp: {data.get('timestamp')}")
        
        return response
    
    def get_fair_price(self, symbol: str) -> Dict[str, Any]:
        """
        Get fair price
        GET /api/v1/contract/fair_price/{symbol}
        
        Args:
            symbol: Contract symbol (required)
        
        Returns:
            Dict containing fair price information
        """
        logger.info(f"\n=== Get Fair Price: {symbol} ===")
        
        endpoint = f"/api/v1/contract/fair_price/{symbol}"
        response = self.get(endpoint, None)
        
        if response and response.get('success'):
            data = response.get('data', {})
            logger.info(f"Fair Price: {data.get('fairPrice')}")
            logger.info(f"Timestamp: {data.get('timestamp')}")
        
        return response
    
    # ==================== Funding Rate Endpoints ====================
    
    def get_funding_rate(self, symbol: str) -> Dict[str, Any]:
        """
        Get funding rate
        GET /api/v1/contract/funding_rate/{symbol}
        
        Args:
            symbol: Contract symbol (required)
        
        Returns:
            Dict containing funding rate information
        """
        logger.info(f"\n=== Get Funding Rate: {symbol} ===")
        
        endpoint = f"/api/v1/contract/funding_rate/{symbol}"
        response = self.get(endpoint, None)
        
        if response and response.get('success'):
            data = response.get('data', {})
            logger.info(f"Funding Rate: {data.get('fundingRate')}")
            logger.info(f"Max Funding Rate: {data.get('maxFundingRate')}")
            logger.info(f"Min Funding Rate: {data.get('minFundingRate')}")
            logger.info(f"Next Settlement: {data.get('nextSettleTime')}")
        
        return response
    
    def get_funding_rate_history(self, symbol: Optional[str] = None, limit: Optional[int] = None) -> Dict[str, Any]:
        """
        Get funding rate history
        GET /api/v1/contract/funding_rate/history
        
        Args:
            symbol: Optional contract symbol. Returns all if None.
            limit: Number of records (default 100, max 500)
        
        Returns:
            Dict containing funding rate history
        """
        logger.info(f"\n=== Get Funding Rate History {symbol if symbol else '(All)'} ===")
        
        params = {}
        if symbol:
            params['symbol'] = symbol
        if limit:
            params['limit'] = str(limit)
        
        response = self.get("/api/v1/contract/funding_rate/history", params)
        
        if response and response.get('success'):
            data = response.get('data', {})
            if isinstance(data, dict) and 'resultList' in data:
                records = data.get('resultList', [])
                logger.info(f"Retrieved {len(records)} funding rate records")
                if records:
                    first = records[0]
                    logger.info(f"Sample - Symbol: {first.get('symbol')}, "
                               f"Rate: {first.get('fundingRate')}, "
                               f"Time: {first.get('settleTime')}")
        
        return response
    
    # ==================== Kline/Candlestick Endpoints ====================
    
    def get_kline(self, symbol: str, interval: str, start: Optional[int] = None,
                  end: Optional[int] = None, limit: Optional[int] = None) -> Dict[str, Any]:
        """
        Get kline/candlestick data
        GET /api/v1/contract/kline/{symbol}
        
        Args:
            symbol: Contract symbol (required)
            interval: Kline interval: 1m,5m,15m,30m,1h,4h,1d,1w,1M
            start: Optional start time in milliseconds
            end: Optional end time in milliseconds
            limit: Optional number of records (default 100, max 500)
        
        Returns:
            Dict containing kline data
        """
        logger.info(f"\n=== Get Kline Data: {symbol} {interval} ===")
        
        endpoint = f"/api/v1/contract/kline/{symbol}"
        params = {'interval': interval}
        
        if start:
            params['start'] = str(start)
        if end:
            params['end'] = str(end)
        if limit:
            params['limit'] = str(limit)
        
        response = self.get(endpoint, params)
        
        if response and response.get('success'):
            data = response.get('data', {})
            if isinstance(data, dict):
                time_array = data.get('time', [])
                logger.info(f"Retrieved {len(time_array)} kline records")
                if time_array and 'close' in data:
                    close_array = data.get('close', [])
                    logger.info(f"First close: {close_array[0] if close_array else 'N/A'}")
                    logger.info(f"Last close: {close_array[-1] if close_array else 'N/A'}")
        
        return response
    
    def get_index_price_kline(self, symbol: str, interval: str, start: int, end: int) -> Dict[str, Any]:
        """
        Get index price kline data
        GET /api/v1/contract/kline/index_price/{symbol}
        
        Args:
            symbol: Contract symbol (required)
            interval: Kline interval: Min1,Min5,Min15,Min30,Hour1,Hour4,Hour8,Day1,Week1,Month1
            start: Start time in seconds (Unix timestamp, required)
            end: End time in seconds (Unix timestamp, required)
        
        Returns:
            Dict containing index price kline data
        """
        logger.info(f"\n=== Get Index Price Kline: {symbol} {interval} ===")
        logger.info(f"Time range: {start} to {end}")
        
        endpoint = f"/api/v1/contract/kline/index_price/{symbol}"
        params = {
            'interval': interval,
            'start': str(start),
            'end': str(end)
        }
        
        response = self.get(endpoint, params)
        
        if response and response.get('success'):
            data = response.get('data', {})
            time_array = data.get('time', [])
            logger.info(f"Retrieved {len(time_array)} index price kline records")
            if time_array:
                logger.info(f"First record - Time: {time_array[0]}, "
                           f"Open: {data.get('open', [])[0] if data.get('open') else None}, "
                           f"Close: {data.get('close', [])[0] if data.get('close') else None}")
        
        return response
    
    def get_fair_price_kline(self, symbol: str, interval: str, start: int, end: int) -> Dict[str, Any]:
        """
        Get fair price kline data
        GET /api/v1/contract/kline/fair_price/{symbol}
        
        Args:
            symbol: Contract symbol (required)
            interval: Kline interval: Min1,Min5,Min15,Min30,Hour1,Hour4,Hour8,Day1,Week1,Month1
            start: Start time in seconds (Unix timestamp, required)
            end: End time in seconds (Unix timestamp, required)
        
        Returns:
            Dict containing fair price kline data
        """
        logger.info(f"\n=== Get Fair Price Kline: {symbol} {interval} ===")
        logger.info(f"Time range: {start} to {end}")
        
        endpoint = f"/api/v1/contract/kline/fair_price/{symbol}"
        params = {
            'interval': interval,
            'start': str(start),
            'end': str(end)
        }
        
        response = self.get(endpoint, params)
        
        if response and response.get('success'):
            data = response.get('data', {})
            time_array = data.get('time', [])
            logger.info(f"Retrieved {len(time_array)} fair price kline records")
            if time_array:
                logger.info(f"First record - Time: {time_array[0]}, "
                           f"Open: {data.get('open', [])[0] if data.get('open') else None}, "
                           f"Close: {data.get('close', [])[0] if data.get('close') else None}")
        
        return response
    
    # ==================== Trade Endpoints ====================
    
    def get_recent_deals(self, symbol: str, limit: Optional[int] = None) -> Dict[str, Any]:
        """
        Get recent deals/trades
        GET /api/v1/contract/deals/{symbol}
        
        Args:
            symbol: Contract symbol (required)
            limit: Number of deals to return (default 100, max 500)
        
        Returns:
            Dict containing recent deals
        """
        logger.info(f"\n=== Get Recent Deals: {symbol} ===")
        
        endpoint = f"/api/v1/contract/deals/{symbol}"
        params = {'limit': limit} if limit else None
        
        response = self.get(endpoint, params)
        
        if response and response.get('success'):
            data = response.get('data', [])
            logger.info(f"Retrieved {len(data)} recent deals")
            
            if data:
                logger.info("\nMost recent deals:")
                count = min(5, len(data))
                for i in range(count):
                    logger.info(f"  {i+1}: {data[i]}")
                
                # Calculate statistics
                total_volume = 0.0
                total_amount = 0.0
                for deal in data:
                    if isinstance(deal, dict):
                        volume = deal.get('v', 0)
                        price = deal.get('p', 0)
                        total_volume += volume
                        total_amount += price * volume
                
                logger.info(f"\nSummary - Total Volume: {total_volume:.4f}, "
                           f"Total Amount: {total_amount:.2f}")
        
        return response
    
    # ==================== Ticker Endpoints ====================
    
    def get_all_tickers(self) -> Dict[str, Any]:
        """
        Get all tickers
        GET /api/v1/contract/ticker
        
        Returns:
            Dict containing all ticker data
        """
        logger.info("\n=== Get All Tickers ===")
        
        response = self.get("/api/v1/contract/ticker", None)
        
        if response and response.get('success'):
            data = response.get('data', [])
            logger.info(f"Retrieved tickers for {len(data)} symbols")
            
            # Print summary for major symbols
            major_symbols = ["BTC_USDT", "ETH_USDT", "BNB_USDT"]
            for symbol in major_symbols:
                for ticker in data:
                    if isinstance(ticker, dict) and ticker.get('symbol') == symbol:
                        logger.info(f"\n{symbol}:")
                        logger.info(f"  Last: {ticker.get('lastPrice')}")
                        logger.info(f"  Change: {ticker.get('riseFallValue')} "
                                   f"({ticker.get('riseFallRate')}%)")
                        logger.info(f"  Volume: {ticker.get('volume24')}")
                        break
        
        return response
    
    # ==================== Risk Reverse Endpoints ====================
    
    def get_risk_reverse_by_symbol(self, symbol: str) -> Dict[str, Any]:
        """
        Get risk reverse by symbol
        GET /api/v1/contract/risk_reverse/{symbol}
        
        Args:
            symbol: Contract symbol (required)
        
        Returns:
            Dict containing risk reverse information
        """
        logger.info(f"\n=== Get Risk Reverse: {symbol} ===")
        
        endpoint = f"/api/v1/contract/risk_reverse/{symbol}"
        response = self.get(endpoint, None)
        
        if response and response.get('success'):
            data = response.get('data', {})
            logger.info(f"Symbol: {data.get('symbol')}")
            logger.info(f"Risk Reverse Amount: {data.get('available')}")
            logger.info(f"Update Time: {data.get('timestamp')}")
        
        return response
    
    def get_risk_reverse_history(self, symbol: Optional[str] = None,
                                  page_num: int = 1, page_size: int = 20) -> Dict[str, Any]:
        """
        Get risk reverse history
        GET /api/v1/contract/risk_reverse/history
        
        Args:
            symbol: Optional contract symbol
            page_num: Page number (default 1)
            page_size: Page size (default 20, max 100)
        
        Returns:
            Dict containing risk reverse history
        """
        logger.info(f"\n=== Get Risk Reverse History {symbol if symbol else ''} ===")
        
        params = {}
        if symbol:
            params['symbol'] = symbol
        params['page_num'] = str(page_num)
        params['page_size'] = str(min(page_size, 100))
        
        response = self.get("/api/v1/contract/risk_reverse/history", params)
        
        if response and response.get('success'):
            data = response.get('data', {})
            logger.info(f"Total records: {data.get('totalCount')}")
            logger.info(f"Current page: {data.get('currentPage')}")
            logger.info(f"Page size: {data.get('pageSize')}")
            
            result_list = data.get('resultList', [])
            logger.info(f"Records in this page: {len(result_list)}")
            
            if result_list:
                first = result_list[0]
                logger.info(f"Sample - Symbol: {first.get('symbol')}, "
                           f"Amount: {first.get('amount')}, "
                           f"Time: {first.get('createTime')}")
        
        return response
def get_funding_rate_history(self, symbol: Optional[str] = None, limit: Optional[int] = None) -> Dict[str, Any]:
    """
    Get funding rate history
    GET /api/v1/contract/funding_rate/history
    
    Retrieves historical funding rate data for all symbols or specific symbol.
    
    Args:
        symbol: Optional contract symbol (e.g., "BTC_USDT"). Returns all if None.
        limit: Number of records (default 100, max 500)
    
    Returns:
        Dict containing funding rate history with pagination info
        
    Example response:
    {
        "success": true,
        "code": 0,
        "data": {
            "pageSize": 20,
            "totalCount": 100,
            "totalPage": 5,
            "currentPage": 1,
            "resultList": [
                {
                    "symbol": "BTC_USDT",
                    "fundingRate": 0.0001,
                    "settleTime": 1640995200000,
                    "nextSettleTime": 1641024000000
                }
            ]
        }
    }
    """
    logger.info(f"\n=== Get Funding Rate History {symbol if symbol else '(All)'} ===")
    
    params = {}
    if symbol:
        params['symbol'] = symbol
    if limit:
        params['limit'] = str(limit)
    
    response = self.get("/api/v1/contract/funding_rate/history", params)
    
    if response and response.get('success'):
        data = response.get('data', {})
        if isinstance(data, dict):
            # Handle paginated response
            if 'resultList' in data:
                result_list = data.get('resultList', [])
                logger.info(f"Retrieved {len(result_list)} funding rate records")
                logger.info(f"Total count: {data.get('totalCount')}")
                logger.info(f"Current page: {data.get('currentPage')}")
                logger.info(f"Page size: {data.get('pageSize')}")
                
                if result_list:
                    first = result_list[0]
                    logger.info(f"Sample - Symbol: {first.get('symbol')}, "
                               f"Rate: {first.get('fundingRate')}, "
                               f"Time: {first.get('settleTime')}")
            else:
                # Handle simple array response
                logger.info(f"Retrieved {len(data)} funding rate records")
                if data and isinstance(data, list) and len(data) > 0:
                    first = data[0]
                    logger.info(f"Sample - Symbol: {first.get('symbol')}, "
                               f"Rate: {first.get('fundingRate')}, "
                               f"Time: {first.get('settleTime')}")
    
    return response    