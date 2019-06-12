package io.bitmax.api.websocket.messages.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    @JsonProperty("coid")
    private String coid;

    @JsonProperty("s")
    private String symbol;

    @JsonProperty("ba")
    private String baseAsset;

    @JsonProperty("qa")
    private String quoteAsset;

    @JsonProperty("t")
    private long timestamp;

    @JsonProperty("p")
    private String limitPrice;

    @JsonProperty("sp")
    private String stopPrice;

    @JsonProperty("q")
    private String orderQuantity;

    @JsonProperty("f")
    private String filledQuantity;

    @JsonProperty("ap")
    private String averagePrice;

    @JsonProperty("bb")
    private String baseAssetTotalBalance;

    @JsonProperty("bpb")
    private String baseAssetPendingBalance;

    @JsonProperty("qp")
    private String quoteAssetTotalBalance;

    @JsonProperty("qpb")
    private String quoteAssetPendingBalance;

    @JsonProperty("fee")
    private String fee;

    @JsonProperty("fa")
    private String feeAsset;

    @JsonProperty("side")
    private String side;

    @JsonProperty("status")
    private String orderStatus;

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(String limitPrice) {
        this.limitPrice = limitPrice;
    }

    public String getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(String stopPrice) {
        this.stopPrice = stopPrice;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getFilledQuantity() {
        return filledQuantity;
    }

    public void setFilledQuantity(String filledQuantity) {
        this.filledQuantity = filledQuantity;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String getBaseAssetTotalBalance() {
        return baseAssetTotalBalance;
    }

    public void setBaseAssetTotalBalance(String baseAssetTotalBalance) {
        this.baseAssetTotalBalance = baseAssetTotalBalance;
    }

    public String getBaseAssetPendingBalance() {
        return baseAssetPendingBalance;
    }

    public void setBaseAssetPendingBalance(String baseAssetPendingBalance) {
        this.baseAssetPendingBalance = baseAssetPendingBalance;
    }

    public String getQuoteAssetTotalBalance() {
        return quoteAssetTotalBalance;
    }

    public void setQuoteAssetTotalBalance(String quoteAssetTotalBalance) {
        this.quoteAssetTotalBalance = quoteAssetTotalBalance;
    }

    public String getQuoteAssetPendingBalance() {
        return quoteAssetPendingBalance;
    }

    public void setQuoteAssetPendingBalance(String quoteAssetPendingBalance) {
        this.quoteAssetPendingBalance = quoteAssetPendingBalance;
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

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "\n\tOrder:\n\t\tcoid: " + coid +
                "\n\t\tsymbol: " + symbol +
                "\n\t\tbaseAsset: " + baseAsset +
                "\n\t\tquoteAsset: " + quoteAsset +
                "\n\t\ttimestamp: " + timestamp +
                "\n\t\tlimitPrice: " + limitPrice +
                "\n\t\tstopPrice: " + stopPrice +
                "\n\t\torderQuantity: " + orderQuantity +
                "\n\t\tfilledQuantity: " + filledQuantity +
                "\n\t\taveragePrice: " + averagePrice +
                "\n\t\tbaseAssetTotalBalance: " + baseAssetTotalBalance +
                "\n\t\tbaseAssetPendingBalance: " + baseAssetPendingBalance +
                "\n\t\tquoteAssetTotalBalance: " + quoteAssetTotalBalance +
                "\n\t\tquoteAssetPendingBalance: " + quoteAssetPendingBalance +
                "\n\t\tfee: " + fee +
                "\n\t\tfeeAsset: " + feeAsset +
                "\n\t\tside: " + side +
                "\n\t\torderStatus: " + orderStatus;
    }
}
