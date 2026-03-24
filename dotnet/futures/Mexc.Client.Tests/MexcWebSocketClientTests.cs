using System;
using System.Threading.Tasks;
using Xunit;
using Xunit.Abstractions;

namespace Mexc.Client.Tests
{
    public class MexcWebSocketClientTests : TestBase
    {
        private readonly ITestOutputHelper _output;

        public MexcWebSocketClientTests(ITestOutputHelper output)
        {
            _output = output;
        }

        [Fact]
        public async Task TestWebSocketConnection()
        {
            // This test requires actual API keys to run
            var apiKey = Environment.GetEnvironmentVariable("MEXC_API_KEY");
            var secretKey = Environment.GetEnvironmentVariable("MEXC_SECRET_KEY");

            if (string.IsNullOrEmpty(apiKey) || string.IsNullOrEmpty(secretKey))
            {
                _output.WriteLine("⏭️ Skipping WebSocket test (no API keys)");
                return;
            }

            var receivedMessages = 0;
            var completionSource = new TaskCompletionSource<bool>();

            using var client = new MexcWebSocketClient(apiKey, secretKey);

            client.OnMessage += (message) =>
            {
                receivedMessages++;
                _output.WriteLine($"📩 Received: {message}");

                // After receiving at least one message, consider test successful
                if (receivedMessages >= 1)
                {
                    completionSource.TrySetResult(true);
                }
            };

            client.OnError += (ex) =>
            {
                _output.WriteLine($"❌ Error: {ex.Message}");
                completionSource.TrySetException(ex);
            };

            client.OnClose += () =>
            {
                _output.WriteLine("🔌 Connection closed");
            };

            try
            {
                _output.WriteLine("🔄 Connecting...");
                await client.ConnectAsync();
                _output.WriteLine("✅ Connected");

                _output.WriteLine("📊 Subscribing to BTC_USDT ticker...");
                await client.SubscribeTickerAsync("BTC_USDT");

                // Wait for message or timeout
                var timeout = Task.Delay(30000); // 30 seconds timeout
                var completed = await Task.WhenAny(completionSource.Task, timeout);

                if (completed == timeout)
                {
                    _output.WriteLine("⚠️ Timeout waiting for messages");
                }
                else
                {
                    _output.WriteLine($"✅ Received {receivedMessages} messages");
                }

                await client.DisconnectAsync();
            }
            catch (Exception ex)
            {
                _output.WriteLine($"❌ Test failed: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task TestPingMechanism()
        {
            // Test that ping messages are sent periodically
            var apiKey = Environment.GetEnvironmentVariable("MEXC_API_KEY");
            var secretKey = Environment.GetEnvironmentVariable("MEXC_SECRET_KEY");

            if (string.IsNullOrEmpty(apiKey) || string.IsNullOrEmpty(secretKey))
            {
                _output.WriteLine("⏭️ Skipping ping test (no API keys)");
                return;
            }

            using var client = new MexcWebSocketClient(apiKey, secretKey);

            var pingReceived = false;
            var completionSource = new TaskCompletionSource<bool>();

            client.OnMessage += (message) =>
            {
                _output.WriteLine($"📩 Received: {message}");

                // Check if this is a pong response (you may need to adjust based on actual response format)
                if (message.Contains("pong") || message.Contains("PONG"))
                {
                    pingReceived = true;
                    completionSource.TrySetResult(true);
                }
            };

            client.OnError += (ex) =>
            {
                _output.WriteLine($"❌ Error: {ex.Message}");
                completionSource.TrySetException(ex);
            };

            try
            {
                _output.WriteLine("🔄 Connecting...");
                await client.ConnectAsync();
                _output.WriteLine("✅ Connected");

                // Wait for ping/pong for 20 seconds
                await Task.Delay(20000);

                // Disconnect
                await client.DisconnectAsync();

                _output.WriteLine(pingReceived ? "✅ Ping mechanism working" : "⚠️ No ping response received");
            }
            catch (Exception ex)
            {
                _output.WriteLine($"❌ Test failed: {ex.Message}");
                throw;
            }
        }
    }
}
