package io.bitmax.api.rest.response.templates;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfo {

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
