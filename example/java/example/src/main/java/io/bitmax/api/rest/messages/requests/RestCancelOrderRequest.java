package io.bitmax.api.rest.messages.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
* Cancel an Order with Rest
* */
public class RestCancelOrderRequest {

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
     * a 32-character unique client cancel order Id
     */
    @JsonProperty("origCoid")
    private String origCoid;

    /**
     * symbol
     */
    @JsonProperty("symbol")
    private String symbol;

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

    public String getOrigCoid() {
        return origCoid;
    }

    public void setOrigCoid(String origCoid) {
        this.origCoid = origCoid;
    }

    @Override
    public String toString() {
        return "RestCancelOrderRequest:\n\ttime: " + time +
                "\n\tcoid: " + coid +
                "\n\tsymbol: " + symbol +
                "\n\torigCoid: " + origCoid;
    }
}
