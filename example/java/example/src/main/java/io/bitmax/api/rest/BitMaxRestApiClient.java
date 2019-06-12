package io.bitmax.api.rest;

import io.bitmax.api.Authorization;
import io.bitmax.api.Mapper;
import io.bitmax.api.rest.messages.responses.UserInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Map;

public class BitMaxRestApiClient {

    private Authorization authClient;
    private OkHttpClient client;

    public BitMaxRestApiClient(String apiKey, String secret, String baseUrl) {
        authClient = new Authorization(baseUrl, apiKey, secret);
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
