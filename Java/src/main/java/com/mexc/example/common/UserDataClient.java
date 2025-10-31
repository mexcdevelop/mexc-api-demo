package com.mexc.example.common;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

@Slf4j
public class UserDataClient {
    private static final String REQUEST_HOST = TestConfig.BASE_URL;

    private static final OkHttpClient OK_HTTP_CLIENT = createOkHttpClient();

    private static final String accessKey = TestConfig.ACCESS_KEY;
    private static final String secretKey = TestConfig.SECRET_KEY;

    private static OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new SignatureInterceptor(secretKey, accessKey))
                .build();
    }

    /**
     * Print request details including complete URL and all headers
     */
    private static void logRequestDetails(Request request) {
        log.info("=== HTTP Request Details ===");
        log.info("Method: {}", request.method());
        log.info("Complete URL: {}", request.url().toString());
        
        Headers headers = request.headers();
        log.info("Headers ({} total):", headers.size());
        for (int i = 0; i < headers.size(); i++) {
            log.info("  {}: {}", headers.name(i), headers.value(i));
        }
        
        if (request.body() != null) {
            log.info("Request Body Type: {}", request.body().contentType());
            try {
                okio.Buffer buffer = new okio.Buffer();
                request.body().writeTo(buffer);
                log.info("Request Body Content: {}", buffer.readUtf8());
            } catch (Exception e) {
                log.warn("Could not read request body: {}", e.getMessage());
            }
        } else {
            log.info("Request Body: null");
        }
        log.info("=== End Request Details ===");
    }

    public static <T> T get(String uri, Map<String, String> params, TypeReference<T> ref) {
        try {
            Request request = new Request.Builder().url(REQUEST_HOST + uri + "?" + SignatureUtil.toQueryString(params)).get().build();
            logRequestDetails(request);
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            return handleResponse(response, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T post(String uri, Map<String, String> params, TypeReference<T> ref) {
        try {
            Request request = new Request.Builder()
                    .url(REQUEST_HOST.concat(uri))
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(SignatureUtil.toQueryString(params), MediaType.get("text/plain")))
                    .build();
            logRequestDetails(request);
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            return handleResponse(response, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * POST request - Mixed mode: some parameters via query string, some via body
     */
    public static <T> T postMixed(String uri, Map<String, String> queryParams, Map<String, String> bodyParams, TypeReference<T> ref) {
        try {
            // Build URL with query string
            HttpUrl.Builder urlBuilder = HttpUrl.parse(REQUEST_HOST.concat(uri)).newBuilder();
            if (queryParams != null) {
                for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                    urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }
            }
            
            // Build body content
            String bodyContent = "";
            if (bodyParams != null && !bodyParams.isEmpty()) {
                bodyContent = SignatureUtil.toQueryString(bodyParams);
            }
            
            Request request = new Request.Builder()
                    .url(urlBuilder.build())
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(bodyContent, MediaType.get("text/plain")))
                    .build();
            logRequestDetails(request);
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            return handleResponse(response, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * POST request - JSON format
     */
    public static <T> T postJson(String uri, Map<String, String> params, TypeReference<T> ref) {
        try {
            Request request = new Request.Builder()
                    .url(REQUEST_HOST.concat(uri))
                    .post(RequestBody.create(SignatureUtil.toQueryString(params), MediaType.get("application/json")))
                    .build();
            logRequestDetails(request);
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            return handleResponse(response, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T postEmptyBody(String uri, Map<String, String> params, TypeReference<T> ref) {
        try {
            // Build URL with parameters, let SignatureInterceptor handle signature
            HttpUrl.Builder urlBuilder = HttpUrl.parse(REQUEST_HOST.concat(uri)).newBuilder();
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }
            }

            RequestBody empty = RequestBody.create(null, new byte[0]);
            Request request = new Request.Builder()
                    .url(urlBuilder.build())
                    .method("POST", empty)
                    .addHeader("Content-Type", "application/json")
                    .header("Content-Length", "0")
                    .build();
            logRequestDetails(request);
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            return handleResponse(response, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T put(String uri, Map<String, String> params, TypeReference<T> ref) {
        try {
            Request request = new Request.Builder()
                    .url(REQUEST_HOST.concat(uri))
                    .addHeader("Content-Type", "application/json")
                    .put(RequestBody.create(SignatureUtil.toQueryString(params), MediaType.get("text/plain")))
                    .build();
            logRequestDetails(request);
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            return handleResponse(response, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static <T> T delete(String uri, Map<String, String> params, TypeReference<T> ref) {
        try {
            Request request = new Request.Builder()
                    .url(REQUEST_HOST.concat(uri))
                    .addHeader("Content-Type", "application/json")
                    .delete(RequestBody.create(SignatureUtil.toQueryString(params), MediaType.get("text/plain")))
                    .build();
            logRequestDetails(request);
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            return handleResponse(response, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static <T> T handleResponse(Response response, TypeReference<T> ref) throws IOException {
        Gson gson = new Gson();
        assert response.body() != null;
        String content = response.body().string();
        if (200 != response.code()) {
            throw new RuntimeException(content);
        }
        return gson.fromJson(content, ref.getType());
    }


}
