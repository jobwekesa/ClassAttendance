package com.adivid.zpattendanceadmin.callback;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.view.ActionMode;

import com.adivid.zpattendanceadmin.R;
import com.adivid.zpattendanceadmin.adapters.DashboardListAdapter;
import com.adivid.zpattendanceadmin.adapters.RecyclerView_Adapter;
import com.adivid.zpattendanceadmin.fragments.DashboardUserListFragment;
import com.adivid.zpattendanceadmin.fragments.PendingUsersListFragment;
import com.adivid.zpattendanceadmin.models.InactivateUserListModel;
import com.adivid.zpattendanceadmin.models_two.DashboardUserListInfo;

import java.util.List;

public class DeactivateActionmodeCallback implements ActionMode.Callback {

    private Context context;
    private DashboardListAdapter dashboardListAdapter;
    private List<DashboardUserListInfo> message_models;
    private boolean isListViewFragment;

    public DeactivateActionmodeCallback(Context context, DashboardListAdapter dashboardListAdapter,
                                        List<DashboardUserListInfo> message_models,
                                        boolean isListViewFragment) {
        this.context = context;
        this.dashboardListAdapter = dashboardListAdapter;
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
                                DashboardUserListFragment.deActivateUsers(context);

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
        dashboardListAdapter.removeSelection();
        DashboardUserListFragment.setNullToActionMode();
    }

}
