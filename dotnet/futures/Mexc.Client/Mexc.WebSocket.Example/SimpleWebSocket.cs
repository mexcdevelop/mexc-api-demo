using System;
using System.Net.WebSockets;
using System.Security.Cryptography;
using System.Text;
using System.Text.Json;
using System.Threading;
using System.Threading.Tasks;

namespace Mexc.WebSocket.Example
{
    class SimpleWebSocket
    {
        private static ClientWebSocket _webSocket;
        private static CancellationTokenSource _cts;
        private static string _apiKey;
        private static string _secretKey;

        static async Task Main(string[] args)
        {
            Console.WriteLine("MEXC WebSocket Example with Private Messages");
            Console.WriteLine("=============================================");

            // 从环境变量获取 API 密钥
            _apiKey = Environment.GetEnvironmentVariable("MEXC_API_KEY") ?? "";
            _secretKey = Environment.GetEnvironmentVariable("MEXC_SECRET_KEY") ?? "";

            if (string.IsNullOrEmpty(_apiKey) || string.IsNullOrEmpty(_secretKey))
            {
                Console.WriteLine("⚠️ Warning: MEXC_API_KEY and MEXC_SECRET_KEY not set");
                Console.WriteLine("Private messages will not be available");
            }

            _webSocket = new ClientWebSocket();
            _cts = new CancellationTokenSource();

            try
            {
                // 连接 WebSocket
                Console.WriteLine("\nConnecting to wss://contract.mexc.co/edge...");
                await _webSocket.ConnectAsync(new Uri("wss://contract.mexc.co/edge"), _cts.Token);
                Console.WriteLine("✅ Connected successfully!");

                // 启动接收消息的任务
                _ = ReceiveMessagesAsync();

                // 如果有 API 密钥，先进行登录认证
                if (!string.IsNullOrEmpty(_apiKey) && !string.IsNullOrEmpty(_secretKey))
                {
                    await LoginAsync();
                }

                // 订阅公开的 ticker
                await SubscribeTickerAsync("BTC_USDT");

                // 如果有 API 密钥，订阅私有消息
                if (!string.IsNullOrEmpty(_apiKey) && !string.IsNullOrEmpty(_secretKey))
                {
                    await SubscribePrivateMessagesAsync();
                }

                // 启动 ping 任务
                _ = SendPingPeriodicallyAsync();

                // 运行60秒
                Console.WriteLine("\n⏳ Receiving messages for 60 seconds...");
                await Task.Delay(60000, _cts.Token);

                // 断开连接
                await _webSocket.CloseAsync(
                    WebSocketCloseStatus.NormalClosure,
                    "Closing",
                    CancellationToken.None
                );
                Console.WriteLine("✅ Disconnected");
            }
            catch (OperationCanceledException)
            {
                Console.WriteLine("Operation cancelled");
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Error: {ex.Message}");
            }
            finally
            {
                _webSocket?.Dispose();
                _cts?.Dispose();
            }
        }

        /// <summary>
        /// 登录认证（生成签名）
        /// </summary>
        private static async Task LoginAsync()
        {
            Console.WriteLine("\n🔐 Authenticating...");

            var timestamp = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds().ToString();
            var signTarget = _apiKey + timestamp;

            // 生成 HMAC SHA256 签名
            using var hmac = new HMACSHA256(Encoding.UTF8.GetBytes(_secretKey));
            var hash = hmac.ComputeHash(Encoding.UTF8.GetBytes(signTarget));
            var signature = BitConverter.ToString(hash).Replace("-", "").ToLower();

            var loginMsg = new
            {
                method = "login",
                param = new
                {
                    apiKey = _apiKey,
                    reqTime = timestamp,
                    signature = signature
                }
            };

            string loginJson = JsonSerializer.Serialize(loginMsg);
            Console.WriteLine($"Sending login: {loginJson}");

            byte[] sendBytes = Encoding.UTF8.GetBytes(loginJson);
            await _webSocket.SendAsync(
                new ArraySegment<byte>(sendBytes),
                WebSocketMessageType.Text,
                true,
                _cts.Token
            );

            Console.WriteLine("✅ Login message sent");
        }

        /// <summary>
        /// 订阅公开 ticker
        /// </summary>
        private static async Task SubscribeTickerAsync(string symbol)
        {
            Console.WriteLine($"\n📊 Subscribing to {symbol} ticker...");

            var subscribeMsg = new
            {
                method = "sub.ticker",
                param = new { symbol = symbol }
            };

            string subscribeJson = JsonSerializer.Serialize(subscribeMsg);
            Console.WriteLine($"Sending: {subscribeJson}");

            byte[] sendBytes = Encoding.UTF8.GetBytes(subscribeJson);
            await _webSocket.SendAsync(
                new ArraySegment<byte>(sendBytes),
                WebSocketMessageType.Text,
                true,
                _cts.Token
            );

            Console.WriteLine($"✅ Subscribed to {symbol} ticker");
        }

