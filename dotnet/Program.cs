using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;

namespace MexcDotNet
{
  class Program
  {
    static async Task Main(string[] args)
    {
      if (args.Count() == 0)
        throw new ArgumentException($"Command missing. Accept commands: signature, market, trade, account, subaccount, margin");

      string apiKey = "your apiKey";
      string apiSecret = "your apiSecret";
      string BaseUrl = "https://api.mexc.com";

      HttpClient httpClient = new HttpClient();
      switch (args[0])
      {
        case "signature": Signature(apiSecret); break;
        case "market": await Market(new MexcService(apiKey, apiSecret, BaseUrl, httpClient)); break;
        case "spot_trade": await Spot_Trade(new MexcService(apiKey, apiSecret, BaseUrl, httpClient)); break;
        case "account": await Account(new MexcService(apiKey, apiSecret, BaseUrl, httpClient)); break;
        case "subaccount": await Sub_Account(new MexcService(apiKey, apiSecret, BaseUrl, httpClient)); break;
        case "margin": await Margin(new MexcService(apiKey, apiSecret, BaseUrl, httpClient)); break;
      }
    }

    private static async Task Market(MexcService MexcService)
    {
      /// Test Connectivity
      using (var response = MexcService.SendPublicAsync("/api/v3/ping", HttpMethod.Get))
      {
        Console.WriteLine(await response);
      };

      /// Check Server Time
      using (var response = MexcService.SendPublicAsync("/api/v3/time", HttpMethod.Get))
      {
        Console.WriteLine(await response);
      };

      /// Exchange Information
      using (var response = MexcService.SendPublicAsync("/api/v3/exchangeInfo", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Order Book
      using (var response = MexcService.SendPublicAsync("/api/v3/depth", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}, {"limit", 5}
            }))
      {
        Console.WriteLine(await response);
      };

      ///Recent Trades List
      using (var response = MexcService.SendPublicAsync("/api/v3/trades", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}, {"limit", 5}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Compressed/Aggregate Trades List
      using (var response = MexcService.SendPublicAsync("/api/v3/aggTrades", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}, {"limit", 5}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Kline/Candlestick Data
      using (var response = MexcService.SendPublicAsync("/api/v3/klines", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}, {"interval", "1d"}, {"limit", 5}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Current Average Price
      using (var response = MexcService.SendPublicAsync("/api/v3/avgPrice", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// 24hr Ticker Price Change Statistics
      using (var response = MexcService.SendPublicAsync("/api/v3/ticker/24hr", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Symbol Price Ticker
      using (var response = MexcService.SendPublicAsync("/api/v3/ticker/price", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Symbol Order Book Ticker
      using (var response = MexcService.SendPublicAsync("/api/v3/ticker/bookTicker", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get ETF info
      using (var response = MexcService.SendPublicAsync("/api/v3/etf/info", HttpMethod.Get))
      {
        Console.WriteLine(await response);
      };
    }

    private static async Task Spot_Trade(MexcService MexcService)
    {
      /// Test New Order
      using (var response = MexcService.SendSignedAsync("/api/v3/order/test", HttpMethod.Post, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}, {"side", "BUY"}, {"type", "LIMIT"}, {"quantity", 1}, {"price", 200}
            }))
      {
        Console.WriteLine(await response);
      };

      /// New Order
      using (var response = MexcService.SendSignedAsync("/api/v3/order", HttpMethod.Post, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}, {"side", "BUY"}, {"type", "LIMIT"}, {"quantity", 0.0006}, {"price", 10000}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Place Batched Odears
      using (var response = MexcService.SendSignedAsync("/api/v3/batchOrders", HttpMethod.Post, new Dictionary<string, object> {
        {"batchOrders", new object[]{
          new{
            symbol = "BTCUSDT",
            side = "BUY",
            type = "LIMIT",
            quantity = 0.0006,
            price = 10000
          },
          new{
            symbol = "ETHUSDT",
            side = "BUY",
            type = "LIMIT",
            quantity = 0.01,
            price = 600
          }}
        }}))
      {
        Console.WriteLine(await response);
      };

      /// Cancel Orde
      using (var response = MexcService.SendSignedAsync("/api/v3/order", HttpMethod.Delete, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}, {"orderId", 155959573394513920 }
            }))
      {
        Console.WriteLine(await response);
      };

      /// Cancel all Open Orders on a Symbol
      using (var response = MexcService.SendSignedAsync("/api/v3/openOrders", HttpMethod.Delete, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Query Order
      using (var response = MexcService.SendSignedAsync("/api/v3/order", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}, {"orderId", 155959573394513920}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Current Open Orders
      using (var response = MexcService.SendSignedAsync("/api/v3/openOrders", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// All Orders
      using (var response = MexcService.SendSignedAsync("/api/v3/allOrders", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Account Trade List
      using (var response = MexcService.SendSignedAsync("/api/v3/myTrades", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}
            }))
      {
        Console.WriteLine(await response);
      };
    }

    private static async Task Account(MexcService MexcService)
    {
      /// Account Information
      using (var response = MexcService.SendSignedAsync("/api/v3/account", HttpMethod.Get))
      {
        Console.WriteLine(await response);
      };

      /// Coin List
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/config/getall", HttpMethod.Get))
      {
        Console.WriteLine(await response);
      }
    }

    private static async Task Sub_Account(MexcService MexcService)
    {
      /// Create SubAccount
      using (var response = MexcService.SendSignedAsync("/api/v3/sub-account/virtualSubAccount", HttpMethod.Post, new Dictionary<string, object> {
                {"subAccount", "subAccount1"}, {"note", "xxx"},
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get SubAccount List
      using (var response = MexcService.SendSignedAsync("/api/v3/sub-account/list", HttpMethod.Get, new Dictionary<string, object>
      {
        // {"subAccount", "subAccount1"}, {"isFreeze", "True"}, {"page", 1}, {"limit", 1}
      }))
      {
        Console.WriteLine(await response);
      };

      /// Create SubAccount APIKEY
      using (var response = MexcService.SendSignedAsync("/api/v3/sub-account/apiKey", HttpMethod.Post, new Dictionary<string, object> {
                {"subAccount", "Eddie0516"}, {"note", "ForContract"}, {"permissions", "CONTRACT_ACCOUNT_READ"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get SubAccount APIKEY
      using (var response = MexcService.SendSignedAsync("/api/v3/sub-account/apiKey", HttpMethod.Get, new Dictionary<string, object> {
                {"subAccount", "Eddie0516"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Delete SubAccount APIKEY
      using (var response = MexcService.SendSignedAsync("/api/v3/sub-account/apiKey", HttpMethod.Delete, new Dictionary<string, object> {
                {"subAccount", "Eddie0516"}, {"apiKey", "mxmxmxmxmxmxmxmx"}
            }))
      {
        Console.WriteLine(await response);
      };
    }

    private static async Task Margin(MexcService MexcService)
    {
      /// Switch TradeMode
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/tradeMode", HttpMethod.Post, new Dictionary<string, object> {
                {"tradeMode", 0}, {"symbol", "BTCUSDT"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// New Order
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/order", HttpMethod.Post, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"},
                {"isIsolated", "TRUE"},
                {"side", "BUY"},
                {"type", "LIMIT"},
                {"quantity", 0.0006},
                //{"quoteOrderQty", 6},
                {"price", 10000 },
                // {"newClientOrderId", 111}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Loan
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/loan", HttpMethod.Post, new Dictionary<string, object> {
                {"asset", "USDT"},
                {"isIsolated", "TRUE"},
                {"symbol", "ETHUSDT"},
                {"amount", 0.01}
      }))
      {
        Console.WriteLine(await response);
      };

      /// Repay Loan
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/repay", HttpMethod.Post, new Dictionary<string, object> {
                {"asset", "USDT"},
                {"isIsolated", "TRUE"},
                {"symbol", "BTCUSDT"},
                {"amount", 0.01},
                {"borrowId", 155959573394513920},
                {"isAllRepay", "true"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Cancel all Open Orders on one Symbol
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/openOrders", HttpMethod.Delete, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}, {"isIsolated", "TRUE"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Query Order
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/order", HttpMethod.Get, new Dictionary<string, object> {
                {"isIsolated", "TRUE"}, {"symbol", "BTCUSDT" },
                //{"orderId", 111111111111111 }, {"origClientOrderId", 111111111111111}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Query Loan Record
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/loan", HttpMethod.Get, new Dictionary<string, object> {
                {"asset", "USDT"}, {"symbol", "BTCUSDT"},
                // {"tranId", "1234567890"},
                // {"startTime", 123456789},
                // {"endTime", 987654321},
                // {"current", 1},
                // {"size", 10}
            }))
      {
        Console.WriteLine(await response);
      };

      /// All Orders
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/allOrders", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"},
                {"isIsolated", "TRUE"},
                // {"orderId", "123456789"},
                // {"startTime", "123456789"},
                // {"endTime", "987654321"},
                {"limit", 10}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Account Trade List
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/myTrades", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT" }
            }))
      {
        Console.WriteLine(await response);
      };

      /// Current Open Orders
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/openOrders", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT" }, {"isIsolated", "TRUE"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Maximum Transferable Amount
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/maxTransferable", HttpMethod.Get, new Dictionary<string, object> {
                {"asset", "BTC"}, {"symbol", "BTCUSDT" }
            }))
      {
        Console.WriteLine(await response);
      };

      /// Margin Price Index
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/priceIndex", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "ETCUSDT" }
            }))
      {
        Console.WriteLine(await response);
      };

      /// Margin Account Order Details
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/order", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}, {"orderId", "12123847321"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Isolated Margin Account Information
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/isolated/account", HttpMethod.Get, new Dictionary<string, object> {
                {"symbols", "BTCUSDT"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Maximum Loan Amount
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/maxBorrowable", HttpMethod.Get, new Dictionary<string, object> {
                {"asset", "BTC"}, {"symbol", "BTCUSDT"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Repay History
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/repay", HttpMethod.Get, new Dictionary<string, object> {
                {"asset", "BTC"}, {"symbol", "BTCUSDT"}, {"tranId", "123456789"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get Isolated Margin Symbol
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/isolated/pair", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get Account Forced Liquidation Record
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/forceLiquidationRec", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get Isolated Margin Rate & Limit
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/isolatedMarginData", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get Isolated Tier
      using (var response = MexcService.SendSignedAsync("/api/v3/margin/myTrades", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}
            }))
      {
        Console.WriteLine(await response);
      };
    }

    private static void Signature(string apiSecret)
    {

      Dictionary<string, object> basicParameters = new Dictionary<string, object> {
                { "timestamp", 1652918401000 }
            };

      string basicQueryString = BuildQueryString(basicParameters);
      Console.WriteLine(basicQueryString);
      string basicSignature = SignatureHelper.Sign(basicQueryString, apiSecret);
      Console.WriteLine(basicSignature);
    }

    /// <summary>Builds a URL encoded query string for the given parameters.</summary>
    private static string BuildQueryString(Dictionary<string, object> queryParameters)
    {
      return string.Join("&", queryParameters.Select(kvp =>
          string.Format("{0}={1}", kvp.Key, HttpUtility.UrlEncode(kvp.Value.ToString()))));
    }

  }
}
