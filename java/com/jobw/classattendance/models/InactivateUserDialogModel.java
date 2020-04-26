package com.adivid.zpattendanceadmin.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InactivateUserDialogModel {

    @SerializedName("user_details")
    private List<InactivateUserDiologInfo> userDetails = null;
    @SerializedName("result")
    private Boolean result;

    public List<InactivateUserDiologInfo> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<InactivateUserDiologInfo> userDetails) {
        this.userDetails = userDetails;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
