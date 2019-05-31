package io.bitmax.api.examples.common;

import io.bitmax.api.websocket.WebSocketBitmax;

import java.net.URI;

public class PublicWebSocket {

    public static void main(String[] args) throws Exception {

        WebSocketBitmax client = new WebSocketBitmax(new URI("wss://bitmax.io/api/public/ETH-BTC"));
        client.connect();

        // wait 3 seconds server response
        Thread.sleep(3000);

        // subscribe to channels
        client.send("{\n" +
                "        \"messageType\":         \"subscribe\",\n" +
                "        \"marketDepthLevel\":    200,\n" +
                "        \"recentTradeMaxCount\": 200\n" +
                "      }");
    }
}
