# Mexc DotNET Demo

Before you use the demo, you need to generate your apikey & apisecret, then enter them first.

* <https://www.mexc.com/user/openapi>

## Spot V3 Demo 

Fill in the corresponding function according to the parameters mentioned in the API documentation and execute it. 

**Rest API V3 doc**   `URL = 'https://api.mexc.com'`

* <https://mxcdevelop.github.io/apidocs/spot_v3_cn/#45fa4e00db>


> ### Example(Spot V3) :

```csharp
private static async Task Market(MexcService MexcService)
{
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
}
```
Run Market Example. => `dotnet run market`
