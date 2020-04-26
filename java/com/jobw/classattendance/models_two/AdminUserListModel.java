package com.adivid.zpattendanceadmin.models_two;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdminUserListModel {

    @SerializedName("admin_users")
    private List<AdminUserListInfo> adminUsers = null;
    @SerializedName("result")
    private Boolean result;

    public List<AdminUserListInfo> getAdminListInfo() {
        return adminUsers;
    }

    public void setAdminUsers(List<AdminUserListInfo> dashboardUsers) {
        this.adminUsers = dashboardUsers;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
