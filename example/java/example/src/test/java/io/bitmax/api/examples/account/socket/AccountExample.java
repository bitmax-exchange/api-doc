package io.bitmax.api.examples.account.socket;

import io.bitmax.api.Authorization;
import io.bitmax.api.websocket.BitMaxApiWebSocketListener;
import io.bitmax.api.rest.client.BitMaxRestApiClientAccount;
import io.bitmax.api.websocket.messages.requests.WebSocketSubscribe;

import java.util.Map;

public class AccountExample {
    public static void main(String[] args) {
        String apiKey = "<apikey>";
        String secret = "<secret>";

        BitMaxRestApiClientAccount restClient = new BitMaxRestApiClientAccount(apiKey, secret);

        int accountGroup = restClient.getUserInfo().getAccountGroup();

        String url = "wss://bitmax.io/" + accountGroup + "/api/stream/ETH-BTC";

        WebSocketSubscribe subscribeMessage = new WebSocketSubscribe();
        subscribeMessage.setMessageType("subscribe");
        subscribeMessage.setMarketDepthLevel(200);

        try {
            Authorization authClient = new Authorization(apiKey, secret);
            Map<String, String> headers = authClient.getHeaderMap("api/stream", System.currentTimeMillis());

            BitMaxApiWebSocketListener listener = new BitMaxApiWebSocketListener(subscribeMessage, headers, url, 5000);

            listener.setSummaryCallback(response -> System.out.println("\n" + response));
            listener.setDepthCallback(response -> System.out.println("\n" + response));
            listener.setBarCallback(response -> System.out.println("\n" + response));
            listener.setMarketTradesCallback(response -> System.out.println("\n" + response));
            listener.setOrderCallback(response -> System.out.println("\n" + response));

            Thread.sleep(10000);

            listener.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
