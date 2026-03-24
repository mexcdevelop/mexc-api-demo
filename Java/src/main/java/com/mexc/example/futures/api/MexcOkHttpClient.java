package com.mexc.example.futures.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * MEXC Futures API OkHttp Client
 * Base client with authentication and HTTP utilities
 */
public class MexcOkHttpClient {
    private static final String BASE_URL = "https://api.mexc.co";
    private static final MediaType JSON = MediaType.parse("application/json");

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String apiKey;
    private final String secretKey;
    private final boolean isSigned;

    public MexcOkHttpClient() {
        this(null, null);
    }

    public MexcOkHttpClient(String apiKey, String secretKey) {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.gson = new GsonBuilder().create();
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.isSigned = apiKey != null && secretKey != null;
    }

    /**
     * Generate HMAC SHA256 signature
     */
    private String generateSignature(String timestamp, Map<String, String> params) {
        try {
            String stringBuilder = createSignatureString(timestamp, params);
            return sign(stringBuilder);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to generate signature", e);
        }
    }

    private String sign(String signTarget) throws NoSuchAlgorithmException, InvalidKeyException {
        System.out.println(signTarget);
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(signTarget.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hash);
    }

    private String createSignatureString(String timestamp, Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(apiKey);
        stringBuilder.append(timestamp);
        if (!params.isEmpty()) {
            Map<String, String> storedMap = new TreeMap<>(params);
            storedMap.forEach((key, value) -> {
                stringBuilder.append(key).append("=").append(value);
                stringBuilder.append("&");
            });
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    private String generateSignature(String timestamp, String bodyRaw) {
        try {
            String stringBuilder = apiKey +
                    timestamp +
                    bodyRaw;
            return sign(stringBuilder);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to generate signature", e);
        }
    }

    /**
     * Build URL with query parameters
     */
    private String buildUrl(String endpoint, Map<String, String> params) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(BASE_URL + endpoint)).newBuilder();
        if (params != null) {
            params.forEach(urlBuilder::addQueryParameter);
        }
        return urlBuilder.build().toString();
    }

    /**
     * Execute HTTP request and parse response
     */
    private JsonObject execute(Request request) throws IOException {
        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (!response.isSuccessful()) {
                System.err.println("Request failed, HTTP status: " + response.code());
                System.err.println("Response body: " + responseBody);
                return null;
            }

            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            if (!jsonResponse.get("success").getAsBoolean()) {
                System.err.println("Business error, code: " + jsonResponse.get("code") + ",msg: " + jsonResponse.get("message"));
            }
            return jsonResponse;
        }
    }

    /**
     * Send GET request for public endpoints
     */
    protected JsonObject get(String endpoint, Map<String, String> params) throws IOException {
        String url = buildUrl(endpoint, params);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .get()
                .build();
        return execute(request);
    }

    /**
     * Send signed GET request for private endpoints
     */
    protected JsonObject getSigned(String endpoint, Map<String, String> params) throws IOException {
        if (!isSigned) {
            throw new IllegalStateException("API key and secret key required");
        }

        if (params == null) {
            params = new HashMap<>();
        }
        String timestamp = String.valueOf(System.currentTimeMillis());
        String url = buildUrl(endpoint, params);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("ApiKey", apiKey)
                .addHeader("Request-Time", timestamp)
                .addHeader("Signature", generateSignature(timestamp, params))
                .get()
                .build();
        return execute(request);
    }

    /**
     * Send signed POST request with JSON body
     */
    protected JsonObject postSigned(String endpoint, Map<String, String> params) throws IOException {
        if (!isSigned) {
            throw new IllegalStateException("API key and secret key required");
        }
        if (params == null) {
            params = new HashMap<>();
        }
        String bodyString = gson.toJson(params);
        RequestBody requestBody = RequestBody.create(bodyString, JSON);
        String timestamp = String.valueOf(System.currentTimeMillis());
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .addHeader("Accept", "application/json")
                .addHeader("ApiKey", apiKey)
                .addHeader("Request-Time", timestamp)
                .addHeader("Signature", generateSignature(timestamp, bodyString))
                .post(requestBody)
                .build();

        return execute(request);
    }

    /**
     * Send signed POST request with array data
     */
    protected JsonObject postSignedWithArrayBody(String endpoint, List<Object> list) throws IOException {
        if (!isSigned) {
            throw new IllegalStateException("API key and secret key required");
        }
        String bodyString = gson.toJson(list);
        RequestBody requestBody = RequestBody.create(bodyString, JSON);
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .addHeader("Accept", "application/json")
                .addHeader("ApiKey", apiKey)
                .addHeader("Request-Time", timestamp)
                .addHeader("Signature", generateSignature(timestamp, bodyString))
                .post(requestBody)
                .build();
        return execute(request);
    }

}