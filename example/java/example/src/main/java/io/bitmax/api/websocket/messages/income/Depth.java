package io.bitmax.api.websocket.messages.income;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Depth {

    @JsonProperty("s")
    private String symbol;

    @JsonProperty("seqnum")
    private long sequenceNumber;

    @JsonProperty("asks")
    private String[][] asks;

    @JsonProperty("bids")
    private String[][] bids;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String[][] getAsks() {
        return asks;
    }

    public void setAsks(String[][] asks) {
        this.asks = asks;
    }

    public String[][] getBids() {
        return bids;
    }

    public void setBids(String[][] bids) {
        this.bids = bids;
    }

    @Override
    public String toString() {
        return "Depth:\n\tsymbol: " + symbol + '\n' +
                "\tsequenceNumber: " + sequenceNumber + '\n' +
                "\tasks: " + Arrays.deepToString(asks) + '\n' +
                "\tbids: " + Arrays.deepToString(bids);
    }
}
