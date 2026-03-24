using System.Net;
using System.Text;
using System.Text.Json;

namespace Mexc.Client.Tests
{
    public class MockHttpMessageHandler : HttpMessageHandler
    {
        private readonly Dictionary<string, Func<HttpRequestMessage, HttpResponseMessage>> _responseMap;
        private readonly List<HttpRequestMessage> _receivedRequests;

        public MockHttpMessageHandler()
        {
            _responseMap = new Dictionary<string, Func<HttpRequestMessage, HttpResponseMessage>>();
            _receivedRequests = new List<HttpRequestMessage>();
        }

        public IReadOnlyList<HttpRequestMessage> ReceivedRequests => _receivedRequests.AsReadOnly();

        public void SetupResponse(string method, string path, HttpStatusCode statusCode, object responseBody)
        {
            var key = $"{method.ToUpper()}:{path}";
            _responseMap[key] = (request) => CreateResponse(statusCode, responseBody);
            Console.WriteLine($"Setup response for key: {key}");
        }

        public void SetupResponse(string method, string path, Func<HttpRequestMessage, HttpResponseMessage> responseFactory)
        {
            var key = $"{method.ToUpper()}:{path}";
            _responseMap[key] = responseFactory;
            Console.WriteLine($"Setup response for key: {key}");
        }

        public void VerifyRequest(string method, string path, Moq.Times times)
        {
            var count = _receivedRequests.Count(r => 
                r.Method.ToString().ToUpper() == method.ToUpper() && 
                r.RequestUri.PathAndQuery.StartsWith(path));
            
            if (times == Moq.Times.Once() && count != 1)
                throw new Exception($"Expected 1 request to {method} {path}, but found {count}");
            else if (times == Moq.Times.Never() && count > 0)
                throw new Exception($"Expected 0 requests to {method} {path}, but found {count}");
            else if (times == Moq.Times.AtLeastOnce() && count == 0)
                throw new Exception($"Expected at least 1 request to {method} {path}, but found 0");
        }

        private HttpResponseMessage CreateResponse(HttpStatusCode statusCode, object body)
        {
            var json = JsonSerializer.Serialize(body);
            return new HttpResponseMessage(statusCode)
            {
                Content = new StringContent(json, Encoding.UTF8, "application/json")
            };
        }

        protected override async Task<HttpResponseMessage> SendAsync(
            HttpRequestMessage request, 
            CancellationToken cancellationToken)
        {
            _receivedRequests.Add(request);
            
            var fullPath = request.RequestUri.PathAndQuery;
            var method = request.Method.ToString().ToUpper();
            
            Console.WriteLine($"Received request: {method} {fullPath}");
            
            // Try exact match first
            var exactKey = $"{method}:{fullPath}";
            if (_responseMap.TryGetValue(exactKey, out var exactResponse))
            {
                Console.WriteLine($"Found exact match for: {exactKey}");
                return exactResponse(request);
            }
            
            // Try path-only match (without query parameters)
            var pathOnly = request.RequestUri.AbsolutePath;
            var pathKey = $"{method}:{pathOnly}";
            if (_responseMap.TryGetValue(pathKey, out var pathResponse))
            {
                Console.WriteLine($"Found path match for: {pathKey}");
                return pathResponse(request);
            }
            
            Console.WriteLine($"No match found for: {method}:{fullPath}");
            
            // Default empty response
            return CreateResponse(HttpStatusCode.OK, new { success = true, code = 0, data = new { } });
        }
    }

    public static class Times
    {
        public static Moq.Times Once => Moq.Times.Once();
        public static Moq.Times Never => Moq.Times.Never();
        public static Moq.Times AtLeastOnce => Moq.Times.AtLeastOnce();
        public static Moq.Times Exactly(int count) => Moq.Times.Exactly(count);
    }
}