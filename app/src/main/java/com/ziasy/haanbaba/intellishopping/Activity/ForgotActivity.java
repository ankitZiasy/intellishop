package com.ziasy.haanbaba.intellishopping.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.ziasy.haanbaba.intellishopping.Common.ConnectionDetector;
import com.ziasy.haanbaba.intellishopping.Common.Constant;
import com.ziasy.haanbaba.intellishopping.Common.SessionManagement;
import com.ziasy.haanbaba.intellishopping.R;
import com.ziasy.haanbaba.intellishopping.Utills.BaseActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotActivity extends BaseActivity implements View.OnClickListener {
    private EditText edtEmail;
    private LinearLayout txtVerify;
    private SessionManagement sd;
    private String strEmail;
    private ConnectionDetector cd;
    private ProgressDialog pd;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_activity);
        cd=new ConnectionDetector(ForgotActivity.this);
        sd = new SessionManagement(ForgotActivity.this);
        pd = new ProgressDialog(ForgotActivity.this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        txtVerify = (LinearLayout) findViewById(R.id.txtVerify);
        edtEmail = (EditText) findViewById(R.id.edtEmail);

        txtVerify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtVerify:
                if (!cd.isConnectingToInternet()){
                    Snackbar.make(findViewById(android.R.id.content),R.string.NoConnection,Snackbar.LENGTH_SHORT).show();
                }else {
                if (strEmail.isEmpty()) {
                    edtEmail.setError("Please Enter Email");
                } else if (strEmail.matches(emailPattern)) {
                    edtEmail.setError("Please Enter Valid Email");

                } else {

                    pd.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.FORGOT_PASSWORD,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        pd.dismiss();
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getString("response").equalsIgnoreCase("success")) {
                                            Toast.makeText(ForgotActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                            //  sd.setUserOtp(jsonObject.getString("mobile"));
                                            Intent i1 = new Intent(ForgotActivity.this, ForgotActivity.class);
                                            startActivity(i1);

                                            pd.dismiss();
                                            finish();
                                        } else {
                                            Snackbar.make(findViewById(android.R.id.content), "Invalid Credential", Snackbar.LENGTH_SHORT).show();
                                            pd.dismiss();
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        pd.dismiss();

                                        Toast.makeText(ForgotActivity.this, "Server is slow Please try after few time", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    pd.dismiss();
                                    Toast.makeText(ForgotActivity.this, "Internet Connection Problem", Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id", sd.getUserId());
                            params.put("email",strEmail );


                            return params;
                        }
                    };

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(ForgotActivity.this);
                    requestQueue.add(stringRequest);
                }
                }
                break;
            default:
                break;
        }
    }
}
