package com.adivid.zpattendanceadmin.callback;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;

import com.adivid.zpattendanceadmin.R;
import com.adivid.zpattendanceadmin.adapters.AppUserListAdapter;
import com.adivid.zpattendanceadmin.adapters.DashboardListAdapter;
import com.adivid.zpattendanceadmin.fragments.AppUserListFragment;
import com.adivid.zpattendanceadmin.fragments.DashboardUserListFragment;
import com.adivid.zpattendanceadmin.models_two.AppUserListInfo;
import com.adivid.zpattendanceadmin.models_two.DashboardUserListInfo;

import java.util.List;

public class DeactivateActionmodeAppUser implements ActionMode.Callback {

    private Context context;
    private AppUserListAdapter appUserListAdapter;
    private List<AppUserListInfo> message_models;
    private boolean isListViewFragment;

    public DeactivateActionmodeAppUser(Context context, AppUserListAdapter appUserListAdapter,
                                       List<AppUserListInfo> message_models,
                                       boolean isListViewFragment) {
        this.context = context;
        this.appUserListAdapter = appUserListAdapter;
        this.message_models = message_models;
        this.isListViewFragment = isListViewFragment;
    }


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.deactivate_actionmode, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_deactivate:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Deactivation")
                        .setMessage("Are u sure u want to de-activate selected users?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AppUserListFragment.deActivateUsers(context);

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();

                break;

        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        appUserListAdapter.removeSelection();
        AppUserListFragment.setNullToActionMode();
    }

}
