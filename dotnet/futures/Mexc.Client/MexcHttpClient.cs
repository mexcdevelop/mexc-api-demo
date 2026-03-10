using System.Net.Http.Headers;
using System.Security.Cryptography;
using System.Text;
using System.Text.Json;
namespace Mexc.Client
{
    /// <summary>
    /// MEXC API Base Client
    /// </summary>
    public class MexcHttpClient : IDisposable
    {
        protected readonly HttpClient _httpClient;
        protected readonly ILogger _logger;
        protected readonly string _apiKey;
        protected readonly string _secretKey;
        protected readonly bool _isSigned;

        public MexcHttpClient(string apiKey = null, string secretKey = null, ILogger logger = null)
        {
            _httpClient = new HttpClient();
            _httpClient.BaseAddress = new Uri("https://api.mexc.co");
            _httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
            
            _logger = logger ?? NullLogger.Instance;
            _apiKey = apiKey;
            _secretKey = secretKey;
            _isSigned = !string.IsNullOrEmpty(apiKey) && !string.IsNullOrEmpty(secretKey);
        }

        #region Signature Generation (Java Implementation)

        /// <summary>
        /// Generate HMAC SHA256 signature
        /// </summary>
        protected virtual string GenerateSignature(string timestamp, Dictionary<string, string> paramsDict)
        {
            try
            {
                var signTarget = CreateSignatureString(timestamp, paramsDict);
                return Sign(signTarget);
            }
            catch (Exception ex)
            {
                _logger?.LogError(ex, "Failed to generate signature");
                throw new InvalidOperationException("Failed to generate signature", ex);
            }
        }

        /// <summary>
        /// Generate signature with body
        /// </summary>
        protected virtual string GenerateSignature(string timestamp, string bodyRaw)
        {
            try
            {
                var signTarget = _apiKey + timestamp + bodyRaw;
                return Sign(signTarget);
            }
            catch (Exception ex)
            {
                _logger?.LogError(ex, "Failed to generate signature with body");
                throw new InvalidOperationException("Failed to generate signature", ex);
            }
        }

        /// <summary>
        /// Sign the target string with HMAC SHA256
        /// </summary>
        private string Sign(string signTarget)
        {
            _logger?.LogDebug($"Sign target: {signTarget}");

            using var hmac = new HMACSHA256(Encoding.UTF8.GetBytes(_secretKey));
            var hash = hmac.ComputeHash(Encoding.UTF8.GetBytes(signTarget));
            
            // Convert to hex string (same as Java Hex.encodeHexString)
            return BitConverter.ToString(hash).Replace("-", "").ToLower();
        }

        /// <summary>
        /// Create signature string from timestamp and parameters
        /// Format: apiKey + timestamp + key1=value1&key2=value2
        /// </summary>
        private string CreateSignatureString(string timestamp, Dictionary<string, string> paramsDict)
        {
            var stringBuilder = new StringBuilder();
            
            // Add apiKey and timestamp
            stringBuilder.Append(_apiKey);
            stringBuilder.Append(timestamp);

            // Add sorted parameters (using TreeMap equivalent - sorted by key)
            if (paramsDict != null && paramsDict.Any())
            {
                // Sort parameters by key (like Java TreeMap)
                var sortedParams = paramsDict.OrderBy(x => x.Key).ToDictionary(x => x.Key, x => x.Value);
                
                foreach (var param in sortedParams)
                {
                    stringBuilder.Append($"{param.Key}={param.Value}");
                    stringBuilder.Append("&");
                }
                
                // Remove the last '&'
                if (stringBuilder.Length > 0 && stringBuilder[^1] == '&')
                {
                    stringBuilder.Remove(stringBuilder.Length - 1, 1);
                }
            }

            return stringBuilder.ToString();
        }

        #endregion

        #region URL Building

        /// <summary>
        /// Build URL with query parameters
        /// </summary>
        protected virtual string BuildUrl(string endpoint, Dictionary<string, string> paramsDict = null)
        {
            if (paramsDict == null || !paramsDict.Any())
            {
                return endpoint;
            }

            var queryString = string.Join("&", paramsDict.Select(x => $"{x.Key}={Uri.EscapeDataString(x.Value)}"));
            return $"{endpoint}?{queryString}";
        }

        #endregion

        #region HTTP Request Execution

