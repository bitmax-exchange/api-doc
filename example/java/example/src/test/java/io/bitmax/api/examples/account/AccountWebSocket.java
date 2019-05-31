package io.bitmax.api.examples.account;

import io.bitmax.api.JavaAuthClient;
import io.bitmax.api.rest.RestBitmax;
import io.bitmax.api.websocket.WebSocketBitmax;

import java.net.URI;
import java.util.Map;

public class AccountWebSocket {
    public static void main(String[] args) {
        String apiKey = "<apikey>";
        String secret = "<secret>";
        String baseUrl = "https://bitmax.io";

        RestBitmax restClient = new RestBitmax(apiKey, secret, baseUrl);

        int accountGroup = restClient.getUserInfo().getAccountGroup();

        try {
            JavaAuthClient authClient = new JavaAuthClient(baseUrl, apiKey, secret);
            Map<String, String> headers = authClient.getHeaderMap("api/stream", System.currentTimeMillis());

            String url = "wss://bitmax.io/" + accountGroup + "/api/stream/ETH-BTC";

            WebSocketBitmax client = new WebSocketBitmax(new URI(url), headers);
            client.connect();

            // wait 3 seconds server response
            Thread.sleep(3000);

            // subscribe to channels
            client.send("{\n" +
                    "        \"messageType\":         \"subscribe\",\n" +
                    "        \"marketDepthLevel\":    200,\n" +
                    "        \"recentTradeMaxCount\": 200\n" +
                    "      }");

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
