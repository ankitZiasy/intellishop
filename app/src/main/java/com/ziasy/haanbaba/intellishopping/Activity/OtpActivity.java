package com.ziasy.haanbaba.intellishopping.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.chaos.view.PinView;
import com.ziasy.haanbaba.intellishopping.Common.ConnectionDetector;
import com.ziasy.haanbaba.intellishopping.Common.Constant;
import com.ziasy.haanbaba.intellishopping.Common.SessionManagement;
import com.ziasy.haanbaba.intellishopping.R;
import com.ziasy.haanbaba.intellishopping.Utills.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class OtpActivity extends BaseActivity implements View.OnClickListener {
    private TextView txtResend;
    private LinearLayout txtVerify;
    private SessionManagement sd;
    private PinView edtPinview;
    private ProgressDialog pd;
    private ConnectionDetector cd;
    private RequestQueue queue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_activity);
        sd = new SessionManagement(OtpActivity.this);
        cd = new ConnectionDetector(OtpActivity.this);
        pd = new ProgressDialog(OtpActivity.this);
        pd.setMessage("Please wait...");
        queue = Volley.newRequestQueue(OtpActivity.this);
        edtPinview = (PinView) findViewById(R.id.pinView);
        txtVerify = (LinearLayout) findViewById(R.id.txtVerify);
        txtResend = (TextView) findViewById(R.id.txtResend);
        txtResend.setOnClickListener(this);
        txtVerify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtVerify:
                String strOtp = edtPinview.getText().toString().trim();
                if (strOtp.isEmpty()) {
                    Toast.makeText(OtpActivity.this, "Please Enter Otp", Toast.LENGTH_LONG).show();
                } else {
                    if (sd.getUserOtp().equalsIgnoreCase(strOtp)) {
                        Intent i = new Intent(OtpActivity.this, MainActivity.class);
                        startActivity(i);
                        sd.setLoginStatus("true");
                        finish();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Invalid Otp", Snackbar.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.txtResend:
                if (!cd.isConnectingToInternet()) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.NoConnection, Snackbar.LENGTH_SHORT).show();
                } else {
                    verifyOtp(sd.getUserEmail());
                }
                break;
            default:
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
                                    sd.setUserOtp(response.getString("otp").toString().trim());

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
            RequestQueue requestQueue = Volley.newRequestQueue(OtpActivity.this);
            requestQueue.add(jsonObjReq);
            queue.add(jsonObjReq);
        }
    }
}
