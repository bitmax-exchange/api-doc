package io.bitmax.api.websocket.messages.outcome;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CancelOrder {

    @JsonProperty("messageType")
    private String messageType;

    @JsonProperty("time")
    private long time;

    @JsonProperty("coid")
    private String coid;

    @JsonProperty("origCoid")
    private String origCoid;

    @JsonProperty("symbol")
    private String symbol;

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

    public String getOrigCoid() {
        return origCoid;
    }

    public void setOrigCoid(String origCoid) {
        this.origCoid = origCoid;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "PlaceOrderMessage:\n\tmessageType: " + messageType +
                "\n\ttime: " + time +
                "\n\tcoid: " + coid +
                "\n\tsymbol: " + symbol +
                "\n\torigCoid: " + origCoid;
    }
}
