using System;
using System.Text.Json;
using System.Threading.Tasks;
using Xunit;

namespace Mexc.Client.Tests
{
    public class AccountAssetsTests : AccountTradingTestBase
    {
        [Fact]
        public async Task Test01_GetAllAccountAssets()
        {
            Console.WriteLine("\n=== Test01: GetAllAccountAssets ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetAllAccountAssetsAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetAllAccountAssetsAsync();
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetAllAccountAssets", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test02_GetSingleAsset()
        {
            Console.WriteLine("\n=== Test02: GetSingleAsset ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                var currency = "USDT";
                Console.WriteLine($"Calling GetSingleAssetAsync for {currency}...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetSingleAssetAsync(currency);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetSingleAsset", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test03_GetTransferRecord()
        {
            Console.WriteLine("\n=== Test03: GetTransferRecord ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetTransferRecordAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetTransferRecordAsync(pageNum: 1, pageSize: 10);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetTransferRecord", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test04_GetProfitRate_Daily()
        {
            Console.WriteLine("\n=== Test04: GetProfitRate (Daily) ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetProfitRateAsync for daily (type=1)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetProfitRateAsync(1);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetProfitRate_Daily", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test05_GetProfitRate_Weekly()
        {
            Console.WriteLine("\n=== Test05: GetProfitRate (Weekly) ===");
            
            if (!_hasApiKeys || _client == null)
            {
                Console.WriteLine("⏭️ Test skipped: No API keys");
                return;
            }

            try
            {
                Console.WriteLine("Calling GetProfitRateAsync for weekly (type=2)...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetProfitRateAsync(2);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetProfitRate_Weekly", response);
                
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
