package com.adivid.zpattendanceadmin.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.adivid.zpattendanceadmin.R;
import com.adivid.zpattendanceadmin.fragments.DashboardFragment;
import com.adivid.zpattendanceadmin.storage.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

import static com.adivid.zpattendanceadmin.storage.SharedPrefManager.SHARED_PREF_NAME;

/**
 * Created by Roshan Adke at 10-01-2020
 */

public class DashboardViewActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private SharedPrefManager prefManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static String user_id, token_id, token_value, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.flContent, new DashboardFragment());
        tx.commit();
        mDrawer = findViewById(R.id.drawerLayout);
        prefManager = new SharedPrefManager(this);
        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        user_id = sharedPreferences.getString("user_id", "");
        token_id = sharedPreferences.getString("token_id", "");
        token_value = sharedPreferences.getString("token_value", "");
        name = sharedPreferences.getString("name", "Profile Name");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_open);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        nvDrawer = findViewById(R.id.navigationView);
        setupDrawerContent(nvDrawer);

    }

    @Override
    public void onClick(View view) {

    }

    private void setupDrawerContent(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        TextView textView = headerView.findViewById(R.id.txt_profile_name);
        textView.setText(name);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }

                });

    }

    public void selectDrawerItem(MenuItem menuItem) {


        switch (menuItem.getItemId()) {

            case R.id.nav_home:
                if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                    mDrawer.closeDrawer(GravityCompat.START);
                }
                break;

            case R.id.nav_activate_user:
                Intent intent = new Intent(DashboardViewActivity.this, ContainerActivity.class);
                intent.putExtra("type", "activate");
                startActivity(intent);
                break;

            case R.id.nav_user_list:
                Intent intent1 = new Intent(DashboardViewActivity.this, ContainerActivity.class);
                intent1.putExtra("type", "app");
                startActivity(intent1);
                break;

            case R.id.nav_dashboard_list:
                Intent intent2 = new Intent(DashboardViewActivity.this, ContainerActivity.class);
                intent2.putExtra("type", "dashboard");
                startActivity(intent2);
                break;

            case R.id.nav_admin_list:
                Intent intent3 = new Intent(DashboardViewActivity.this, ContainerActivity.class);
                intent3.putExtra("type", "admin");
                startActivity(intent3);
                break;

            case R.id.nav_download:
                Intent intent4 = new Intent(DashboardViewActivity.this, DownloadQrActivity.class);
                startActivity(intent4);
                break;

            case R.id.nav_log_out:
                showLogOutDialog();
                break;

        }

    }

    private void showLogOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm")
                .setMessage("Are you sure you want to Logout ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        prefManager.clear();
                        startActivity(new Intent(DashboardViewActivity.this, LoginActivity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.show();

    }

}
