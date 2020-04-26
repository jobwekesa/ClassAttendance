package com.adivid.zpattendanceadmin.adapters;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adivid.zpattendanceadmin.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    LinearLayout linearLayout;
    public TextView full_name, email, mobile;
    CircleImageView circleImageView;
    DashboardListAdapter.OnDashboardListItemClickListener dbListItemClickListener;


    DashboardViewHolder(@NonNull View itemView,
                        DashboardListAdapter.OnDashboardListItemClickListener dbListItemClickListener) {
        super(itemView);

        this.dbListItemClickListener = dbListItemClickListener;
        linearLayout = itemView.findViewById(R.id.linearLayoutList);
        full_name = itemView.findViewById(R.id.txt_full_name);
        email = itemView.findViewById(R.id.txt_email);
        mobile = itemView.findViewById(R.id.txt_mobile);
        circleImageView = itemView.findViewById(R.id.img_profile_thumbnail);

        circleImageView.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
        linearLayout.setOnLongClickListener(this);

    }

    @Override
    public void onClick(View view) {
        dbListItemClickListener.onItemClick(view, getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View view) {
        dbListItemClickListener.onItemLongClick(view, getAdapterPosition());
        return true;
    }
}
