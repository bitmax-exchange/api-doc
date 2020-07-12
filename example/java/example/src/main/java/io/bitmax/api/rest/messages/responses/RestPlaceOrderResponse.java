package io.bitmax.api.rest.messages.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestPlaceOrderResponse {

    /**
     * time
     */
    @JsonProperty("time")
    private long time;

    /**
     * the unique identifier, you will need
     */
    @JsonProperty("coid")
    private String coid;

    /**
     * symbol
     */
    @JsonProperty("symbol")
    private String symbol;

    /**
     * base asset
     */
    @JsonProperty("baseAsset")
    private String baseAsset;

    /**
     * quote asset
     */
    @JsonProperty("quoteAsset")
    private String quoteAsset;

    /**
     * order side
     */
    @JsonProperty("side")
    private String side;

    /**
     * order price - only available for limit and stop limit orders
     */
    @JsonProperty("orderPrice")
    private String orderPrice;

    /**
     * order stop price - only available for stop market and stop limit orders
     */
    @JsonProperty("stopPrice")
    private String stopPrice;

    /**
     * order quantity
     */
    @JsonProperty("orderQty")
    private String orderQty;

    /**
     * filled quantity
     */
    @JsonProperty("filledQty")
    private String filledQty;

    /**
     * cumulative fee paid for this order
     */
    @JsonProperty("fee")
    private String fee;

    /**
     * the asset of fee
     */
    @JsonProperty("feeAsset")
    private String feeAsset;

    /**
     * order status
     */
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

    public String getFilledQty() {
        return filledQty;
    }

    public void setFilledQty(String filledQty) {
        this.filledQty = filledQty;
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
        return "\nOrder:\n\ttime: " + time + '\n' +
                "\tcoid: " + coid + '\n' +
                "\tsymbol: " + symbol + '\n' +
                "\tbaseAsset: " + baseAsset + '\n' +
                "\tquoteAsset: " + quoteAsset + '\n' +
                "\tside: " + side + '\n' +
                "\torderPrice: " + orderPrice + '\n' +
                "\tstopPrice: " + stopPrice + '\n' +
                "\torderQty: " + orderQty + '\n' +
                "\tfilledQty: " + filledQty + '\n' +
                "\tfee: " + fee + '\n' +
                "\tfeeAsset: " + feeAsset + '\n' +
                "\tstatus: " + status;
    }
}
