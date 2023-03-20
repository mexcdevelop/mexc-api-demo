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
        throw new ArgumentException($"Command missing. Accept commands: signature, market, trade, subaccount, capital, rebate");

      string apiKey = "your apikey";
      string apiSecret = "your secret";
      string BaseUrl = "https://api.mexc.com";

      HttpClient httpClient = new HttpClient();
      switch (args[0])
      {
        case "signature": Signature(apiSecret); break;
        case "market": await Market(new MexcService(apiKey, apiSecret, BaseUrl, httpClient)); break;
        case "trade": await Trade(new MexcService(apiKey, apiSecret, BaseUrl, httpClient)); break;
        case "subaccount": await Sub_Account(new MexcService(apiKey, apiSecret, BaseUrl, httpClient)); break;
        case "capital": await Capital(new MexcService(apiKey, apiSecret, BaseUrl, httpClient)); break;
        case "rebate": await Rebate(new MexcService(apiKey, apiSecret, BaseUrl, httpClient)); break;
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

      /// Get DefaultSymbols
      using (var response = MexcService.SendPublicAsync("/api/v3/defaultSymbols", HttpMethod.Get))
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

    private static async Task Trade(MexcService MexcService)
    {
      /// Account SelfSymbols
      using (var response = MexcService.SendSignedAsync("/api/v3/selfSymbols", HttpMethod.Get))
      {
        Console.WriteLine(await response);
      };

      /// Test New Order
      using (var response = MexcService.SendSignedAsync("/api/v3/order/test", HttpMethod.Post, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}, {"side", "BUY"}, {"type", "LIMIT"}, {"quantity", 0.0006}, {"price", 10000}
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

      /// Place Batched Orders
      using (var response = MexcService.SendSignedAsync("/api/v3/batchOrders", HttpMethod.Post, new Dictionary<string, object> {
        {"batchOrders",
        "[{'symbol':'MXUSDT','price':'0.5','quantity':'10','side':'BUY','type':'LIMIT'},{'symbol':'MXUSDT','price':'0.6','quantity':'10','side':'BUY','type':'LIMIT'}]"
        }}))
      {
        Console.WriteLine(await response);
      };

      /// Cancel Order
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

      /// Account Information
      using (var response = MexcService.SendSignedAsync("/api/v3/account", HttpMethod.Get))
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

      /// Enable MxDeduct
      using (var response = MexcService.SendSignedAsync("/api/v3/mxDeduct/enable", HttpMethod.Post, new Dictionary<string, object> {
                {"mxDeductEnable", "True"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get MxDeduct Status
      using (var response = MexcService.SendSignedAsync("/api/v3/mxDeduct/enable", HttpMethod.Get))
      {
        Console.WriteLine(await response);
      };
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
      using (var response = MexcService.SendSignedAsync("/api/v3/sub-account/list", HttpMethod.Get, new Dictionary<string, object>{
                {"subAccount", "subAccount1"}, {"isFreeze", "True"}, {"page", 1}, {"limit", 1}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Create SubAccount APIKEY
      using (var response = MexcService.SendSignedAsync("/api/v3/sub-account/apiKey", HttpMethod.Post, new Dictionary<string, object> {
                {"subAccount", "xxxx"}, {"note", "ForContract"}, {"permissions", "CONTRACT_ACCOUNT_READ"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get SubAccount APIKEY
      using (var response = MexcService.SendSignedAsync("/api/v3/sub-account/apiKey", HttpMethod.Get, new Dictionary<string, object> {
                {"subAccount", "xxxx"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Delete SubAccount APIKEY
      using (var response = MexcService.SendSignedAsync("/api/v3/sub-account/apiKey", HttpMethod.Delete, new Dictionary<string, object> {
                {"subAccount", "xxxx"}, {"apiKey", "mxmxmxmxmxmxmxmx"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Account UniversalTransfer
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/sub-account/universalTransfer", HttpMethod.Post, new Dictionary<string, object> {
                {"fromAccount", "xxxx"}, {"toAccount", "xxxx"}, {"fromAccountType", "SPOT"}, {"toAccountType", "SPOT"},
                {"symbol", "xxx"}, {"asset", "xxx"}, {"amount", "xxx"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get Account UniversalTransfer List
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/sub-account/universalTransfer", HttpMethod.Get, new Dictionary<string, object> {
                {"fromAccount", "xxxx"}, {"toAccount", "xxxx"}, {"fromAccountType", "SPOT"}, {"toAccountType", "SPOT"},
                {"startTime", "xxx"}, {"endTime", "xxx"}, {"page", "xxx"}, {"limit", "xxx"}
            }))
      {
        Console.WriteLine(await response);
      };
    }

    private static async Task Capital(MexcService MexcService)
    {
      /// Coin List
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/config/getall", HttpMethod.Get))
      {
        Console.WriteLine(await response);
      };

      /// Withdraw
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/withdraw/apply", HttpMethod.Post, new Dictionary<string, object> {
                {"coin", "USDT"}, {"withdrawOrderId", "1234554321"}, {"network", "TRC20"}, {"address", "xxx"}, {"memo", "xxx"},
                {"amount", "1000"}, {"remark", "test-withdraw"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Cancel Withdraw
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/withdraw", HttpMethod.Delete, new Dictionary<string, object> {
                {"id", "1234554321"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get Deposit History
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/deposit/hisrec", HttpMethod.Get, new Dictionary<string, object> {
                {"coin", "USDT"}, {"startTime", "1669865156000"}, {"endTime", "1679282756000"},
                // {"status", "xxx"}, {"limit", "1000"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get Withdraw History
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/withdraw/history", HttpMethod.Get, new Dictionary<string, object> {
                {"coin", "USDT"}, {"startTime", "1669865156000"}, {"endTime", "1679282756000"},
                // {"status", "xxx"}, {"limit", "1000"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Generate Deposit Address
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/deposit/address", HttpMethod.Post, new Dictionary<string, object> {
                {"coin", "USDT"}, {"network", "TRC20"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get Deposit Address
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/deposit/address", HttpMethod.Get, new Dictionary<string, object> {
                {"coin", "USDT"}, {"network", "TRC20"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get Withdraw Address
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/withdraw/address", HttpMethod.Get, new Dictionary<string, object> {
                {"coin", "USDT"}, // {"page", "1"}, {"limit", "1000"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Account Transfer
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/transfer", HttpMethod.Post, new Dictionary<string, object> {
                {"fromAccountType", "xxx"}, {"toAccountType", "xxx"}, {"asset", "xxx"}, {"amount", "xxx"}, {"symbol", "xxx"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get Transfer History
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/transfer", HttpMethod.Get, new Dictionary<string, object> {
                {"fromAccountType", "xxx"}, {"toAccountType", "xxx"}, {"startTime", "1669865156000"}, {"endTime", "1679282756000"},
                {"page", "xxx"}, {"size", "xxx"}, {"symbol", "xxx"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get Transfer History (By tranId)
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/transfer/tranId", HttpMethod.Get, new Dictionary<string, object> {
                {"tranId", "xxx"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get Convert List
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/convert/list", HttpMethod.Get))
      {
        Console.WriteLine(await response);
      };

      /// Convert
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/convert", HttpMethod.Post, new Dictionary<string, object> {
                {"asset", "xxx"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get Convert History
      using (var response = MexcService.SendSignedAsync("/api/v3/capital/convert", HttpMethod.Get, new Dictionary<string, object> {
                {"startTime", "1669865156000"}, {"endTime", "1679282756000"},
                // {"page", "1"}, {"limit", "1000"}
            }))
      {
        Console.WriteLine(await response);
      };
    }

    private static async Task Rebate(MexcService MexcService)
    {
      /// Get Rebate History
      using (var response = MexcService.SendSignedAsync("/api/v3/rebate/taxQuery", HttpMethod.Get, new Dictionary<string, object> {
                {"startTime", "1669865156000"}, {"endTime", "1679282756000"}, {"page", "1"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get Rebate Detail
      using (var response = MexcService.SendSignedAsync("/api/v3/rebate/detail", HttpMethod.Get, new Dictionary<string, object> {
                {"startTime", "1669865156000"}, {"endTime", "1679282756000"}, {"page", "1"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get Rebate Kickback Detail
      using (var response = MexcService.SendSignedAsync("/api/v3/rebate/detail/kickback", HttpMethod.Get, new Dictionary<string, object> {
                {"startTime", "1669865156000"}, {"endTime", "1679282756000"}, {"page", "1"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Get Rebate ReferCode
      using (var response = MexcService.SendSignedAsync("/api/v3/rebate/referCode", HttpMethod.Get))
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