        /// <summary>
        /// 订阅私有消息（账户资产、订单等）
        /// </summary>
        private static async Task SubscribePrivateMessagesAsync()
        {
            Console.WriteLine("\n🔒 Subscribing to private messages...");

            // 订阅账户资产变动
            var assetMsg = new
            {
                method = "sub.personal",
                param = new
                {
                    filters = new[]
                    {
                        new { type = "asset" }  // 账户资产变动
                    }
                }
            };

            string assetJson = JsonSerializer.Serialize(assetMsg);
            Console.WriteLine($"Sending asset subscription: {assetJson}");

            byte[] sendBytes = Encoding.UTF8.GetBytes(assetJson);
            await _webSocket.SendAsync(
                new ArraySegment<byte>(sendBytes),
                WebSocketMessageType.Text,
                true,
                _cts.Token
            );

            // 等待一下再发送下一个订阅
            await Task.Delay(1000);

            // 订阅订单更新
            var orderMsg = new
            {
                method = "sub.personal",
                param = new
                {
                    filters = new[]
                    {
                        new { type = "order" }  // 订单更新
                    }
                }
            };

            string orderJson = JsonSerializer.Serialize(orderMsg);
            Console.WriteLine($"Sending order subscription: {orderJson}");

            sendBytes = Encoding.UTF8.GetBytes(orderJson);
            await _webSocket.SendAsync(
                new ArraySegment<byte>(sendBytes),
                WebSocketMessageType.Text,
                true,
                _cts.Token
            );

            // 订阅持仓更新
            await Task.Delay(1000);

            var positionMsg = new
            {
                method = "sub.personal",
                param = new
                {
                    filters = new[]
                    {
                        new { type = "position" }  // 持仓更新
                    }
                }
            };

            string positionJson = JsonSerializer.Serialize(positionMsg);
            Console.WriteLine($"Sending position subscription: {positionJson}");

            sendBytes = Encoding.UTF8.GetBytes(positionJson);
            await _webSocket.SendAsync(
                new ArraySegment<byte>(sendBytes),
                WebSocketMessageType.Text,
                true,
                _cts.Token
            );

            Console.WriteLine("✅ Private messages subscriptions sent");
        }

        /// <summary>
        /// 订阅所有私有消息（一次性订阅多个过滤器）
        /// </summary>
        private static async Task SubscribeAllPrivateMessagesAsync()
        {
            Console.WriteLine("\n🔒 Subscribing to all private messages...");

            var allMsg = new
            {
                method = "sub.personal",
                param = new
                {
                    filters = new[]
                    {
                        new { type = "asset" },     // 账户资产变动
                        new { type = "order" },     // 订单更新
                        new { type = "position" },  // 持仓更新
                        new { type = "deal" }       // 成交记录
                    }
                }
            };

            string allJson = JsonSerializer.Serialize(allMsg);
            Console.WriteLine($"Sending: {allJson}");

            byte[] sendBytes = Encoding.UTF8.GetBytes(allJson);
            await _webSocket.SendAsync(
                new ArraySegment<byte>(sendBytes),
                WebSocketMessageType.Text,
                true,
                _cts.Token
            );

            Console.WriteLine("✅ All private messages subscriptions sent");
        }

        private static async Task ReceiveMessagesAsync()
        {
            var buffer = new byte[16384]; // 更大的缓冲区

            try
            {
                while (_webSocket.State == WebSocketState.Open && !_cts.Token.IsCancellationRequested)
                {
                    var result = await _webSocket.ReceiveAsync(
                        new ArraySegment<byte>(buffer),
                        _cts.Token
                    );

                    if (result.MessageType == WebSocketMessageType.Close)
                    {
                        Console.WriteLine("🔌 Server requested close");
                        break;
                    }

                    var message = Encoding.UTF8.GetString(buffer, 0, result.Count);
                    
                    // 美化 JSON 输出
                    try
                    {
                        var jsonDoc = JsonDocument.Parse(message);
                        var formattedJson = JsonSerializer.Serialize(jsonDoc,
                            new JsonSerializerOptions { WriteIndented = true });
                        Console.WriteLine($"\n📩 Received: {formattedJson}");
                    }
                    catch
                    {
                        Console.WriteLine($"\n📩 Received: {message}");
                    }

                    // 检查是否是登录响应
                    if (message.Contains("login") && message.Contains("success"))
                    {
                        Console.WriteLine("✅ Login successful!");
                    }
                }
            }
            catch (OperationCanceledException)
            {
                // Normal cancellation
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Receive error: {ex.Message}");
            }
        }

        private static async Task SendPingPeriodicallyAsync()
        {
            try
            {
                while (_webSocket.State == WebSocketState.Open && !_cts.Token.IsCancellationRequested)
                {
                    await Task.Delay(10000, _cts.Token); // 10秒

                    if (_webSocket.State == WebSocketState.Open)
                    {
                        var pingMsg = new { method = "ping" };
                        string pingJson = JsonSerializer.Serialize(pingMsg);

                        byte[] pingBytes = Encoding.UTF8.GetBytes(pingJson);
                        await _webSocket.SendAsync(
                            new ArraySegment<byte>(pingBytes),
                            WebSocketMessageType.Text,
                            true,
                            _cts.Token
                        );

                        Console.WriteLine("📤 Ping sent");
                    }
                }
            }
            catch (OperationCanceledException)
            {
                // Normal cancellation
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Ping error: {ex.Message}");
            }
        }
    }
}
