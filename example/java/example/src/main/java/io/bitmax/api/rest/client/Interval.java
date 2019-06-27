package io.bitmax.api.rest.client;

/**
 * Interval enumeration interval's for bars history request
 */
public enum Interval {
    ONE_MINUTE("1"),
    FIVE_MINUTES("5"),
    FIFTEEN_MINUTES("15"),
    HALF_HOURLY("30"),
    HOURLY("60"),
    TWO_HOURLY("120"),
    FOUR_HOURLY("240"),
    SIX_HOURLY("360"),
    TWELVE_HOURLY("720"),
    DAILY("1d"),
    WEEKLY("1w"),
    MONTHLY("1m");

    private String val;

    Interval(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    @Override
    public String toString() {
        return getVal();
    }
}
