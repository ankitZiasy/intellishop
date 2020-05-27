package com.ziasy.haanbaba.intellishopping.DB;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziasy.haanbaba.intellishopping.Activity.CheckOutActivity;
import com.ziasy.haanbaba.intellishopping.Activity.MainActivity;
import com.ziasy.haanbaba.intellishopping.Activity.ScanBarcodeActivity;
import com.ziasy.haanbaba.intellishopping.Common.ConnectionDetector;
import com.ziasy.haanbaba.intellishopping.Common.Constant;
import com.ziasy.haanbaba.intellishopping.Common.SessionManagement;
import com.ziasy.haanbaba.intellishopping.Common.onCallBackInterfaceForTest;
import com.ziasy.haanbaba.intellishopping.Model.TrolleyModel;
import com.ziasy.haanbaba.intellishopping.R;
import com.ziasy.haanbaba.intellishopping.bluetooth.BlueToothMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.PRODUCT_ID;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.PRODUCT_NAME;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.PRODUCT_PRICE;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.PRODUCT_QUANTITY;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.PRODUCT_RETAILR_ID;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.PRODUCT_WIEGTH;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.PRODUCT_IMAGE;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.PRODUCT_LIST_TABLE_NAME;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.ID;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.SCAN_ID;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.SCAN_PRODUCT_ID;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.SCAN_PRODUCT_IMAGE;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.SCAN_PRODUCT_LIST_TABLE_NAME;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.SCAN_PRODUCT_NAME;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.SCAN_PRODUCT_PRICE;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.SCAN_PRODUCT_QUANTITY;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.SCAN_PRODUCT_RETAILR_ID;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.SCAN_PRODUCT_WIEGTH;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.TROLLEY_ID;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.TROLLEY_QRCODE;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.TROLLEY_TABLE_NAME;


/**
 * Created by ANDROID on 14-Sep-17.
 */

public class DBUtil {
    private static onCallBackInterfaceForTest callBackInterfaceForTest;

    public DBUtil() {

    }

    private static final String TAG = "DBUtil";

    /**
     * Insert type into db
     *
     * @param context
     * @param
     */
    public static ProductDatabaseModel productInsert(Context context, int productId, String productName, String productQuantity, String productWeigth, String productImage, String productPrice, String productRetailerId) {

        SQLiteDatabase db = new DBData(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PRODUCT_ID, productId);
        values.put(PRODUCT_NAME, productName);
        values.put(PRODUCT_QUANTITY, productQuantity);
        values.put(PRODUCT_WIEGTH, productWeigth);
        values.put(PRODUCT_IMAGE, productImage);
        values.put(PRODUCT_PRICE, productPrice);
        values.put(PRODUCT_RETAILR_ID, productRetailerId);

        long id = db.insertOrThrow(PRODUCT_LIST_TABLE_NAME, null, values);
        db.close();
        ProductDatabaseModel newType = fetchDay(context, (int) id);
        return newType;
    }

    /**
     * Insert type into db
     *
     * @param context
     * @param
     */
    public static TrolleyModel trolleyInsert(Context context, String trolleyQrcode) {
        SQLiteDatabase db = new DBData(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TROLLEY_QRCODE, trolleyQrcode);
        long id = db.insertOrThrow(TROLLEY_TABLE_NAME, null, values);
        db.close();
        TrolleyModel newType = fetchTrolley(context, (int) id);
        return newType;
    }

