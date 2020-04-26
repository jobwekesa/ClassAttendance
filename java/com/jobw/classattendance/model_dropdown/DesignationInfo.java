package com.adivid.zpattendanceadmin.model_dropdown;

import com.google.gson.annotations.SerializedName;

public class DesignationInfo {

    @SerializedName("designation_id")
    private String designationId;
    @SerializedName("designation")
    private String designation;

    public String getDesignationId() {
        return designationId;
    }

    public void setDesignationId(String designationId) {
        this.designationId = designationId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

}
