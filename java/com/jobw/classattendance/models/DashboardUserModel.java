package com.adivid.zpattendanceadmin.models;

import com.google.gson.annotations.SerializedName;

public class DashboardUserModel {

    @SerializedName("result")
    private Boolean result;
    @SerializedName("total")
    private DashboardUserCount dashboardUserCountInfo;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public DashboardUserCount getDashboardUserCountInfo() {
        return dashboardUserCountInfo;
    }

    public void setDashboardUserCountInfo(DashboardUserCount dashboardUserCountInfo) {
        this.dashboardUserCountInfo = dashboardUserCountInfo;
    }
}
