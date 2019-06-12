package io.bitmax.api.rest.client;

import okhttp3.OkHttpClient;

public class BitMaxRestApiClientPublic extends BitMaxRestApiClient {
    public BitMaxRestApiClientPublic() {
        client = new OkHttpClient();
    }
}