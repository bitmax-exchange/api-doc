package io.bitmax.api.examples.account;

import io.bitmax.api.Authorization;
import io.bitmax.api.rest.BitMaxRestApiClient;
import io.bitmax.api.websocket.BitMaxApiWebSocketListener;
import io.bitmax.api.websocket.messages.requests.CancelOrder;
import io.bitmax.api.websocket.messages.requests.PlaceOrder;
import io.bitmax.api.websocket.messages.requests.Subscribe;

import java.util.Map;

public class OrderWebSocketExample {
    public static void main(String[] args) {
        String apiKey = "<apikey>";
        String secret = "<secret>";
        String baseUrl = "https://bitmax.io";

        BitMaxRestApiClient restClient = new BitMaxRestApiClient(apiKey, secret, baseUrl);

        int accountGroup = restClient.getUserInfo().getAccountGroup();

        String url = "wss://bitmax.io/" + accountGroup + "/api/stream/EOS-ETH";

        Subscribe subscribeMessage = new Subscribe();
        subscribeMessage.setMessageType("subscribe");
        subscribeMessage.setMarketDepthLevel(200);
        subscribeMessage.setRecentTradeMaxCount(200);

        try {
            Authorization authClient = new Authorization(baseUrl, apiKey, secret);
            Map<String, String> headers = authClient.getHeaderMap("api/stream", System.currentTimeMillis());

            BitMaxApiWebSocketListener listener = new BitMaxApiWebSocketListener(subscribeMessage, headers, url);

            listener.setOrderCallback(response -> System.out.println("\n" + response));

            // waiting before webSocket setUp
            Thread.sleep(1500);

            long time1 = System.currentTimeMillis();

            String placeCoid = "coid_" + time1;

            PlaceOrder placeOrder = new PlaceOrder();
            placeOrder.setMessageType("newOrderRequest");
            placeOrder.setTime(time1);
            placeOrder.setSymbol("EOS/ETH");
            placeOrder.setCoid(placeCoid);
            placeOrder.setOrderPrice("0.02");
            placeOrder.setOrderQty("5.0");
            placeOrder.setOrderType("limit");
            placeOrder.setSide("buy");

            listener.send(placeOrder);

            // waiting before order will be placed
            Thread.sleep(1500);

            long time2 = System.currentTimeMillis();

            String cancelCoid = "coid_" + time2;

            CancelOrder cancelOrder = new CancelOrder();
            cancelOrder.setMessageType("cancelOrderRequest");
            cancelOrder.setTime(time2);
            cancelOrder.setCoid(cancelCoid);
            cancelOrder.setOrigCoid(placeCoid);
            cancelOrder.setSymbol("EOS/ETH");

            listener.send(cancelOrder);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
