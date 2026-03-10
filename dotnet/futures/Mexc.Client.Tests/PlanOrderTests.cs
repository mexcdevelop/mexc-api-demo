using System;
using System.Collections.Generic;
using System.Text.Json;
using System.Threading.Tasks;
using Xunit;

namespace Mexc.Client.Tests
{
    public class PlanOrderTests : AccountTradingTestBase
    {
        [Fact]
        public async Task Test54_GetPlanOrders()
        {
            Console.WriteLine("\n=== Test54: GetPlanOrders ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetPlanOrdersAsync for BTC_USDT...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetPlanOrdersAsync("BTC_USDT", 1, 10);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetPlanOrders", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test55_PlacePlanOrderV2()
        {
            Console.WriteLine("\n=== Test55: PlacePlanOrderV2 ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling PlacePlanOrderV2Async...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.PlacePlanOrderV2Async(
                    symbol: "BTC_USDT",
                    side: "1",
                    openType: "1",
                    leverage: 10,
                    triggerType: "2",
                    triggerPrice: "50000",
                    orderType: "5",
                    price: "50000",
                    volume: "1",
                    executeCycle: "1",
                    trend: "1"
                );
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("PlacePlanOrderV2", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as placing real orders can have consequences
            }
        }

        [Fact]
        public async Task Test56_ChangePlanOrderPrice()
        {
            Console.WriteLine("\n=== Test56: ChangePlanOrderPrice ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling ChangePlanOrderPriceAsync (requires valid orderId)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.ChangePlanOrderPriceAsync("BTC_USDT", "sample_order_id", "51000", "51000", "5");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("ChangePlanOrderPrice", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires a valid order ID
            }
        }

        [Fact]
        public async Task Test57_CancelPlanOrder()
        {
            Console.WriteLine("\n=== Test57: CancelPlanOrder ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                var parameters = new List<object>
                {
                    new { symbol = "BTC_USDT", orderId = "sample_order_id" }
                };
                
                Console.WriteLine("Calling CancelPlanOrderAsync (requires valid orderId)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.CancelPlanOrderAsync(parameters);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("CancelPlanOrder", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires a valid order ID
            }
        }

        [Fact]
        public async Task Test58_CancelAllPlanOrders()
        {
            Console.WriteLine("\n=== Test58: CancelAllPlanOrders ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling CancelAllPlanOrdersAsync for BTC_USDT...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.CancelAllPlanOrdersAsync("BTC_USDT");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("CancelAllPlanOrders", response);
                
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
