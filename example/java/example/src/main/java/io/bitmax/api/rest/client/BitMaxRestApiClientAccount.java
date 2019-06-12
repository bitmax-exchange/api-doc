package io.bitmax.api.rest.client;

import io.bitmax.api.Authorization;
import io.bitmax.api.Mapper;
import io.bitmax.api.rest.messages.responses.UserInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Map;

public class BitMaxRestApiClientAccount extends BitMaxRestApiClient {

    private Authorization authClient;

    public BitMaxRestApiClientAccount(String apiKey, String secret, String baseUrl) {
        authClient = new Authorization(baseUrl, apiKey, secret);
        client = new OkHttpClient();
    }

    public UserInfo getUserInfo() {
        Map<String, String> headers = authClient.getHeaderMap(PATH_INFO, System.currentTimeMillis());

        Request.Builder builder = new Request.Builder()
                .url(URL + PATH_INFO)
                .get();

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }

        try {
            Response response = client.newCall(builder.build()).execute();

            return Mapper.asObject(response.body().string(), UserInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
