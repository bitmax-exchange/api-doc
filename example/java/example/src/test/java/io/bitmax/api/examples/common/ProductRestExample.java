package io.bitmax.api.examples.common;

import io.bitmax.api.rest.client.BitMaxRestApiClientPublic;
import io.bitmax.api.rest.client.Interval;
import io.bitmax.api.rest.messages.responses.BarHist;
import io.bitmax.api.rest.messages.responses.Product;

import java.util.Arrays;

public class ProductRestExample {
    public static void main(String[] args) {

        BitMaxRestApiClientPublic restClient = new BitMaxRestApiClientPublic();
        Product[] products = restClient.getProducts();

        String barsStr = Arrays.toString(products);
        System.out.println(barsStr);
    }
}
