package io.bitmax.api.examples.account.rest;

import io.bitmax.api.rest.client.BitMaxRestApiClientAccount;
import io.bitmax.api.rest.messages.requests.RestPlaceOrderRequest;

public class PlaceOrderExample {
    public static void main(String[] args) {
        String apiKey = "<apikey>";
        String secret = "<secret>";

        BitMaxRestApiClientAccount restClient = new BitMaxRestApiClientAccount(apiKey, secret);

        long time = System.currentTimeMillis();

        String coid = "coid_" + time;

        RestPlaceOrderRequest restPlaceOrderRequest = new RestPlaceOrderRequest();
        restPlaceOrderRequest.setTime(time);
        restPlaceOrderRequest.setSymbol("EOS/ETH");
        restPlaceOrderRequest.setCoid(coid);
        restPlaceOrderRequest.setOrderPrice("0.01");
        restPlaceOrderRequest.setOrderQty("4.0");
        restPlaceOrderRequest.setOrderType("limit");
        restPlaceOrderRequest.setSide("buy");

        System.out.println(restClient.placeOrder(restPlaceOrderRequest));
    }
}
