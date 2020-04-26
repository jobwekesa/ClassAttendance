package com.adivid.zpattendanceadmin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InactivateUserDiologInfo implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("email")
    private String email;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("birth_date")
    private String birth_date;
    @SerializedName("sevarth_number")
    private String sevarth_number;

    @SerializedName("block_name")
    private String block_name;
    @SerializedName("department")
    private String department;
    @SerializedName("designation")
    private String designation;
    @SerializedName("office_type")
    private String office_type;
    @SerializedName("office_name")
    private String office_name;

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getOffice_type() {
        return office_type;
    }

    public void setOffice_type(String office_type) {
        this.office_type = office_type;
    }

    public String getOffice_name() {
        return office_name;
    }

    public void setOffice_name(String office_name) {
        this.office_name = office_name;
    }

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

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getSevarth_number() {
        return sevarth_number;
    }

    public void setSevarth_number(String sevarth_number) {
        this.sevarth_number = sevarth_number;
    }
}
