package io.bitmax.api.rest.client;

import io.bitmax.api.Authorization;
import io.bitmax.api.Mapper;
import io.bitmax.api.rest.messages.responses.RestOpenOrdersList;
import io.bitmax.api.rest.messages.responses.RestOrderDetails;
import io.bitmax.api.rest.messages.responses.RestUserInfo;
import io.bitmax.api.rest.messages.requests.RestPlaceOrderRequest;
import okhttp3.*;

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
     * @exception RuntimeException throws if something wrong (e.g. not correct response)
     */
    public RestUserInfo getUserInfo() {
        Map<String, String> headers = authClient.getHeaderMap(PATH_INFO, System.currentTimeMillis());

        Request.Builder builder = new Request.Builder()
                .url(URL + API + PATH_INFO)
                .get();

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }

        try {
            Response response = client.newCall(builder.build()).execute();

            return Mapper.asObject(response.body().string(), RestUserInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @return list of open orders
     * @exception RuntimeException throws if something wrong (e.g. not correct response)
     */
    public RestOpenOrdersList getOpenOrders() {
        Map<String, String> headers = authClient.getHeaderMap(PATH_ORDERS, System.currentTimeMillis());

        Request.Builder builder = new Request.Builder()
                .url(URL + accountGroup + '/' + API + PATH_ORDERS)
                .get();

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }

        try {
            Response response = client.newCall(builder.build()).execute();

            return Mapper.asObject(response.body().string(), RestOpenOrdersList.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @return detailed info about specific order
     * @exception RuntimeException throws if something wrong (e.g. not correct response)
     * @param coid id of expected order
     */
    public RestOrderDetails getOrder(String coid) {
        Map<String, String> headers = authClient.getHeaderMap(PATH_ORDER, System.currentTimeMillis());

        Request.Builder builder = new Request.Builder()
                .url(URL + accountGroup + '/' + API + PATH_ORDER + '/' + coid)
                .get();

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }

        try {
            Response response = client.newCall(builder.build()).execute();

            return Mapper.asObject(response.body().string(), RestOrderDetails.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @return detailed info about order
     * @exception RuntimeException throws if something wrong (order was not published)
     * @param order is object which contains information about order
     */
    public String placeOrder(RestPlaceOrderRequest order) {
        long timestamp = System.currentTimeMillis();
        Map<String, String> headers = authClient.getHeaderMap(PATH_ORDER, timestamp, order.getCoid());

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Mapper.asString(order));

        Request.Builder builder = new Request.Builder()
                .url(URL + accountGroup + '/' + API + PATH_ORDER)
                .post(body);

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }

        try {
            Response response = client.newCall(builder.build()).execute();

            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
