package io.bitmax.api.websocket;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.*;

import com.squareup.okhttp.OkHttpClient;
import io.bitmax.api.JavaAuthClient;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketBitmax extends WebSocketClient {

    private JavaAuthClient authClient;
    private OkHttpClient client;

    public WebSocketBitmax(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public WebSocketBitmax(URI serverURI) {
        super(serverURI);
    }

    public WebSocketBitmax(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("opened connection");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("received: " + message);
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        System.out.println("received: " + bytes);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
