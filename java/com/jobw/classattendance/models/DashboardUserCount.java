package com.adivid.zpattendanceadmin.models;

import com.google.gson.annotations.SerializedName;

public class DashboardUserCount {

    @SerializedName("dashboard_users")
    private Integer dashboardUsers;
    @SerializedName("activate_users")
    private Integer activateUsers;
    @SerializedName("admin_users")
    private Integer adminUsers;
    @SerializedName("app_users")
    private Integer appUsers;

    public Integer getDashboardUsers() {
        return dashboardUsers;
    }

    public void setDashboardUsers(Integer dashboardUsers) {
        this.dashboardUsers = dashboardUsers;
    }

    public Integer getActivateUsers() {
        return activateUsers;
    }

    public void setActivateUsers(Integer activateUsers) {
        this.activateUsers = activateUsers;
    }

    public Integer getAdminUsers() {
        return adminUsers;
    }

    public void setAdminUsers(Integer adminUsers) {
        this.adminUsers = adminUsers;
    }

    public Integer getAppUsers() {
        return appUsers;
    }

    public void setAppUsers(Integer appUsers) {
        this.appUsers = appUsers;
    }

}
