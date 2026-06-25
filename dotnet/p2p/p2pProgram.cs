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

      string apiKey = "your apiKey";
      string apiSecret = "your secret";
      string BaseUrl = "https://api.mexc.com";

      HttpClient httpClient = new HttpClient();
      switch (args[0])
      {
        case "signature": Signature(apiSecret); break;
        case "p2p": await P2P(new MexcService(apiKey, apiSecret, BaseUrl, httpClient)); break;
      }
    }


    private static async Task P2P(MexcService MexcService){
        using (var response = MexcService.SendSignedAsync(
            "/api/v3/fiat/merchant/ads/pagination",
            HttpMethod.Get,
            new Dictionary<string, object>
            {
                { "page", "1" },
                { "limit", "10" }
            }))
        {
            System.Console.WriteLine(await response);
        }

        using (var response = MexcService.SendSignedAsync(
            "/api/v3/fiat/market/ads/pagination",
            HttpMethod.Get,
            new Dictionary<string, object>
            {
                { "coinId", "USDT" },
                { "fiatUnit", "USD" },
                { "page", "1" }
            }))
        {
            System.Console.WriteLine(await response);
        }


        using (var response = MexcService.SendSignedAsync(
            "/api/v3/fiat/merchant/order/paginationV2",
            HttpMethod.Get,
            new Dictionary<string, object>
            {
                { "lastId", "1" },
                { "limit", "10" }
            }))
        {
            System.Console.WriteLine(await response);
        }

        using (var response = MexcService.SendSignedAsync(
            "/api/v3/fiat/market/order/paginationV2",
            HttpMethod.Get,
            new Dictionary<string, object>
            {
                { "lastId", "1" },
                { "limit", "10" }
            }))
        {
            System.Console.WriteLine(await response);
        }

        using (var response = MexcService.SendSignedAsync(
            "/api/v3/fiat/order/detail",
            HttpMethod.Get,
            new Dictionary<string, object>
            {
                { "advOrderNo", "123456789" }
            }))
        {
            System.Console.WriteLine(await response);
        }

        using (var response = MexcService.SendSignedAsync(
            "/api/v3/fiat/merchant/order/deal",
            HttpMethod.Post,
            new Dictionary<string, object>
            {
                { "advNo", "10001" },
                { "amount", "100" },
                { "userConfirmPaymentId", "1001111" }
            }))
        {
            System.Console.WriteLine(await response);
        }

        using (var response = MexcService.SendSignedAsync(
            "/api/v3/fiat/confirm_paid",
            HttpMethod.Post,
            new Dictionary<string, object>
            {
                { "advOrderNo", "123456789" },
                { "userConfirmPaymentId", "123456789" }
            }))
        {
            System.Console.WriteLine(await response);
        }


        using (var response = MexcService.SendSignedAsync(
            "/api/v3/fiat/release_coin",
            HttpMethod.Post,
            new Dictionary<string, object>
            {
                { "advOrderNo", "123456789" }
            }))
        {
            System.Console.WriteLine(await response);
        }

        using (var response = MexcService.SendSignedAsync(
            "/api/v3/fiat/merchant/ads/save_or_update",
            HttpMethod.Post,
            new Dictionary<string, object>
            {
                { "price", "7.0" },
                { "coinId", "USDT" },
                { "payTimeLimit", "30" },
                { "initQuantity", "1000" },
                { "side", "SELL" },
                { "fiatUnit", "USD" },
                { "payMethod", "1678" },
                { "minSingleTransAmount", "100" },
                { "maxSingleTransAmount", "1000000" }
            }))
        {
            System.Console.WriteLine(await response);
        }

        using (var response = MexcService.SendSignedAsync(
            "/api/v3/fiat/merchant/service/switch",
            HttpMethod.Post,
            new Dictionary<string, object>
            {
                { "open", "true" }
            }))
        {
            System.Console.WriteLine(await response);
        }


        using (var response = MexcService.SendSignedAsync(
            "/api/v3/userDataStream",
            HttpMethod.Post,
            new Dictionary<string, object>())
        )
        {
            System.Console.WriteLine(await response);
        }


        using (var response = MexcService.SendSignedAsync(
            "/api/v3/userDataStream",
            HttpMethod.Get,
            new Dictionary<string, object>
            {
                { "recvWindow", "60000" }
            }))
        {
            System.Console.WriteLine(await response);
        }

        using (var response = MexcService.SendSignedAsync(
            "/api/v3/fiat/retrieveChatConversation",
            HttpMethod.Get,
            new Dictionary<string, object>
            {
                { "orderId", "123456789" }
            }))
        {
            System.Console.WriteLine(await response);
        }


        using (var response = MexcService.SendSignedAsync(
            "/api/v3/fiat/retrieveChatMessageWithPagination",
            HttpMethod.Get,
            new Dictionary<string, object>
            {
                { "conversationId", "10001" },
                { "page", "1" },
                { "limit", "20" }
            }))
        {
            System.Console.WriteLine(await response);
        }

        using (var response = MexcService.SendSignedAsync(
            "/api/v3/fiat/downloadFile",
            HttpMethod.Get,
            new Dictionary<string, object>
            {
                { "fileId", "xxxxxx" }
            }))
        {
            System.Console.WriteLine(await response);
        }
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
