package io.bitmax.api.rest.messages.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
* Place a New Order with Rest
* */
public class RestPlaceOrderRequest {

    /**
     * milliseconds since UNIX epoch in UTC
     */
    @JsonProperty("time")
    private long time;

    /**
     * a 32-character unique client order Id
     */
    @JsonProperty("coid")
    private String coid;

    /**
     * symbol
     */
    @JsonProperty("symbol")
    private String symbol;

    /**
     * order price
     */
    @JsonProperty("orderPrice")
    private String orderPrice;

    /**
     * order quantity
     */
    @JsonProperty("orderQty")
    private String orderQty;

    /**
     * order type, you shall specify one of the following: "limit", "market", "stop_market", "stop_limit".
     */
    @JsonProperty("orderType")
    private String orderType;

    /**
     * order side "buy" or "sell"
     */
    @JsonProperty("side")
    private String side;

    /**
     * Optional, if true, the order will either be posted to the limit order book or be cancelled, i.e. the order cannot take liquidity; default value is false
     */
    @JsonProperty("postOnly")
    private boolean postOnly = false;

    /**
     * optional, stop price of the order. This field is required for stop market orders and stop limit orders.
     */
    @JsonProperty("stopPrice")
    private String stopPrice = "";

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCoid() {
        return coid;
    }

    public void setCoid(String coid) {
        this.coid = coid;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public boolean isPostOnly() {
        return postOnly;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public String getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(String stopPrice) {
        this.stopPrice = stopPrice;
    }

    @Override
    public String toString() {
        return "RestPlaceOrderRequest:\n\ttime: " + time +
                "\n\tcoid: " + coid +
                "\n\tsymbol: " + symbol +
                "\n\torderPrice: " + orderPrice +
                "\n\torderQty: " + orderQty +
                "\n\torderType: " + orderType +
                "\n\tside: " + side +
                "\n\tpostOnly: " + postOnly +
                "\n\tstopPrice: " + stopPrice;
    }
}
