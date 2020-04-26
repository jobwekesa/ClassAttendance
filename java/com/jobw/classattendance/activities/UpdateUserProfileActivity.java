package com.adivid.zpattendanceadmin.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.adivid.zpattendanceadmin.R;
import com.adivid.zpattendanceadmin.model_dropdown.BlockInfo;
import com.adivid.zpattendanceadmin.model_dropdown.BlockModel;
import com.adivid.zpattendanceadmin.model_dropdown.DepartmentInfo;
import com.adivid.zpattendanceadmin.model_dropdown.DepartmentNOfficeModel;
import com.adivid.zpattendanceadmin.model_dropdown.DesignationInfo;
import com.adivid.zpattendanceadmin.model_dropdown.DesignationModel;
import com.adivid.zpattendanceadmin.model_dropdown.OfficeNameInfo;
import com.adivid.zpattendanceadmin.model_dropdown.OfficeNameModel;
import com.adivid.zpattendanceadmin.model_dropdown.OfficeTypeInfo;
import com.adivid.zpattendanceadmin.models.InactivateUserDiologInfo;
import com.adivid.zpattendanceadmin.models.UpdateUserInfo;
import com.adivid.zpattendanceadmin.models_two.SecurityKeyResult;
import com.adivid.zpattendanceadmin.network.RetrofitClient;
import com.adivid.zpattendanceadmin.network.RetrofitClient2;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.adivid.zpattendanceadmin.activities.DashboardViewActivity.token_id;
import static com.adivid.zpattendanceadmin.activities.DashboardViewActivity.token_value;
import static com.adivid.zpattendanceadmin.activities.DashboardViewActivity.user_id;

public class UpdateUserProfileActivity extends AppCompatActivity {

    private static String TAG = "UpdateUserProfile";
    private EditText edt_name, edt_email, edt_mobile, edt_birth_date,
            edt_sevarth_no, edt_password, edt_c_password;
    private MaterialButton btnUpdate;
    private String name, email, mobile, birth_date, password, c_password, sevarth_no, user_update_id;
    private int pos;
    private InactivateUserDiologInfo userInformation;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private AppCompatSpinner spinnerBlockType, spinnerParentDpt, spinnerWorkingDpt,
            spinnerOfficeType, spinnerOfficeName, spinnerDesignation;
    private List<String> listBlockType, listParentDpt, listWorkingDpt,
            listOfficeType, listOfficeName, listDesignation;

    private ArrayList<HashMap<String, String>> arrayListBlockTypeInfo, arrayListParentDpt, arrayListWorkingDpt, arrayListOfficeType, arrayListOfficeName, arrayListDesignation;

    private ArrayAdapter<String> dataAdapter, dataAdapter1, dataAdapter2, dataAdapter3, dataAdapterOfficeName;
    private String workingDptId, officeTypeId, keyIdDefault, keyIdSelected, workingDptNewId, officeTypeNewId;

