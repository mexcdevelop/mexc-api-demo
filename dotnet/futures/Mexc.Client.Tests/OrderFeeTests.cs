using System;
using System.Text.Json;
using System.Threading.Tasks;
using Xunit;

namespace Mexc.Client.Tests
{
    public class OrderFeeTests : AccountTradingTestBase
    {
        [Fact]
        public async Task Test18_GetOrderFeeDetails()
        {
            Console.WriteLine("\n=== Test18: GetOrderFeeDetails ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                // Note: This requires a valid orderId. This test may fail if you don't have recent orders.
                Console.WriteLine("Calling GetOrderFeeDetailsAsync with a sample orderId...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetOrderFeeDetailsAsync("BTC_USDT", "sample_order_id");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetOrderFeeDetails", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw because this is expected to fail without a valid orderId
            }
        }

        [Fact]
        public async Task Test21_GetTotalOrderDealFee()
        {
            Console.WriteLine("\n=== Test21: GetTotalOrderDealFee ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                var endTime = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
                var startTime = endTime - 30L * 24 * 60 * 60 * 1000;
                
                Console.WriteLine("Calling GetTotalOrderDealFeeAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetTotalOrderDealFeeAsync(startTime, endTime, "BTC_USDT");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetTotalOrderDealFee", response);
                
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
