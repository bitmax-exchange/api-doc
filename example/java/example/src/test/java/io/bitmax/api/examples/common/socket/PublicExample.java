package io.bitmax.api.examples.common.socket;

import io.bitmax.api.websocket.BitMaxApiWebSocketListener;
import io.bitmax.api.websocket.messages.requests.WebSocketSubscribe;

public class PublicExample {

    public static void main(String[] args) {
        String url = "wss://bitmax.io/api/public/EOS-ETH";

        WebSocketSubscribe subscribeMessage = new WebSocketSubscribe();
        subscribeMessage.setMessageType("subscribe");
        subscribeMessage.setMarketDepthLevel(200);

        try {
            BitMaxApiWebSocketListener listener = new BitMaxApiWebSocketListener(subscribeMessage, url);

            listener.setSummaryCallback(response -> System.out.println("\n" + response));
            listener.setBarCallback(response -> System.out.println("\n" + response));
            listener.setMarketTradesCallback(response -> System.out.println("\n" + response));
            listener.setDepthCallback(response -> System.out.println("\n" + response));

            Thread.sleep(10000);

            listener.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
