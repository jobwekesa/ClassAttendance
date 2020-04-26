package com.adivid.zpattendanceadmin.activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.adivid.zpattendanceadmin.R;
import com.adivid.zpattendanceadmin.adapters.SliderAdapter;
import com.adivid.zpattendanceadmin.models.UserData1;
import com.adivid.zpattendanceadmin.models.activaiton_result;
import com.adivid.zpattendanceadmin.models_two.DeleteImages;
import com.adivid.zpattendanceadmin.models_two.SecurityKeyResult;
import com.adivid.zpattendanceadmin.models_two.UserImages;
import com.adivid.zpattendanceadmin.models_two.UserProfileImagesModel;
import com.adivid.zpattendanceadmin.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.adivid.zpattendanceadmin.activities.DashboardViewActivity.token_id;
import static com.adivid.zpattendanceadmin.activities.DashboardViewActivity.token_value;
import static com.adivid.zpattendanceadmin.activities.DashboardViewActivity.user_id;

public class ProfileImagesActivity extends AppCompatActivity {
    private static final String TAG = "ProfileImages";
    private SpringDotsIndicator springDotsIndicator;
    private Toolbar toolbar;
    private String user_profile_id;
    private List<UserImages> userImages;
    private SliderAdapter adapter;
    private Bitmap[] arrayOfBitmap;
    private ProgressBar progressBar;
    private ArrayList<Bitmap> bitmapArray;
    private MaterialButton btnDelete, btnBack;
    private String deleteImagesResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_images);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        init();

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateSecurityKey();


            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void init() {
        bitmapArray = new ArrayList<>();

        user_profile_id = getIntent().getStringExtra("user_profile_id");
        btnDelete = findViewById(R.id.btn_delete);
        btnBack = findViewById(R.id.btn_back);
        arrayOfBitmap = new Bitmap[5];
        progressBar = findViewById(R.id.progressBar);
        getProfileImages();
        springDotsIndicator = (SpringDotsIndicator) findViewById(R.id.spring_dots_indicator);
        ViewPager viewPager = findViewById(R.id.viewPager);
        adapter = new SliderAdapter(ProfileImagesActivity.this, bitmapArray);
        viewPager.setAdapter(adapter);
        springDotsIndicator.setViewPager(viewPager);
    }

    private void validateSecurityKey() {
        View view = getLayoutInflater().inflate(R.layout.dialog_security_key, null);
        final EditText editText = view.findViewById(R.id.categoryEditText);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("Enter Security Key");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String securityKey = editText.getText().toString();
                deleteImageApiCall(securityKey);

            }

        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();


            }
        });

        builder.show();

    }

    private void deleteImageApiCall(String securityKey) {
        progressBar.setVisibility(View.VISIBLE);

        Call<SecurityKeyResult> call = RetrofitClient
                .getInstance()
                .getApi()
                .validateSecurityKey(user_id, token_id, token_value, user_profile_id, securityKey);

        call.enqueue(new Callback<SecurityKeyResult>() {
            @Override
            public void onResponse(Call<SecurityKeyResult> call, Response<SecurityKeyResult> response) {
                boolean result = response.body().isResult();
                if (result) {
                    showDeleteImageDiaolog();
                } else {
                    String message = response.body().getMessage();
                    Toast.makeText(ProfileImagesActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SecurityKeyResult> call, Throwable t) {
                Toast.makeText(ProfileImagesActivity.this, "Poor Internet Connection",
                        Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void showDeleteImageDiaolog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileImagesActivity.this);
        builder.setTitle("Delete Images")
                .setMessage("Are you sure you want to delete profile images?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteProfileImages();
                        Log.d(TAG, "onClick: here");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.show();
    }

    private void deleteProfileImages() {
        progressBar.setVisibility(View.VISIBLE);
        Call<DeleteImages> call = RetrofitClient
                .getInstance()
                .getApi()
                .deleteProfileImages(user_id, token_id, token_value, user_profile_id);

        call.enqueue(new Callback<DeleteImages>() {
            @Override
            public void onResponse(Call<DeleteImages> call, Response<DeleteImages> response) {
                deleteImagesResult = response.body().getResult();
                if (deleteImagesResult.equals("true")) {
                    Toast.makeText(ProfileImagesActivity.this, "Images Deleted Succesfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ProfileImagesActivity.this, "Server Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteImages> call, Throwable t) {

                Toast.makeText(ProfileImagesActivity.this,
                        "Poor Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void getProfileImages() {
        progressBar.setVisibility(View.VISIBLE);
        Call<UserProfileImagesModel> call = RetrofitClient
                .getInstance()
                .getApi()
                .getUserProfileImages(user_id, token_id, token_value, user_profile_id);

        call.enqueue(new Callback<UserProfileImagesModel>() {
            @Override
            public void onResponse(Call<UserProfileImagesModel> call, Response<UserProfileImagesModel> response) {
                if (response.body() != null) {
                    userImages = response.body().getUserImages();
                    if (userImages.size() == 0) {
                        Toast.makeText(ProfileImagesActivity.this, "No Images Available",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        storeImagesInBitmapArray();
                    }

                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UserProfileImagesModel> call, Throwable t) {

                Toast.makeText(ProfileImagesActivity.this, "Poor Internet Connection", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void storeImagesInBitmapArray() {
        try {
            byte[] decodedString1 = Base64.decode(userImages.get(0).getImage1(), Base64.DEFAULT);
            arrayOfBitmap[0] = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
            bitmapArray.add(arrayOfBitmap[0]);

            byte[] decodedString2 = Base64.decode(userImages.get(0).getImage2(), Base64.DEFAULT);
            arrayOfBitmap[1] = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
            bitmapArray.add(arrayOfBitmap[1]);

            byte[] decodedString3 = Base64.decode(userImages.get(0).getImage3(), Base64.DEFAULT);
            arrayOfBitmap[2] = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
            bitmapArray.add(arrayOfBitmap[2]);

            byte[] decodedString4 = Base64.decode(userImages.get(0).getImage4(), Base64.DEFAULT);
            arrayOfBitmap[3] = BitmapFactory.decodeByteArray(decodedString4, 0, decodedString4.length);
            bitmapArray.add(arrayOfBitmap[3]);

            byte[] decodedString5 = Base64.decode(userImages.get(0).getImage5(), Base64.DEFAULT);
            arrayOfBitmap[4] = BitmapFactory.decodeByteArray(decodedString5, 0, decodedString5.length);
            bitmapArray.add(arrayOfBitmap[4]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
