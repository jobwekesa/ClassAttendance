package com.adivid.zpattendanceadmin.interfaces;

import com.adivid.zpattendanceadmin.model_dropdown.BlockModel;
import com.adivid.zpattendanceadmin.models.DashboardUserModel;
import com.adivid.zpattendanceadmin.models.InactivateUserData;
import com.adivid.zpattendanceadmin.models.InactivateUserDialogModel;
import com.adivid.zpattendanceadmin.models.UpdateUserInfo;
import com.adivid.zpattendanceadmin.models.UserData1;
import com.adivid.zpattendanceadmin.models.activaiton_result;
import com.adivid.zpattendanceadmin.models_two.AdminUserListModel;
import com.adivid.zpattendanceadmin.models_two.AppUserListModel;
import com.adivid.zpattendanceadmin.models_two.DashboardUserListModel;
import com.adivid.zpattendanceadmin.models_two.DeleteImages;
import com.adivid.zpattendanceadmin.models_two.SecurityKeyResult;
import com.adivid.zpattendanceadmin.models_two.UserProfileImagesModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("session/login.php")
    Call<UserData1> loginUser(
            @Field("username") String mobile,
            @Field("password") String password,
            @Field("device_id") String device_id,
            @Field("fcm_id") String fcm_id
    );

    @FormUrlEncoded
    @POST("user/activate_user.php")
    Call<InactivateUserData> getInactivateUsers(
            @Field("user_id") String user_id,
            @Field("token_id") String token_id,
            @Field("token_value") String token_value
    );

    @FormUrlEncoded
    @POST("user/user_details.php")
    Call<InactivateUserDialogModel> getInactivateUsersInfo(
            @Field("user_id") String user_id,
            @Field("token_id") String token_id,
            @Field("token_value") String token_value,
            @Field("user_update_id") String user_update_id
    );

    @FormUrlEncoded
    @POST("user/activation.php")
    Call<activaiton_result> activateUsers(
            @Field("user_id") String user_id,
            @Field("token_id") String token_id,
            @Field("token_value") String token_value,
            @Field("user_activation_id") String[] user_update_id
    );



    @FormUrlEncoded
    @POST("user/update_user.php")
    Call<UpdateUserInfo> updateUser(
            @Field("update_user_id") String user_update_id,
            @Field("full_name") String full_name,
            @Field("email") String email,
            @Field("birth_date") String birth_date,
            @Field("sevarth") String sevarth,
            @Field("mobile") String mobile,
            @Field("password") String password,
            @Field("user_id") String user_id,
            @Field("token_id") String token_id,
            @Field("token_value") String token_value,
            @Field("block") String block,
            @Field("department") String department,
            @Field("office_type") String office_type,
            @Field("office_name") String office_name,
            @Field("designation") String designation
    );


    @FormUrlEncoded
    @POST("user/home_page.php")
    Call<DashboardUserModel> getDashboardUserCount(
            @Field("user_id") String user_id,
            @Field("token_id") String token_id,
            @Field("token_value") String token_value
    );

    @FormUrlEncoded
    @POST(" user/dashboard_users.php")
    Call<DashboardUserListModel> getDashboardUserList(
            @Field("user_id") String user_id,
            @Field("token_id") String token_id,
            @Field("token_value") String token_value
    );

    @FormUrlEncoded
    @POST(" user/admin_users.php")
    Call<AdminUserListModel> getAdminList(
            @Field("user_id") String user_id,
            @Field("token_id") String token_id,
            @Field("token_value") String token_value
    );

    @FormUrlEncoded
    @POST(" user/app_users.php")
    Call<AppUserListModel> getAppUserList(
            @Field("search") String search,
            @Field("user_id") String user_id,
            @Field("token_id") String token_id,
            @Field("token_value") String token_value
    );

    @FormUrlEncoded
    @POST("user/user_profile_images.php")
    Call<UserProfileImagesModel> getUserProfileImages(
            @Field("user_id") String user_id,
            @Field("token_id") String token_id,
            @Field("token_value") String token_value,
            @Field("user_profile_id") String user_profile_id
    );

    @FormUrlEncoded
    @POST("user/user_profile_images_delete.php")
    Call<DeleteImages> deleteProfileImages(
            @Field("user_id") String user_id,
            @Field("token_id") String token_id,
            @Field("token_value") String token_value,
            @Field("profile_images_user_id") String user_profile_id
    );

    @FormUrlEncoded
    @GET("user/user_profile_images_delete.php")
    Call<BlockModel> getBlockType(
            @Field("user_id") String user_id,
            @Field("token_id") String token_id,
            @Field("token_value") String token_value,
            @Field("profile_images_user_id") String user_profile_id
    );

    @FormUrlEncoded
    @POST("user/deactivation.php")
    Call<activaiton_result> deactivateUsers(
            @Field("user_id") String user_id,
            @Field("token_id") String token_id,
            @Field("token_value") String token_value,
            @Field("user_deactivation_id") String[] user_deactivation_id
    );

    @FormUrlEncoded
    @POST("user/security_key.php")
    Call<SecurityKeyResult> validateSecurityKey(
            @Field("user_id") String user_id,
            @Field("token_id") String token_id,
            @Field("token_value") String token_value,
            @Field("user_profie_id") String user_profie_id,
            @Field("security") String security
    );

}
