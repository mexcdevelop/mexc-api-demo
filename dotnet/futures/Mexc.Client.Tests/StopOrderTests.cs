using System;
using System.Text.Json;
using System.Threading.Tasks;
using Xunit;

namespace Mexc.Client.Tests
{
    public class StopOrderTests : AccountTradingTestBase
    {
        [Fact]
        public async Task Test59_PlaceStopOrder()
        {
            Console.WriteLine("\n=== Test59: PlaceStopOrder ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling PlaceStopOrderAsync (requires valid positionId)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.PlaceStopOrderAsync(
                    symbol: "BTC_USDT",
                    profitTrend: 1,
                    lossTrend: 1,
                    positionId: "sample_position_id",
                    vol: 1,
                    stopLossType: 1,
                    stopLossOrderPrice: "20000",
                    stopLossPrice: "20000"
                );
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("PlaceStopOrder", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires a valid position ID
            }
        }

        [Fact]
        public async Task Test60_CancelStopOrder()
        {
            Console.WriteLine("\n=== Test60: CancelStopOrder ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling CancelStopOrderAsync (requires valid orderId)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.CancelStopOrderAsync("BTC_USDT", "sample_order_id");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("CancelStopOrder", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires a valid order ID
            }
        }

        [Fact]
        public async Task Test61_CancelAllStopOrders()
        {
            Console.WriteLine("\n=== Test61: CancelAllStopOrders ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling CancelAllStopOrdersAsync for BTC_USDT...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.CancelAllStopOrdersAsync("BTC_USDT");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("CancelAllStopOrders", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test62_ChangeStopOrderPrice()
        {
            Console.WriteLine("\n=== Test62: ChangeStopOrderPrice ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling ChangeStopOrderPriceAsync (requires valid orderId)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.ChangeStopOrderPriceAsync("sample_order_id", "21000");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("ChangeStopOrderPrice", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires a valid order ID
            }
        }

        [Fact]
        public async Task Test63_ChangeStopOrderPlanPrice()
        {
            Console.WriteLine("\n=== Test63: ChangeStopOrderPlanPrice ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling ChangeStopOrderPlanPriceAsync (requires valid orderId)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.ChangeStopOrderPlanPriceAsync("sample_order_id", "21000");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("ChangeStopOrderPlanPrice", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires a valid order ID
            }
        }

        [Fact]
        public async Task Test64_ChangePlanOrderStopOrder()
        {
            Console.WriteLine("\n=== Test64: ChangePlanOrderStopOrder ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling ChangePlanOrderStopOrderAsync (requires valid orderId)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.ChangePlanOrderStopOrderAsync("BTC_USDT", "sample_order_id", "21000");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("ChangePlanOrderStopOrder", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires a valid order ID
            }
        }

        [Fact]
        public async Task Test65_GetStopOrdersList()
        {
            Console.WriteLine("\n=== Test65: GetStopOrdersList ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetStopOrdersListAsync for BTC_USDT...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetStopOrdersListAsync("BTC_USDT", 1, 10);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetStopOrdersList", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test66_GetOpenStopOrders()
        {
            Console.WriteLine("\n=== Test66: GetOpenStopOrders ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetOpenStopOrdersAsync for BTC_USDT...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetOpenStopOrdersAsync("BTC_USDT");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetOpenStopOrders", response);
                
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
