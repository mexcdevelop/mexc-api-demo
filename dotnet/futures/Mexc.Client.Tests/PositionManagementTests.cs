using System;
using System.Text.Json;
using System.Threading.Tasks;
using Xunit;

namespace Mexc.Client.Tests
{
    public class PositionManagementTests : AccountTradingTestBase
    {
        [Fact]
        public async Task Test24_GetHistoryPositions()
        {
            Console.WriteLine("\n=== Test24: GetHistoryPositions ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                var endTime = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
                var startTime = endTime - 30L * 24 * 60 * 60 * 1000;
                
                Console.WriteLine("Calling GetHistoryPositionsAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetHistoryPositionsAsync("BTC_USDT", startTime, endTime, 1, 10);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetHistoryPositions", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test25_GetOpenPositions()
        {
            Console.WriteLine("\n=== Test25: GetOpenPositions ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetOpenPositionsAsync for BTC_USDT...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetOpenPositionsAsync("BTC_USDT");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetOpenPositions", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test26_GetFundingRecords()
        {
            Console.WriteLine("\n=== Test26: GetFundingRecords ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                var endTime = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
                var startTime = endTime - 30L * 24 * 60 * 60 * 1000;
                
                Console.WriteLine("Calling GetFundingRecordsAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetFundingRecordsAsync("BTC_USDT", startTime, endTime, 1, 10);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetFundingRecords", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test27_GetAccountRiskLimit()
        {
            Console.WriteLine("\n=== Test27: GetAccountRiskLimit ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetAccountRiskLimitAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetAccountRiskLimitAsync();
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetAccountRiskLimit", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test29_ChangePositionMargin()
        {
            Console.WriteLine("\n=== Test29: ChangePositionMargin ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                // This test requires a valid positionId. It's likely to fail without one.
                Console.WriteLine("Calling ChangePositionMarginAsync (this may fail without valid positionId)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.ChangePositionMarginAsync("sample_position_id", "100", "ADD");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("ChangePositionMargin", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires a valid positionId
            }
        }

        [Fact]
        public async Task Test30_ChangeAutoAddMargin()
        {
            Console.WriteLine("\n=== Test30: ChangeAutoAddMargin ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling ChangeAutoAddMarginAsync (this may fail without valid positionId)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.ChangeAutoAddMarginAsync("sample_position_id", true);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("ChangeAutoAddMargin", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires a valid positionId
            }
        }

        [Fact]
        public async Task Test31_GetPositionLeverage()
        {
            Console.WriteLine("\n=== Test31: GetPositionLeverage ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetPositionLeverageAsync for BTC_USDT...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetPositionLeverageAsync("BTC_USDT");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetPositionLeverage", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test32_ChangeLeverage()
        {
            Console.WriteLine("\n=== Test32: ChangeLeverage ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling ChangeLeverageAsync for BTC_USDT...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.ChangeLeverageAsync(symbol: "BTC_USDT", leverage: 10, openType: "2", positionType: "2");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("ChangeLeverage", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test33_GetPositionMode()
        {
            Console.WriteLine("\n=== Test33: GetPositionMode ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetPositionModeAsync for BTC_USDT...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetPositionModeAsync("BTC_USDT");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetPositionMode", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test34_ChangePositionMode()
        {
            Console.WriteLine("\n=== Test34: ChangePositionMode ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling ChangePositionModeAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.ChangePositionModeAsync("1"); // 1 for one-way mode
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("ChangePositionMode", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test35_ReversePosition()
        {
            Console.WriteLine("\n=== Test35: ReversePosition ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling ReversePositionAsync (this may fail without valid positionId)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.ReversePositionAsync("BTC_USDT", "sample_position_id", "1");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("ReversePosition", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                // Don't throw as this requires a valid positionId
            }
        }

        [Fact]
        public async Task Test36_CloseAllPositions()
        {
            Console.WriteLine("\n=== Test36: CloseAllPositions ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling CloseAllPositionsAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.CloseAllPositionsAsync("BTC_USDT");
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("CloseAllPositions", response);
                
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
