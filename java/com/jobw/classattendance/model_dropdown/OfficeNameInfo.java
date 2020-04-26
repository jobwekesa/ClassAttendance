package com.adivid.zpattendanceadmin.model_dropdown;

import com.google.gson.annotations.SerializedName;

public class OfficeNameInfo {


    @SerializedName("id_officename")
    private String idOfficename;
    @SerializedName("office_name")
    private String officeName;

    public String getIdOfficename() {
        return idOfficename;
    }

    public void setIdOfficename(String idOfficename) {
        this.idOfficename = idOfficename;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }
}
