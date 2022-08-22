using System;
using System.Text;
using System.Web;
using Newtonsoft.Json;
using System.Collections.Generic;
using System.Security.Cryptography;
using WebSocket4Net;


namespace mexc_websocket_dotnet
{
  public class Program
  {
    public static void Main(string[] args)
    {
      using (WebSocket websocket = new WebSocket("wss://wbs.mexc.com/raw/ws"))
      {
        websocket.Opened += new EventHandler(websocket_Opened);
        websocket.Error += new EventHandler<SuperSocket.ClientEngine.ErrorEventArgs>(websocket_Error);
        websocket.Closed += new EventHandler(websocket_Closed);
        websocket.MessageReceived += new EventHandler<MessageReceivedEventArgs>(websocket_MessageReceived);
        websocket.Open();

        Console.WriteLine("Connecting .......");
        while (websocket.State != WebSocketState.Open)
        {
          Console.Write("");
        }
        Console.WriteLine("-------- Connected --------");

        // subscribe limited depth
        websocket.Send("{\"op\":\"sub.limit.depth\",\"symbol\":\"BTC_USDT\",\"depth\": 5}");

        // sbuscribe personal order & deal;
        string apiKey = "your apikey";
        string apiSecret = "your apisecret";
        long now = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();

        Dictionary<string, string> param = new Dictionary<string, string>();
        param["api_key"] = apiKey;
        param["op"] = "sub.personal";
        param["req_time"] = now + "";
        param["sign"] = Sign(SignStr(param, apiSecret));
        string json = JsonConvert.SerializeObject(param);
        websocket.Send(json);
        Console.WriteLine(json);
        Console.ReadKey();
      }
    }

    private static void websocket_Opened(object? sender, EventArgs e)
    {
      Console.WriteLine($"socket OPENED, sender: {sender} and eventargs e: {e}");
    }

    private static void websocket_Error(object? sender, SuperSocket.ClientEngine.ErrorEventArgs e)
    {
      Console.WriteLine($"socket ERROR, sender: {sender} and eventargs e: {e.Exception}");
    }

    private static void websocket_Closed(object? sender, EventArgs e)
    {
      Console.WriteLine($"socket CLOSED, sender: {sender} and eventargs e: {e}");
    }

    private static void websocket_MessageReceived(object? sender, MessageReceivedEventArgs e)
    {
      // Console.WriteLine($"socket MESSAGE RECEIVED, sender: {sender} and eventargs e: {e.Message}");
      Console.WriteLine($"{e.Message}");
    }

    private static string Sign(string stringOfSign)
    {
      System.Text.UTF8Encoding encoding = new System.Text.UTF8Encoding();

      using (MD5 hmacsha256 = MD5.Create())
      {
        byte[] utf8EncodedDataBytes = encoding.GetBytes(stringOfSign);
        byte[] md5HashBytes = hmacsha256.ComputeHash(utf8EncodedDataBytes);
        string base64md5HashString = Convert.ToBase64String(md5HashBytes);

        return BitConverter.ToString(md5HashBytes).Replace("-", "").ToLower();
      }
    }

    private static string SignStr(Dictionary<string, string> param, string apiSecret)
    {
      StringBuilder queryStringBuilder = new StringBuilder();
      if (!(param is null))
      {
        string queryParameterString = string.Join("&", param.Where(kvp => !string.IsNullOrWhiteSpace(kvp.Value?.ToString())).Select(kvp => string.Format("{0}={1}", kvp.Key, HttpUtility.UrlEncode(kvp.Value.ToString()))));
        queryStringBuilder.Append(queryParameterString);
      }
      queryStringBuilder.Append("&").Append("api_secret").Append("=").Append(apiSecret);

      return queryStringBuilder.ToString();
    }
  }
}

