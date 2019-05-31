package io.bitmax.api.rest;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.bitmax.api.JavaAuthClient;
import io.bitmax.api.Mapper;
import io.bitmax.api.rest.response.templates.UserInfo;
import java.util.Map;

public class RestBitmax {

    private JavaAuthClient authClient;
    private OkHttpClient client;

    public RestBitmax(String apiKey, String secret, String baseUrl) {
        authClient = new JavaAuthClient(baseUrl, apiKey, secret);
        client = new OkHttpClient();
    }

    public UserInfo getUserInfo() {
        Map<String, String> headers = authClient.getHeaderMap("user/info", System.currentTimeMillis());

        Request.Builder builder = new Request.Builder()
                .url("https://bitmax.io/api/v1/user/info")
                .get();

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }

        try {
            Response response = client.newCall(builder.build()).execute();

            return Mapper.readObjectFromString(response.body().string(), UserInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
