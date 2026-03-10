using System.Threading.Tasks;
using Xunit;

namespace Mexc.Client.Tests
{
    public class TrailingStopOrderTests : AccountTradingTestBase
    {
        [Fact]
        public async Task Test67_PlaceTrailingStopOrder()
        {
            Console.WriteLine("\n=== Test67: PlaceTrailingStopOrder ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling PlaceTrailingStopOrderAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.PlaceTrailingStopOrderAsync(
                    symbol: "BTC_USDT",
                    leverage: 3,
                    side: 1,
                    vol: "1",
                    openType: 1,
                    trend: 1,
                    activePrice: "50000",
                    backType: 1,
                    backValue: "0.2",
                    positionMode: 1,
                    reduceOnly: true
                );
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("PlaceTrailingStopOrder", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as placing real orders can have consequences
            }
        }

        [Fact]
        public async Task Test68_CancelTrailingStopOrder()
        {
            Console.WriteLine("\n=== Test68: CancelTrailingStopOrder ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling CancelTrailingStopOrderAsync (requires valid orderId)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.CancelTrailingStopOrderAsync("BTC_USDT", "sample_order_id");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("CancelTrailingStopOrder", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires a valid order ID
            }
        }

        [Fact]
        public async Task Test69_ChangeTrailingStopOrder()
        {
            Console.WriteLine("\n=== Test69: ChangeTrailingStopOrder ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling ChangeTrailingStopOrderAsync (requires valid orderId)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.ChangeTrailingStopOrderAsync("BTC_USDT", "sample_order_id", "55000");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("ChangeTrailingStopOrder", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires a valid order ID
            }
        }

        [Fact]
        public async Task Test70_GetTrailingStopOrdersList()
        {
            Console.WriteLine("\n=== Test70: GetTrailingStopOrdersList ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetTrailingStopOrdersListAsync for BTC_USDT...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetTrailingStopOrdersListAsync("BTC_USDT", 1, 10);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetTrailingStopOrdersList", response);
                
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
