package io.bitmax.api.websocket.messages.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebSocketOrder {

    /**
     * client order id, (needed to cancel order)
     */
    @JsonProperty("coid")
    private String coid;

    /**
     * for each user, this is a strictly increasing long integer (represented as string)
     */
    @JsonProperty("execId")
    private String execId;

    /**
     * optional, the original order id, see canceling orders for more details
     */
    @JsonProperty("origCoid")
    private String origCoid;

    /**
     * Limit, Market, StopLimit, StopMarket
     */
    @JsonProperty("orderType")
    private String orderType;

    /**
     * symbol
     */
    @JsonProperty("s")
    private String symbol;

    /**
     * timestamp
     */
    @JsonProperty("t")
    private long timestamp;

    /**
     * limit price, only available for limit and stop limit orders
     */
    @JsonProperty("p")
    private String limitPrice;

    /**
     * stop price, only available for stop market and stop limit orders
     */
    @JsonProperty("sp")
    private String stopPrice;

    /**
     * order quantity
     */
    @JsonProperty("q")
    private String orderQuantity;

    /**
     * last quantity, the quantity executed by the last fill
     */
    @JsonProperty("q")
    private String lastQuantity;

    /**
     * filled quantity, this is the aggregated quantity executed by all past fills
     */
    @JsonProperty("f")
    private String filledQuantity;

    /**
     * average filled price
     */
    @JsonProperty("ap")
    private String averagePrice;

    /**
     * base asset total balance
     */
    @JsonProperty("bb")
    private String baseAssetTotalBalance;

    /**
     * base asset pending balance
     */
    @JsonProperty("bpb")
    private String baseAssetPendingBalance;

    /**
     * quote asset total balance
     */
    @JsonProperty("qp")
    private String quoteAssetTotalBalance;

    /**
     * quote asset pending balance
     */
    @JsonProperty("qpb")
    private String quoteAssetPendingBalance;

    /**
     * fee
     */
    @JsonProperty("fee")
    private String fee;

    /**
     * fee asset
     */
    @JsonProperty("fa")
    private String feeAsset;

    /**
     * if possitive, this is the BTMX commission charged by reverse mining, if negative, this is the mining output of the current fill.
     */
    @JsonProperty("bc")
    private String bitmaxComission;

    /**
     * optional, the BTMX balance of the current account. This field is only available when bc is non-zero.
     */
    @JsonProperty("btmxBal")
    private String bitmaxBalance;

    /**
     * side
     */
    @JsonProperty("side")
    private String side;

    /**
     * order status
     */
    @JsonProperty("status")
    private String orderStatus;

    /**
     *  if the order is rejected, this field explains why
     */
    @JsonProperty("errorCode")
    private String errorCode;

    /**
     * account category: CASH/MARGIN
     */
    @JsonProperty("cat")
    private String category;

    /**
     * execution instruction
     */
    @JsonProperty("ei")
    private String instruction;

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

    public String getExecId() {
        return execId;
    }

    public void setExecId(String execId) {
        this.execId = execId;
    }

    public String getOrigCoid() {
        return origCoid;
    }

    public void setOrigCoid(String origCoid) {
        this.origCoid = origCoid;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "\nOrder:\n\tcoid: " + coid +
                "\n\torigCoid: " + origCoid +
                "\n\tsymbol: " + symbol +
                "\n\texecId: " + execId +
                "\n\torderType: " + orderType +
                "\n\tlastQuantity: " + lastQuantity +
                "\n\tbitmaxComission: " + bitmaxComission +
                "\n\tbitmaxBalance: " + bitmaxBalance +
                "\n\ttimestamp: " + timestamp +
                "\n\tlimitPrice: " + limitPrice +
                "\n\tstopPrice: " + stopPrice +
                "\n\torderQuantity: " + orderQuantity +
                "\n\tfilledQuantity: " + filledQuantity +
                "\n\taveragePrice: " + averagePrice +
                "\n\tbaseAssetTotalBalance: " + baseAssetTotalBalance +
                "\n\tbaseAssetPendingBalance: " + baseAssetPendingBalance +
                "\n\tquoteAssetTotalBalance: " + quoteAssetTotalBalance +
                "\n\tquoteAssetPendingBalance: " + quoteAssetPendingBalance +
                "\n\tfee: " + fee +
                "\n\tfeeAsset: " + feeAsset +
                "\n\tside: " + side +
                "\n\torderStatus: " + orderStatus +
                "\n\tcategory: " + category +
                "\n\terrorCode: " + errorCode +
                "\n\tinstruction: " + instruction;
    }
}
