package com.ziasy.haanbaba.intellishopping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziasy.haanbaba.intellishopping.Model.ChildInfo;
import com.ziasy.haanbaba.intellishopping.Model.GroupInfo;
import com.ziasy.haanbaba.intellishopping.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<GroupInfo> deptList;

    public CustomAdapter(Context context, ArrayList<GroupInfo> deptList) {
        this.context = context;
        this.deptList = deptList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ChildInfo> productList = deptList.get(groupPosition).getProductList();
        return productList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        ChildInfo detailInfo = (ChildInfo) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.child_items, null);
        }

        ImageView listIconId=(ImageView)view.findViewById(R.id.listIconId);

        TextView txtName = (TextView) view.findViewById(R.id.txtName);
        txtName.setText(detailInfo.getProduct_id().trim());
  TextView txtQuantity = (TextView) view.findViewById(R.id.txtQuantity);
        txtQuantity.setText(detailInfo.getQuantity().trim());

  TextView txtPrize = (TextView) view.findViewById(R.id.txtPrize);
        txtPrize.setText(detailInfo.getPrice().trim());

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        ArrayList<ChildInfo> productList = deptList.get(groupPosition).getProductList();
        return productList.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return deptList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return deptList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        GroupInfo headerInfo = (GroupInfo) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.group_items, null);
        }

        TextView date = (TextView) view.findViewById(R.id.date);
        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText("Order No: "+headerInfo.getName().trim());
        date.setText("Date : "+headerInfo.getDate().trim());

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
