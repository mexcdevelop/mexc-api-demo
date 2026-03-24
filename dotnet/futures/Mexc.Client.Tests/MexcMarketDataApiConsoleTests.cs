using System;
using System.Text.Json;
using System.Threading.Tasks;
using Xunit;

namespace Mexc.Client.Tests
{
    public class MexcMarketDataApiConsoleTests : TestBase
    {
        private readonly MexcMarketDataApi _client;

        public MexcMarketDataApiConsoleTests()
        {
            Console.WriteLine("✅ Initializing MexcMarketDataApi client");
            _client = new MexcMarketDataApi(MockLogger.Object);
        }

        private void PrintResponse(string methodName, JsonDocument response)
        {
            Console.WriteLine($"\n=== {methodName} Response ===");
            
            if (response == null)
            {
                Console.WriteLine("Response is null");
                return;
            }

            try
            {
                // 格式化输出 JSON
                var options = new JsonSerializerOptions
                {
                    WriteIndented = true,
                    Encoder = System.Text.Encodings.Web.JavaScriptEncoder.UnsafeRelaxedJsonEscaping
                };
                
                var jsonString = JsonSerializer.Serialize(response, options);
                Console.WriteLine(jsonString);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error serializing response: {ex.Message}");
                
                try
                {
                    var root = response.RootElement;
                    Console.WriteLine($"Raw response: {root}");
                }
                catch
                {
                    Console.WriteLine("Could not parse response");
                }
            }
            
            Console.WriteLine("=====================================\n");
        }

