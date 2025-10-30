package com.mexc.example.common;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.stream.Collectors;

public class SignatureUtil {

    public static String actualSignature(String inputStr, String key) {
        Mac hmacSha256 = null;
        try {
            hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secKey =
                    new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSha256.init(secKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No such algorithm: " + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key: " + e.getMessage());
        }
        byte[] hash = hmacSha256.doFinal(inputStr.getBytes(StandardCharsets.UTF_8));
        return byte2hex(hash);
    }

    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String temp;
        for (int n = 0; b != null && n < b.length; n++) {
            temp = Integer.toHexString(b[n] & 0XFF);
            if (temp.length() == 1) {
                hs.append('0');
            }
            hs.append(temp);
        }
        return hs.toString();
    }

    /**
     * Standard URL encoding. Note the difference from JDK default: spaces are encoded as %20 instead of +.
     *
     * @param s String to encode
     * @return URL encoded string
     */
    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("UTF-8 encoding not supported!");
        }
    }

    public static String toQueryString(Map<String, String> params) {
        return params.entrySet().stream().map((entry) -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("&"));
    }

    public static String toQueryStringWithEncoding(Map<String, String> params) {
        return params.entrySet().stream().map((entry) -> entry.getKey() + "=" + urlEncode(entry.getValue())).collect(Collectors.joining("&"));
    }
}
