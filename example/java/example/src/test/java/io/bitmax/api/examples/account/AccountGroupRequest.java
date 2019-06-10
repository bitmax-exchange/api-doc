package io.bitmax.api.examples.account;

import io.bitmax.api.rest.RestBitMax;

public class AccountGroupRequest {
    public static void main(String[] args) {
        String apiKey = "<apikey>";
        String secret = "<secret>";
        String baseUrl = "https://bitmax.io";

        RestBitMax restClient = new RestBitMax(apiKey, secret, baseUrl);

        System.out.println(restClient.getUserInfo());
    }
}