    /**
     * Fetch all Exercises
     *
     * @param context
     * @return
     */
    public static List<ProductDatabaseModel> fetchAllDay(Context context) {
        String[] FROM = {ID, PRODUCT_ID, PRODUCT_NAME, PRODUCT_QUANTITY, PRODUCT_WIEGTH, PRODUCT_IMAGE, PRODUCT_PRICE,PRODUCT_RETAILR_ID};
        SQLiteDatabase db = new DBData(context).getReadableDatabase();
        List<ProductDatabaseModel> day = new ArrayList<ProductDatabaseModel>();
        Cursor cursor = db.query(PRODUCT_LIST_TABLE_NAME, FROM, null, null, null, null, PRODUCT_NAME);
        while (cursor.moveToNext()) {
            ProductDatabaseModel temp = new ProductDatabaseModel(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
            day.add(temp);
        }

        cursor.close();
        db.close();
        return day;
    }

    /**
     * Fetch all Exercises
     *
     * @param context
     * @return
     */
    public static List<TrolleyModel> fetchAllTrolley(Context context) {
        String[] FROM = {TROLLEY_ID, TROLLEY_QRCODE};
        SQLiteDatabase db = new DBData(context).getReadableDatabase();
        List<TrolleyModel> day = new ArrayList<TrolleyModel>();
        Cursor cursor = db.query(TROLLEY_TABLE_NAME, FROM, null, null, null, null, TROLLEY_QRCODE);
        while (cursor.moveToNext()) {
            TrolleyModel temp = new TrolleyModel(cursor.getString(0), cursor.getString(1));
            day.add(temp);
        }
        cursor.close();
        db.close();
        return day;
    }

    /**
     * Fetch a type by id
     *
     * @param context
     * @param typeId
     * @return
     */

    public static ProductDatabaseModel fetchDay(Context context, int typeId) {
        String[] FROM = {ID, PRODUCT_ID, PRODUCT_NAME, PRODUCT_QUANTITY, PRODUCT_WIEGTH, PRODUCT_IMAGE, PRODUCT_PRICE,PRODUCT_RETAILR_ID};
        String where = ID + "=" + typeId;

        SQLiteDatabase db = new DBData(context).getReadableDatabase();
        Cursor cursor = db.query(PRODUCT_LIST_TABLE_NAME, FROM, where, null, null, null, PRODUCT_NAME);
        cursor.moveToNext();
        ProductDatabaseModel temp = new ProductDatabaseModel(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        cursor.close();
        db.close();

        return temp;
    }

    /**
     * Fetch a type by id
     *
     * @param context
     * @param typeId
     * @return
     */

    public static TrolleyModel fetchTrolley(Context context, int typeId) {
        String[] FROM = {TROLLEY_ID, TROLLEY_QRCODE};
        String where = TROLLEY_ID + "=" + typeId;

        SQLiteDatabase db = new DBData(context).getReadableDatabase();
        Cursor cursor = db.query(TROLLEY_TABLE_NAME, FROM, where, null, null, null, PRODUCT_NAME);
        cursor.moveToNext();
        TrolleyModel temp = new TrolleyModel(cursor.getString(0), cursor.getString(1));
        cursor.close();
        db.close();

        return temp;
    }

    /**
     * Delete a Exercise
     *
     * @param context
     * @param dayId
     */
    public static void deleteDay(Context context, int dayId) {

        SQLiteDatabase db = new DBData(context).getWritableDatabase();
        String where = PRODUCT_ID + "=" + dayId;
        db.delete(PRODUCT_LIST_TABLE_NAME, where, null);
        db.close();
    }

    public static ScanProductDatabaseModel scanproductInsert(Context context, int productId, String productName, String productQuantity, String productWeigth, String productImage, String productPrice, String productRetailerId) {
        SQLiteDatabase db = new DBData(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SCAN_PRODUCT_ID, productId);
        values.put(SCAN_PRODUCT_NAME, productName);
        values.put(SCAN_PRODUCT_QUANTITY, productQuantity);
        values.put(SCAN_PRODUCT_WIEGTH, productWeigth);
        values.put(SCAN_PRODUCT_IMAGE, productImage);
        values.put(SCAN_PRODUCT_PRICE, productPrice);
        values.put(SCAN_PRODUCT_RETAILR_ID, productRetailerId);

        long id = db.insertOrThrow(SCAN_PRODUCT_LIST_TABLE_NAME, null, values);
        db.close();
        ScanProductDatabaseModel newType = scanfetchDay(context, (int) id);
        return newType;
    }

    /**
     * Fetch all Exercises
     *
     * @param context
     * @return
     */
    public static List<ScanProductDatabaseModel> scanfetchAllDay(Context context) {
        String[] FROM = {SCAN_ID, SCAN_PRODUCT_ID, SCAN_PRODUCT_NAME, SCAN_PRODUCT_QUANTITY, SCAN_PRODUCT_WIEGTH, SCAN_PRODUCT_IMAGE, SCAN_PRODUCT_PRICE,SCAN_PRODUCT_RETAILR_ID};
        SQLiteDatabase db = new DBData(context).getReadableDatabase();
        List<ScanProductDatabaseModel> day = new ArrayList<ScanProductDatabaseModel>();
        Cursor cursor = db.query(SCAN_PRODUCT_LIST_TABLE_NAME, FROM, null, null, null, null, SCAN_PRODUCT_NAME);
        while (cursor.moveToNext()) {
            ScanProductDatabaseModel temp = new ScanProductDatabaseModel(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
            day.add(temp);
        }

        cursor.close();
        db.close();
        return day;
    }

    /**
     * Fetch a type by id
     *
     * @param context
     * @param typeId
     * @return
     */

    public static ScanProductDatabaseModel scanfetchDay(Context context, int typeId) {
        String[] FROM = {SCAN_ID, SCAN_PRODUCT_ID, SCAN_PRODUCT_NAME, SCAN_PRODUCT_QUANTITY, SCAN_PRODUCT_WIEGTH, SCAN_PRODUCT_IMAGE, SCAN_PRODUCT_PRICE,SCAN_PRODUCT_RETAILR_ID};
        String where = SCAN_ID + "=" + typeId;

        SQLiteDatabase db = new DBData(context).getReadableDatabase();
        Cursor cursor = db.query(SCAN_PRODUCT_LIST_TABLE_NAME, FROM, where, null, null, null, SCAN_PRODUCT_NAME);
        cursor.moveToNext();
        ScanProductDatabaseModel temp = new ScanProductDatabaseModel(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        cursor.close();
        db.close();

        return temp;
    }

    /**
     * Delete a Exercise
     *
     * @param context
     * @param dayId
     */
    public static void scandeleteDay(Context context, int dayId) {

        SQLiteDatabase db = new DBData(context).getWritableDatabase();
        String where = SCAN_PRODUCT_ID + "=" + dayId;
        db.delete(SCAN_PRODUCT_LIST_TABLE_NAME, where, null);
        db.close();
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    public static void updateProduct(Context context, ProductDatabaseModel user) {
        SQLiteDatabase db = new DBData(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PRODUCT_QUANTITY, user.getProductQuantity());

        // updating row
        db.update(PRODUCT_LIST_TABLE_NAME, values, PRODUCT_ID + " = ?",
                new String[]{String.valueOf(user.getProductId())});
        db.close();
    }
    public static void deleteAllExersice(Context context, int exerciseId) {
        SQLiteDatabase db = new DBData(context).getWritableDatabase();
        db.delete(PRODUCT_LIST_TABLE_NAME, null, null);
        db.delete(SCAN_PRODUCT_LIST_TABLE_NAME, null, null);
        db.close();

    }
    /**
     * This method to check user exist or not
     *
     * @param
     * @return true/false
     */
    public static boolean checkProduct(Context context, String product_id) {
        // array of columns to fetch
        String[] columns = {PRODUCT_ID};
        SQLiteDatabase db = new DBData(context).getReadableDatabase();
        // selection criteria
        String selection = PRODUCT_ID + " = ?";
        // selection argument
        String[] selectionArgs = {product_id};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(PRODUCT_LIST_TABLE_NAME, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }


    /**
     * This method to update user record
     *
     * @param user
     */
    public static void scanUpdateProduct(Context context, ScanProductDatabaseModel user) {
        SQLiteDatabase db = new DBData(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SCAN_PRODUCT_QUANTITY, user.getProductQuantity());

        // updating row
        db.update(SCAN_PRODUCT_LIST_TABLE_NAME, values, SCAN_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(user.getProductId())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param
     * @return true/false
     */
    public static boolean scanCheckProduct(Context context, String product_id) {
        // array of columns to fetch
        String[] columns = {SCAN_PRODUCT_ID};
        SQLiteDatabase db = new DBData(context).getReadableDatabase();
        // selection criteria
        String selection = SCAN_PRODUCT_ID + " = ?";
        // selection argument
        String[] selectionArgs = {product_id};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(SCAN_PRODUCT_LIST_TABLE_NAME, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public static void getAllDataForVolley(final Context context, final String barcode) {

        StringBuffer bufferProduct_Id = new StringBuffer();
        StringBuffer bufferProduct_Name = new StringBuffer();
        StringBuffer bufferQuantity = new StringBuffer();
        StringBuffer bufferPrice = new StringBuffer();
        StringBuffer bufferWeigth = new StringBuffer();
        StringBuffer bufferRetailer = new StringBuffer();
        double tottalWeigth = 0;
        double tottalPrice = 0;
        double tottalQauntity = 0;

        SQLiteDatabase db = new DBData(context).getWritableDatabase();
        String[] FROM = {ID, PRODUCT_ID, PRODUCT_NAME, PRODUCT_QUANTITY, PRODUCT_WIEGTH, PRODUCT_IMAGE, PRODUCT_PRICE,PRODUCT_RETAILR_ID};
        Cursor cursor = db.query(PRODUCT_LIST_TABLE_NAME, FROM, null, null, null, null, null);

        while (cursor.moveToNext()) {

            ProductDatabaseModel temp = new ProductDatabaseModel(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
            String product_id = String.valueOf(temp.getProductId());
            String product_name = String.valueOf(temp.getProductName()).toUpperCase();
            String product_price = String.valueOf(temp.getProductPrice()).toLowerCase();
            String product_quantity = String.valueOf(temp.getProductQuantity()).toLowerCase();
            String product_weigth = String.valueOf(temp.getProductQuantity()).toLowerCase();
            String product_retailer = String.valueOf(temp.getProduct_retailer_id()).toLowerCase();

            if (bufferProduct_Id.length() == 0) {
                bufferProduct_Id.append(product_id);
            } else {
                bufferProduct_Id.append("," + product_id);
            }

            if (bufferRetailer.length() == 0) {
                bufferRetailer.append(product_retailer);
            } else {
                bufferRetailer.append("," + product_retailer);
            }

            if (bufferProduct_Name.length() == 0) {
                bufferProduct_Name.append(product_name);
            } else {
                bufferProduct_Name.append("," + product_name);
            }
            if (bufferWeigth.length() == 0) {
                bufferWeigth.append(product_weigth);
            } else {
                bufferWeigth.append("," + product_weigth);
            }

            if (bufferQuantity.length() == 0) {
                bufferQuantity.append(product_quantity);

            } else {
                bufferQuantity.append("," + product_quantity);
            }

            if (bufferPrice.length() == 0) {
                bufferPrice.append(product_price);
            } else {
                bufferPrice.append("," + product_price);
            }
            tottalPrice = tottalPrice + Double.parseDouble(product_price);
            tottalQauntity = tottalQauntity + Double.parseDouble(product_quantity);
            tottalWeigth = tottalWeigth + Double.parseDouble(product_weigth);
           // Log.e("DAYS", tottalQauntity + " : " + tottalPrice + ":" + bufferProduct_Id);
        }
        cursor.close();
        db.close();
        if (barcode != null) {
            submitVolley(context, barcode, String.valueOf(bufferProduct_Id), String.valueOf(bufferProduct_Name), String.valueOf(bufferQuantity), String.valueOf(bufferPrice), String.valueOf(tottalPrice), String.valueOf(tottalQauntity),String.valueOf(tottalWeigth),String.valueOf(bufferWeigth),String.valueOf(bufferRetailer));
        }
    }

    public static void submitVolley(final Context mContext, final String trolleyId, final String str_product_id, final String str_ProductName, final String str_productQuantity, final String str_ProductPrice, final String str_totalPrice, final String str_totalQuantity, final String str_totalWeigth, final String str_productWeigth, final String str_productRetailer) {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        final SessionManagement sd = new SessionManagement(mContext);
        ConnectionDetector cd = new ConnectionDetector(mContext);
        if (!cd.isConnectingToInternet()) {
            Snackbar.make(((Activity) mContext).findViewById(android.R.id.content), R.string.NoConnection, Snackbar.LENGTH_SHORT).show();

        } else {
            //  progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.SEND_PRELIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject(response);
                                String result = jsonObject.getString("response");
                                if (result.equalsIgnoreCase("success")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(mContext, "Submit Succesfully", Toast.LENGTH_SHORT).show();
                                    sd.setUserTrolleyId(trolleyId);
                                    Intent i = new Intent(mContext, ScanBarcodeActivity.class);
                                //    Intent i = new Intent(mContext, BlueToothMainActivity.class);
                                    mContext.startActivity(i);
                                    ((Activity) mContext).finish();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(mContext, "Submit Unsuccesfully", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(mContext, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(mContext, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", sd.getUserId());
                    params.put("product_id", str_product_id);
                    params.put("quantity", str_productQuantity);
                    params.put("price", str_ProductPrice);
                    params.put("weight", str_productWeigth);
                    params.put("totalquantity", str_totalQuantity);
                    params.put("totalamount", str_totalPrice);
                    params.put("totalweight", str_totalWeigth);

                    return params;
                }
            };
            stringRequest.setRetryPolicy(new
                    DefaultRetryPolicy(50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(stringRequest);
        }
    }


 public static void getScanAllDataForVolley(final Context context, final String barcode) {

                        StringBuffer bufferProduct_Id = new StringBuffer();
                        StringBuffer bufferProduct_Name = new StringBuffer();
                        StringBuffer bufferQuantity = new StringBuffer();
                        StringBuffer bufferPrice = new StringBuffer();
                        StringBuffer bufferWeigth = new StringBuffer();
                        StringBuffer bufferRetailerId = new StringBuffer();
                        double tottalWeigth = 0;
                        double tottalPrice = 0;
                        double tottalQauntity = 0;
                        SQLiteDatabase db = new DBData(context).getWritableDatabase();
                        String[] FROM = {SCAN_ID, SCAN_PRODUCT_ID, SCAN_PRODUCT_NAME, SCAN_PRODUCT_QUANTITY, SCAN_PRODUCT_WIEGTH, SCAN_PRODUCT_IMAGE, SCAN_PRODUCT_PRICE,SCAN_PRODUCT_RETAILR_ID};
                        Cursor cursor = db.query(SCAN_PRODUCT_LIST_TABLE_NAME, FROM, null, null, null, null, null);

                        while (cursor.moveToNext()) {

                            ScanProductDatabaseModel temp = new ScanProductDatabaseModel(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
                            String product_id = String.valueOf(temp.getProductId());
                            String product_name = String.valueOf(temp.getProductName()).toUpperCase();
                            String product_price = String.valueOf(temp.getProductPrice()).toLowerCase();
                            String product_quantity = String.valueOf(temp.getProductQuantity()).toLowerCase();
                            String product_weigth = String.valueOf(temp.getProductQuantity()).toLowerCase();
                            String product_product_id = String.valueOf(temp.getProduct_retailer_id()).toLowerCase();

                            if (bufferProduct_Id.length() == 0) {
                                bufferProduct_Id.append(product_id);
                            } else {
                                bufferProduct_Id.append("," + product_id);
                            }

                            if (bufferRetailerId.length() == 0) {
                                bufferRetailerId.append(product_product_id);
                            } else {
                                bufferRetailerId.append("," + product_product_id);
                            }
                            if (bufferWeigth.length() == 0) {
                                bufferWeigth.append(product_weigth);
                            } else {
                                bufferWeigth.append("," + product_weigth);
                            }
                            if (bufferProduct_Name.length() == 0) {
                                bufferProduct_Name.append(product_name);
                            } else {
                                bufferProduct_Name.append("," + product_name);
                            }

                            if (bufferQuantity.length() == 0) {
                                bufferQuantity.append(product_quantity);

                            } else {
                                bufferQuantity.append("," + product_quantity);
                            }

                            if (bufferPrice.length() == 0) {
                                bufferPrice.append(product_price);
                            } else {
                                bufferPrice.append("," + product_price);
                            }
                            tottalPrice = tottalPrice + Double.parseDouble(product_price);
                            tottalQauntity = tottalQauntity + Double.parseDouble(product_quantity);
                            tottalWeigth = tottalWeigth + Double.parseDouble(product_weigth);
                            Log.e("DAYS", tottalQauntity + " : " + tottalPrice + ":" + bufferProduct_Id);
                        }
                        cursor.close();
                        db.close();
                        if (barcode != null) {
                            submitScanVolley(context, barcode, String.valueOf(bufferProduct_Id), String.valueOf(bufferProduct_Name), String.valueOf(bufferQuantity), String.valueOf(bufferPrice), String.valueOf(tottalPrice), String.valueOf(tottalQauntity),String.valueOf(tottalWeigth),String.valueOf(bufferWeigth),String.valueOf(bufferRetailerId));

                        }
                    }


    public static void submitScanVolley(final Context mContext, final String trolleyId, final String str_product_id, final String str_ProductName, final String str_productQuantity, final String str_ProductPrice, final String str_totalPrice, final String str_totalQuantity, final String str_totalWeigth, final String str_productWeigth, final String str_productRetailerId) {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        final SessionManagement sd = new SessionManagement(mContext);
        ConnectionDetector cd = new ConnectionDetector(mContext);
        if (!cd.isConnectingToInternet()) {
            Snackbar.make(((Activity) mContext).findViewById(android.R.id.content), R.string.NoConnection, Snackbar.LENGTH_SHORT).show();

        } else {
            //  progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.CHECK_OUT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject(response);
                                String result = jsonObject.getString("response");
                                if (result.equalsIgnoreCase("success")) {
                                    progressDialog.dismiss();
                                   deleteAllExersice(mContext,0);
                                    Toast.makeText(mContext, "CheckOut Succesfully", Toast.LENGTH_SHORT).show();

                                   sd.setUserTrolleyId("USER_TROLLEY_ID");
                                    Intent i = new Intent(mContext, CheckOutActivity.class);
                                    mContext.startActivity(i);
                                    ((Activity) mContext).finish();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(mContext, "CheckOut Unsuccesfully", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(mContext, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(mContext, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", sd.getUserId());
                    params.put("trolley_id", trolleyId);
                    params.put("product_id", str_product_id);
                    params.put("quantity", str_productQuantity);
                    params.put("price", str_ProductPrice);
                    params.put("weight", str_productWeigth);
                    params.put("totalquantity", str_totalQuantity);
                    params.put("totalamount", str_totalPrice);
                    params.put("totalweight", str_totalWeigth);
                    params.put("retailer_id", str_productRetailerId);

                    return params;
                }
            };
            stringRequest.setRetryPolicy(new
                    DefaultRetryPolicy(50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(stringRequest);
        }
    }
}
