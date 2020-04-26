package com.adivid.zpattendanceadmin.models_two;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppUserListModel {

    @SerializedName("app_users")
    private List<AppUserListInfo> appUsers = null;
    @SerializedName("result")
    private Boolean result;

    public List<AppUserListInfo> getAppUserListInfo() {
        return appUsers;
    }

    public void setAppUserListInfo(List<AppUserListInfo> appUsers) {
        this.appUsers = appUsers;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
