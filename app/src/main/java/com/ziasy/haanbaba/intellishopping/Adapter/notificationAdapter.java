package com.ziasy.haanbaba.intellishopping.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziasy.haanbaba.intellishopping.Model.PrelistModel;
import com.ziasy.haanbaba.intellishopping.Model.notificationModel;
import com.ziasy.haanbaba.intellishopping.R;

import java.util.List;

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.prelistHolder> {
    Context context;
    List<notificationModel.notificationData> list;

    public notificationAdapter(Context context, List<notificationModel.notificationData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public prelistHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new prelistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull prelistHolder holder, int position) {
        holder.notDiscription.setText(list.get(position).description);
        holder.notTitle.setText(list.get(position).heading);
        holder.notDateTime.setText(list.get(position).date);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class prelistHolder extends RecyclerView.ViewHolder {

        private TextView notDiscription,notDateTime,notTitle;
        public prelistHolder(View itemView) {
            super(itemView);
            notDiscription=(TextView) itemView.findViewById(R.id.notDiscription);
            notDateTime=(TextView) itemView.findViewById(R.id.notDateTime);
            notTitle=(TextView) itemView.findViewById(R.id.notTitle);
        }
    }
}