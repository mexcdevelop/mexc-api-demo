using System;
using System.Text.Json;
using Microsoft.Extensions.Logging;
using Moq;

namespace Mexc.Client.Tests
{
    public abstract class TestBase : IDisposable
    {
        protected readonly Mock<ILogger> MockLogger;
        protected readonly string TestSymbol = "BTC_USDT";
        protected readonly string TestCurrency = "USDT";

        protected TestBase()
        {
            MockLogger = new Mock<ILogger>();
        }

        public virtual void Dispose()
        {
            GC.SuppressFinalize(this);
        }

        protected JsonElement GetProperty(JsonDocument doc, string propertyName)
        {
            if (doc == null)
                return default;
                
            return doc.RootElement.TryGetProperty(propertyName, out var value) ? value : default;
        }

        protected bool IsSuccessResponse(JsonDocument response)
        {
            return response != null && 
                   response.RootElement.TryGetProperty("success", out var success) && 
                   success.GetBoolean();
        }
    }
}
