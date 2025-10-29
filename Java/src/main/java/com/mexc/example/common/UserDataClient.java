package com.mexc.example.common;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

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


    public static <T> T get(String uri, Map<String, String> params, TypeReference<T> ref) {
        try {
            Response response = OK_HTTP_CLIENT
                    .newCall(new Request.Builder().url(REQUEST_HOST + uri + "?" + SignatureUtil.toQueryString(params)).get().build())
                    .execute();
            return handleResponse(response, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T post(String uri, Map<String, String> params, TypeReference<T> ref) {
        try {
            Response response = OK_HTTP_CLIENT
                    .newCall(new Request.Builder()
                            .url(REQUEST_HOST.concat(uri))
                            .post(RequestBody.create(SignatureUtil.toQueryString(params), MediaType.get("text/plain"))).build()).execute();
            return handleResponse(response, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> T postEmptyBody(String uri, Map<String, String> params, TypeReference<T> ref) {
        try {
            String timestamp = Instant.now().toEpochMilli() + "";
            String paramsStr = SignatureUtil.toQueryStringWithEncoding(params);
            paramsStr += "&timestamp=" + timestamp;
            String signature = SignatureUtil.actualSignature(paramsStr, secretKey);
            paramsStr += "&signature=" + signature;


            RequestBody empty = RequestBody.create(null, new byte[0]);
            Request.Builder body = new Request.Builder().url(REQUEST_HOST.concat(uri).concat("?").concat(paramsStr)).method("POST", empty).header("Content-Length", "0");
            Response response = OK_HTTP_CLIENT
                    .newCall(body.build()).execute();
            return handleResponse(response, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T put(String uri, Map<String, String> params, TypeReference<T> ref) {
        try {
            Response response = OK_HTTP_CLIENT
                    .newCall(new Request.Builder()
                            .url(REQUEST_HOST.concat(uri))
                            .put(RequestBody.create(SignatureUtil.toQueryString(params), MediaType.get("text/plain"))).build()).execute();
            return handleResponse(response, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static <T> T delete(String uri, Map<String, String> params, TypeReference<T> ref) {
        try {
            return handleResponse(OK_HTTP_CLIENT
                    .newCall(new Request.Builder()
                            .url(REQUEST_HOST.concat(uri))
                            .delete(RequestBody.create(SignatureUtil.toQueryString(params), MediaType.get("text/plain"))).build()).execute(), ref);
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
