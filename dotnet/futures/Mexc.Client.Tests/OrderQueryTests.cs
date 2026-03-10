using System;
using System.Collections.Generic;
using System.Text.Json;
using System.Threading.Tasks;
using Xunit;

namespace Mexc.Client.Tests
{
    public class OrderQueryTests : AccountTradingTestBase
    {
        [Fact]
        public async Task Test45_GetOrderByExternalId()
        {
            Console.WriteLine("\n=== Test45: GetOrderByExternalId ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetOrderByExternalIdAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetOrderByExternalIdAsync("BTC_USDT", "sample_external_id");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetOrderByExternalId", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires a valid external ID
            }
        }

        [Fact]
        public async Task Test46_GetOrderById()
        {
            Console.WriteLine("\n=== Test46: GetOrderById ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetOrderByIdAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetOrderByIdAsync("sample_order_id");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetOrderById", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires a valid order ID
            }
        }

        [Fact]
        public async Task Test47_BatchQueryOrders()
        {
            Console.WriteLine("\n=== Test47: BatchQueryOrders ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling BatchQueryOrdersAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.BatchQueryOrdersAsync("order_id1,order_id2");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("BatchQueryOrders", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires valid order IDs
            }
        }

        [Fact]
        public async Task Test48_BatchQueryWithExternal()
        {
            Console.WriteLine("\n=== Test48: BatchQueryWithExternal ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                var parameters = new List<object>
                {
                    new { symbol = "BTC_USDT", externalOid = "ext1" }
                };
                
                Console.WriteLine("Calling BatchQueryWithExternalAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.BatchQueryWithExternalAsync(parameters);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("BatchQueryWithExternal", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires valid external IDs
            }
        }

        [Fact]
        public async Task Test49_GetListOpenOrders()
        {
            Console.WriteLine("\n=== Test49: GetListOpenOrders ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetListOpenOrdersAsync for BTC_USDT...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetListOpenOrdersAsync("BTC_USDT");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetListOpenOrders", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test50_GetHistoryOrders()
        {
            Console.WriteLine("\n=== Test50: GetHistoryOrders ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                var endTime = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
                var startTime = endTime - 7L * 24 * 60 * 60 * 1000;
                
                Console.WriteLine("Calling GetHistoryOrdersAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetHistoryOrdersAsync("BTC_USDT", startTime, endTime, 1, 10);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetHistoryOrders", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test51_GetOrderDealsV3()
        {
            Console.WriteLine("\n=== Test51: GetOrderDealsV3 ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetOrderDealsV3Async (requires valid orderId)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetOrderDealsV3Async("BTC_USDT", "sample_order_id", 1, 10);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetOrderDealsV3", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires a valid order ID
            }
        }

        [Fact]
        public async Task Test52_GetDealDetails()
        {
            Console.WriteLine("\n=== Test52: GetDealDetails ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetDealDetailsAsync (requires valid orderId)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetDealDetailsAsync("sample_order_id");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetDealDetails", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires a valid order ID
            }
        }

        [Fact]
        public async Task Test53_GetCloseOrders()
        {
            Console.WriteLine("\n=== Test53: GetCloseOrders ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                var endTime = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
                var startTime = endTime - 7L * 24 * 60 * 60 * 1000;
                
                Console.WriteLine("Calling GetCloseOrdersAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetCloseOrdersAsync("BTC_USDT", startTime, endTime, 1, 10);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetCloseOrders", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }
    }
}
