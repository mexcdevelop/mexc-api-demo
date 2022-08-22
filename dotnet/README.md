# Mexc DotNet Demo

Before you use the demo, you need to generate your apikey & apisecret, then enter them first.

* <https://www.mexc.com/user/openapi>

## Spot V3 Demo 

Fill in the corresponding function according to the parameters mentioned in the API documentation and execute it. => `dotnet run`

**Rest API V3 doc**   `URL = 'https://api.mexc.com'`

* <https://mxcdevelop.github.io/apidocs/spot_v3_cn/#45fa4e00db>


> ### Example(Spot V3) :

Use Program.cs as a test example

```csharp
static async Task Main(string[] args)
{
   if (args.Count() == 0)
      throw new ArgumentException($"Command missing. Accept commands: signature, market, trade, account, subaccount, margin");

   string apiKey = "your apikey";
   string apiSecret = "your secret";
   string BaseUrl = "https://api.mexc.com";

   HttpClient httpClient = new HttpClient();
   switch (args[0])
   {
     case "market": await Market(new MexcService(apiKey, apiSecret, BaseUrl, httpClient)); break;
   }
}
private static async Task Market(MexcService MexcService)
{
   using (var response = MexcService.SendPublicAsync("/api/v3/klines", HttpMethod.Get, new Dictionary<string, object> {
                {"symbol", "BTCUSDT"}, {"interval", "1d"}, {"limit", 5}
   }))
   {
     Console.WriteLine(await response);
   };
}
```
`dotnet run market`

## Spot Websocket Demo 

According to the information you want to subscribe, change the content of the params according to the websocket documentation, ex: "op" or "symbol".   Execute the entire file after adjusting the parameters.



**WebSocket doc**   `URL = 'wss://wbs.mexc.com/raw/ws'`

* <https://mxcdevelop.github.io/apidocs/spot_v2_cn/#websocket-api>


`dotnet run`


