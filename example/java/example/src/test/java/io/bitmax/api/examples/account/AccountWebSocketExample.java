package io.bitmax.api.examples.account;

import io.bitmax.api.JavaAuthClient;
import io.bitmax.api.websocket.BitMaxApiWebSocketListener;
import io.bitmax.api.rest.RestBitMax;
import io.bitmax.api.websocket.messages.outcome.Subscribe;

import java.util.Map;

public class AccountWebSocketExample {
    public static void main(String[] args) {
        String apiKey = "<apikey>";
        String secret = "<secret>";
        String baseUrl = "https://bitmax.io";

        RestBitMax restClient = new RestBitMax(apiKey, secret, baseUrl);

        int accountGroup = restClient.getUserInfo().getAccountGroup();

        String url = "wss://bitmax.io/" + accountGroup + "/api/stream/ETH-BTC";

        Subscribe subscribeMessage = new Subscribe();
        subscribeMessage.setMessageType("subscribe");
        subscribeMessage.setMarketDepthLevel(200);
        subscribeMessage.setRecentTradeMaxCount(200);

        try {
            JavaAuthClient authClient = new JavaAuthClient(baseUrl, apiKey, secret);
            Map<String, String> headers = authClient.getHeaderMap("api/stream", System.currentTimeMillis());

            BitMaxApiWebSocketListener listener = new BitMaxApiWebSocketListener(subscribeMessage, headers, url);

            listener.setSummaryCallback(response -> System.out.println("\n" + response));
            listener.setBarCallback(response -> System.out.println("\n" + response));
            listener.setMarketTradesCallback(response -> System.out.println("\n" + response));
            listener.setDepthCallback(response -> System.out.println("\n" + response));

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
