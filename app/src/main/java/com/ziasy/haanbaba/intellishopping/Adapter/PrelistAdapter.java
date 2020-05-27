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
import com.ziasy.haanbaba.intellishopping.R;

import java.util.List;

public class PrelistAdapter extends RecyclerView.Adapter<PrelistAdapter.prelistHolder> {
    Context context;
    List<PrelistModel> list;

    public PrelistAdapter(Context context, List<PrelistModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public prelistHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_single_item, parent, false);
        return new prelistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull prelistHolder holder, int index) {
        holder.txtName.setText(list.get(index).getName());
        holder.txtPrice.setText("Rs "+list.get(index).getPrice());
        holder.txtQuantity.setText("Qt. "+list.get(index).getWeigth());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class prelistHolder extends RecyclerView.ViewHolder {
        private ImageView listIcon;
        private TextView txtName,txtQuantity,txtPrice;
        public prelistHolder(View itemView) {
            super(itemView);
           listIcon=(ImageView)itemView.findViewById(R.id.listIconId);
           txtName=(TextView) itemView.findViewById(R.id.txtName);
           txtQuantity=(TextView) itemView.findViewById(R.id.txtQuantity);
            txtPrice=(TextView) itemView.findViewById(R.id.txtPrize);

        }
    }
}
