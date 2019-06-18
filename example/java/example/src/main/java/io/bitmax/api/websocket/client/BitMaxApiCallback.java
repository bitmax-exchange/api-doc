package io.bitmax.api.websocket.client;

/**
 * BitMaxApiCallback is a functional interface used for webSocket client.
 *
 * @param <T> the return type from the callback
 */
@FunctionalInterface
public interface BitMaxApiCallback<T> {

    /**
     * Called whenever a response comes back from the BitMax API.
     *
     * @param response the expected response object
     */
    void onResponse(T response);

    /**
     * Called whenever an error occurs.
     *
     * @param cause the cause of the failure
     */
    default void onFailure(Throwable cause) {}
}