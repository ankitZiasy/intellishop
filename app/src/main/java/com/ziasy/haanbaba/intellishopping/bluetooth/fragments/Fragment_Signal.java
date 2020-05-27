package com.ziasy.haanbaba.intellishopping.bluetooth.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ziasy.haanbaba.intellishopping.R;
import com.ziasy.haanbaba.intellishopping.bluetooth.BlueToothMainActivity;

public class Fragment_Signal extends Fragment {
    int MAX_COUNT = 4;
    BlueToothMainActivity activity;

    class FragmentSignalTouch implements OnTouchListener {
        FragmentSignalTouch() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == 2) {
            }
            return true;
        }
    }

    public static Fragment_Signal newInstance(BlueToothMainActivity activity, String amount) {
        Fragment_Signal fragment_send = new Fragment_Signal();
        fragment_send.activity = activity;
        return fragment_send;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signal, container, false);
        view.setOnTouchListener(new FragmentSignalTouch());
        final ImageButton btnSpray = (ImageButton) view.findViewById(R.id.btnSpray);
        btnSpray.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    btnSpray.setScaleX(0.9f);
                    btnSpray.setScaleY(0.9f);
                }
                if (motionEvent.getAction() == 1) {
                    btnSpray.setScaleX(1.0f);
                    btnSpray.setScaleY(1.0f);
                }
                return false;
            }
        });
        final TextView count_spray = (TextView) view.findViewById(R.id.count_spray);
        count_spray.setText("4");
        btnSpray.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (Fragment_Signal.this.MAX_COUNT > 0) {
                    Fragment_Signal fragment_Signal = Fragment_Signal.this;
                    fragment_Signal.MAX_COUNT--;
                    count_spray.setText("" + Fragment_Signal.this.MAX_COUNT);
                    Fragment_Signal.this.activity.sendMessage("Hi from" + Fragment_Signal.this.activity.connectedDeviceName);
                    return;
                }
                BlueToothMainActivity.activity.showTransactionFragment("", "");
            }
        });
        return view;
    }

    public void onSaveInstanceState(Bundle outState) {
    }
}
