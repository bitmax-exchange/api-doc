package io.bitmax.api.examples.account;

import io.bitmax.api.rest.client.BitMaxRestApiClientAccount;

public class GetAccountOrdersRest {
    public static void main(String[] args) {
        String apiKey = "<apikey>";
        String secret = "<secret>";
        String baseUrl = "https://bitmax.io";

        BitMaxRestApiClientAccount restClient = new BitMaxRestApiClientAccount(apiKey, secret, baseUrl);

        System.out.println(restClient.getOpenOrders());
    }
}
