package io.bitmax.api.websocket.messages.outcome;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Subscribe {

    @JsonProperty("messageType")
    private String messageType;

    @JsonProperty("marketDepthLevel")
    private int marketDepthLevel;

    @JsonProperty("recentTradeMaxCount")
    private int recentTradeMaxCount;

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

    @Override
    public String toString() {
        return "Trade:\n\tmessageType: " + messageType +
                "\n\tmarketDepthLevel: " + marketDepthLevel +
                "\n\trecentTradeMaxCount: " + recentTradeMaxCount;
    }
}
