package io.bitmax.api.websocket.messages.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Subscribe to webSocket
 */
public class WebSocketSubscribe {

    /**
     * message type
     */
    @JsonProperty("messageType")
    private String messageType;

    /**
     * max number of price levels on each side to be included in the first market depth message
     */
    @JsonProperty("marketDepthLevel")
    private int marketDepthLevel;

    /**
     * max number of recent trades to be included in the first market trades message
     */
    @JsonProperty("recentTradeMaxCount")
    private int recentTradeMaxCount;

    /**
     * optional, set to true if you don't want to receive summary messages, default false
     */
    @JsonProperty("skipSummary")
    private boolean skipSummary = false;

    /**
     * optional, set to true if you don't want to receive bar messages, default false
     */
    @JsonProperty("skipBars")
    private boolean skipBars = false;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public int getMarketDepthLevel() {
        return marketDepthLevel;
    }

    public void setMarketDepthLevel(int marketDepthLevel) {
        this.marketDepthLevel = marketDepthLevel;
    }

    public int getRecentTradeMaxCount() {
        return recentTradeMaxCount;
    }

    public void setRecentTradeMaxCount(int recentTradeMaxCount) {
        this.recentTradeMaxCount = recentTradeMaxCount;
    }

    public boolean isSkipSummary() {
        return skipSummary;
    }

    public void setSkipSummary(boolean skipSummary) {
        this.skipSummary = skipSummary;
    }

    public boolean isSkipBars() {
        return skipBars;
    }

    public void setSkipBars(boolean skipBars) {
        this.skipBars = skipBars;
    }

    @Override
    public String toString() {
        return "Subscribe:\n\tmessageType: " + messageType +
                "\n\tmarketDepthLevel: " + marketDepthLevel +
                "\n\trecentTradeMaxCount: " + recentTradeMaxCount +
                "\n\tskipSummary: " + skipSummary +
                "\n\tskipBars: " + skipBars;
    }
}
