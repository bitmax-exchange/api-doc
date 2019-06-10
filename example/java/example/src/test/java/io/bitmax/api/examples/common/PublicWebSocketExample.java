package io.bitmax.api.examples.common;

import io.bitmax.api.websocket.BitMaxApiWebSocketListener;
import io.bitmax.api.websocket.messages.outcome.SubscribeMessage;

public class PublicWebSocketExample {

    public static void main(String[] args) {
        String url = "wss://bitmax.io/api/public/EOS-ETH";

        SubscribeMessage subscribeMessage = new SubscribeMessage();
        subscribeMessage.setMessageType("subscribe");
        subscribeMessage.setMarketDepthLevel(200);
        subscribeMessage.setRecentTradeMaxCount(200);

        try {
            BitMaxApiWebSocketListener listener = new BitMaxApiWebSocketListener(subscribeMessage, url);

            listener.setSummaryCallback(response -> System.out.println("\n" + response));
            listener.setBarCallback(response -> System.out.println("\n" + response));
            listener.setMarketTradesCallback(response -> System.out.println("\n" + response));
            listener.setDepthCallback(response -> System.out.println("\n" + response));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
