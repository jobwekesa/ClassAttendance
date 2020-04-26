package com.adivid.zpattendanceadmin.interfaces;

import com.adivid.zpattendanceadmin.model_dropdown.BlockModel;
import com.adivid.zpattendanceadmin.model_dropdown.DepartmentNOfficeModel;
import com.adivid.zpattendanceadmin.model_dropdown.DesignationModel;
import com.adivid.zpattendanceadmin.model_dropdown.OfficeNameModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api2 {


    @GET("block.php")
    Call<BlockModel> getBlockType(

    );

    @FormUrlEncoded
    @POST("department_office_type.php")
    Call<DepartmentNOfficeModel> getDepartmentOfficeType(
            @Field("id_block") String id_block

    );


    @FormUrlEncoded
    @POST("office_name.php")
    Call<OfficeNameModel> getOfficeName(
            @Field("id_block") String id_block,
            @Field("id_workingdepartment") String id_workingdepartment,
            @Field("id_officetype") String id_officetype

    );

    @GET("designation.php")
    Call<DesignationModel> getDesignation(

    );

}
