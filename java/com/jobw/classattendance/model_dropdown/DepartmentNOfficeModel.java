package com.adivid.zpattendanceadmin.model_dropdown;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DepartmentNOfficeModel {

    @SerializedName("department")
    private List<DepartmentInfo> department = null;
    @SerializedName("office_type")
    private List<OfficeTypeInfo> officeType = null;

    public List<DepartmentInfo> getDepartment() {
        return department;
    }

    public void setDepartment(List<DepartmentInfo> department) {
        this.department = department;
    }

    public List<OfficeTypeInfo> getOfficeType() {
        return officeType;
    }

    public void setOfficeType(List<OfficeTypeInfo> officeType) {
        this.officeType = officeType;
    }

}
