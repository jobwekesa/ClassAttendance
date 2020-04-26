package com.adivid.zpattendanceadmin.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adivid.zpattendanceadmin.R;
import com.adivid.zpattendanceadmin.activities.ProfileImagesActivity;
import com.adivid.zpattendanceadmin.activities.UpdateUserProfileActivity;
import com.adivid.zpattendanceadmin.adapters.DashboardListAdapter;
import com.adivid.zpattendanceadmin.callback.DeactivateActionmodeCallback;
import com.adivid.zpattendanceadmin.callback.Toolbar_ActionMode_Callback;
import com.adivid.zpattendanceadmin.listeners.RecyclerClick_Listener;
import com.adivid.zpattendanceadmin.listeners.RecyclerTouchListener;
import com.adivid.zpattendanceadmin.models.InactivateUserDialogModel;
import com.adivid.zpattendanceadmin.models.InactivateUserDiologInfo;
import com.adivid.zpattendanceadmin.models.InactivateUserListModel;
import com.adivid.zpattendanceadmin.models.activaiton_result;
import com.adivid.zpattendanceadmin.models_two.DashboardUserListInfo;
import com.adivid.zpattendanceadmin.models_two.DashboardUserListModel;
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
public class DashboardUserListFragment extends Fragment implements DashboardListAdapter.OnDashboardListItemClickListener {

    private View view;
    private SharedPreferences sharedPreferences, positionPrefs;
    private SharedPreferences.Editor editor, editorPosition;
    private SharedPrefManager prefManager;
    private Dialog dialog, customImageDiaolog;
    private ProgressBar endProgressBar, diaologProgressBar;
    private InactivateUserDiologInfo customDialogUserInfo;
    private LinearLayoutManager linearLayoutManager;

    private static ActionMode mActionMode;

    private RecyclerView recyclerView;
    private static DashboardListAdapter dashboardListAdapter;
    private static List<DashboardUserListInfo> userTotalList, userDisplayList;
    private boolean isScrolling, flag1;
    private int currentItems, scrolledOutItems, totalItems;

    private static ProgressBar progressBar;

