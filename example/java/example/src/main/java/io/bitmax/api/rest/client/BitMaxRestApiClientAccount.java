package io.bitmax.api.rest.client;

import io.bitmax.api.Authorization;
import io.bitmax.api.Mapper;
import io.bitmax.api.rest.messages.responses.OpenOrdersList;
import io.bitmax.api.rest.messages.responses.OrderDetails;
import io.bitmax.api.rest.messages.responses.UserInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Map;

/**
 * BitMaxRestApiClientAccount private restClient for authorized users.
 */
public class BitMaxRestApiClientAccount extends BitMaxRestApiClient {

    private Authorization authClient;
    private int accountGroup;

    /**
     * @param apiKey public API user-key
     * @param secret secret API user-key
     */
    public BitMaxRestApiClientAccount(String apiKey, String secret) {
        authClient = new Authorization(apiKey, secret);
        client = new OkHttpClient();
        accountGroup = getUserInfo().getAccountGroup();
    }

    /**
     * @return 'UserInfo' object that contains 'userGroup' field
     */
    public UserInfo getUserInfo() {
        Map<String, String> headers = authClient.getHeaderMap(PATH_INFO, System.currentTimeMillis());

        Request.Builder builder = new Request.Builder()
                .url(URL + API + PATH_INFO)
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

    /**
     * @return list of open orders
     */
    public OpenOrdersList getOpenOrders() {
        Map<String, String> headers = authClient.getHeaderMap(PATH_ORDERS, System.currentTimeMillis());

        Request.Builder builder = new Request.Builder()
                .url(URL + accountGroup + '/' + API + PATH_ORDERS)
                .get();

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }

        try {
            Response response = client.newCall(builder.build()).execute();

            return Mapper.asObject(response.body().string(), OpenOrdersList.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @return detailed info about specific order
     * @param coid id of expected order
     */
    public OrderDetails getOrder(String coid) {
        Map<String, String> headers = authClient.getHeaderMap(PATH_ORDER, System.currentTimeMillis());

        Request.Builder builder = new Request.Builder()
                .url(URL + accountGroup + '/' + API + PATH_ORDER + '/' + coid)
                .get();

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }

        try {
            Response response = client.newCall(builder.build()).execute();

            return Mapper.asObject(response.body().string(), OrderDetails.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
