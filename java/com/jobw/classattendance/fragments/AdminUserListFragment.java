package com.adivid.zpattendanceadmin.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adivid.zpattendanceadmin.R;
import com.adivid.zpattendanceadmin.adapters.AdminListAdapter;
import com.adivid.zpattendanceadmin.adapters.DashboardListAdapter;
import com.adivid.zpattendanceadmin.listeners.RecyclerClick_Listener;
import com.adivid.zpattendanceadmin.listeners.RecyclerTouchListener;
import com.adivid.zpattendanceadmin.models.InactivateUserDialogModel;
import com.adivid.zpattendanceadmin.models.InactivateUserDiologInfo;
import com.adivid.zpattendanceadmin.models_two.AdminUserListInfo;
import com.adivid.zpattendanceadmin.models_two.AdminUserListModel;
import com.adivid.zpattendanceadmin.network.RetrofitClient;
import com.adivid.zpattendanceadmin.storage.SharedPrefManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.adivid.zpattendanceadmin.activities.DashboardViewActivity.token_id;
import static com.adivid.zpattendanceadmin.activities.DashboardViewActivity.token_value;
import static com.adivid.zpattendanceadmin.activities.DashboardViewActivity.user_id;
import static com.adivid.zpattendanceadmin.storage.SharedPrefManager.SHARED_PREF_NAME;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminUserListFragment extends Fragment implements DashboardListAdapter.OnDashboardListItemClickListener {

    private View view;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPrefManager prefManager;

    private Dialog dialog, customImageDiaolog;
    private ProgressBar endProgressBar, diaologProgressBar;
    private InactivateUserDiologInfo customDialogUserInfo;

    private RecyclerView recyclerView;
    private AdminListAdapter adminListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private static List<AdminUserListInfo> userTotalList, userDisplayList;

    private ProgressBar progressBar;

    public AdminUserListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_admin_user_list, container, false);
        initView();
        return view;
    }

    private void initView() {

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        prefManager = new SharedPrefManager(getActivity());

        userDisplayList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adminListAdapter = new AdminListAdapter(getActivity(), userDisplayList, this);
        recyclerView.setAdapter(adminListAdapter);
        adminListAdapter.notifyDataSetChanged();

        progressBar = view.findViewById(R.id.progressBar);

        setUpAdminList();


    }


    private void setUpAdminList() {

        progressBar.setVisibility(View.VISIBLE);

        Call<AdminUserListModel> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAdminList(user_id, token_id, token_value);

        call.enqueue(new Callback<AdminUserListModel>() {
            @Override
            public void onResponse(Call<AdminUserListModel> call, Response<AdminUserListModel> response) {
                if (response.body() != null) {
                    List<AdminUserListInfo> userListInfo = response.body().getAdminListInfo();
                    userDisplayList.addAll(userListInfo);
                }

                if (adminListAdapter != null) {
                    adminListAdapter.notifyDataSetChanged();
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AdminUserListModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Poor Internet Connection", Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.GONE);
            }
        });

    }


    private void showUserInfoDialog(final int pos) {
        dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.admin_user_dialog);

        dialog.show();

        diaologProgressBar = dialog.findViewById(R.id.dialog_progress_bar);
        makeApiCall(pos);


        MaterialButton btnCancel = dialog.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    private void makeApiCall(int pos) {

        String user_update_id = userDisplayList.get(pos).getId();
        Call<InactivateUserDialogModel> call = RetrofitClient
                .getInstance()
                .getApi()
                .getInactivateUsersInfo(user_id, token_id, token_value, user_update_id);

        call.enqueue(new Callback<InactivateUserDialogModel>() {
            @Override
            public void onResponse(Call<InactivateUserDialogModel> call, Response<InactivateUserDialogModel> response) {
                if (response.body().getUserDetails() != null) {
                    List<InactivateUserDiologInfo> list = response.body().getUserDetails();
                    customDialogUserInfo = list.get(0);

                    setFetchedData();

                }
            }

            @Override
            public void onFailure(Call<InactivateUserDialogModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Poor Internet Connection", Toast.LENGTH_SHORT).show();

                diaologProgressBar.setVisibility(View.GONE);
            }
        });

    }

    private void setFetchedData() {
        final TextView txt_email, txt_name, txt_mobile, txt_birth_date,
                txt_sevarth_no, txt_block_name, txt_department, txt_designation,
                txt_office_type, txt_office_name;

        txt_email = dialog.findViewById(R.id.txt_email);
        txt_name = dialog.findViewById(R.id.txt_name);
        txt_mobile = dialog.findViewById(R.id.txt_mobile);
        txt_birth_date = dialog.findViewById(R.id.txt_birth_date);
        txt_sevarth_no = dialog.findViewById(R.id.txt_sevarth_no);

        txt_block_name = dialog.findViewById(R.id.txt_block_name);
        txt_department = dialog.findViewById(R.id.txt_department);
        txt_designation = dialog.findViewById(R.id.txt_designation);
        txt_office_type = dialog.findViewById(R.id.txt_office_type);
        txt_office_name = dialog.findViewById(R.id.txt_office_name);

        String email, name, mobile, birth_date, sevarth_no, block_name,
                department, designation, office_type, office_name;

        email = "" + checkNull((customDialogUserInfo.getEmail()));
        name = "" + checkNull(customDialogUserInfo.getFullName());
        mobile = "" + checkNull(customDialogUserInfo.getMobile());
        birth_date = "" + checkNull(customDialogUserInfo.getBirth_date());
        sevarth_no = "" + checkNull(customDialogUserInfo.getSevarth_number());
        block_name = "" + checkNull(customDialogUserInfo.getBlock_name());
        department = "" + checkNull(customDialogUserInfo.getDepartment());
        designation = "" + checkNull(customDialogUserInfo.getDesignation());
        office_type = "" + checkNull(customDialogUserInfo.getOffice_type());
        office_name = "" + checkNull(customDialogUserInfo.getOffice_name());

        txt_email.setText(email);
        txt_name.setText(name);
        txt_mobile.setText(mobile);
        txt_birth_date.setText(birth_date);
        txt_sevarth_no.setText(sevarth_no);

        txt_block_name.setText(block_name);
        txt_department.setText(department);
        txt_designation.setText(designation);
        txt_office_type.setText(office_type);
        txt_office_name.setText(office_name);

        txt_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + txt_mobile.getText().toString().trim()));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        });

        diaologProgressBar.setVisibility(View.GONE);

    }

    private String checkNull(String str) {
        if (str == null || str.equals("")) {
            return "--";
        } else {
            return str;
        }
    }


    private void showCustomImageDiaolog(int pos) {
        customImageDiaolog = new Dialog(getActivity());

        customImageDiaolog.setContentView(R.layout.image_custom_diaolog);
        diaologProgressBar = customImageDiaolog.findViewById(R.id.image_diolog_progressbar);
        ImageView imageDialogView = customImageDiaolog.findViewById(R.id.imgv_dialog_image);
        String imageStr = userDisplayList.get(pos).getIdProof();
        if (imageStr == null || imageStr.equals("")) {
            Toast.makeText(getActivity(), "Id Image Not Set", Toast.LENGTH_LONG).show();
        } else {
            customImageDiaolog.show();
            byte[] decodedString = Base64.decode(imageStr, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageDialogView.setImageBitmap(decodedByte);
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getId() == R.id.img_profile_thumbnail) {
            showCustomImageDiaolog(position);
        } else if (view.getId() == R.id.linearLayoutList) {
            showUserInfoDialog(position);
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }


}
