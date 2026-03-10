using System;
using System.Text.Json;
using System.Threading.Tasks;
using Xunit;

namespace Mexc.Client.Tests
{
    public abstract class AccountTradingTestBase : TestBase
    {
        protected readonly MexcAccountTradingApi _client;
        protected readonly bool _hasApiKeys;

        protected AccountTradingTestBase()
        {
            var apiKey = Environment.GetEnvironmentVariable("MEXC_API_KEY");
            var secretKey = Environment.GetEnvironmentVariable("MEXC_SECRET_KEY");
            
            _hasApiKeys = !string.IsNullOrEmpty(apiKey) && !string.IsNullOrEmpty(secretKey);
            
            if (_hasApiKeys)
            {
                Console.WriteLine("✅ API keys found");
                Console.WriteLine($"API Key: {apiKey.Substring(0, 5)}...");
                _client = new MexcAccountTradingApi(apiKey, secretKey, MockLogger.Object);
            }
            else
            {
                Console.WriteLine("⚠️ No API keys found, tests will be skipped");
                _client = null;
            }
        }

        protected void PrintResponse(string methodName, JsonDocument response)
        {
            Console.WriteLine($"\n=== {methodName} Response ===");
            
            if (response == null)
            {
                Console.WriteLine("Response is null");
                return;
            }

            try
            {
                using var stream = new System.IO.MemoryStream();
                using var writer = new Utf8JsonWriter(stream, new JsonWriterOptions { Indented = true });
                response.RootElement.WriteTo(writer);
                writer.Flush();
                
                var jsonString = System.Text.Encoding.UTF8.GetString(stream.ToArray());
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

        protected void SkipIfNoApiKeys()
        {
            if (!_hasApiKeys || _client == null)
            {
                throw new SkipException("⏭️ Test skipped: No API keys");
            }
        }
    }

    public class SkipException : Exception
    {
        public SkipException(string message) : base(message) { }
    }
}
