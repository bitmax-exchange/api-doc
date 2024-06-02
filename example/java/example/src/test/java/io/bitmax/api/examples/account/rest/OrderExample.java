package io.bitmax.api.examples.account.rest;

import io.bitmax.api.rest.client.BitMaxRestApiClientAccount;
import io.bitmax.api.rest.messages.requests.RestCancelOrderRequest;
import io.bitmax.api.rest.messages.requests.RestPlaceOrderRequest;
import okhttp3.Response;

import java.io.IOException;

public class OrderExample {
    public static void main(String[] args) throws IOException, InterruptedException {
        String apiKey = "<apikey>";
        String secret = "<secret>";

        BitMaxRestApiClientAccount restClient = new BitMaxRestApiClientAccount(apiKey, secret);

        long time = System.currentTimeMillis();

        String placeCoid = "coid_" + time;

        RestPlaceOrderRequest restPlaceOrderRequest = new RestPlaceOrderRequest();
        restPlaceOrderRequest.setTime(time);
        restPlaceOrderRequest.setSymbol("EOS/ETH");
        restPlaceOrderRequest.setCoid(placeCoid);
        restPlaceOrderRequest.setOrderPrice("0.01");
        restPlaceOrderRequest.setOrderQty("4.0");
        restPlaceOrderRequest.setOrderType("limit");
        restPlaceOrderRequest.setSide("buy");

        System.out.println(restPlaceOrderRequest);

        Response placeOrderResponse = restClient.placeOrder(restPlaceOrderRequest);

        System.out.println(placeOrderResponse.body().string());


        // waiting before order will be placed
        Thread.sleep(1500);

        long time2 = System.currentTimeMillis();

        String cancelCoid = "coid_" + time2;

        RestCancelOrderRequest restCancelOrderRequest = new RestCancelOrderRequest();
        restCancelOrderRequest.setTime(time);
        restCancelOrderRequest.setSymbol("EOS/ETH");
        restCancelOrderRequest.setCoid(cancelCoid);
        restCancelOrderRequest.setOrigCoid(placeCoid);

        System.out.println(restCancelOrderRequest);

        Response cancelOrderResponse = restClient.cancelOrder(restCancelOrderRequest);

        System.out.println(cancelOrderResponse.body().string());
    }
}
