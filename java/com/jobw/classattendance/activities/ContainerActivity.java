package com.adivid.zpattendanceadmin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.adivid.zpattendanceadmin.fragments.AppUserListFragment;
import com.adivid.zpattendanceadmin.fragments.AdminUserListFragment;
import com.adivid.zpattendanceadmin.R;
import com.adivid.zpattendanceadmin.fragments.DashboardUserListFragment;
import com.adivid.zpattendanceadmin.fragments.PendingUsersListFragment;

public class ContainerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        extras = getIntent().getStringExtra("type");
        setUpFragment();

    }

    private void setUpFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (extras) {
            case "activate":
                toolbar.setTitle("Activate Users");
                PendingUsersListFragment fragment = new PendingUsersListFragment();
                fragmentTransaction.add(R.id.fl_container, fragment);
                fragmentTransaction.commit();
                break;
            case "dashboard":
                toolbar.setTitle("Dashboard Users");
                DashboardUserListFragment fragment1 = new DashboardUserListFragment();
                fragmentTransaction.add(R.id.fl_container, fragment1);
                fragmentTransaction.commit();
                break;
            case "app":
                toolbar.setTitle("App Users");
                AppUserListFragment fragment2 = new AppUserListFragment();
                fragmentTransaction.add(R.id.fl_container, fragment2);
                fragmentTransaction.commit();
                break;
            case "admin":
                toolbar.setTitle("Admin Users");
                AdminUserListFragment fragment3 = new AdminUserListFragment();
                fragmentTransaction.add(R.id.fl_container, fragment3);
                fragmentTransaction.commit();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
