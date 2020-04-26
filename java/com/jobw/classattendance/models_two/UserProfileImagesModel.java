package com.adivid.zpattendanceadmin.models_two;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfileImagesModel {

    @SerializedName("user_images")
    private List<UserImages> userImages = null;
    @SerializedName("result")
    private Boolean result;

    public List<UserImages> getUserImages() {
        return userImages;
    }

    public void setUserImages(List<UserImages> userImages) {
        this.userImages = userImages;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
