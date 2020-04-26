package com.adivid.zpattendanceadmin.models;

import com.google.gson.annotations.SerializedName;

public class UserData1 {

    @SerializedName("login")
    private Boolean login;
    @SerializedName("data")
    private UserInfo data;

    public Boolean getLogin() {
        return login;
    }

    public void setLogin(Boolean login) {
        this.login = login;
    }

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
