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
        throw new ArgumentException($"Command missing. Accept commands: spot");

      string apiKey = "Your apiKey";
      string apiSecret = "Your apiSecret";
      string spotBaseUrl = "https://api.mexc.com"; 

      HttpClient httpClient = new HttpClient();
      switch (args[0])
      {
        case "signature": Signature(apiSecret); break;
        case "spot": await Spot(new MexcService(apiKey, apiSecret, spotBaseUrl, httpClient)); break;
      }
    }

    private static async Task Spot(MexcService MexcService)
    {
      /// Test Connectivity
      using (var response = MexcService.SendPublicAsync("/api/v3/ping", HttpMethod.Get ))
      {
        Console.WriteLine(await response);
      };

      /// Check Server Time
      using (var response = MexcService.SendPublicAsync("/api/v3/time", HttpMethod.Get ))
      {
        Console.WriteLine(await response);
      };

      /// Exchange Information
      using (var response = MexcService.SendPublicAsync("/api/v3/exchangeInfo", HttpMethod.Get ))
      {
        Console.WriteLine(await response);
      };

      /// Order Book
      using (var response = MexcService.SendPublicAsync("/api/v3/depth", HttpMethod.Get, new Dictionary<string, object> {
                { "symbol", "BTCUSDT" }
            }))
      {
        Console.WriteLine(await response);
      };

      ///Recent Trades List
      using (var response = MexcService.SendPublicAsync("/api/v3/trades", HttpMethod.Get, new Dictionary<string, object> {
                { "symbol", "BTCUSDT" }
            }))
      {
        Console.WriteLine(await response);
      };

      /// Old Trade Lookup
      using (var response = MexcService.SendPublicAsync("/api/v3/historicalTrades", HttpMethod.Get, new Dictionary<string, object> {
                { "symbol", "BTCUSDT" }
            }))
      {
        Console.WriteLine(await response);
      };

      /// Compressed/Aggregate Trades List
      using (var response = MexcService.SendPublicAsync("/api/v3/aggTrades", HttpMethod.Get, new Dictionary<string, object> {
                { "symbol", "BTCUSDT" }
            }))
      {
        Console.WriteLine(await response);
      };

      /// Kline/Candlestick Data
      using (var response = MexcService.SendPublicAsync("/api/v3/klines", HttpMethod.Get, new Dictionary<string, object> {
                { "symbol", "BTCUSDT" },
                { "interval", "1d"}
            }))
      {
        Console.WriteLine(await response);
      };

      /// Current Average Price
      using (var response = MexcService.SendPublicAsync("/api/v3/avgPrice", HttpMethod.Get, new Dictionary<string, object> {
                { "symbol", "BTCUSDT" }
            }))
      {
        Console.WriteLine(await response);
      };

      /// 24hr Ticker Price Change Statistics
      using (var response = MexcService.SendPublicAsync("/api/v3/ticker/24hr", HttpMethod.Get, new Dictionary<string, object> {
                { "symbol", "BTCUSDT" }
            }))
      {
        Console.WriteLine(await response);
      };

      /// Symbol Price Ticker
      using (var response = MexcService.SendPublicAsync("/api/v3/ticker/price", HttpMethod.Get, new Dictionary<string, object> {
                { "symbol", "BTCUSDT" }
            }))
      {
        Console.WriteLine(await response);
      };

      /// Symbol Order Book Ticker
      using (var response = MexcService.SendPublicAsync("/api/v3/ticker/bookTicker", HttpMethod.Get, new Dictionary<string, object> {
                { "symbol", "BTCUSDT" }
            }))
      {
        Console.WriteLine(await response);
      };

      /// Test New Order
      using (var response = MexcService.SendSignedAsync("/api/v3/order/test", HttpMethod.Post, new Dictionary<string, object> {
                {"symbol", "BTCUSDT" },
                {"side", "BUY" },
                {"type", "LIMIT" },
                {"quantity", 1 },
                {"price", 200 }
            }))
      {
        Console.WriteLine(await response);
      };

      /// New Order
      using (var response = MexcService.SendSignedAsync("/api/v3/order", HttpMethod.Post, new Dictionary<string, object> {
                {"symbol", "BTCUSDT" },
                {"side", "BUY" },
                {"type", "LIMIT" },
                {"quantity", 0.0003 },
                {"price", 20000 }
            }))
      {
        Console.WriteLine(await response);
      };

      /// Cancel Orde
      using (var response = MexcService.SendSignedAsync("/api/v3/order", HttpMethod.Delete, new Dictionary<string, object> {
                {"symbol", "BTCUSDT" },
                {"orderId", 155959573394513920 }
            }))
      {
        Console.WriteLine(await response);
      };

      /// Cancel all Open Orders on a Symbol
      using (var response = MexcService.SendSignedAsync("/api/v3/openOrders", HttpMethod.Delete, new Dictionary<string, object> {
                {"symbol", "BTCUSDT" }
            }))
      {
        Console.WriteLine(await response);
      };

      /// Query Order
      using (var response = MexcService.SendSignedAsync("/api/v3/order", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT" },
                {"orderId", 155959573394513920 }
            }))
      {
        Console.WriteLine(await response);
      };

      /// Current Open Orders
      using (var response = MexcService.SendSignedAsync("/api/v3/openOrders", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT" }
            }))
      {
        Console.WriteLine(await response);
      };

      /// All Orders
      using (var response = MexcService.SendSignedAsync("/api/v3/allOrders", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT" }
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
                {"symbol", "BTCUSDT" }
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
