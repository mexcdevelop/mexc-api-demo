using System;
using System.Text;
using System.Threading;
using System.Reactive.Linq;
using Websocket.Client;
using Google.Protobuf;

class Program
{
    private static WebsocketClient client;

    static void Main()
    {
        var url = new Uri("wss://wbs-api.mexc.com/ws");
        client = new WebsocketClient(url)
        {
            ReconnectTimeout = TimeSpan.FromSeconds(10) // Set auto-reconnect timeout to 10 seconds
        };

        // Subscribe to WebSocket disconnection events
        client.DisconnectionHappened.Subscribe(info =>
        {
            Console.WriteLine($"⚠️ WebSocket disconnected: {info.Type}，attempting to reconnect...");
        });

        // Subscribe to WebSocket message events
        client.MessageReceived
            .Where(msg => msg.Binary != null)  // Filter only Protobuf messages
            .Subscribe(msg =>
            {
                try
                {
                    var response = PushDataV3ApiWrapper.Parser.ParseFrom(msg.Binary);
                    Console.WriteLine($"✅ Successfully parsed: {response}");
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"❌ Parsing failed: {ex.Message}");
                }
            });

        // Start WebSocket connection
        client.Start();
        Console.WriteLine("🚀 WebSocket connected！");

        // Send subscription request to MEXC API
        SubscribeToMexc();

        // Send a ping message every 30 seconds to keep the connection alive
        Timer pingTimer = new Timer(_ =>
        {
            client.Send("{\"method\": \"ping\"}");
            Console.WriteLine("📍 Sent ping...");
        }, null, 0, 30000);

        // Prevent the main program from exiting
        Console.ReadLine();
    }

    static void SubscribeToMexc()
    {
        var subscriptionMessage = new
        {
            method = "SUBSCRIPTION",
            @params = new string[] { "spot@public.aggre.deals.v3.api.pb@100ms@BTCUSDT" }
        };
        string json = System.Text.Json.JsonSerializer.Serialize(subscriptionMessage);
        client.Send(json);
        Console.WriteLine($"📡 Sent subscription request: {json}");
    }
}
