package com.ziasy.haanbaba.intellishopping.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;
import com.ziasy.haanbaba.intellishopping.Common.ConnectionDetector;
import com.ziasy.haanbaba.intellishopping.Common.Constant;
import com.ziasy.haanbaba.intellishopping.Common.Permission;
import com.ziasy.haanbaba.intellishopping.Common.SessionManagement;
import com.ziasy.haanbaba.intellishopping.Model.signUpModel;
import com.ziasy.haanbaba.intellishopping.Network.ApiClient;
import com.ziasy.haanbaba.intellishopping.Network.ApiInterface;
import com.ziasy.haanbaba.intellishopping.R;
import com.ziasy.haanbaba.intellishopping.Utills.BaseActivity;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;*/

public class SignUpActivity extends BaseActivity implements View.OnClickListener {
    TextView txtDob, txtSignUp;
    private RadioGroup rbdGroup;
    private RadioButton radiogenderButton;
    private String str_radiogenderButton;
    EditText edtName, edtPassword, edtMobile, edtEmail;
    String strDob, strName, strPassword, strMobile, strEmail, strIMEI;
    ApiInterface apiService;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Calendar c;
    ProgressDialog pd;
    String formatDay, formatMounth;
    ConnectionDetector cd;
    private SessionManagement sd;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        c = Calendar.getInstance();
        cd = new ConnectionDetector(SignUpActivity.this);
        sd = new SessionManagement(SignUpActivity.this);
        pd = new ProgressDialog(SignUpActivity.this);
        pd.setMessage("Please wait...");
        apiService = ApiClient.getClient().create(ApiInterface.class);
        rbdGroup = (RadioGroup) findViewById(R.id.rbdGroup);
        edtName = (EditText) findViewById(R.id.edtName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtMobile = (EditText) findViewById(R.id.edtMobile);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        txtDob = (TextView) findViewById(R.id.txtDob);
        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        txtDob.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtDob:
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerFragmentDialog dialog= DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                       if (dayOfMonth == 0) {

                           formatDay = "0";
                       } else if (dayOfMonth == 10) {
                           formatDay = "";
                       } else if (dayOfMonth > 10) {

                           formatDay = "";
                       } else {
                           formatDay = "0";
                       }

                       if (monthOfYear == 0) {

                           formatMounth = "0";
                       } else if (monthOfYear == 9) {
                           formatMounth = "";
                       } else if (monthOfYear > 9) {

                           formatMounth = "";
                       } else {
                           formatMounth = "0";
                       }
                       txtDob.setText(formatDay + dayOfMonth + "-" + formatMounth + (monthOfYear + 1) + "-" + year);
                   }
               });
                dialog.show(getSupportFragmentManager(),"tag");

                break;
            case R.id.txtSignUp:
                try {
                    if (!cd.isConnectingToInternet()) {
                        Snackbar.make(findViewById(android.R.id.content), R.string.NoConnection, Snackbar.LENGTH_SHORT).show();
                    } else {
                       // boolean account = Permission.checkGetAccountPermission(SignUpActivity.this);
                        boolean getReadPhoneState = Permission.permissionReadPhoneState(SignUpActivity.this);
                       // if (account) {
                            if (getReadPhoneState) {
                                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                                strIMEI = telephonyManager.getDeviceId();
                                if (strIMEI != null) {
                                    Log.d("msg", "DeviceImei " + strIMEI);


                                } else {
                                    strIMEI = "78787878787878";

                                }
                                strName = edtName.getText().toString().trim();
                                strPassword = edtPassword.getText().toString().trim();
                                strMobile = edtMobile.getText().toString().trim();
                                strEmail = edtEmail.getText().toString().toLowerCase().trim();
                                strDob = txtDob.getText().toString().trim();

                                int selectedId = rbdGroup.getCheckedRadioButtonId();
                                radiogenderButton = (RadioButton) findViewById(selectedId);
                                str_radiogenderButton = radiogenderButton.getText().toString();
                                Log.e("str_radiogenderButton",str_radiogenderButton);

                                if (strName.equalsIgnoreCase("")) {
                                    edtName.setError("Please Enter Name");
                                } else if (strEmail.equalsIgnoreCase("")) {
                                    edtEmail.setError("Please Enter Email");
                                } else if (!strEmail.matches(emailPattern)) {
                                    edtEmail.setError("Please Enter Valid Email");
                                } else if (strPassword.equalsIgnoreCase("")) {
                                    edtPassword.setError("Please Enter Password");
                                } else if (strMobile.equalsIgnoreCase("")) {
                                    edtMobile.setError("Please Enter Mobile");
                                } else if (strMobile.length() != 10) {
                                    edtMobile.setError("Please Enter 10 Digit Number");
                                } else if (strDob.equalsIgnoreCase("")) {
                                    Toast.makeText(SignUpActivity.this, "Please Select DOB", Toast.LENGTH_SHORT).show();
                                } else {

                                    pd.show();
                                    pd.setCancelable(false);
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.SIGNUP,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        pd.dismiss();
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        if (jsonObject.getString("response").equalsIgnoreCase("success")) {
                                                  /*  Intent i1 = new Intent(SignUpActivity.this, LoginActivity.class);
                                                    startActivity(i1);*/
                                                            pd.dismiss();
                                                            Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();


                                                            finish();
                                                        } else if (jsonObject.getString("response").equalsIgnoreCase("Duplicate Request")) {

                                                            pd.dismiss();
                                                            Toast.makeText(SignUpActivity.this, "Email Already Registered", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Snackbar.make(findViewById(android.R.id.content), "Invalid Credential", Snackbar.LENGTH_SHORT).show();
                                                            pd.dismiss();
                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        pd.dismiss();

                                                        Toast.makeText(SignUpActivity.this, "Server is slow Please try after few time", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    pd.dismiss();
                                                    Toast.makeText(SignUpActivity.this, "Internet Connection Problem", Toast.LENGTH_LONG).show();
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("name", strName);
                                            params.put("email", strEmail);
                                            params.put("password", strPassword);
                                            params.put("mobile", strMobile);
                                            params.put("dob", strDob);
                                            params.put("imei", strIMEI);
                                            params.put("gender", str_radiogenderButton);
                                            params.put("device_id", sd.getUserFcmId());

                                            return params;
                                        }
                                    };

                                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
                                    requestQueue.add(stringRequest);
                                }
                            }
                        }
                  //  }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
