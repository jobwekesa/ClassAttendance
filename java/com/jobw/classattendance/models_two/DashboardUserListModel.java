package com.adivid.zpattendanceadmin.models_two;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DashboardUserListModel {

    @SerializedName("dashboard_users")
    private List<DashboardUserListInfo> dashboardUsers = null;
    @SerializedName("result")
    private Boolean result;

    public List<DashboardUserListInfo> getDashboardUsersListInfo() {
        return dashboardUsers;
    }

    public void setDashboardUsers(List<DashboardUserListInfo> dashboardUsers) {
        this.dashboardUsers = dashboardUsers;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
