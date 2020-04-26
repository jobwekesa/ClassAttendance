package com.adivid.zpattendanceadmin.callback;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;

import com.adivid.zpattendanceadmin.R;
import com.adivid.zpattendanceadmin.adapters.RecyclerView_Adapter;
import com.adivid.zpattendanceadmin.fragments.PendingUsersListFragment;
import com.adivid.zpattendanceadmin.models.InactivateUserListModel;

import java.util.List;

public class Toolbar_ActionMode_Callback implements androidx.appcompat.view.ActionMode.Callback {

    private Context context;
    private RecyclerView_Adapter recyclerView_adapter;
    private List<InactivateUserListModel> message_models;
    private boolean isListViewFragment;

    public Toolbar_ActionMode_Callback(Context context, RecyclerView_Adapter recyclerView_adapter,
                                       List<InactivateUserListModel> message_models,
                                       boolean isListViewFragment) {
        this.context = context;
        this.recyclerView_adapter = recyclerView_adapter;
        this.message_models = message_models;
        this.isListViewFragment = isListViewFragment;
    }


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Activation")
                        .setMessage("Are u sure u want to activate selected users?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PendingUsersListFragment.activateSelectedUsers(context);
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


        recyclerView_adapter.removeSelection();
        PendingUsersListFragment.setNullToActionMode();
    }

}
