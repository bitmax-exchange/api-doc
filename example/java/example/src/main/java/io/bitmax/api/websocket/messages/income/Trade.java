package io.bitmax.api.websocket.messages.income;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Trade {

    @JsonProperty("p")
    private String price;

    @JsonProperty("q")
    private String quantity;

    @JsonProperty("t")
    private long timestamp;

    @JsonProperty("bm")
    private boolean marketMaker;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isMarketMaker() {
        return marketMaker;
    }

    public void setMarketMaker(boolean marketMaker) {
        this.marketMaker = marketMaker;
    }

    @Override
    public String toString() {
        return "\n\tTrade:\n\t\tprice: " + price +
                "\n\t\tquantity: " + quantity +
                "\n\t\ttimestamp: " + timestamp +
                "\n\t\tmarketMaker: " + marketMaker;
    }
}
