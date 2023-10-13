package com.ead.train_management.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class disable {
    @SerializedName("AccountStatus")
    @Expose
    private boolean trv_account;

    public boolean isTrv_account() {
        return trv_account;
    }

    public void setTrv_account(boolean trv_account) {
        this.trv_account = trv_account;
    }
}
