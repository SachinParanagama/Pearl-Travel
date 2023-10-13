package com.ead.train_management.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class train {
    @SerializedName("id")
    @Expose
    private String tidc;

    public String getTidc() {
        return tidc;
    }

    public void setTidc(String tidc) {
        this.tidc = tidc;
    }
}
