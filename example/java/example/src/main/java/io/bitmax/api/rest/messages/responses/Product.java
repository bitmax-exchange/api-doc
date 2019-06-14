package io.bitmax.api.rest.messages.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("domain")
    private String domain;

    @JsonProperty("baseAsset")
    private String baseAsset;

    @JsonProperty("quoteAsset")
    private String quoteAsset;

    @JsonProperty("priceScale")
    private int priceScale;

    @JsonProperty("qtyScale")
    private int qtyScale;

    @JsonProperty("notionalScale")
    private int notionalScale;

    @JsonProperty("minQty")
    private double minQty;

    @JsonProperty("maxQty")
    private double maxQty;

    @JsonProperty("minNotional")
    private double minNotional;

    @JsonProperty("maxNotional")
    private double maxNotional;

    @JsonProperty("status")
    private String status;

    @JsonProperty("miningStatus")
    private String miningStatus;

    @JsonProperty("marginTradable")
    private String marginTradable;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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

    public int getPriceScale() {
        return priceScale;
    }

    public void setPriceScale(int priceScale) {
        this.priceScale = priceScale;
    }

    public int getQtyScale() {
        return qtyScale;
    }

    public void setQtyScale(int qtyScale) {
        this.qtyScale = qtyScale;
    }

    public int getNotionalScale() {
        return notionalScale;
    }

    public void setNotionalScale(int notionalScale) {
        this.notionalScale = notionalScale;
    }

    public double getMinQty() {
        return minQty;
    }

    public void setMinQty(double minQty) {
        this.minQty = minQty;
    }

    public double getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(double maxQty) {
        this.maxQty = maxQty;
    }

    public double getMinNotional() {
        return minNotional;
    }

    public void setMinNotional(double minNotional) {
        this.minNotional = minNotional;
    }

    public double getMaxNotional() {
        return maxNotional;
    }

    public void setMaxNotional(double maxNotional) {
        this.maxNotional = maxNotional;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMiningStatus() {
        return miningStatus;
    }

    public void setMiningStatus(String miningStatus) {
        this.miningStatus = miningStatus;
    }

    public String getMarginTradable() {
        return marginTradable;
    }

    public void setMarginTradable(String marginTradable) {
        this.marginTradable = marginTradable;
    }

    @Override
    public String toString() {
        return "\nProduct:\n\tsymbol: " + symbol + '\n' +
                "\tdomain: " + domain + '\n' +
                "\tbaseAsset: " + baseAsset + '\n' +
                "\tquoteAsset: " + quoteAsset + '\n' +
                "\tpriceScale: " + priceScale + '\n' +
                "\tqtyScale: " + qtyScale + '\n' +
                "\tnotionalScale: " + notionalScale + '\n' +
                "\tminQty: " + minQty + '\n' +
                "\tmaxQty: " + maxQty + '\n' +
                "\tminNotional: " + minNotional + '\n' +
                "\tmaxNotional: " + maxNotional + '\n' +
                "\tstatus: " + status + '\n' +
                "\tminingStatus: " + miningStatus + '\n' +
                "\tmarginTradable: " + marginTradable ;
    }
}
