package com.ziasy.haanbaba.intellishopping.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ziasy.haanbaba.intellishopping.DB.DBUtil;
import com.ziasy.haanbaba.intellishopping.DB.ProductDatabaseModel;
import com.ziasy.haanbaba.intellishopping.Fragment.PreList_Fragment;
import com.ziasy.haanbaba.intellishopping.R;

import java.util.List;

public class PrelistDatabaseAdapter extends RecyclerView.Adapter<PrelistDatabaseAdapter.prelistHolder> {
    Context context;
    List<ProductDatabaseModel> list;
    //PreList_Fragment fragment;

    public PrelistDatabaseAdapter(Context context, List<ProductDatabaseModel> list) {
        this.context = context;
      //  this.fragment = fragment;
        this.list = list;

    }

    @NonNull
    @Override
    public prelistHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_single_item, parent, false);
        return new prelistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull prelistHolder holder, final int position) {
        holder.txtName.setText(list.get(position).getProductName());
        holder.txtPrice.setText("Rs "+list.get(position).getProductWeigth());
        holder.txtQuantity.setText("Qt. "+list.get(position).getProductQuantity());
        Picasso.with(context).load(list.get(position).getProductImage()).into(holder.listIcon);
      /*  holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.deleteExercise(context,position);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeItem(int position) {
        ProductDatabaseModel exercise = list.get(position);
        DBUtil.deleteDay(context, exercise.getProductId());
        Toast.makeText(context, "DELETE", Toast.LENGTH_SHORT).show();
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
    }

    public void restoreItem(ProductDatabaseModel item, int position) {
        ProductDatabaseModel newExercise =  DBUtil.productInsert(context, item.getProductId(), item.getProductName(), item.getProductQuantity(), item.getProductWeigth(), item.getProductImage(), item.getProductPrice(),item.getProduct_retailer_id());
        list.add(position, newExercise);
        // notify item added by position
        notifyItemInserted(position);
    }
    public class prelistHolder extends RecyclerView.ViewHolder {
        private ImageView listIcon,imageDelete;
        private TextView txtName,txtQuantity,txtPrice;
        public RelativeLayout viewBackground, viewForeground;
        public prelistHolder(View itemView) {
            super(itemView);
           listIcon=(ImageView)itemView.findViewById(R.id.listIconId);
            imageDelete=(ImageView)itemView.findViewById(R.id.imageDelete);
           txtName=(TextView) itemView.findViewById(R.id.txtName);
           txtQuantity=(TextView) itemView.findViewById(R.id.txtQuantity);
            txtPrice=(TextView) itemView.findViewById(R.id.txtPrize);
            listIcon=(ImageView) itemView.findViewById(R.id.listIconId);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

        }
    }
}