    private String block_name, department, designation, office_type, office_name;
    private int flag = 0, flag1 = 0, flag2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        setUpData();
        try {
            setUpAdapters();
            setBlockSpinnerList();
            setUpDesignationList();

        } catch (Exception e) {
            e.printStackTrace();
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInformation();
            }
        });
        /*progressBar.setVisibility(View.GONE);*/
        validateSecurityKey();

    }


    private void initView() {

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        linearLayout = findViewById(R.id.linearLayout);
        userInformation = (InactivateUserDiologInfo) getIntent().getSerializableExtra("usereditinfo");
        pos = getIntent().getIntExtra("position", 9999);
        preferences = getSharedPreferences("adapterPosition", Context.MODE_PRIVATE);
        editor = preferences.edit();
        user_update_id = userInformation.getId();
        Log.d(TAG, "initView: " + user_update_id);
        btnUpdate = findViewById(R.id.btn_update);
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_birth_date = findViewById(R.id.edt_birth_date);
        edt_sevarth_no = findViewById(R.id.edt_sevarth_no);
        edt_password = findViewById(R.id.edt_password);
        edt_c_password = findViewById(R.id.edt_c_password);

        init2();
    }

    private void init2() {
        spinnerBlockType = findViewById(R.id.spinner_block_type);
        spinnerDesignation = findViewById(R.id.spinner_designation);
        spinnerOfficeName = findViewById(R.id.spinner_office_name);
        spinnerOfficeType = findViewById(R.id.spinner_office_type);
        spinnerWorkingDpt = findViewById(R.id.spinner_working_dpt);
        listBlockType = new ArrayList<>();
        listWorkingDpt = new ArrayList<>();
        listOfficeType = new ArrayList<>();
        listOfficeName = new ArrayList<>();
        listDesignation = new ArrayList<>();
        arrayListBlockTypeInfo = new ArrayList<>();
        arrayListParentDpt = new ArrayList<>();
        arrayListWorkingDpt = new ArrayList<>();
        arrayListOfficeType = new ArrayList<>();
        arrayListOfficeName = new ArrayList<>();
        arrayListDesignation = new ArrayList<>();

        init3();

    }

    private void init3() {
        if (userInformation.getBlock_name() != null && !userInformation.getBlock_name().equals("")) {
            block_name = userInformation.getBlock_name();
        } else {
            block_name = "Select Block Type";
            listWorkingDpt.add("Select Working Department");
            listOfficeType.add("Select Office Type");
            listOfficeName.add("Select Office Name");
            spinnerWorkingDpt.setEnabled(false);
            spinnerOfficeType.setEnabled(false);
            spinnerOfficeName.setEnabled(false);
        }

        if (userInformation.getDepartment() != null && !userInformation.getDepartment().equals("")) {
            department = userInformation.getDepartment();
        } else {
            department = "Select Working Department";
        }

        if (userInformation.getDesignation() != null && !userInformation.getDesignation().equals("")) {
            designation = userInformation.getDesignation();
        } else {
            designation = "Select Designation";
        }
        if (userInformation.getOffice_type() != null && !userInformation.getOffice_type().equals("")) {
            office_type = userInformation.getOffice_type();
        } else {
            office_type = "Select Office Type";
        }
        if (userInformation.getOffice_name() != null && !userInformation.getOffice_name().equals("")) {
            office_name = userInformation.getOffice_name();
        } else {
            office_name = "Select Office Name";
        }
    }

    private void setUpData() {
        edt_name.setText(userInformation.getFullName());
        edt_email.setText(userInformation.getEmail());
        edt_mobile.setText(userInformation.getMobile());
        edt_birth_date.setText(userInformation.getBirth_date());
        edt_sevarth_no.setText(userInformation.getSevarth_number());
        edt_password.setText("");
        edt_c_password.setText("");
        /*progressBar.setVisibility(View.GONE);*/
    }

    private void setUpAdapters() {
        dataAdapter = new ArrayAdapter<String>(UpdateUserProfileActivity.this,
                android.R.layout.simple_spinner_item, listBlockType);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBlockType.setAdapter(dataAdapter);
        spinnerBlockType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = spinnerBlockType.getSelectedItem().toString();
                if (selectedItem.equals("Select Block Type")) {
                    listWorkingDpt.clear();
                    listOfficeType.clear();
                    listOfficeName.clear();
                    listWorkingDpt.add("Select Working Department");
                    listOfficeType.add("Select Office Type");
                    listOfficeName.add("Select Office Name");
                    dataAdapter2.notifyDataSetChanged();
                    dataAdapter3.notifyDataSetChanged();
                    dataAdapterOfficeName.notifyDataSetChanged();
                    flag++;
                } else {
                    if (flag != 0) {
                        spinnerWorkingDpt.setEnabled(true);
                        spinnerOfficeType.setEnabled(true);
                        spinnerOfficeName.setEnabled(true);
                        listOfficeName.clear();
                        listOfficeName.add("Select Office Name");
                        dataAdapterOfficeName.notifyDataSetChanged();
                        keyIdSelected = getKeyId(selectedItem, arrayListBlockTypeInfo);
                        setUpNewDepartmentAndOfficeTypeList(keyIdSelected);
                    }
                    flag++;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dataAdapter2 = new ArrayAdapter<String>(UpdateUserProfileActivity.this,
                android.R.layout.simple_spinner_item, listWorkingDpt);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWorkingDpt.setAdapter(dataAdapter2);

        spinnerWorkingDpt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = spinnerWorkingDpt.getSelectedItem().toString();
                workingDptNewId = getKeyId(selectedItem, arrayListWorkingDpt);
                if (flag1 != 0) {
                    if (workingDptNewId != null && officeTypeNewId != null) {
                        setUpNewOfficeNameList(workingDptNewId, officeTypeNewId);
                    }
                }
                flag1++;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dataAdapter3 = new ArrayAdapter<String>(UpdateUserProfileActivity.this,
                android.R.layout.simple_spinner_item, listOfficeType);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOfficeType.setAdapter(dataAdapter3);
        spinnerOfficeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedItem = spinnerOfficeType.getSelectedItem().toString();
                officeTypeNewId = getKeyId(selectedItem, arrayListOfficeType);
                if (flag2 != 0) {
                    if (workingDptNewId != null && officeTypeNewId != null) {
                        setUpNewOfficeNameList(workingDptNewId, officeTypeNewId);
                    }
                }
                flag2++;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dataAdapterOfficeName = new ArrayAdapter<String>(UpdateUserProfileActivity.this,
                android.R.layout.simple_spinner_item, listOfficeName);
        dataAdapterOfficeName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOfficeName.setAdapter(dataAdapterOfficeName);
    }

    private void setBlockSpinnerList() {
        progressBar.setVisibility(View.VISIBLE);
        Call<BlockModel> call = RetrofitClient2
                .getInstance()
                .getApiTwo()
                .getBlockType();
        call.enqueue(new Callback<BlockModel>() {
            @Override
            public void onResponse(Call<BlockModel> call, Response<BlockModel> response) {

                listBlockType.add(block_name);
                if (response.body() != null) {
                    List<BlockInfo> list = response.body().getBlockInfo();
                    HashMap<String, String> map = new HashMap<>();
                    for (int i = 0; i < list.size(); i++) {
                        if (block_name.equals(list.get(i).getBlockName())) {
                            keyIdDefault = list.get(i).getId();
                            keyIdSelected = keyIdDefault;
                            map.put(list.get(i).getId(), list.get(i).getBlockName());
                        } else {
                            map.put(list.get(i).getId(), list.get(i).getBlockName());
                            listBlockType.add(list.get(i).getBlockName());
                        }
                    }
                    arrayListBlockTypeInfo.add(map);
                    dataAdapter.notifyDataSetChanged();
                    setUpDepartmentAndOfficeTypeList(keyIdDefault);

                }

            }

            @Override
            public void onFailure(Call<BlockModel> call, Throwable t) {
                Toast.makeText(UpdateUserProfileActivity.this, "Poor Internet Connection1", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void setUpDepartmentAndOfficeTypeList(String keyId) {
        /*progressBar.setVisibility(View.VISIBLE);*/

        Call<DepartmentNOfficeModel> call = RetrofitClient2
                .getInstance()
                .getApiTwo()
                .getDepartmentOfficeType(keyId);

        call.enqueue(new Callback<DepartmentNOfficeModel>() {
            @Override
            public void onResponse(Call<DepartmentNOfficeModel> call, Response<DepartmentNOfficeModel> response) {

                listWorkingDpt.clear();
                listOfficeType.clear();
                arrayListWorkingDpt.clear();
                arrayListOfficeType.clear();
                listWorkingDpt.add(department);
                listOfficeType.add(office_type);
                List<DepartmentInfo> list = response.body().getDepartment();
                List<OfficeTypeInfo> list2 = response.body().getOfficeType();
                HashMap<String, String> map = new HashMap<>();
                HashMap<String, String> map2 = new HashMap<>();
                if (response.body() != null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (department.equals(list.get(i).getDepartment())) {
                            workingDptId = list.get(i).getIdDepartment();
                            map.put(list.get(i).getIdDepartment(), list.get(i).getDepartment());
                        } else {
                            map.put(list.get(i).getIdDepartment(), list.get(i).getDepartment());
                            listWorkingDpt.add(list.get(i).getDepartment());

                        }
                    }
                    for (int i = 0; i < list2.size(); i++) {
                        if (office_type.equals(list2.get(i).getOfficeType())) {
                            officeTypeId = list2.get(i).getIdOfficeType();
                            map2.put(list2.get(i).getIdOfficeType(), list2.get(i).getOfficeType());

                        } else {
                            map2.put(list2.get(i).getIdOfficeType(), list2.get(i).getOfficeType());
                            listOfficeType.add(list2.get(i).getOfficeType());
                        }
                    }
                    arrayListWorkingDpt.add(map);
                    arrayListOfficeType.add(map2);

                }
                dataAdapter2.notifyDataSetChanged();
                dataAdapter3.notifyDataSetChanged();
                setUpOfficeNameList(workingDptId, officeTypeId);
                /*progressBar.setVisibility(View.GONE);*/

            }

            @Override
            public void onFailure(Call<DepartmentNOfficeModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void setUpNewDepartmentAndOfficeTypeList(String keyIdSelected) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DepartmentNOfficeModel> call = RetrofitClient2
                .getInstance()
                .getApiTwo()
                .getDepartmentOfficeType(keyIdSelected);

        call.enqueue(new Callback<DepartmentNOfficeModel>() {
            @Override
            public void onResponse(Call<DepartmentNOfficeModel> call, Response<DepartmentNOfficeModel> response) {

                try {
                    listWorkingDpt.clear();
                    listOfficeType.clear();
                    listWorkingDpt.add("Select Working Department");
                    listOfficeType.add("Select Office Type");
                    arrayListWorkingDpt.clear();
                    arrayListOfficeType.clear();
                    List<DepartmentInfo> list = response.body().getDepartment();
                    List<OfficeTypeInfo> list2 = response.body().getOfficeType();
                    HashMap<String, String> map = new HashMap<>();
                    HashMap<String, String> map2 = new HashMap<>();
                    if (response.body() != null) {
                        for (int i = 0; i < list.size(); i++) {
                            map.put(list.get(i).getIdDepartment(), list.get(i).getDepartment());
                            listWorkingDpt.add(list.get(i).getDepartment());

                        }
                        for (int i = 0; i < list2.size(); i++) {
                            map2.put(list2.get(i).getIdOfficeType(), list2.get(i).getOfficeType());
                            listOfficeType.add(list2.get(i).getOfficeType());

                        }
                        arrayListWorkingDpt.add(map);
                        arrayListOfficeType.add(map2);

                    }
                    spinnerWorkingDpt.setSelection(0);
                    spinnerOfficeType.setSelection(0);
                    dataAdapter2.notifyDataSetChanged();
                    dataAdapter3.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DepartmentNOfficeModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }


    private void setUpOfficeNameList(String workingDptId, String officeTypeId) {
        /*progressBar.setVisibility(View.VISIBLE);*/

        Call<OfficeNameModel> call = RetrofitClient2
                .getInstance()
                .getApiTwo()
                .getOfficeName(keyIdDefault, workingDptId, officeTypeId);

        call.enqueue(new Callback<OfficeNameModel>() {
            @Override
            public void onResponse(Call<OfficeNameModel> call, Response<OfficeNameModel> response) {
                listOfficeName.clear();
                arrayListOfficeName.clear();
                listOfficeName.add(office_name);

                if (response.body() != null) {
                    List<OfficeNameInfo> list = response.body().getOfficeNameInfo();
                    HashMap<String, String> map = new HashMap<>();
                    for (int i = 0; i < list.size(); i++) {
                        if (office_name.equals(list.get(i).getOfficeName())) {
                            map.put(list.get(i).getIdOfficename(), list.get(i).getOfficeName());
                        } else {
                            map.put(list.get(i).getIdOfficename(), list.get(i).getOfficeName());
                            listOfficeName.add(list.get(i).getOfficeName());

                        }
                    }
                    arrayListOfficeName.add(map);

                }
                dataAdapterOfficeName.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<OfficeNameModel> call, Throwable t) {
                Toast.makeText(UpdateUserProfileActivity.this, "Poor Internet Connection2", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void setUpNewOfficeNameList(String workingDptNewId, String officeTypeNewId) {
        progressBar.setVisibility(View.VISIBLE);

        Call<OfficeNameModel> call = RetrofitClient2
                .getInstance()
                .getApiTwo()
                .getOfficeName(keyIdSelected, workingDptNewId, officeTypeNewId);

        call.enqueue(new Callback<OfficeNameModel>() {
            @Override
            public void onResponse(Call<OfficeNameModel> call, Response<OfficeNameModel> response) {
                listOfficeName.clear();
                arrayListOfficeName.clear();
                listOfficeName.add("Select Office Name");
                if (response.body() != null) {
                    List<OfficeNameInfo> list = response.body().getOfficeNameInfo();
                    HashMap<String, String> map = new HashMap<>();
                    for (int i = 0; i < list.size(); i++) {
                        map.put(list.get(i).getIdOfficename(), list.get(i).getOfficeName());
                        listOfficeName.add(list.get(i).getOfficeName());

                    }
                    arrayListOfficeName.add(map);

                }
                dataAdapterOfficeName.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<OfficeNameModel> call, Throwable t) {
                /*Toast.makeText(UpdateUserProfileActivity.this, "Poor Internet Connection3", Toast.LENGTH_SHORT).show();*/
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void setUpDesignationList() {
        progressBar.setVisibility(View.VISIBLE);
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UpdateUserProfileActivity.this,
                android.R.layout.simple_spinner_item, listDesignation);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDesignation.setAdapter(dataAdapter);

        spinnerDesignation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Call<DesignationModel> call2 = RetrofitClient2
                .getInstance()
                .getApiTwo()
                .getDesignation();

        call2.enqueue(new Callback<DesignationModel>() {
            @Override
            public void onResponse(Call<DesignationModel> call, Response<DesignationModel> response) {
                listDesignation.add(designation);
                if (response.body() != null) {
                    List<DesignationInfo> list = response.body().getDesignationInfo();
                    HashMap<String, String> map = new HashMap<>();
                    for (int i = 0; i < list.size(); i++) {
                        if (designation.equals(list.get(i).getDesignation())) {
                            map.put(list.get(i).getDesignationId(), list.get(i).getDesignation());
                        } else {
                            map.put(list.get(i).getDesignationId(), list.get(i).getDesignation());
                            listDesignation.add(list.get(i).getDesignation());
                        }
                    }
                    arrayListDesignation.add(map);
                }
                dataAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DesignationModel> call, Throwable t) {
                Toast.makeText(UpdateUserProfileActivity.this,
                        "Poor Internet Connection4", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void updateUserInformation() {

        progressBar.setVisibility(View.VISIBLE);
        name = edt_name.getText().toString();
        email = edt_email.getText().toString();
        mobile = edt_mobile.getText().toString();
        birth_date = edt_birth_date.getText().toString();
        sevarth_no = edt_sevarth_no.getText().toString();
        password = edt_password.getText().toString();
        c_password = edt_c_password.getText().toString();

        if (validateUserInfo() && validateDropDowns()) {

            String block_id, department_id, office_type_id, office_name_id, office_designation_id;
            block_id = getKeyId(spinnerBlockType.getSelectedItem().toString(), arrayListBlockTypeInfo);
            department_id = getKeyId(spinnerWorkingDpt.getSelectedItem().toString(), arrayListWorkingDpt);
            office_type_id = getKeyId(spinnerOfficeType.getSelectedItem().toString(), arrayListOfficeType);
            office_name_id = getKeyId(spinnerOfficeName.getSelectedItem().toString(), arrayListOfficeName);
            office_designation_id = getKeyId(spinnerDesignation.getSelectedItem().toString(), arrayListDesignation);

            Call<UpdateUserInfo> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .updateUser(user_update_id, name, email, birth_date, sevarth_no, mobile, password,
                            user_id, token_id, token_value, block_id, department_id, office_type_id,
                            office_name_id, office_designation_id);

            call.enqueue(new Callback<UpdateUserInfo>() {
                @Override
                public void onResponse(Call<UpdateUserInfo> call, Response<UpdateUserInfo> response) {
                    Toast.makeText(UpdateUserProfileActivity.this, "User Updated Successfully", Toast.LENGTH_SHORT).show();
                    editor.putInt("position", pos);
                    editor.commit();
                    progressBar.setVisibility(View.GONE);
                    finish();
                }

                @Override
                public void onFailure(Call<UpdateUserInfo> call, Throwable t) {
                    Toast.makeText(UpdateUserProfileActivity.this, "Poor Internet Connection5", Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.GONE);
                }
            });

        } else {
            progressBar.setVisibility(View.GONE);
        }


    }

    private boolean validateUserInfo() {
        if (name.isEmpty()) {
            edt_name.setError("Name is Required!");
            edt_name.requestFocus();
            return false;
        } else if (email.isEmpty()) {
            edt_email.setError("Email is Required!");
            edt_email.requestFocus();
            return false;
        } else if (mobile.isEmpty()) {
            edt_mobile.setError("Mobile No. is Required!");
            edt_mobile.requestFocus();
            return false;
        } else if (birth_date.isEmpty()) {
            edt_birth_date.setError("Birth Date is Required!");
            edt_birth_date.requestFocus();
            return false;
        } else if (!password.isEmpty()) {
            if (!password.equals(c_password)) {
                edt_c_password.setError("Passwords do not match!");
                edt_c_password.requestFocus();
                return false;
            }
        }
        return true;
    }

    private boolean validateDropDowns() {
        if (spinnerBlockType.getSelectedItem().toString().equals("Select Block Type")) {
            Toast.makeText(this, "Select Block Type", Toast.LENGTH_SHORT).show();
            return false;
        } else if (spinnerWorkingDpt.getSelectedItem().toString().equals("Select Working Department")) {
            Toast.makeText(this, "Select Working Department", Toast.LENGTH_SHORT).show();
            return false;
        } else if (spinnerOfficeType.getSelectedItem().toString().equals("Select Office Type")) {
            Toast.makeText(this, "Select Office Type", Toast.LENGTH_SHORT).show();
            return false;
        } else if (spinnerOfficeName.getSelectedItem().toString().equals("Select Office Name")) {
            Toast.makeText(this, "Select Office Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (spinnerDesignation.getSelectedItem().toString().equals("Select Designation")) {
            Toast.makeText(this, "Select Designation", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void validateSecurityKey() {
        linearLayout.setVisibility(View.GONE);
        View view = getLayoutInflater().inflate(R.layout.dialog_security_key, null);
        final EditText editText = view.findViewById(R.id.categoryEditText);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("Enter Security Key");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String securityKey = editText.getText().toString();
                makeSecurityKeyApiCall(securityKey);
            }

        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                finish();
            }
        });

        builder.show();



    }

    private void makeSecurityKeyApiCall(String securityKey) {
        progressBar.setVisibility(View.VISIBLE);

        Call<SecurityKeyResult> call = RetrofitClient
                .getInstance()
                .getApi()
                .validateSecurityKey(user_id, token_id, token_value, user_update_id, securityKey);

        call.enqueue(new Callback<SecurityKeyResult>() {
            @Override
            public void onResponse(Call<SecurityKeyResult> call, Response<SecurityKeyResult> response) {
                boolean result = response.body().isResult();
                if (result) {
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    String message = response.body().getMessage();
                    Toast.makeText(UpdateUserProfileActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SecurityKeyResult> call, Throwable t) {
                Toast.makeText(UpdateUserProfileActivity.this, "Poor Internet Connection",
                        Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private String getKeyId(String strName, ArrayList<HashMap<String, String>> arrayList) {

        for (HashMap<String, String> map : arrayList) {
            for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                String key = mapEntry.getKey();
                String value = mapEntry.getValue();
                if (value.equals(strName)) {
                    return key;
                }
            }
        }
        return "-1";
    }


}
