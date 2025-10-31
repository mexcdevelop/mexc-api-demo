package com.mexc.example.common;

import static com.mexc.example.common.SignatureUtil.actualSignature;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

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
        
        if ("GET".equals(method)) {
            return chain.proceed(createUrlSignRequest(origRequest));
        } else if ("POST".equals(method) || "DELETE".equals(method) || "PUT".equals(method)) {
            RequestBody origBody = origRequest.body();
            if (origBody != null) {
                return chain.proceed(createBodySignRequest(origRequest, origBody, method));
            } else {
                return chain.proceed(createUrlSignRequest(origRequest));
            }
        }
        return chain.proceed(origRequest);
    }

    private Request createBodySignRequest(Request origRequest, RequestBody origBody, String method) {
        // Special handling for batch orders API
        if (origRequest.url().uri().getPath().equals("/api/v3/batchOrders")) {
            return origRequest.newBuilder()
                    .addHeader(HEADER_ACCESS_KEY, accessKey)
                    .post(RequestBody.create(StringUtils.EMPTY, MediaType.get("application/json")))
                    .build();
        }

        String bodyContent = bodyToString(origBody);
        String timestamp = Instant.now().toEpochMilli() + "";
        String queryString = origRequest.url().query();
        
        log.info("Original query string: {}", queryString);
        log.info("Original body content: {}", bodyContent);
        log.info("Content-Type: {}", origBody.contentType());

        String signatureData = buildSignatureData(queryString, bodyContent, timestamp);
        String signature = actualSignature(signatureData, secretKey);
        String finalBodyContent = buildFinalBodyContent(bodyContent, timestamp, signature);

        log.info("Signature data: {}", signatureData);
        log.info("Generated signature: {}", signature);
        log.info("Final body content: {}", finalBodyContent);

        return origRequest.newBuilder()
                .addHeader(HEADER_ACCESS_KEY, accessKey)
                .method(method, RequestBody.create(finalBodyContent, MediaType.get("application/json")))
                .build();
    }

    private String buildSignatureData(String queryString, String bodyContent, String timestamp) {
        StringBuilder signatureBuilder = new StringBuilder();
        
        // Add query string (URL encoded)
        if (queryString != null && !queryString.isEmpty()) {
            String[] queryPairs = queryString.split("&");
            for (int i = 0; i < queryPairs.length; i++) {
                if (i > 0) signatureBuilder.append("&");
                String[] pair = queryPairs[i].split("=", 2);
                signatureBuilder.append(pair[0]).append("=");
                if (pair.length == 2) {
                    signatureBuilder.append(SignatureUtil.urlEncode(pair[1]));
                }
            }
        }
        
        // Add body parameters (URL encoded)
        Map<String, String> bodyMap = parseBodyToMap(bodyContent);
        bodyMap.put("timestamp", timestamp);
        
        boolean isFirstBodyParam = true;
        for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
            if (signatureBuilder.length() > 0 && isFirstBodyParam) {
                // No & separator between query string and first body parameter
                isFirstBodyParam = false;
            } else if (!isFirstBodyParam) {
                signatureBuilder.append("&");
            }
            signatureBuilder.append(entry.getKey()).append("=")
                           .append(SignatureUtil.urlEncode(entry.getValue()));
            if (isFirstBodyParam) {
                isFirstBodyParam = false;
            }
        }
        
        return signatureBuilder.toString();
    }

    private String buildFinalBodyContent(String bodyContent, String timestamp, String signature) {
        Map<String, String> bodyMap = parseBodyToMap(bodyContent);
        bodyMap.put("timestamp", timestamp);
        bodyMap.put("signature", signature);
        
        StringBuilder finalBodyBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
            if (finalBodyBuilder.length() > 0) {
                finalBodyBuilder.append("&");
            }
            finalBodyBuilder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        
        return finalBodyBuilder.toString();
    }

    private Request createUrlSignRequest(Request request) {
        String timestamp = Instant.now().toEpochMilli() + "";
        HttpUrl.Builder urlBuilder = request.url().newBuilder()
                .setQueryParameter("timestamp", timestamp);
        String queryParams = urlBuilder.build().query();
        urlBuilder.setQueryParameter("signature", actualSignature(queryParams, secretKey));
        
        return request.newBuilder()
                .addHeader(HEADER_ACCESS_KEY, accessKey)
                .url(urlBuilder.build())
                .build();
    }

    private String bodyToString(RequestBody body) {
        try {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            return buffer.readString(Charset.defaultCharset());
        } catch (Exception e) {
            log.error("Failed to read request body", e);
            return "";
        }
    }

    private Map<String, String> parseBodyToMap(String body) {
        Map<String, String> result = new LinkedHashMap<>();
        if (StringUtils.isEmpty(body)) {
            return result;
        }

        String[] params = body.contains("&") ? body.split("&") : new String[]{body};
        for (String param : params) {
            if (param.contains("=")) {
                String[] pair = param.split("=", 2);
                result.put(pair[0], pair.length == 2 ? pair[1] : "");
            }
        }
        return result;
    }
}
