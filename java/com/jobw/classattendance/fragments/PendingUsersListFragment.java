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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adivid.zpattendanceadmin.R;
import com.adivid.zpattendanceadmin.activities.UpdateUserProfileActivity;
import com.adivid.zpattendanceadmin.adapters.DashboardListAdapter;
import com.adivid.zpattendanceadmin.adapters.RecyclerView_Adapter;
import com.adivid.zpattendanceadmin.callback.Toolbar_ActionMode_Callback;
import com.adivid.zpattendanceadmin.listeners.RecyclerClick_Listener;
import com.adivid.zpattendanceadmin.listeners.RecyclerTouchListener;
import com.adivid.zpattendanceadmin.models.InactivateUserData;
import com.adivid.zpattendanceadmin.models.InactivateUserDialogModel;
import com.adivid.zpattendanceadmin.models.InactivateUserDiologInfo;
import com.adivid.zpattendanceadmin.models.InactivateUserListModel;
import com.adivid.zpattendanceadmin.models.activaiton_result;
import com.adivid.zpattendanceadmin.network.RetrofitClient;
import com.adivid.zpattendanceadmin.storage.SharedPrefManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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
public class PendingUsersListFragment extends Fragment implements DashboardListAdapter.OnDashboardListItemClickListener {

    private final String TAG = "PendingUsersList";
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View headerView;
    private SharedPreferences sharedPreferences, positionPrefs;
    private SharedPreferences.Editor editor, editorPosition;
    private SharedPrefManager prefManager;
    private static List<InactivateUserListModel> userTotalList, userDisplayList;

    private RecyclerView recyclerView;
    private static RecyclerView_Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private static ProgressBar progressBar;
    private ProgressBar endProgressBar, diaologProgressBar;
    private Dialog dialog, customImageDiaolog;

    private static ActionMode mActionMode;

    private boolean isScrolling, flag1;
    private int currentItems, scrolledOutItems, totalItems;

    private InactivateUserDiologInfo customDialogUserInfo;
    private View view;

