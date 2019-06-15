package io.bitmax.api.examples.common;

import io.bitmax.api.rest.client.BitMaxRestApiClient;
import io.bitmax.api.rest.client.Interval;
import io.bitmax.api.rest.messages.responses.BarHist;

import java.util.Arrays;

public class BarHistRestExample {
    public static void main(String[] args) {

        BitMaxRestApiClient restClient = new BitMaxRestApiClient();
        BarHist[] bars = restClient.getCandlestickBars("EOS/ETH", Interval.FIFTEEN_MINUTES, 200);

        String barsStr = Arrays.toString(bars);
        System.out.println(barsStr);
    }
}
