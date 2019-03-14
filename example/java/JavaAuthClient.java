package sample.api;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JavaAuthClient {

    private String baseUrl;
    private String apiKey;
    private String secretKey;
    private Mac hmac;
    private byte[] hmacKey;
    private SecretKeySpec keySpec;

    public static void main(String[] args) {
        String apiKey = "<apikey>";
        String secret = "<secret>";
        String baseUrl = "https://bitmax.io";

        try {
            JavaAuthClient client = new JavaAuthClient(baseUrl, apiKey, secret);
            client.makeHeader();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    public JavaAuthClient(String baseUrl, String apiKey, String secretKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.secretKey = secretKey;

        hmacKey = Base64.getDecoder().decode(secretKey);
        try {
            hmac = Mac.getInstance("HmacSHA256");
            keySpec = new SecretKeySpec(hmacKey, "HmacSHA256");
            hmac.init(keySpec);
        } catch (Exception e) {
            //
        }
    }

    public void makeHeader() throws Exception{
        String path = "user/info";
        Map<String, String> headersMap = getHeaderMap(path, System.currentTimeMillis());
        for (Map.Entry<String,String> entry : headersMap.entrySet()) {
            System.out.println(entry.getKey() + "->" + entry.getValue());
        }
    }


    private Map<String, String> getHeaderMap(String url, long timestamp) throws Exception{
        Map<String, String> headers = new HashMap<>();
        headers.put("x-auth-key", apiKey);
        headers.put("x-auth-signature", generateSig(url, timestamp));
        headers.put("x-auth-timestamp", timestamp + "");
        return headers;
    }

    private String generateSig(String url, long timestamp) throws UnsupportedEncodingException {
        String prehash = timestamp + "+" + url;
        byte[] encoded = Base64.getEncoder().encode(hmac.doFinal(prehash.getBytes("UTF-8")));
        return new String(encoded);
    }
}
