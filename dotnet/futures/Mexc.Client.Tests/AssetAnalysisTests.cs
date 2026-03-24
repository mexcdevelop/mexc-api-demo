using System.Threading.Tasks;
using Xunit;

namespace Mexc.Client.Tests
{
    public class AssetAnalysisTests : AccountTradingTestBase
    {
        [Fact]
        public async Task Test06_GetAssetAnalysis_ThisWeek()
        {
            Console.WriteLine("\n=== Test06: GetAssetAnalysis (This Week) ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetAssetAnalysisAsync for this week (type=1)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetAssetAnalysisAsync(1, "USDT");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetAssetAnalysis_ThisWeek", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test07_GetAssetAnalysis_ThisMonth()
        {
            Console.WriteLine("\n=== Test07: GetAssetAnalysis (This Month) ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetAssetAnalysisAsync for this month (type=2)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetAssetAnalysisAsync(2, "USDT");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetAssetAnalysis_ThisMonth", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test08_GetAssetAnalysis_AllTime()
        {
            Console.WriteLine("\n=== Test08: GetAssetAnalysis (All Time) ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetAssetAnalysisAsync for all time (type=3)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetAssetAnalysisAsync(3, "USDT");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetAssetAnalysis_AllTime", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test09_GetAssetAnalysis_CustomRange()
        {
            Console.WriteLine("\n=== Test09: GetAssetAnalysis (Custom Range) ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                var endTime = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
                var startTime = endTime - 30L * 24 * 60 * 60 * 1000; // 30 days ago
                
                Console.WriteLine($"Calling GetAssetAnalysisAsync with custom range (type=4)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetAssetAnalysisAsync(4, "USDT", startTime, endTime);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetAssetAnalysis_CustomRange", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test10_GetFeeDeductConfigs()
        {
            Console.WriteLine("\n=== Test10: GetFeeDeductConfigs ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetFeeDeductConfigsAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetFeeDeductConfigsAsync();
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetFeeDeductConfigs", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test11_GetYesterdayPnl()
        {
            Console.WriteLine("\n=== Test11: GetYesterdayPnl ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetYesterdayPnlAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetYesterdayPnlAsync();
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetYesterdayPnl", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test16_GetTodayPnl()
        {
            Console.WriteLine("\n=== Test16: GetTodayPnl ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetTodayPnlAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetTodayPnlAsync();
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetTodayPnl", response);
                
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
