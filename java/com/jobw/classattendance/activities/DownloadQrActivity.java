package com.adivid.zpattendanceadmin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adivid.zpattendanceadmin.R;
import com.adivid.zpattendanceadmin.model_dropdown.BlockInfo;
import com.adivid.zpattendanceadmin.model_dropdown.BlockModel;
import com.adivid.zpattendanceadmin.model_dropdown.DepartmentInfo;
import com.adivid.zpattendanceadmin.model_dropdown.DepartmentNOfficeModel;
import com.adivid.zpattendanceadmin.model_dropdown.OfficeNameInfo;
import com.adivid.zpattendanceadmin.model_dropdown.OfficeNameModel;
import com.adivid.zpattendanceadmin.model_dropdown.OfficeTypeInfo;
import com.adivid.zpattendanceadmin.network.DownloadTask;
import com.adivid.zpattendanceadmin.network.RetrofitClient2;

import org.json.JSONException;
import org.json.JSONObject;

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

public class DownloadQrActivity extends AppCompatActivity {
    private static final String TAG = "DownloadQrActivity";
    private Button btnDownload;
    private ProgressBar progressBar;
    private String downloadDocUrl = "https://nashikzp.thethirdeyeindia.com/api/admin_app/qr/download_qr.pdf.php";

    private AppCompatSpinner spinnerBlockType, spinnerParentDpt, spinnerWorkingDpt,
            spinnerOfficeType, spinnerOfficeName, spinnerDesignation;
    private List<String> listBlockType, listParentDpt, listWorkingDpt,
            listOfficeType, listOfficeName, listDesignation;

    private ArrayList<HashMap<String, String>> arrayListBlockTypeInfo, arrayListParentDpt, arrayListWorkingDpt, arrayListOfficeType, arrayListOfficeName, arrayListDesignation;

    private ArrayAdapter<String> dataAdapter, dataAdapter1, dataAdapter2, dataAdapter3, dataAdapterOfficeName;
    private String keyIdSelected, workingDptNewId, officeTypeNewId, officeNameId;

    private int flag = 0, flag1 = 0, flag2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_qr);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        setUpAdapters();
        setBlockSpinnerList();

    }


    private void init() {
        btnDownload = findViewById(R.id.btn_download);
        progressBar = findViewById(R.id.progressBar);

        spinnerBlockType = findViewById(R.id.spinner_block_type);
        spinnerOfficeName = findViewById(R.id.spinner_office_name);
        spinnerOfficeType = findViewById(R.id.spinner_office_type);
        spinnerWorkingDpt = findViewById(R.id.spinner_working_dpt);
        listBlockType = new ArrayList<>();
        listWorkingDpt = new ArrayList<>();
        listOfficeType = new ArrayList<>();
        listOfficeName = new ArrayList<>();
        arrayListBlockTypeInfo = new ArrayList<>();
        arrayListParentDpt = new ArrayList<>();
        arrayListWorkingDpt = new ArrayList<>();
        arrayListOfficeType = new ArrayList<>();
        arrayListOfficeName = new ArrayList<>();

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPdfFile();
            }
        });

        init2();
    }

    private void init2() {
        listBlockType.add("Select Block Type");
        listWorkingDpt.add("Select Working Department");
        listOfficeType.add("Select Office Type");
        listOfficeName.add("Select Office Name");
        spinnerWorkingDpt.setEnabled(false);
        spinnerOfficeType.setEnabled(false);
        spinnerOfficeName.setEnabled(false);
    }


    private void setUpAdapters() {
        dataAdapter = new ArrayAdapter<String>(DownloadQrActivity.this,
                android.R.layout.simple_spinner_item, listBlockType);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBlockType.setAdapter(dataAdapter);
        spinnerBlockType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = spinnerBlockType.getSelectedItem().toString();
                keyIdSelected = getKeyId(selectedItem, arrayListBlockTypeInfo);
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
                    btnDownload.setText("Download QR Code");
                } else {
                    spinnerWorkingDpt.setEnabled(true);
                    spinnerOfficeType.setEnabled(true);
                    spinnerOfficeName.setEnabled(true);
                    listOfficeName.clear();
                    listOfficeName.add("Select Office Name");
                    dataAdapterOfficeName.notifyDataSetChanged();
                    keyIdSelected = getKeyId(selectedItem, arrayListBlockTypeInfo);
                    setUpNewDepartmentAndOfficeTypeList(keyIdSelected);
                    btnDownload.setText("Download QR Code");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dataAdapter2 = new ArrayAdapter<String>(DownloadQrActivity.this,
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
                        btnDownload.setText("Download QR Code");
                    }
                }
                flag1++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dataAdapter3 = new ArrayAdapter<String>(DownloadQrActivity.this,
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
                        btnDownload.setText("Download QR Code");
                    }
                }
                flag2++;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dataAdapterOfficeName = new ArrayAdapter<String>(DownloadQrActivity.this,
                android.R.layout.simple_spinner_item, listOfficeName);
        dataAdapterOfficeName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOfficeName.setAdapter(dataAdapterOfficeName);
        spinnerOfficeName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = spinnerOfficeName.getSelectedItem().toString();
                officeNameId = getKeyId(selectedItem, arrayListOfficeName);
                btnDownload.setText("Download QR Code");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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

                if (response.body() != null) {
                    List<BlockInfo> list = response.body().getBlockInfo();
                    HashMap<String, String> map = new HashMap<>();
                    for (int i = 0; i < list.size(); i++) {
                        map.put(list.get(i).getId(), list.get(i).getBlockName());
                        listBlockType.add(list.get(i).getBlockName());

                    }
                    arrayListBlockTypeInfo.add(map);
                    dataAdapter.notifyDataSetChanged();

                }
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<BlockModel> call, Throwable t) {
                Toast.makeText(DownloadQrActivity.this, "Poor Internet Connection1", Toast.LENGTH_SHORT).show();
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

                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DepartmentNOfficeModel> call, Throwable t) {
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
                    if (list.size() > 1) {
                        listOfficeName.add("All");
                    }
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
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void downloadPdfFile() {
        if (ContextCompat.checkSelfPermission(DownloadQrActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission(DownloadQrActivity.this);

        } else {
            if (validateDropDowns()) {
                new DownloadTask(DownloadQrActivity.this, btnDownload,
                        downloadDocUrl, createJsonObject(),
                        spinnerBlockType.getSelectedItem().toString(),
                        spinnerOfficeType.getSelectedItem().toString(),
                        spinnerOfficeName.getSelectedItem().toString());
            }
        }
    }

    private JSONObject createJsonObject() {
        JSONObject postDataParams = new JSONObject();
        try {
            postDataParams.put("block", keyIdSelected);
            postDataParams.put("department", workingDptNewId);
            postDataParams.put("office_type", officeTypeNewId);
            postDataParams.put("office_name", officeNameId);
            postDataParams.put("user_id", user_id);
            postDataParams.put("token_id", token_id);
            postDataParams.put("token_value", token_value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postDataParams;
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
        } else if (spinnerOfficeName.getSelectedItem().toString().equals("All")) {
            officeNameId = "0";
        }

        return true;
    }

    public static void requestStoragePermission(final Context context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);

        } else {
            // permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (validateDropDowns()) {
                        new DownloadTask(DownloadQrActivity.this, btnDownload,
                                downloadDocUrl, createJsonObject(),
                                spinnerBlockType.getSelectedItem().toString(),
                                spinnerOfficeType.getSelectedItem().toString(),
                                spinnerOfficeName.getSelectedItem().toString());
                    }
                }
                break;
        }
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
