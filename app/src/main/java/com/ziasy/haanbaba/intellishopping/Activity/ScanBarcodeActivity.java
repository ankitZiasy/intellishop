package com.ziasy.haanbaba.intellishopping.Activity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.ziasy.haanbaba.intellishopping.Adapter.PrelistDatabaseAdapter;
import com.ziasy.haanbaba.intellishopping.Adapter.ScanPrelistDatabaseAdapter;
import com.ziasy.haanbaba.intellishopping.Common.Connection;
import com.ziasy.haanbaba.intellishopping.Common.ConnectionDetector;
import com.ziasy.haanbaba.intellishopping.Common.Constant;
import com.ziasy.haanbaba.intellishopping.Common.RecyclerItemTouchHelper;
import com.ziasy.haanbaba.intellishopping.Common.ScanRecyclerItemTouchHelper;
import com.ziasy.haanbaba.intellishopping.Common.SessionManagement;
import com.ziasy.haanbaba.intellishopping.DB.DBUtil;
import com.ziasy.haanbaba.intellishopping.DB.ProductDatabaseModel;
import com.ziasy.haanbaba.intellishopping.DB.ScanProductDatabaseModel;
import com.ziasy.haanbaba.intellishopping.R;
import com.ziasy.haanbaba.intellishopping.bluetooth.BlueToothMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Date.parse;
import static junit.framework.Assert.assertNotNull;

public class ScanBarcodeActivity extends AppCompatActivity implements ScanRecyclerItemTouchHelper.ScanRecyclerItemTouchHelperListener {

    private static final String BARCODE_KEY = "BARCODE";
    private Barcode barcodeResult;
    private TextView txtTrolley;
    private SessionManagement sd;
    private RequestQueue queue;
    private TextView txtWeightShow, txtQuantityShow, txtPriceShow;
    private RecyclerView recyclerViewId;
    private ConnectionDetector cd;
    private String oldQuantity;
    private LinearLayout linear_next;
    private List<ScanProductDatabaseModel> list;
    private ScanPrelistDatabaseAdapter scanPrelistDatabaseAdapter;

