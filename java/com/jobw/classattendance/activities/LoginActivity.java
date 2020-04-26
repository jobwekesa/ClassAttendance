package com.adivid.zpattendanceadmin.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.adivid.zpattendanceadmin.MyFirebaseMessagingService;
import com.adivid.zpattendanceadmin.R;
import com.adivid.zpattendanceadmin.models.UserData1;
import com.adivid.zpattendanceadmin.models.UserInfo;
import com.adivid.zpattendanceadmin.network.RetrofitClient;
import com.adivid.zpattendanceadmin.storage.SharedPrefManager;
import com.google.firebase.FirebaseApp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.adivid.zpattendanceadmin.storage.SharedPrefManager.SHARED_PREF_NAME;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private SharedPrefManager sharedPrefManager;
    private Button btnLogin;
    private String JSON_URL = "https://nashikzp.thethirdeyeindia.com/api/admin_app/session/login.php";
    public static String deviceid, getappversion = "", osversion, company, model, fcm_id;
    private String mobile, password;
    private EditText edt_mobile, edt_password;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initView();

    }

    private void setData() {
        try {
            FirebaseApp.initializeApp(this);
            Intent i = new Intent(this, MyFirebaseMessagingService.class);
            startService(i);
        } catch (Exception ignored) {
        }
        deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            getappversion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        fcm_id = pref.getString("FCM", " ");
        osversion = Build.VERSION.RELEASE;
        company = Build.MANUFACTURER;
        model = Build.MODEL;

    }

    private void initView() {
        preferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

        sharedPrefManager = new SharedPrefManager(this);

        btnLogin = findViewById(R.id.btn_login);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_password = findViewById(R.id.edt_password);
        progressBar = findViewById(R.id.progressBar);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (!isNetworkAvailable()) {
                    Toast.makeText(LoginActivity.this, "Please check your internet connection and Try again!", Toast.LENGTH_SHORT).show();
                } else {
                    if (validateUser()) {
                        loginUser();
                    }
                }

                break;
        }
    }

    private boolean validateUser() {
        mobile = edt_mobile.getText().toString();
        password = edt_password.getText().toString();

        if (mobile.isEmpty()) {
            edt_mobile.setError("Enter Mobile Number!");
            edt_mobile.requestFocus();
            return false;
        } else if (mobile.length() < 10) {
            edt_mobile.setError("Mobile No.should have 10 digits");
            edt_mobile.requestFocus();
            return false;
        } else if (password.isEmpty()) {
            edt_password.setError("Enter Password!");
            edt_password.requestFocus();
            return false;
        }
        return true;
    }

    private void loginUser() {
        progressBar.setVisibility(View.VISIBLE);
        setData();
        Call<UserData1> call = RetrofitClient
                .getInstance()
                .getApi()
                .loginUser(mobile, password, deviceid, fcm_id);


        call.enqueue(new Callback<UserData1>() {
            @Override
            public void onResponse(Call<UserData1> call, Response<UserData1> response) {
                UserData1 userData1 = response.body();
                UserInfo userInfo = response.body().getData();
                if (userData1.getLogin()) {
                    sharedPrefManager.saveUser(userInfo);
                    Intent intent = new Intent(LoginActivity.this, DashboardViewActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UserData1> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Poor Internet Connection", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPrefManager.isLoggedIn()) {
            finish();
            startActivity(new Intent(LoginActivity.this, DashboardViewActivity.class));

        } else {
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
