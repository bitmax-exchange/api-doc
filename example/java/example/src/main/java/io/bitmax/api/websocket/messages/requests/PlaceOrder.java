package io.bitmax.api.websocket.messages.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlaceOrder {

    @JsonProperty("messageType")
    private String messageType;

    @JsonProperty("time")
    private long time;

    @JsonProperty("coid")
    private String coid;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("orderPrice")
    private String orderPrice;

    @JsonProperty("orderQty")
    private String orderQty;

    @JsonProperty("orderType")
    private String orderType;

    @JsonProperty("side")
    private String side;

    @JsonProperty("postOnly")
    private boolean postOnly = false;

    @JsonProperty("stopPrice")
    private String stopPrice = "";

    @JsonProperty("timeInForce")
    private String timeInForce = "GTC";

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

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

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    @Override
    public String toString() {
        return "PlaceOrder:\n\tmessageType: " + messageType +
                "\n\ttime: " + time +
                "\n\tcoid: " + coid +
                "\n\tsymbol: " + symbol +
                "\n\torderPrice: " + orderPrice +
                "\n\torderQty: " + orderQty +
                "\n\torderType: " + orderType +
                "\n\tside: " + side +
                "\n\tpostOnly: " + postOnly +
                "\n\tstopPrice: " + stopPrice +
                "\n\ttimeInForce: " + timeInForce;
    }
}