        /// <summary>
        /// Execute HTTP request and parse response
        /// </summary>
        protected virtual async Task<JsonDocument> ExecuteAsync(HttpRequestMessage request)
        {
            try
            {
                using var response = await _httpClient.SendAsync(request);
                var responseBody = await response.Content.ReadAsStringAsync();

                if (!response.IsSuccessStatusCode)
                {
                    _logger?.LogError($"Request failed, HTTP status: {response.StatusCode}");
                    _logger?.LogError($"Response body: {responseBody}");
                    return null;
                }

                var jsonResponse = JsonDocument.Parse(responseBody);
                var root = jsonResponse.RootElement;

                if (root.TryGetProperty("success", out var success) && !success.GetBoolean())
                {
                    var code = root.TryGetProperty("code", out var codeElem) ? codeElem.GetInt32() : 0;
                    var message = root.TryGetProperty("message", out var msgElem) ? msgElem.GetString() : "";
                    _logger?.LogError($"Business error, code: {code}, msg: {message}");
                }

                return jsonResponse;
            }
            catch (Exception ex)
            {
                _logger?.LogError(ex, "Request failed");
                throw;
            }
        }

        #endregion

        #region Public HTTP Methods

        /// <summary>
        /// Send GET request (public endpoint)
        /// </summary>
        public virtual async Task<JsonDocument> GetAsync(string endpoint, Dictionary<string, string> paramsDict = null)
        {
            var url = BuildUrl(endpoint, paramsDict);
            _logger?.LogDebug($"GET {url}");

            var request = new HttpRequestMessage(HttpMethod.Get, url);
            request.Headers.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

            return await ExecuteAsync(request);
        }

        /// <summary>
        /// Send signed GET request (private endpoint)
        /// </summary>
        public virtual async Task<JsonDocument> GetSignedAsync(string endpoint, Dictionary<string, string> paramsDict = null)
        {
            if (!_isSigned)
            {
                throw new InvalidOperationException("API key and secret key required for private endpoints");
            }

            paramsDict ??= new Dictionary<string, string>();
            
            // Add authentication parameters
            var timestamp = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds().ToString();
            
            // Build URL with parameters
            var url = BuildUrl(endpoint, paramsDict);

            // Create request
            var request = new HttpRequestMessage(HttpMethod.Get, url);
            
            // Add headers (same as Java implementation)
            request.Headers.Add("ApiKey", _apiKey);
            request.Headers.Add("Request-Time", timestamp);
            request.Headers.Add("Signature", GenerateSignature(timestamp, paramsDict));
            request.Headers.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

            return await ExecuteAsync(request);
        }

        /// <summary>
        /// Send signed POST request with JSON body
        /// </summary>
        public virtual async Task<JsonDocument> PostSignedAsync(string endpoint, Dictionary<string, string> paramsDict = null)
        {
            if (!_isSigned)
            {
                throw new InvalidOperationException("API key and secret key required for private endpoints");
            }

            paramsDict ??= new Dictionary<string, string>();
            
            // Serialize body
            var bodyString = JsonSerializer.Serialize(paramsDict);
            var timestamp = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds().ToString();

            // Create request
            var request = new HttpRequestMessage(HttpMethod.Post, $"{_httpClient.BaseAddress}{endpoint}");
            
            // Add headers
            request.Headers.Add("ApiKey", _apiKey);
            request.Headers.Add("Request-Time", timestamp);
            request.Headers.Add("Signature", GenerateSignature(timestamp, bodyString));
            request.Headers.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
            
            // Set content with proper Content-Type
            request.Content = new StringContent(bodyString, Encoding.UTF8, "application/json");

            return await ExecuteAsync(request);
        }

        /// <summary>
        /// Send signed POST request with array body
        /// </summary>
        public virtual async Task<JsonDocument> PostSignedWithArrayBodyAsync(string endpoint, List<object> list)
        {
            if (!_isSigned)
            {
                throw new InvalidOperationException("API key and secret key required for private endpoints");
            }

            // Serialize array body
            var bodyString = JsonSerializer.Serialize(list);
            var timestamp = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds().ToString();

            // Create request
            var request = new HttpRequestMessage(HttpMethod.Post, $"{_httpClient.BaseAddress}{endpoint}");
            
            // Add headers
            request.Headers.Add("ApiKey", _apiKey);
            request.Headers.Add("Request-Time", timestamp);
            request.Headers.Add("Signature", GenerateSignature(timestamp, bodyString));
            request.Headers.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
            
            // Set content with proper Content-Type
            request.Content = new StringContent(bodyString, Encoding.UTF8, "application/json");

            return await ExecuteAsync(request);
        }

        #endregion

        #region IDisposable Implementation

        public void Dispose()
        {
            _httpClient?.Dispose();
            GC.SuppressFinalize(this);
        }

        #endregion
    }
}