        [Fact]
        public async Task Test01_Ping()
        {
            Console.WriteLine("\n=== Test01: Ping ===");
            
            try
            {
                Console.WriteLine("Calling PingAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.PingAsync();
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("Ping", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                Console.WriteLine($"Stack trace: {ex.StackTrace}");
                throw;
            }
        }

        [Fact]
        public async Task Test02_GetContractDetail_All()
        {
            Console.WriteLine("\n=== Test02: GetContractDetail (All) ===");
            
            try
            {
                Console.WriteLine("Calling GetContractDetailAsync with null...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetContractDetailAsync();
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetContractDetail_All", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test03_GetContractDetail_BySymbol()
        {
            Console.WriteLine("\n=== Test03: GetContractDetail (BTC_USDT) ===");
            
            try
            {
                var symbol = "BTC_USDT";
                Console.WriteLine($"Calling GetContractDetailAsync for {symbol}...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetContractDetailAsync(symbol);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetContractDetail_BTC", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test04_GetSupportCurrencies()
        {
            Console.WriteLine("\n=== Test04: GetSupportCurrencies ===");
            
            try
            {
                Console.WriteLine("Calling GetSupportCurrenciesAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetSupportCurrenciesAsync();
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetSupportCurrencies", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test05_GetDepth()
        {
            Console.WriteLine("\n=== Test05: GetDepth ===");
            
            try
            {
                var symbol = "BTC_USDT";
                Console.WriteLine($"Calling GetDepthAsync for {symbol}...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetDepthAsync(symbol, 10);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetDepth", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test06_GetDepthCommits()
        {
            Console.WriteLine("\n=== Test06: GetDepthCommits ===");
            
            try
            {
                var symbol = "BTC_USDT";
                var limit = 5;
                Console.WriteLine($"Calling GetDepthCommitsAsync for {symbol} with limit {limit}...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetDepthCommitsAsync(symbol, limit);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetDepthCommits", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test07_GetIndexPrice()
        {
            Console.WriteLine("\n=== Test07: GetIndexPrice ===");
            
            try
            {
                var symbol = "BTC_USDT";
                Console.WriteLine($"Calling GetIndexPriceAsync for {symbol}...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetIndexPriceAsync(symbol);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetIndexPrice", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test08_GetFairPrice()
        {
            Console.WriteLine("\n=== Test08: GetFairPrice ===");
            
            try
            {
                var symbol = "BTC_USDT";
                Console.WriteLine($"Calling GetFairPriceAsync for {symbol}...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetFairPriceAsync(symbol);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetFairPrice", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test09_GetFundingRate()
        {
            Console.WriteLine("\n=== Test09: GetFundingRate ===");
            
            try
            {
                var symbol = "BTC_USDT";
                Console.WriteLine($"Calling GetFundingRateAsync for {symbol}...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetFundingRateAsync(symbol);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetFundingRate", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test10_GetFundingRateHistory()
        {
            Console.WriteLine("\n=== Test10: GetFundingRateHistory ===");
            
            try
            {
                var symbol = "BTC_USDT";
                Console.WriteLine($"Calling GetFundingRateHistoryAsync for {symbol}...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetFundingRateHistoryAsync(symbol, 10);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetFundingRateHistory", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test11_GetKline()
        {
            Console.WriteLine("\n=== Test11: GetKline ===");
            
            try
            {
                var symbol = "BTC_USDT";
                var interval = "1h";
                var endTime = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
                var startTime = endTime - 7L * 24 * 60 * 60 * 1000; // 7天前
                
                Console.WriteLine($"Calling GetKlineAsync for {symbol} with interval {interval}...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetKlineAsync(symbol, interval, startTime, endTime, 100);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetKline", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test12_GetIndexPriceKline()
        {
            Console.WriteLine("\n=== Test12: GetIndexPriceKline ===");
            
            try
            {
                var symbol = "BTC_USDT";
                var interval = "Min15";
                var endTime = DateTimeOffset.UtcNow.ToUnixTimeSeconds();
                var startTime = endTime - 7L * 24 * 60 * 60; // 7天前（秒）
                
                Console.WriteLine($"Calling GetIndexPriceKlineAsync for {symbol} with interval {interval}...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetIndexPriceKlineAsync(symbol, interval, startTime, endTime);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetIndexPriceKline", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test13_GetFairPriceKline()
        {
            Console.WriteLine("\n=== Test13: GetFairPriceKline ===");
            
            try
            {
                var symbol = "BTC_USDT";
                var interval = "Min15";
                var endTime = DateTimeOffset.UtcNow.ToUnixTimeSeconds();
                var startTime = endTime - 7L * 24 * 60 * 60; // 7天前（秒）
                
                Console.WriteLine($"Calling GetFairPriceKlineAsync for {symbol} with interval {interval}...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetFairPriceKlineAsync(symbol, interval, startTime, endTime);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetFairPriceKline", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test14_GetRecentDeals()
        {
            Console.WriteLine("\n=== Test14: GetRecentDeals ===");
            
            try
            {
                var symbol = "BTC_USDT";
                Console.WriteLine($"Calling GetRecentDealsAsync for {symbol}...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetRecentDealsAsync(symbol, 20);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetRecentDeals", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test15_GetAllTickers()
        {
            Console.WriteLine("\n=== Test15: GetAllTickers ===");
            
            try
            {
                Console.WriteLine("Calling GetAllTickersAsync...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetAllTickersAsync();
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetAllTickers", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test16_GetRiskReverseBySymbol()
        {
            Console.WriteLine("\n=== Test16: GetRiskReverseBySymbol ===");
            
            try
            {
                var symbol = "BTC_USDT";
                Console.WriteLine($"Calling GetRiskReverseBySymbolAsync for {symbol}...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetRiskReverseBySymbolAsync(symbol);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetRiskReverseBySymbol", response);
                
                Assert.NotNull(response);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"❌ Exception: {ex.GetType().Name}: {ex.Message}");
                throw;
            }
        }

        [Fact]
        public async Task Test17_GetRiskReverseHistory()
        {
            Console.WriteLine("\n=== Test17: GetRiskReverseHistory ===");
            
            try
            {
                var symbol = "BTC_USDT";
                Console.WriteLine($"Calling GetRiskReverseHistoryAsync for {symbol}...");
                var stopwatch = System.Diagnostics.Stopwatch.StartNew();
                
                var response = await _client.GetRiskReverseHistoryAsync(symbol, 1, 20);
                
                stopwatch.Stop();
                Console.WriteLine($"✅ API call completed in {stopwatch.ElapsedMilliseconds}ms");
                
                PrintResponse("GetRiskReverseHistory", response);
                
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
