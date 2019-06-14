package io.bitmax.api.rest.client;

import io.bitmax.api.Mapper;
import io.bitmax.api.rest.messages.responses.BarHist;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public abstract class BitMaxRestApiClient {
    final String URL = "https://bitmax.io/api/v1/";
    final String PATH_BARS = "barhist";
    final String PATH_INFO = "user/info";

    OkHttpClient client;

    public BarHist[] getCandlestickBars(String symbol, Interval interval, int limit) {
        long[] between = getFrom(interval, limit);
        String params = "?symbol=" + symbol + "&interval="+interval+"&from=" + between[0] + "&to=" + between[1];

        try {
            Response result = client.newCall(new Request.Builder()
                    .url(URL + PATH_BARS + params)
                    .get()
                    .build()
            ).execute();

            return Mapper.asObject(result.body().string(), BarHist[].class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

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