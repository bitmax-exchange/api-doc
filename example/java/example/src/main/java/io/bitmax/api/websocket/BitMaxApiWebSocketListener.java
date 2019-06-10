package io.bitmax.api.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bitmax.api.websocket.client.BitMaxApiCallback;
import io.bitmax.api.websocket.messages.income.Bar;
import io.bitmax.api.websocket.messages.income.Depth;
import io.bitmax.api.websocket.messages.income.MarketTrades;
import io.bitmax.api.websocket.messages.income.Summary;
import io.bitmax.api.websocket.messages.outcome.SubscribeMessage;
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

public class BitMaxApiWebSocketListener implements WebSocketListener {
    private final ExecutorService writeExecutor = Executors.newSingleThreadExecutor();

    private final Pattern summaryPattern = Pattern.compile("\\s*\\{\\s*\"m\"\\s*:\\s*\"summary\"\\s*");
    private final Pattern depthPattern = Pattern.compile("\\s*\\{\\s*\"m\"\\s*:\\s*\"depth\"\\s*");
    private final Pattern marketTradesPattern = Pattern.compile("\\s*\\{\\s*\"m\"\\s*:\\s*\"marketTrades\"\\s*");
    private final Pattern barPattern = Pattern.compile("\\s*\\{\\s*\"m\"\\s*:\\s*\"bar\"\\s*");
    private final Pattern pongPattern = Pattern.compile("\\s*\\{\\s*\"m\"\\s*:\\s*\"pong\"\\s*}");

    private BitMaxApiCallback<Summary> summaryCallback;
    private BitMaxApiCallback<Depth> depthCallback;
    private BitMaxApiCallback<MarketTrades> marketTradesCallback;
    private BitMaxApiCallback<Bar> barCallback;

    private SubscribeMessage message;

    private ObjectMapper mapper = new ObjectMapper();

    public BitMaxApiWebSocketListener(SubscribeMessage message, Map<String, String> headersMap, String url) {
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

    public BitMaxApiWebSocketListener(SubscribeMessage message, String url) {
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
        writeExecutor.execute(() -> {
            try {
                webSocket.sendMessage(RequestBody.create(TEXT, mapper.writeValueAsString(message)));
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
                summaryCallback.onResponse(mapper.readValue(text, Summary.class));
                return;
            }
        } else if (depthPattern.matcher(text).find()) {
            if (depthCallback != null) {
                depthCallback.onResponse(mapper.readValue(text, Depth.class));
                return;
            }
        } else if (marketTradesPattern.matcher(text).find()) {
            if (marketTradesCallback != null) {
                marketTradesCallback.onResponse(mapper.readValue(text, MarketTrades.class));
                return;
            }
        } else if (barPattern.matcher(text).find()) {
            if (barCallback != null) {
                barCallback.onResponse(mapper.readValue(text, Bar.class));
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
        writeExecutor.shutdown();
    }

    @Override
    public void onFailure(IOException e, Response response) {
        e.printStackTrace();
        writeExecutor.shutdown();
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
}
