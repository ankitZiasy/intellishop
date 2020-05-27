package com.ziasy.haanbaba.intellishopping.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziasy.haanbaba.intellishopping.Model.offerModel;
import com.ziasy.haanbaba.intellishopping.R;

import java.util.ArrayList;

/**
 * Created by ANDROID on 09-Jan-18.
 */
public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder>{
    ArrayList<offerModel> al;
    Context context;

    public OfferAdapter(ArrayList<offerModel> al, Context context){
        this.al=al;
        this.context=context;
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        public TextView tvTitle,tvMessage;
        public ImageView imgIcon;

        public MyViewHolder(View view){
            super(view);
            tvTitle=(TextView)view.findViewById(R.id.tvTitle);
            tvMessage=(TextView)view.findViewById(R.id.tvMessage);
            imgIcon=(ImageView) view.findViewById(R.id.imgCard);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_list_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        offerModel data = al.get(position);
        holder.tvTitle.setText(data.getTitle());
        holder.tvMessage.setText(data.getMessage());
        holder.imgIcon.setImageResource(data.getIcon());


    }

    @Override
    public int getItemCount() {
        return al.size();
    }

}
