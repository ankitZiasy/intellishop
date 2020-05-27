package com.ziasy.haanbaba.intellishopping.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ziasy.haanbaba.intellishopping.Activity.ScanBarcodeActivity;
import com.ziasy.haanbaba.intellishopping.Activity.ScanTrolleyBarcodeActivity;
import com.ziasy.haanbaba.intellishopping.Common.SessionManagement;
import com.ziasy.haanbaba.intellishopping.DB.DBUtil;
import com.ziasy.haanbaba.intellishopping.R;

public class ScanList_Fragment extends Fragment implements View.OnClickListener {
    LinearLayout scanImage;
    private SessionManagement sd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.barcode_scanner, null, false);
        sd = new SessionManagement(getActivity());
        scanImage = (LinearLayout) view.findViewById(R.id.linear_next);
        scanImage.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_next:
                if (DBUtil.fetchAllDay(getActivity()).size() > 0||DBUtil.scanfetchAllDay(getActivity()).size() > 0) {
                    if (!sd.getUserTrolleyId().equalsIgnoreCase("USER_TROLLEY_ID") && sd.getUserTrolleyId() != null) {
                        Intent i = new Intent(getActivity(), ScanBarcodeActivity.class);
                        startActivity(i);
                        // getActivity().finish();
                    } else {
                        Intent i = new Intent(getActivity(), ScanTrolleyBarcodeActivity.class);
                        startActivity(i);
                        // getActivity().finish();
                        break;
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("Please Frist Create Prelist");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
        }
    }
}
