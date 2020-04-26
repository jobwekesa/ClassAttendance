package com.adivid.zpattendanceadmin.model_dropdown;

import com.google.gson.annotations.SerializedName;

public class OfficeTypeInfo {

    @SerializedName("id_office_type")
    private String idOfficeType;
    @SerializedName("office_type")
    private String officeType;

    public String getIdOfficeType() {
        return idOfficeType;
    }

    public void setIdOfficeType(String idOfficeType) {
        this.idOfficeType = idOfficeType;
    }

    public String getOfficeType() {
        return officeType;
    }

    public void setOfficeType(String officeType) {
        this.officeType = officeType;
    }
}
