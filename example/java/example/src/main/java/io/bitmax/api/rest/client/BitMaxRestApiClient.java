package io.bitmax.api.rest.client;

import io.bitmax.api.Mapper;
import io.bitmax.api.rest.messages.responses.RestBarHist;
import io.bitmax.api.rest.messages.responses.RestProduct;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * BitMaxRestApiClient public restClient for unauthorized users.
 */
public class BitMaxRestApiClient {
    final String URL = "https://bitmax.io/";
    final String API = "api/v1/";
    final String PATH_BARS = "barhist";
    final String PATH_PRODUCTS = "products";
    final String PATH_INFO = "user/info";
    final String PATH_ORDERS = "order/open";
    final String PATH_ORDER = "order";

    OkHttpClient client;

    public BitMaxRestApiClient() {
        client = new OkHttpClient();
    }

    /**
     * @return history of bars
     * @param symbol trade pair
     * @param interval bars interval
     * @param limit max bars count
     */
    public RestBarHist[] getCandlestickBars(String symbol, Interval interval, int limit) {
        long[] between = getFrom(interval, limit);
        String params = "?symbol=" + symbol + "&interval="+interval+"&from=" + between[0] + "&to=" + between[1];

        try {
            Response result = client.newCall(new Request.Builder()
                    .url(URL + API + PATH_BARS + params)
                    .get()
                    .build()
            ).execute();

            return Mapper.asObject(result.body().string(), RestBarHist[].class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @return Each market summary data record contains current information about every product.
     */
    public RestProduct[] getProducts() {
        try {
            Response result = client.newCall(new Request.Builder()
                    .url(URL + API + PATH_PRODUCTS)
                    .get()
                    .build()
            ).execute();

            return Mapper.asObject(result.body().string(), RestProduct[].class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @return two timestamps, range for bar history
     * @param interval bars interval
     * @param limit max bars count
     */
    private long[] getFrom(Interval interval, int limit) {
        long time = System.currentTimeMillis();

        switch (interval) {
            case ONE_MINUTE:
                return new long[]{time - 60_000L * (limit), time};
            case FIVE_MINUTES:
                return new long[]{time - 300_000L * (limit), time};
            case FIFTEEN_MINUTES:
                return new long[]{time - 900_000L * (limit), time};
            case HALF_HOURLY:
                return new long[]{time - 1_800_000L * (limit), time};
            case HOURLY:
                return new long[]{time - 3_600_000L * (limit), time};
            case TWO_HOURLY:
                return new long[]{time - 3_600_000L * 2 * (limit), time};
            case FOUR_HOURLY:
                return new long[]{time - 3_600_000L * 4 * (limit), time};
            case SIX_HOURLY:
                return new long[]{time - 3_600_000L * 6 * (limit), time};
            case TWELVE_HOURLY:
                return new long[]{time - 3_600_000L * 12 * (limit), time};
            case DAILY:
                return new long[]{time - 3_600_000L * 24 * (limit), time};
            case WEEKLY:
                return new long[]{time - 3_600_000L * 24 * 7 * (limit), time};
            case MONTHLY:
                return new long[]{time - 3_600_000L * 24 * 31 * (limit), time};
            default:
                throw new RuntimeException();
        }
    }
}
