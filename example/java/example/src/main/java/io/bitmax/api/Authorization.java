package io.bitmax.api;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Authorization {

    private String apiKey;
    private Mac hmac;
    private byte[] hmacKey;
    private SecretKeySpec keySpec;

    /**
     * An Authorization API Key Header for requests.
     */
    public Authorization(String apiKey, String secretKey) {
        this.apiKey = apiKey;

        hmacKey = Base64.getDecoder().decode(secretKey);
        try {
            hmac = Mac.getInstance("HmacSHA256");
            keySpec = new SecretKeySpec(hmacKey, "HmacSHA256");
            hmac.init(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @return authorization headers
     * @param path path for generating specific signature
     * @param timestamp milliseconds since UNIX epoch in UTC
     */
    public Map<String, String> getHeaderMap(String path, long timestamp) {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-auth-key", apiKey);
        headers.put("x-auth-signature", generateSig(path, timestamp));
        headers.put("x-auth-timestamp", String.valueOf(timestamp));
        return headers;
    }

    /**
     * @return authorization headers
     * @param path path for generating specific signature
     * @param timestamp milliseconds since UNIX epoch in UTC
     * @param coid generated unique orderId
     */
    public Map<String, String> getHeaderMap(String path, long timestamp, String coid) {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-auth-key", apiKey);
        headers.put("x-auth-signature", generateSig(path, timestamp, coid));
        headers.put("x-auth-timestamp", String.valueOf(timestamp));
        headers.put("x-auth-coid", coid);
        return headers;
    }

    /**
     * @return signature, signed using sha256 using the base64-decoded secret key
     */
    private String generateSig(String url, long timestamp) {
        String preHash = timestamp + "+" + url;
        return new String(Base64.getEncoder().encode(hmac.doFinal(preHash.getBytes(StandardCharsets.UTF_8))));
    }

    /**
     * @return signature, signed using sha256 using the base64-decoded secret key
     */
    private String generateSig(String url, long timestamp, String coid) {
        String preHash = timestamp + "+" + url + "+" + coid;
        return new String(Base64.getEncoder().encode(hmac.doFinal(preHash.getBytes(StandardCharsets.UTF_8))));
    }
}