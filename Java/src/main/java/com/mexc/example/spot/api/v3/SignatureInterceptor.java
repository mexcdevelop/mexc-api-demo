package com.mexc.example.spot.api.v3;


import com.mexc.example.common.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;


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
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request origRequest = chain.request();
        String method = origRequest.method();
        Request newRequest;
        if ("GET".equals(method)) {
            newRequest = createUrlSignRequest(origRequest);
        } else if ("POST".equals(method) || "DELETE".equals(method)) {
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
        String timestamp = Instant.now().toEpochMilli() + "";
        String params = bodyToString(origBody);
        params += "&timestamp=" + timestamp;
        String signature = SignatureUtil.actualSignature(params, secretKey);
        params += "&signature=" + signature;
        if ("POST".equals(method)) {
            return origRequest.newBuilder()
                    .addHeader(HEADER_ACCESS_KEY, accessKey)
                    .post(RequestBody.create(params, MediaType.get("text/plain"))).build();
        } else {
            return origRequest.newBuilder()
                    .addHeader(HEADER_ACCESS_KEY, accessKey)
                    .delete(RequestBody.create(params, MediaType.get("text/plain"))).build();
        }
    }

    private Request createUrlSignRequest(Request request) {
        String timestamp = Instant.now().toEpochMilli() + "";
        HttpUrl url = request.url();
        HttpUrl.Builder urlBuilder = url
                .newBuilder()
                .setQueryParameter("timestamp", timestamp);
        String queryParams = urlBuilder.build().query();
        urlBuilder.setQueryParameter("signature", SignatureUtil.actualSignature(queryParams, secretKey));
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
}


