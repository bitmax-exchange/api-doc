package io.bitmax.api.examples.common;

import io.bitmax.api.rest.client.BitMaxRestApiClient;
import io.bitmax.api.rest.messages.responses.Product;

import java.util.Arrays;

public class ProductRestExample {
    public static void main(String[] args) {

        BitMaxRestApiClient restClient = new BitMaxRestApiClient();
        Product[] products = restClient.getProducts();

        String barsStr = Arrays.toString(products);
        System.out.println(barsStr);
    }
}
