package io.bitmax.api.examples.common.rest;

import io.bitmax.api.rest.client.BitMaxRestApiClient;
import io.bitmax.api.rest.messages.responses.RestProduct;

import java.util.Arrays;

public class ProductExample {
    public static void main(String[] args) {

        BitMaxRestApiClient restClient = new BitMaxRestApiClient();
        RestProduct[] products = restClient.getProducts();

        String barsStr = Arrays.toString(products);
        System.out.println(barsStr);
    }
}
