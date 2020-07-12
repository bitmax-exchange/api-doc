package io.bitmax.api.websocket;

import com.neovisionaries.ws.client.*;
import io.bitmax.api.Mapper;
import io.bitmax.api.websocket.client.BitMaxApiCallback;
import io.bitmax.api.websocket.messages.responses.*;
import io.bitmax.api.websocket.messages.requests.WebSocketSubscribe;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Represents a Listener of webSocket channels
 */
public class BitMaxApiWebSocketListener {

    /**
     * WebSocket executor service, execute webSocket tasks such as open/close channel and send message
     */
    private WebSocket webSocket;

    /**
     * The timeout value in milliseconds for socket connection.
     */
    private static final int TIMEOUT = 5000;

    /**
     * patterns to determine type of message
     */
    private final Pattern summaryPattern = Pattern.compile("\\s*\\{\\s*\"m\"\\s*:\\s*\"summary\"\\s*");
    private final Pattern depthPattern = Pattern.compile("\\s*\\{\\s*\"m\"\\s*:\\s*\"depth\"\\s*");
    private final Pattern marketTradesPattern = Pattern.compile("\\s*\\{\\s*\"m\"\\s*:\\s*\"marketTrades\"\\s*");
    private final Pattern barPattern = Pattern.compile("\\s*\\{\\s*\"m\"\\s*:\\s*\"bar\"\\s*");
    private final Pattern pongPattern = Pattern.compile("\\s*\\{\\s*\"m\"\\s*:\\s*\"pong\"\\s*}");
    private final Pattern orderPattern = Pattern.compile("\\s*\\{\\s*\"m\"\\s*:\\s*\"order\"\\s*");

    /**
     * callBacks or every message type
     */
    private BitMaxApiCallback<WebSocketSummary> summaryCallback;
    private BitMaxApiCallback<WebSocketDepth> depthCallback;
    private BitMaxApiCallback<WebSocketMarketTrades> marketTradesCallback;
    private BitMaxApiCallback<WebSocketBar> barCallback;
    private BitMaxApiCallback<WebSocketOrder> orderCallback;

    /**
     * Initialize listener for authorized user
     */
    public BitMaxApiWebSocketListener(WebSocketSubscribe message, Map<String, String> headersMap, String url, int timeout) {

        try {
            webSocket = new WebSocketFactory()
                    .setConnectionTimeout(timeout)
                    .createSocket(url)
                    .addListener(new WebSocketAdapter() {
                        public void onTextMessage(WebSocket websocket, String message) {
                            onMessage(message);
                        }

                        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame,
                                                   WebSocketFrame clientCloseFrame, boolean closedByServer) {
                            System.out.println("Socket Disconnected!");
                        }
                    })
                    .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE);

            headersMap.forEach((key, val) -> webSocket.addHeader(key, val));

            webSocket.connect();

            if (message != null) send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize listener for common messages
     */
    public BitMaxApiWebSocketListener(WebSocketSubscribe message, String url) {

        try {
            webSocket = new WebSocketFactory()
                    .setConnectionTimeout(TIMEOUT)
                    .createSocket(url)
                    .addListener(new WebSocketAdapter() {
                        public void onTextMessage(WebSocket websocket, String message) {
                            onMessage(message);
                        }

                        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame,
                                                   WebSocketFrame clientCloseFrame, boolean closedByServer) {
                            System.out.println("Socket Disconnected!");
                        }
                    })
                    .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE);

            webSocket.connect();

            if (message != null) send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(Object message) {
        webSocket.sendText(Mapper.asString(message));
    }

    private void onMessage(String message) {

        if (summaryPattern.matcher(message).find()) {
            if (summaryCallback != null) {
                summaryCallback.onResponse(Mapper.asObject(message, WebSocketSummary.class));
            }
        } else if (depthPattern.matcher(message).find()) {
            if (depthCallback != null) {
                depthCallback.onResponse(Mapper.asObject(message, WebSocketDepth.class));
            }
        } else if (marketTradesPattern.matcher(message).find()) {
            if (marketTradesCallback != null) {
                marketTradesCallback.onResponse(Mapper.asObject(message, WebSocketMarketTrades.class));
            }
        } else if (barPattern.matcher(message).find()) {
            if (barCallback != null) {
                barCallback.onResponse(Mapper.asObject(message, WebSocketBar.class));
            }
        } else if (orderPattern.matcher(message).find()) {
            if (orderCallback != null) {
                orderCallback.onResponse(Mapper.asObject(message, WebSocketOrder.class));
            }
        }
    }

    public void close() {
        webSocket.disconnect();
    }

    public void setSummaryCallback(BitMaxApiCallback<WebSocketSummary> summaryCallback) {
        this.summaryCallback = summaryCallback;
    }

    public void setDepthCallback(BitMaxApiCallback<WebSocketDepth> depthCallback) {
        this.depthCallback = depthCallback;
    }

    public void setMarketTradesCallback(BitMaxApiCallback<WebSocketMarketTrades> marketTradesCallback) {
        this.marketTradesCallback = marketTradesCallback;
    }

    public void setBarCallback(BitMaxApiCallback<WebSocketBar> barCallback) {
        this.barCallback = barCallback;
    }

    public void setOrderCallback(BitMaxApiCallback<WebSocketOrder> orderCallback) {
        this.orderCallback = orderCallback;
    }
}