    public PendingUsersListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pending_users_list, container, false);

        initView();
        setUpInacivateUserList();

        return view;
    }

    private void initView() {
        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        positionPrefs = getActivity().getSharedPreferences("adapterPosition", Context.MODE_PRIVATE);
        editorPosition = positionPrefs.edit();
        prefManager = new SharedPrefManager(getActivity());

        userDisplayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        setUpRecyclerView();

        progressBar = view.findViewById(R.id.progressBar);
        endProgressBar = view.findViewById(R.id.progressBarAtEnd);


    }

    private void setUpRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerView_Adapter(getActivity(), userDisplayList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                    adapter.notifyDataSetChanged();
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
        adapter.notifyDataSetChanged();

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


    private void showCustomImageDiaolog(int pos) {
        customImageDiaolog = new Dialog(getActivity());

        customImageDiaolog.setContentView(R.layout.image_custom_diaolog);
        diaologProgressBar = customImageDiaolog.findViewById(R.id.image_diolog_progressbar);
        ImageView imageDialogView = customImageDiaolog.findViewById(R.id.imgv_dialog_image);
        String imageStr = userDisplayList.get(pos).getId_proof();
        if (imageStr == null || imageStr.equals("")) {
            Toast.makeText(getActivity(), "Id Image Not Set", Toast.LENGTH_LONG).show();
        } else {
            customImageDiaolog.show();
            byte[] decodedString = Base64.decode(imageStr, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageDialogView.setImageBitmap(decodedByte);
        }


    }

    private void showUserInfoDialog(final int pos) {
        dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.custom_dialog);

        dialog.show();
        diaologProgressBar = dialog.findViewById(R.id.dialog_progress_bar);
        makeApiCall(pos);

        MaterialButton btnActivate = dialog.findViewById(R.id.btn_activate);
        MaterialButton btnCancel = dialog.findViewById(R.id.btn_cancel);
        MaterialButton btnEdit = dialog.findViewById(R.id.btn_edit);
        ImageView imgCancel = dialog.findViewById(R.id.imgv_cancel);

        btnActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> mylist = new ArrayList<String>();

                activateUsers(pos);

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateUserProfileActivity.class);
                intent.putExtra("usereditinfo", customDialogUserInfo);
                intent.putExtra("position", pos);
                startActivity(intent);

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void activateUsers(final int pos) {
        progressBar.setVisibility(View.VISIBLE);
        String user_activation_id = userDisplayList.get(pos).getId();
        ArrayList<String> mylist = new ArrayList<String>();
        mylist.add(user_activation_id);

        Call<activaiton_result> call = RetrofitClient
                .getInstance()
                .getApi()
                .activateUsers(user_id, token_id, token_value, new String[]{String.valueOf(mylist)});

        call.enqueue(new Callback<activaiton_result>() {
            @Override
            public void onResponse(Call<activaiton_result> call, Response<activaiton_result> response) {
                if (response.body() != null && response.body().isResult()) {
                    Toast.makeText(getActivity(), "Activated User!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    removeList(pos);
                }
            }

            @Override
            public void onFailure(Call<activaiton_result> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void removeList(int pos) {
        userDisplayList.remove(pos);
        adapter.notifyDataSetChanged();

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
                Log.d(TAG, "onFailure: " + t.getMessage());
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

    private void onListItemSelect(int position) {
        adapter.toggleSelection(position);//Toggle the selection
        boolean hasCheckedItems = adapter.getSelectedCount() > 0;
        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new
                    Toolbar_ActionMode_Callback(getActivity(), adapter,
                    userDisplayList, false));
        else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
            mActionMode.finish();

        if (mActionMode != null)
            //set action mode title on item selection
            mActionMode.setTitle(adapter
                    .getSelectedCount() + " selected");

    }


    private void setUpInacivateUserList() {
        progressBar.setVisibility(View.VISIBLE);


        Call<InactivateUserData> call = RetrofitClient
                .getInstance()
                .getApi()
                .getInactivateUsers(user_id, token_id, token_value);

        call.enqueue(new Callback<InactivateUserData>() {
            @Override
            public void onResponse(Call<InactivateUserData> call, Response<InactivateUserData> response) {
                userDisplayList.clear();
                userTotalList = response.body().getUsers();
                for (int i = 0; i < 13; i++) {
                    try {
                        if (userTotalList.get(i) != null) {
                            userDisplayList.add(userTotalList.get(i));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<InactivateUserData> call, Throwable t) {
                Toast.makeText(getActivity(), "Poor Internet Connection", Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.GONE);
            }
        });

    }


    //Set action mode null after use
    public static void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    public static void activateSelectedUsers(Context context) {
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<String> mylist = new ArrayList<String>();
        ArrayList<Integer> adapterNoList = new ArrayList<Integer>();
        SparseBooleanArray selected = adapter
                .getSelectedIds();//Get selected ids

        //Loop all selected ids
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                /*item_models.remove(selected.keyAt(i));*/
                int key = selected.keyAt(i);
                InactivateUserListModel userList = userDisplayList.get(key);
                mylist.add(userList.getId());
                adapterNoList.add(key);

            }
        }
        activateMultipleUsers(mylist, adapterNoList, context);
        /*Toast.makeText(MainActivity.this, selected.size() + " item deleted.", Toast.LENGTH_SHORT).show();//Show Toast*/
        mActionMode.finish();//Finish action mode after use

    }

    private static void activateMultipleUsers(final ArrayList<String> mylist,
                                              final ArrayList<Integer> adapterNoList,
                                              final Context context) {
        final int size1 = mylist.size();
        Call<activaiton_result> call = RetrofitClient
                .getInstance()
                .getApi()
                .activateUsers(user_id, token_id, token_value, new String[]{String.valueOf(mylist)});

        call.enqueue(new Callback<activaiton_result>() {
            @Override
            public void onResponse(Call<activaiton_result> call, Response<activaiton_result> response) {
                if (response.body() != null && response.body().isResult()) {
                    Toast.makeText(context, "Activated " + size1 + " Users", Toast.LENGTH_SHORT).show();

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
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }


    private void startActivateAllUsers() {
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<String> mylist = new ArrayList<String>();
        for (int i = 0; i < userDisplayList.size(); i++) {
            InactivateUserListModel userList = userDisplayList.get(i);
            mylist.add(userList.getId());
        }
        activateAllUsers(mylist);

    }

    private void activateAllUsers(ArrayList<String> mylist) {
        final int size1 = mylist.size();
        if (size1 == 0) {
            Toast.makeText(getActivity(), "No Records!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        } else {
            Call<activaiton_result> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .activateUsers(user_id, token_id, token_value, new String[]{String.valueOf(mylist)});

            call.enqueue(new Callback<activaiton_result>() {
                @Override
                public void onResponse(Call<activaiton_result> call, Response<activaiton_result> response) {
                    if (response.body() != null && response.body().isResult()) {
                        Toast.makeText(getActivity(), "Activated " + size1 + " Users", Toast.LENGTH_SHORT).show();
                        userDisplayList.clear();
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<activaiton_result> call, Throwable t) {

                    progressBar.setVisibility(View.GONE);
                }
            });
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_refresh:
                setUpInacivateUserList();
                break;
            case R.id.item_activate_all:
                startActivateAllUsers();
                break;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        int p = positionPrefs.getInt("position", -1);
        if (p != -1) {
            makeApiCall(p);
            setUpInacivateUserList();
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
}
