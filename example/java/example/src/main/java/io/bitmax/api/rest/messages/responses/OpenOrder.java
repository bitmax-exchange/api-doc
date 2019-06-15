package io.bitmax.api.rest.messages.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenOrder {

    @JsonProperty("time")
    private long time;

    @JsonProperty("coid")
    private String coid;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("baseAsset")
    private String baseAsset;

    @JsonProperty("quoteAsset")
    private String quoteAsset;

    @JsonProperty("side")
    private String side;

    @JsonProperty("orderPrice")
    private String orderPrice;

    @JsonProperty("stopPrice")
    private String stopPrice;

    @JsonProperty("orderQty")
    private String orderQty;

    @JsonProperty("filled")
    private String filled;

    @JsonProperty("fee")
    private String fee;

    @JsonProperty("feeAsset")
    private String feeAsset;

    @JsonProperty("status")
    private String status;

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

    public String getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(String baseAsset) {
        this.baseAsset = baseAsset;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(String quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(String stopPrice) {
        this.stopPrice = stopPrice;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getFilled() {
        return filled;
    }

    public void setFilled(String filled) {
        this.filled = filled;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getFeeAsset() {
        return feeAsset;
    }

    public void setFeeAsset(String feeAsset) {
        this.feeAsset = feeAsset;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "\nOpenOrder:\n\ttime: " + time + '\n' +
                "\tcoid: " + coid + '\n' +
                "\tsymbol: " + symbol + '\n' +
                "\tbaseAsset: " + baseAsset + '\n' +
                "\tquoteAsset: " + quoteAsset + '\n' +
                "\tside: " + side + '\n' +
                "\torderPrice: " + orderPrice + '\n' +
                "\tstopPrice: " + stopPrice + '\n' +
                "\torderQty: " + orderQty + '\n' +
                "\tfilled: " + filled + '\n' +
                "\tfee: " + fee + '\n' +
                "\tfeeAsset: " + feeAsset + '\n' +
                "\tstatus: " + status;
    }
}