    private static final int REQUEST_ENABLE_BT = 12;

    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanning_layout);

        cd = new ConnectionDetector(ScanBarcodeActivity.this);
        txtPriceShow = (TextView) findViewById(R.id.txtPriceShow);
        txtQuantityShow = (TextView) findViewById(R.id.txtQuantityShow);
        txtWeightShow = (TextView) findViewById(R.id.txtWeightShow);
        txtTrolley = (TextView) findViewById(R.id.txtTrolleyId);
        linear_next = (LinearLayout) findViewById(R.id.linear_next);
        sd = new SessionManagement(ScanBarcodeActivity.this);
        txtTrolley.setText("Your Trolley Id : " + sd.getUserTrolleyId());
        recyclerViewId = (RecyclerView) findViewById(R.id.recyclerViewId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ScanBarcodeActivity.this);
        recyclerViewId.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        list = DBUtil.scanfetchAllDay(ScanBarcodeActivity.this);
        queue = Volley.newRequestQueue(this);
        scanPrelistDatabaseAdapter = new ScanPrelistDatabaseAdapter(ScanBarcodeActivity.this, list);
        recyclerViewId.setAdapter(scanPrelistDatabaseAdapter);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assertNotNull(recyclerViewId);
        assertNotNull(fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cd.isConnectingToInternet()) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.NoConnection, Snackbar.LENGTH_SHORT).show();
                } else {
                    startScan();
                }
            }
        });
        if (savedInstanceState != null) {
            Barcode restoredBarcode = savedInstanceState.getParcelable(BARCODE_KEY);
            if (restoredBarcode != null) {

                barcodeResult = restoredBarcode;
            }
        }
        linear_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScanBarcodeActivity.this);
                alertDialogBuilder.setMessage("Are You  Completed Your Shopping");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (list.size() != 0) {
                                    Intent i = new Intent(ScanBarcodeActivity.this, BlueToothMainActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScanBarcodeActivity.this);
                                    alertDialogBuilder.setMessage("Please Frist Start Shop For Checkout");
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();

                                }
                            }
                        });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.setCancelable(false);
            }
        });
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ScanRecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerViewId);
      /*  setBluetoothData();
        Log.e("setBluetoothData","setBluetoothData");

        if (Connection.blueTooth()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }*/
        showTotal(list);
    }

    public void showTotal(List<ScanProductDatabaseModel> list) {
        Double total = 00.0;
        int qountity = 0, weight = 0;
        if (list!=null){
        for (int i = 0; i < list.size(); i++) {
            weight = weight + Integer.parseInt(list.get(i).getProductWeigth());
            qountity = Integer.parseInt(qountity + list.get(i).getProductQuantity());
            total = total + Double.valueOf(list.get(i).getProductWeigth());
        }
        txtQuantityShow.setText(String.valueOf(qountity));
        txtWeightShow.setText(String.valueOf(weight));
        txtPriceShow.setText(String.valueOf(qountity*total));
    }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        txtTrolley.setText("");
        setBluetoothData();
    }

    private void setBluetoothData() {

        // Getting the Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        txtTrolley.append("\nAdapter: " + bluetoothAdapter.toString() + "\n\nName: " + bluetoothAdapter.getName() + "\nAddress: " + bluetoothAdapter.getAddress());

        // Check for Bluetooth support in the first place
        // Emulator doesn't support Bluetooth and will return null
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth NOT supported. Aborting.",
                    Toast.LENGTH_LONG).show();
        }

        // Starting the device discovery
        txtTrolley.append("\n\nStarting discovery...");
        bluetoothAdapter.startDiscovery();
        txtTrolley.append("\nDone with discovery...\n");

        // Listing paired devices
        txtTrolley.append("\nDevices Pared:");
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            txtTrolley.append("\nFound device: " + device.getName() + " Add: " + device.getAddress());
        }
    }

    public void deleteExercise(Context mContext, int position) {
        ScanProductDatabaseModel exercise = list.get(position);
        DBUtil.scandeleteDay(mContext, exercise.getProductId());
        Toast.makeText(mContext, "DELETE", Toast.LENGTH_SHORT).show();
        list.remove(position);
        scanPrelistDatabaseAdapter.notifyItemRemoved(position);
        scanPrelistDatabaseAdapter.notifyDataSetChanged();
    }

    private void makeJsonObjReq(String barcode) {
        if (!cd.isConnectingToInternet()) {
            Snackbar.make(findViewById(android.R.id.content), R.string.NoConnection, Snackbar.LENGTH_SHORT).show();
        } else {
            Map<String, String> postParam = new HashMap<String, String>();
            postParam.put("qrcode", barcode);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constant.SEND_BARCODE, new JSONObject(postParam),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //   Log.d(TAG, response.toString());

                            try {
                                String code = response.getString("code");
                                if (code.equalsIgnoreCase("200")) {
                                    JSONArray array = response.getJSONArray("response");
                                    for (int i = 0; i <= array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        String id = object.getString("id");
                                        String retailerId = object.getString("user_id");
                                        String name = object.getString("name");
                                        String description = object.getString("description");
                                        String quantity = object.getString("quantity");
                                        String amount = object.getString("amount");
                                        String weight = object.getString("weight");
                                        String picture = object.getString("picture");
                                        String date = object.getString("date");

                                        String path = "http://meetintelli.com/intellishopping/uploads/product/";
                                        if (!DBUtil.scanCheckProduct(ScanBarcodeActivity.this, id)) {

                                            ScanProductDatabaseModel databaseModel = DBUtil.scanproductInsert(ScanBarcodeActivity.this, Integer.parseInt(id), name, quantity, weight, path + picture, amount, retailerId);
                                            DBUtil.deleteDay(ScanBarcodeActivity.this, Integer.parseInt(id));
                                            list.add(databaseModel);
                                            if (scanPrelistDatabaseAdapter != null) {
                                                scanPrelistDatabaseAdapter.notifyDataSetChanged();
                                            }

                                        } else {
                                            for (ScanProductDatabaseModel model : list) {
                                                if (model.getProductId() == Integer.parseInt(id)) {
                                                    oldQuantity = model.getProductQuantity();
                                                    int totalQuantity = Integer.parseInt(oldQuantity) + Integer.parseInt(quantity);
                                                    model.setProductQuantity(String.valueOf(totalQuantity));
                                                }
                                            }
                                            Snackbar.make(findViewById(android.R.id.content), "Already Add", Snackbar.LENGTH_SHORT).show();
                                            int totalQuantity = Integer.parseInt(oldQuantity) + Integer.parseInt(quantity);
                                            Snackbar.make(findViewById(android.R.id.content), "Total Quantity : " + totalQuantity, Snackbar.LENGTH_SHORT).show();
                                            DBUtil.scanUpdateProduct(ScanBarcodeActivity.this, new ScanProductDatabaseModel(1, Integer.parseInt(id), name, String.valueOf(totalQuantity), weight, path + picture, amount, retailerId));
                                            if (scanPrelistDatabaseAdapter != null) {
                                                scanPrelistDatabaseAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        showTotal(list);

                                        //txtWeightShow.setText("");
                                        Toast.makeText(ScanBarcodeActivity.this, "Add", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ScanBarcodeActivity.this, "Not Available in list", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //  VolleyLog.d(TAG, "Error: " + error.getMessage());
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
            queue.add(jsonObjReq);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.prelist:
                Intent i = new Intent(ScanBarcodeActivity.this, PreList_Activity.class);
                startActivity(i);
                break;
            default:
                break;
        }
        return true;
    }

    private void startScan() {
        /**
         * Build a new MaterialBarcodeScanner
         */
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(ScanBarcodeActivity.this)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withCenterTracker()
                .withText("Scanning...")
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                        barcodeResult = barcode;
                        makeJsonObjReq(barcode.rawValue);
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BARCODE_KEY, barcodeResult);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MaterialBarcodeScanner.RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScan();
            return;
        }
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(android.R.string.ok, listener)
                .show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ScanPrelistDatabaseAdapter.prelistHolder) {
            // get the removed item name to display it in snack bar
            String name = list.get(viewHolder.getAdapterPosition()).getProductName();

            // backup of removed item for undo purpose
            final ScanProductDatabaseModel deletedItem = list.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            scanPrelistDatabaseAdapter.removeItem(viewHolder.getAdapterPosition());


            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    scanPrelistDatabaseAdapter.restoreItem(deletedItem, deletedIndex);


                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

}