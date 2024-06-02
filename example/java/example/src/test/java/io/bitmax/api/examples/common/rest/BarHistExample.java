package io.bitmax.api.examples.common.rest;

import io.bitmax.api.rest.client.BitMaxRestApiClient;
import io.bitmax.api.rest.client.Interval;
import io.bitmax.api.rest.messages.responses.RestBarHist;

import java.util.Arrays;

public class BarHistExample {
    public static void main(String[] args) {

        BitMaxRestApiClient restClient = new BitMaxRestApiClient();
        RestBarHist[] bars = restClient.getCandlestickBars("EOS/ETH", Interval.FIFTEEN_MINUTES, 200);

        String barsStr = Arrays.toString(bars);
        System.out.println(barsStr);
    }
}
