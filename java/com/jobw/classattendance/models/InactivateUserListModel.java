package com.adivid.zpattendanceadmin.models;

import com.google.gson.annotations.SerializedName;

public class InactivateUserListModel {

    @SerializedName("id")
    private String id;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("email")
    private String email;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("error_email")
    private boolean error_email;
    @SerializedName("error_mobile")
    private boolean error_mobile;
    @SerializedName("id_proof")
    private String id_proof;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean getError_email() {
        return error_email;
    }

    public void setError_email(boolean error_email) {
        this.error_email = error_email;
    }

    public boolean getError_mobile() {
        return error_mobile;
    }



    public boolean isError_mobile() {
        return error_mobile;
    }

    public String getId_proof() {
        return id_proof;
    }

    public void setId_proof(String id_proof) {
        this.id_proof = id_proof;
    }
}
