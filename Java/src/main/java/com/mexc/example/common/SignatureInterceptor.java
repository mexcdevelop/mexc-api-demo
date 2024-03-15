package com.mexc.example.common;


import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.mexc.example.common.SignatureUtil.actualSignature;
import static com.mexc.example.common.SignatureUtil.toQueryStringWithEncoding;


@Slf4j
public class SignatureInterceptor implements Interceptor {

    private static final String HEADER_ACCESS_KEY = "X-MEXC-APIKEY";
    private final String secretKey;
    private final String accessKey;

    public SignatureInterceptor(String secretKey, String accessKey) {
        this.secretKey = secretKey;
        this.accessKey = accessKey;
    }


    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request origRequest = chain.request();
        String method = origRequest.method();
        Request newRequest;
        if ("GET".equals(method)) {
            newRequest = createUrlSignRequest(origRequest);
        } else if ("POST".equals(method) || "DELETE".equals(method) || "PUT".equals(method)) {
            RequestBody origBody = origRequest.body();
            if (origBody != null) {
                //support body params
                newRequest = createBodySignRequest(origRequest, origBody, method);
            } else {
                //support query params
                newRequest = createUrlSignRequest(origRequest);
            }
        } else {
            return chain.proceed(origRequest);
        }
        return chain.proceed(newRequest);
    }

    private Request createBodySignRequest(Request origRequest, RequestBody origBody, String method) {

        if (origRequest.url().uri().getPath().equals("/api/v3/batchOrders")) {
            return origRequest.newBuilder()
                    .addHeader(HEADER_ACCESS_KEY, accessKey)
                    .post(RequestBody.create(StringUtils.EMPTY, MediaType.get("application/json"))).build();
        }

        String params = bodyToString(origBody);
        params += "&timestamp=" + Instant.now().toEpochMilli();
        String originalParamsStr = toQueryStringWithEncoding(body2Map(params));
        params += "&signature=" + actualSignature(originalParamsStr, secretKey);

        if ("POST".equals(method)) {
            return origRequest.newBuilder()
                    .addHeader(HEADER_ACCESS_KEY, accessKey)
                    .post(RequestBody.create(params, MediaType.get("application/json"))).build();
        } else {
            return origRequest.newBuilder()
                    .addHeader(HEADER_ACCESS_KEY, accessKey)
                    .delete(RequestBody.create(params, MediaType.get("application/json"))).build();
        }
    }

    private Request createUrlSignRequest(Request request) {
        String timestamp = Instant.now().toEpochMilli() + "";
        HttpUrl url = request.url();
        HttpUrl.Builder urlBuilder = url
                .newBuilder()
                .setQueryParameter("timestamp", timestamp);
        String queryParams = urlBuilder.build().query();
        urlBuilder.setQueryParameter("signature", actualSignature(queryParams, secretKey));
        return request.newBuilder()
                .addHeader(HEADER_ACCESS_KEY, accessKey)
                .url(urlBuilder.build()).build();
    }


    private String bodyToString(RequestBody body) {
        try {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            return buffer.readString(Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private Map<String, String> body2Map(String body) {
        Map<String, String> retVal = new LinkedHashMap<>();
        if (StringUtils.isEmpty(body)) {
            return retVal;
        }

        if (body.contains("&")) {
            String[] paramsList = body.split("&");
            for (String param : paramsList) {
                if (param.contains("=")) {
                    String[] pair = param.split("=");
                    if (pair.length == 2) {
                        retVal.put(pair[0], pair[1]);
                    } else {
                        retVal.put(pair[0], "");
                    }
                }
            }
        } else if (body.contains("=")) {
            String[] pair = body.split("=");
            if (pair.length == 2) {
                retVal.put(pair[0], pair[1]);
            } else {
                retVal.put(pair[0], "");
            }
        }
        return retVal;
    }

}


