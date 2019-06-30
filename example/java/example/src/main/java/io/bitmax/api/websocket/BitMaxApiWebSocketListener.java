package io.bitmax.api.websocket;

import io.bitmax.api.Mapper;
import io.bitmax.api.websocket.client.BitMaxApiCallback;
import io.bitmax.api.websocket.messages.responses.*;
import io.bitmax.api.websocket.messages.requests.Subscribe;
import okhttp3.*;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static okhttp3.ws.WebSocket.TEXT;

/**
 * Represents a Listener of webSocket channels
 */
public class BitMaxApiWebSocketListener implements WebSocketListener {

    /**
     * WebSocket executor service, execute webSocket tasks such as open/close channel and send message
     */
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * WebSocket executor service, execute webSocket tasks such as open/close channel and send message
     */
    private WebSocket webSocket;

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
    private BitMaxApiCallback<Summary> summaryCallback;
    private BitMaxApiCallback<Depth> depthCallback;
    private BitMaxApiCallback<MarketTrades> marketTradesCallback;
    private BitMaxApiCallback<Bar> barCallback;
    private BitMaxApiCallback<Order> orderCallback;

    /**
     * message for subscribe to channels
     */
    private Subscribe message;

    /**
     * Initialize listener for authorized user
     */
    public BitMaxApiWebSocketListener(Subscribe message, Map<String, String> headersMap, String url) {
        this.message = message;

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headersMap))
                .build();

        WebSocketCall.create(client, request).enqueue(this);
    }

    /**
     * Initialize listener for common messages
     */
    public BitMaxApiWebSocketListener(Subscribe message, String url) {
        this.message = message;

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        WebSocketCall.create(client, request).enqueue(this);
    }


    @Override
    public void onOpen(final WebSocket webSocket, Response response) {
        executor.execute(() -> {
            try {
                this.webSocket = webSocket;
                webSocket.sendMessage(RequestBody.create(TEXT, Mapper.asString(message)));
            } catch (IOException e) {
                System.err.println("Unable to send messages: " + e.getMessage());
            }
        });
    }


    public void send(Object message) {
        executor.execute(() -> {
            try {
                webSocket.sendMessage(RequestBody.create(TEXT, Mapper.asString(message)));
            } catch (IOException e) {
                System.err.println("Unable to send messages: " + e.getMessage());
            }
        });
    }


    @Override
    public void onMessage(ResponseBody message) throws IOException {
        String text;
        if (message.contentType() == TEXT) {
            text = message.string();
        } else {
            text = message.source().readByteString().hex();
        }
        message.close();

        if (pongPattern.matcher(text).find()) return;
        else if (summaryPattern.matcher(text).find()) {
            if (summaryCallback != null) {
                summaryCallback.onResponse(Mapper.asObject(text, Summary.class));
                return;
            }
        } else if (depthPattern.matcher(text).find()) {
            if (depthCallback != null) {
                depthCallback.onResponse(Mapper.asObject(text, Depth.class));
                return;
            }
        } else if (marketTradesPattern.matcher(text).find()) {
            if (marketTradesCallback != null) {
                marketTradesCallback.onResponse(Mapper.asObject(text, MarketTrades.class));
                return;
            }
        } else if (barPattern.matcher(text).find()) {
            if (barCallback != null) {
                barCallback.onResponse(Mapper.asObject(text, Bar.class));
                return;
            }
        } else if (orderPattern.matcher(text).find()) {
            if (orderCallback != null) {
                orderCallback.onResponse(Mapper.asObject(text, Order.class));
                return;
            }
        }
        System.out.println("Warn! Not found callback for message: " + text);
    }

    @Override
    public void onPong(Buffer payload) {
    }

    @Override
    public void onClose(int code, String reason) {
        System.out.println("CLOSE: " + code + " " + reason);
        executor.shutdown();
    }

    @Override
    public void onFailure(IOException e, Response response) {
        e.printStackTrace();
        executor.shutdown();
    }

    public void setSummaryCallback(BitMaxApiCallback<Summary> summaryCallback) {
        this.summaryCallback = summaryCallback;
    }

    public void setDepthCallback(BitMaxApiCallback<Depth> depthCallback) {
        this.depthCallback = depthCallback;
    }

    public void setMarketTradesCallback(BitMaxApiCallback<MarketTrades> marketTradesCallback) {
        this.marketTradesCallback = marketTradesCallback;
    }

    public void setBarCallback(BitMaxApiCallback<Bar> barCallback) {
        this.barCallback = barCallback;
    }

    public void setOrderCallback(BitMaxApiCallback<Order> orderCallback) {
        this.orderCallback = orderCallback;
    }
}
