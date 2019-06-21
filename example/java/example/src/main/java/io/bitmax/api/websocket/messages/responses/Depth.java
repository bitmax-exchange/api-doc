package io.bitmax.api.websocket.messages.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Depth {

    /**
     * symbol
     */
    @JsonProperty("s")
    private String symbol;

    /**
     * timestamp, 64-bit
     */
    @JsonProperty("ts")
    private long timestamp;

    /**
     * sequence number, 64-bit
     */
    @JsonProperty("seqnum")
    private long sequenceNumber;

    /**
     * ask levels, (could be empty)
     * two dimensions array:
     * first dimension: pair with price and quantity
     * second dimension: [0] = price, [1] = quantity
     */
    @JsonProperty("asks")
    private String[][] asks;

    /**
     * bid levels, (could be empty)
     * two dimensions array:
     * first dimension: pair with price and quantity
     * second dimension: [0] = price, [1] = quantity
     */
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Depth:\n\tsymbol: " + symbol + '\n' +
                "\tsequenceNumber: " + sequenceNumber + '\n' +
                "\ttimestamp: " + timestamp + '\n' +
                "\tasks: " + Arrays.deepToString(asks) + '\n' +
                "\tbids: " + Arrays.deepToString(bids);
    }
}
