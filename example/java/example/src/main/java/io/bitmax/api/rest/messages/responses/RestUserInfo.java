package io.bitmax.api.rest.messages.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestUserInfo {


    /**
     * user account group
     */
    @JsonProperty("accountGroup")
    private int accountGroup;

    public int getAccountGroup() {
        return accountGroup;
    }

    public void setAccountGroup(int accountGroup) {
        this.accountGroup = accountGroup;
    }

    @Override
    public String toString() {
        return "accountGroup: " + accountGroup;
    }
}
