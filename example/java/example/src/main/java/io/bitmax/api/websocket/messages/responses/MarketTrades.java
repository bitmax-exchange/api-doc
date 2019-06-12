package io.bitmax.api.websocket.messages.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketTrades {

    @JsonProperty("s")
    private String symbol;

    @JsonProperty("trades")
    private Trade[] trades;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Trade[] getTrades() {
        return trades;
    }

    public void setTrades(Trade[] trades) {
        this.trades = trades;
    }

    @Override
    public String toString() {
        return "MarketTrades:\n\tsymbol: " + symbol + '\n' +
                "\ttrades: " + Arrays.toString(trades);
    }
}