    public DashboardUserListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dashboard_user_list, container, false);
        initView();
        setUpDashboardUserList();
        return view;
    }

    private void initView() {

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        positionPrefs = getActivity().getSharedPreferences("adapterPosition", Context.MODE_PRIVATE);
        editorPosition = positionPrefs.edit();

        prefManager = new SharedPrefManager(getActivity());

        userDisplayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        progressBar = view.findViewById(R.id.progressBar);
        endProgressBar = view.findViewById(R.id.progressBarAtEnd);


        setUpRecyclerView();


    }

    private void setUpRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dashboardListAdapter = new DashboardListAdapter(getActivity(), userDisplayList, this);
        recyclerView.setAdapter(dashboardListAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                    dashboardListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrolledOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrolledOutItems == totalItems)) {
                    endProgressBar.setVisibility(View.VISIBLE);
                    isScrolling = false;

                    fetchData();

                }


            }
        });
        dashboardListAdapter.notifyDataSetChanged();

    }

    private void fetchData() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int temp_no = userDisplayList.size();
                try {
                    for (int i = temp_no; i < temp_no + 10; i++) {
                        userDisplayList.add(userTotalList.get(i));
                    }
                } catch (Exception e) {
                }
                endProgressBar.setVisibility(View.GONE);
            }
        }, 1500);

    }

    private void setUpDashboardUserList() {

        progressBar.setVisibility(View.VISIBLE);

        Call<DashboardUserListModel> call = RetrofitClient
                .getInstance()
                .getApi()
                .getDashboardUserList(user_id, token_id, token_value);

        call.enqueue(new Callback<DashboardUserListModel>() {
            @Override
            public void onResponse(Call<DashboardUserListModel> call, Response<DashboardUserListModel> response) {
                userDisplayList.clear();
                if (response.body() != null) {

                    userTotalList = response.body().getDashboardUsersListInfo();
                    for (int i = 0; i < 13; i++) {
                        try {
                            if (userTotalList.get(i) != null) {
                                userDisplayList.add(userTotalList.get(i));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

                if (dashboardListAdapter != null) {
                    dashboardListAdapter.notifyDataSetChanged();
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DashboardUserListModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Poor Internet Connection", Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.GONE);
            }
        });

    }


    private void showUserInfoDialog(final int pos) {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dashboard_user_dialog);

        dialog.show();

        diaologProgressBar = dialog.findViewById(R.id.dialog_progress_bar);
        makeApiCall(pos);

        MaterialButton btnImages = dialog.findViewById(R.id.btn_images);
        MaterialButton btnEdit = dialog.findViewById(R.id.btn_edit);
        ImageView imgCancel = dialog.findViewById(R.id.imgv_cancel);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateUserProfileActivity.class);
                intent.putExtra("usereditinfo", customDialogUserInfo);
                intent.putExtra("position", pos);

                startActivity(intent);
            }
        });


        btnImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ProfileImagesActivity.class);
                intent.putExtra("user_profile_id", userDisplayList.get(pos).getId());
                startActivity(intent);

            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
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
    public void onResume() {
        super.onResume();

        int p = positionPrefs.getInt("position", -1);
        if (p != -1) {
            makeApiCall(p);
            setUpDashboardUserList();
            editorPosition.putInt("position", -1);
            editorPosition.commit();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mActionMode != null) {
            onListItemSelect(position);
        } else {
            if (view.getId() == R.id.img_profile_thumbnail) {
                showCustomImageDiaolog(position);
            } else if (view.getId() == R.id.linearLayoutList) {
                showUserInfoDialog(position);
            }
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {
        onListItemSelect(position);

    }

    private void onListItemSelect(int position) {
        dashboardListAdapter.toggleSelection(position);
        boolean hasCheckedItems = dashboardListAdapter.getSelectedCount() > 0;
        if (hasCheckedItems && mActionMode == null)

            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new
                    DeactivateActionmodeCallback(getActivity(), dashboardListAdapter,
                    userDisplayList, false));
        else if (!hasCheckedItems && mActionMode != null)

            mActionMode.finish();

        if (mActionMode != null)

            mActionMode.setTitle(dashboardListAdapter
                    .getSelectedCount() + " selected");

    }

    public static void deActivateUsers(Context context) {
        progressBar.setVisibility(View.VISIBLE);

        ArrayList<String> mylist = new ArrayList<String>();
        ArrayList<Integer> adapterNoList = new ArrayList<Integer>();
        SparseBooleanArray selected = dashboardListAdapter
                .getSelectedIds();

        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                int key = selected.keyAt(i);
                DashboardUserListInfo userListInfo = userDisplayList.get(key);
                mylist.add(userListInfo.getId());
                adapterNoList.add(key);

            }
        }
        deActivateMultipleUsers(mylist, adapterNoList, context);

        mActionMode.finish();

    }

    private static void deActivateMultipleUsers(ArrayList<String> mylist,
                                                final ArrayList<Integer> adapterNoList,
                                                final Context context) {
        final int size1 = mylist.size();
        Call<activaiton_result> call = RetrofitClient
                .getInstance()
                .getApi()
                .deactivateUsers(user_id, token_id, token_value, new String[]{String.valueOf(mylist)});

        call.enqueue(new Callback<activaiton_result>() {
            @Override
            public void onResponse(Call<activaiton_result> call, Response<activaiton_result> response) {
                if (response.body() != null && response.body().isResult()) {
                    Toast.makeText(context, "Deactivated " + size1 + " Users", Toast.LENGTH_SHORT).show();

                    removeMultipleRecords(adapterNoList);
                }
            }

            @Override
            public void onFailure(Call<activaiton_result> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private static void removeMultipleRecords(ArrayList<Integer> adapterListNo) {
        int size1 = adapterListNo.size() - 1;
        for (int i = 0; i <= size1; i++) {
            int pos = adapterListNo.get(i);
            userDisplayList.remove(pos);
        }
        dashboardListAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    public static void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

}
