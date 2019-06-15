package io.bitmax.api.examples.account.rest;

import io.bitmax.api.rest.client.BitMaxRestApiClientAccount;

public class GetOneOrderExample {
    public static void main(String[] args) {
        String apiKey = "<apikey>";
        String secret = "<secret>";
        String baseUrl = "https://bitmax.io";

        BitMaxRestApiClientAccount restClient = new BitMaxRestApiClientAccount(apiKey, secret, baseUrl);

        System.out.println(restClient.getOrder("IRFJ1WgqpHLRrWa8iHIGj7tFRLtK69KL"));
    }
}
