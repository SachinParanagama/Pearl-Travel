package com.ead.train_management.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class login {
    @SerializedName("NIC")
    @Expose
    private String nic;

    @SerializedName("UserPassword")
    @Expose
    private String userPassword;

    public login() {
    }

    public login(String nic, String userPassword) {
        this.nic = nic;
        this.userPassword = userPassword;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
