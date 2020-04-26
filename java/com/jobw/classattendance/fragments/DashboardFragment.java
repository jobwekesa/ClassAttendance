package com.adivid.zpattendanceadmin.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.adivid.zpattendanceadmin.R;
import com.adivid.zpattendanceadmin.activities.ContainerActivity;
import com.adivid.zpattendanceadmin.models.DashboardUserCount;
import com.adivid.zpattendanceadmin.models.DashboardUserModel;
import com.adivid.zpattendanceadmin.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.adivid.zpattendanceadmin.activities.DashboardViewActivity.token_id;
import static com.adivid.zpattendanceadmin.activities.DashboardViewActivity.token_value;
import static com.adivid.zpattendanceadmin.activities.DashboardViewActivity.user_id;
import static com.adivid.zpattendanceadmin.storage.SharedPrefManager.SHARED_PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView txt_dashbaord_user_count, txt_app_user_count, txt_admin_user_count,
            txt_activate_user_count;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private CardView cardViewActivate, cardViewDash, cardViewAdmin, cardViewApp;
    private ProgressBar progressBar;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initView();
        setUpData();

        return view;
    }

    private void initView() {
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        cardViewActivate = view.findViewById(R.id.card_pending_users);
        cardViewActivate.setOnClickListener(this);
        cardViewDash = view.findViewById(R.id.card_dashboard_users);
        cardViewDash.setOnClickListener(this);
        cardViewAdmin = view.findViewById(R.id.card_admin_users);
        cardViewAdmin.setOnClickListener(this);
        cardViewApp = view.findViewById(R.id.card_app_users);
        cardViewApp.setOnClickListener(this);

        progressBar = view.findViewById(R.id.progressBar);

    }

    private void setUpData() {
        progressBar.setVisibility(View.VISIBLE);
        txt_activate_user_count = view.findViewById(R.id.txt_pending_user_count);
        txt_admin_user_count = view.findViewById(R.id.txt_admin_users_count);
        txt_app_user_count = view.findViewById(R.id.txt_app_users_count);
        txt_dashbaord_user_count = view.findViewById(R.id.txt_dashboard_user_count);

        Call<DashboardUserModel> call = RetrofitClient
                .getInstance()
                .getApi()
                .getDashboardUserCount(user_id, token_id, token_value);

        call.enqueue(new Callback<DashboardUserModel>() {
            @Override
            public void onResponse(Call<DashboardUserModel> call, Response<DashboardUserModel> response) {
                DashboardUserCount dashboardUserCountInfo = response.body().getDashboardUserCountInfo();

                txt_activate_user_count.setText(dashboardUserCountInfo.getActivateUsers().toString());
                txt_admin_user_count.setText(dashboardUserCountInfo.getAdminUsers().toString());
                txt_app_user_count.setText(dashboardUserCountInfo.getAppUsers().toString());
                txt_dashbaord_user_count.setText(dashboardUserCountInfo.getDashboardUsers().toString());

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<DashboardUserModel> call, Throwable t) {

                Toast.makeText(getActivity(), "Poor Internet Connection!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_pending_users:
                Intent intent = new Intent(getActivity(), ContainerActivity.class);
                intent.putExtra("type", "activate");
                startActivity(intent);
                break;
            case R.id.card_dashboard_users:
                Intent intent1 = new Intent(getActivity(), ContainerActivity.class);
                intent1.putExtra("type", "dashboard");
                startActivity(intent1);
                break;
            case R.id.card_admin_users:
                Intent intent2 = new Intent(getActivity(), ContainerActivity.class);
                intent2.putExtra("type", "admin");
                startActivity(intent2);
                break;
            case R.id.card_app_users:
                Intent intent3 = new Intent(getActivity(), ContainerActivity.class);
                intent3.putExtra("type", "app");
                startActivity(intent3);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpData();
    }
}
