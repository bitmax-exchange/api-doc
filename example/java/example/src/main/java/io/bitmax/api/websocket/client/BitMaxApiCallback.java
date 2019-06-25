package io.bitmax.api.websocket.client;

/**
 * @param <T> the return type from the callback
 */
@FunctionalInterface
public interface BitMaxApiCallback<T> {

    void onResponse(T response);

    default void onFailure(Throwable cause) {}
}