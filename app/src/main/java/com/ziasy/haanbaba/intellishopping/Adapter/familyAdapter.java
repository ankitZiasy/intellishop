package com.ziasy.haanbaba.intellishopping.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ziasy.haanbaba.intellishopping.Model.familyModel;
import com.ziasy.haanbaba.intellishopping.Model.notificationModel;
import com.ziasy.haanbaba.intellishopping.R;

import java.util.List;

public class familyAdapter extends RecyclerView.Adapter<familyAdapter.prelistHolder> {
    Context context;
    List<familyModel.familyDataList> list;

    public familyAdapter(Context context, List<familyModel.familyDataList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public prelistHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_list_item, parent, false);
        return new prelistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull prelistHolder holder, int position) {
        holder.txt_user.setText(list.get(position).getName());
        holder.txt_mobile.setText(list.get(position).getMobile());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class prelistHolder extends RecyclerView.ViewHolder {

        private TextView txt_user, txt_mobile;

        public prelistHolder(View itemView) {
            super(itemView);

            txt_user = (TextView) itemView.findViewById(R.id.txt_user);

            txt_mobile = (TextView) itemView.findViewById(R.id.txt_lastMessage);


        }
    }
}
