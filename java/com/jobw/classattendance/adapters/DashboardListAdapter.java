package com.adivid.zpattendanceadmin.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adivid.zpattendanceadmin.R;
import com.adivid.zpattendanceadmin.models_two.DashboardUserListInfo;

import java.util.List;

public class DashboardListAdapter extends RecyclerView.Adapter<DashboardViewHolder> {
    private Context context;
    private List<DashboardUserListInfo> arrayList;
    private DashboardListAdapter.OnDashboardListItemClickListener dbListItemClickListener;
    private SparseBooleanArray mSelectedItemIds;

    public DashboardListAdapter(Context context,
                                List<DashboardUserListInfo> arrayList,
                                DashboardListAdapter.OnDashboardListItemClickListener dbListItemClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.dbListItemClickListener = dbListItemClickListener;
        this.mSelectedItemIds=new SparseBooleanArray();
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) inflater.inflate(R.layout.inactivate_user_list_layout, parent, false);
        return new DashboardViewHolder(mainGroup, dbListItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
        if (arrayList.get(position) != null) {
            holder.full_name.setText(arrayList.get(position).getFullName());
            holder.email.setText(arrayList.get(position).getEmail());
            holder.mobile.setText(arrayList.get(position).getMobile());

            if (arrayList.get(position).getIdProof() == null || arrayList.get(position).getIdProof().equals("")) {
                holder.circleImageView.setImageResource(R.drawable.id_card);
            } else {
                byte[] decodedString = Base64.decode(arrayList.get(position).getIdProof(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.circleImageView.setImageBitmap(decodedByte);
            }
        }

        holder.itemView
                .setBackgroundColor(mSelectedItemIds.get(position) ? 0x9934B5E4
                        : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }



    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemIds.get(position));
    }


    public void removeSelection() {
        mSelectedItemIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemIds.put(position, value);
        else
            mSelectedItemIds.delete(position);

        notifyDataSetChanged();
    }


    public int getSelectedCount() {
        return mSelectedItemIds.size();
    }


    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemIds;
    }

    public interface OnDashboardListItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);

    }

}
