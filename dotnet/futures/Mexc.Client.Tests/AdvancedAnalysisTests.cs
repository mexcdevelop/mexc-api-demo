using System;
using System.Text.Json;
using System.Threading.Tasks;
using Xunit;

namespace Mexc.Client.Tests
{
    public class AdvancedAnalysisTests : AccountTradingTestBase
    {
        [Fact]
        public async Task Test12_GetAssetAnalysisV3()
        {
            Console.WriteLine("\n=== Test12: GetAssetAnalysisV3 ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                var endTime = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
                var startTime = endTime - 30L * 24 * 60 * 60 * 1000;
                
                Console.WriteLine("Calling GetAssetAnalysisV3Async...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetAssetAnalysisV3Async(startTime, endTime, 1, 1, "BTC_USDT");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetAssetAnalysisV3", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test13_GetCalendarDailyV3()
        {
            Console.WriteLine("\n=== Test13: GetCalendarDailyV3 ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                var endTime = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
                var startTime = endTime - 30L * 24 * 60 * 60 * 1000;
                
                Console.WriteLine("Calling GetCalendarDailyV3Async...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetCalendarDailyV3Async(startTime, endTime, 1, 1);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetCalendarDailyV3", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test14_GetCalendarMonthlyV3()
        {
            Console.WriteLine("\n=== Test14: GetCalendarMonthlyV3 ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetCalendarMonthlyV3Async...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetCalendarMonthlyV3Async(1, 1);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetCalendarMonthlyV3", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test15_GetRecentAnalysisV3()
        {
            Console.WriteLine("\n=== Test15: GetRecentAnalysisV3 ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetRecentAnalysisV3Async...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetRecentAnalysisV3Async(1, 1, "BTC_USDT");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetRecentAnalysisV3", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test20_ExportAssetAnalysis()
        {
            Console.WriteLine("\n=== Test20: ExportAssetAnalysis ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                var endTime = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
                var startTime = endTime - 30L * 24 * 60 * 60 * 1000;
                
                Console.WriteLine("Calling ExportAssetAnalysisAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.ExportAssetAnalysisAsync(startTime, endTime, "BTC_USDT", 1, 1);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("ExportAssetAnalysis", response);
                
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
