package com.ziasy.haanbaba.intellishopping.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziasy.haanbaba.intellishopping.Common.ConnectionDetector;
import com.ziasy.haanbaba.intellishopping.Common.Constant;
import com.ziasy.haanbaba.intellishopping.Common.Permission;
import com.ziasy.haanbaba.intellishopping.Common.SessionManagement;
import com.ziasy.haanbaba.intellishopping.Network.ApiClient;
import com.ziasy.haanbaba.intellishopping.Network.ApiInterface;
import com.ziasy.haanbaba.intellishopping.R;
import com.ziasy.haanbaba.intellishopping.Utills.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView txtSignIn, txtSignUp;
    private SessionManagement sd;
    private ConnectionDetector cd;
    private EditText edtPassword, edtEmail;
    private String strPassword, strEmail;
    private ApiInterface apiService;
    private String deviceid = null;
    private ProgressDialog pd;
    private RequestQueue queue;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sd = new SessionManagement(LoginActivity.this);
        cd = new ConnectionDetector(LoginActivity.this);
        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Please wait...");
        queue = Volley.newRequestQueue(LoginActivity.this);
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        txtSignIn = (TextView) findViewById(R.id.txtSignIn);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        txtSignUp.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtSignUp:
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);

                break;
            case R.id.txtSignIn:
                try {
                    if (!cd.isConnectingToInternet()) {
                        Snackbar.make(findViewById(android.R.id.content), R.string.NoConnection, Snackbar.LENGTH_SHORT).show();
                    } else {
                        boolean account = Permission.checkGetAccountPermission(LoginActivity.this);
                        boolean getReadPhoneState = Permission.permissionReadPhoneState(LoginActivity.this);
                        // if (account) {
                        if (getReadPhoneState) {
                            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                            deviceid = telephonyManager.getDeviceId();
                            if (deviceid != null) {
                                Log.d("msg", "DeviceImei " + deviceid);

                            } else {
                                deviceid = "78787878787878";

                            }

                            strPassword = edtPassword.getText().toString().trim();

                            strEmail = edtEmail.getText().toString().toLowerCase().trim();

                            if (strEmail.equalsIgnoreCase("")) {
                                edtEmail.setError("Please Enter Email");
                            } else if (!strEmail.matches(emailPattern)) {
                                edtEmail.setError("Please Enter Valid Email");
                            } else if (strPassword.equalsIgnoreCase("")) {
                                edtPassword.setError("Please Enter Password");
                            } else {
                                pd.show();
                                pd.setCancelable(false);
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.LOGIN_URL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    pd.dismiss();
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    if (jsonObject.getString("response").equalsIgnoreCase("success")) {
                                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                                        sd.setUserId(jsonObject.getString("id"));
                                                        sd.setUserName(jsonObject.getString("name"));
                                                        sd.setUserEmail(jsonObject.getString("email"));
                                                        sd.setUserMobile(jsonObject.getString("mobile"));
                                                        //verifyOtp(sd.getUserEmail());

                                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                                        startActivity(i);
                                                        sd.setLoginStatus("true");
                                                        finish();
                                                    } else {
                                                        Snackbar.make(findViewById(android.R.id.content), "Invalid Credential", Snackbar.LENGTH_SHORT).show();
                                                        pd.dismiss();
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    pd.dismiss();

                                                    Toast.makeText(LoginActivity.this, "Server is slow Please try after few time", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                pd.dismiss();
                                                Toast.makeText(LoginActivity.this, "Internet Connection Problem", Toast.LENGTH_LONG).show();
                                            }
                                        }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("email", strEmail);
                                        params.put("password", strPassword);
                                        params.put("imei", deviceid);
                                        params.put("device_id", sd.getUserFcmId());

                                        return params;
                                    }
                                };

                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                                requestQueue.add(stringRequest);
                            }
                        }
                    }
                    //   }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private void verifyOtp(final String email){
        if (!cd.isConnectingToInternet()) {
            Snackbar.make(findViewById(android.R.id.content), R.string.NoConnection, Snackbar.LENGTH_SHORT).show();
        } else {
            pd.show();
            pd.setCancelable(false);
            Map<String, String> postParam = new HashMap<String, String>();
            postParam.put("email",email);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constant.VERIFY_OTP, new JSONObject(postParam),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("DADADADADADAD", response.toString());
                            try {
                                pd.dismiss();
                                String code = response.getString("code");
                                if (code.equalsIgnoreCase("200")) {
                                    pd.dismiss();
                                        Intent i1 = new Intent(LoginActivity.this, OtpActivity.class);
                                        startActivity(i1);
                                        sd.setUserOtp(response.getString("otp").toString().trim());
                                    finish();
                                } else {
                                    Snackbar.make(findViewById(android.R.id.content), "Some think is wrong..", Snackbar.LENGTH_SHORT).show();
                                    pd.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                pd.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("DADADADADADAD", "Error: " + error.getMessage());
                    pd.dismiss();
                }
            }) {
                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            requestQueue.add(jsonObjReq);
            queue.add(jsonObjReq);
        }
    }
}