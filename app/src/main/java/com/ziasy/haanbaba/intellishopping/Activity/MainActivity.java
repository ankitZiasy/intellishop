package com.ziasy.haanbaba.intellishopping.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ziasy.haanbaba.intellishopping.Adapter.DrawerItemCustomAdapter;
import com.ziasy.haanbaba.intellishopping.Common.SessionManagement;
import com.ziasy.haanbaba.intellishopping.Fragment.AddFamily_Fragment;
import com.ziasy.haanbaba.intellishopping.Fragment.CartFragment;
import com.ziasy.haanbaba.intellishopping.Fragment.HomeFragment;
import com.ziasy.haanbaba.intellishopping.Fragment.OerderFragment;
import com.ziasy.haanbaba.intellishopping.Model.DataModel;
import com.ziasy.haanbaba.intellishopping.R;


public class MainActivity extends AppCompatActivity {

    Fragment fragment = null;
    FragmentManager fm;

    FragmentTransaction ft;
    private String[] mNavigationDrawerItemTitles;
    static int count = 0;
    static int coutn1 = 0;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private SessionManagement sd;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout linearOffer,linearNotification;
    TextView txt_name, txt_email;
    DataModel drawerItem[];
    //ImageButton img_edit;

    ProgressDialog progressDialog;
    private static final int TIME_INTERVAL = 3000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    ImageView image_walllet;
    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
             sd=new SessionManagement(MainActivity.this);
            mTitle = mDrawerTitle = getTitle();
            mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerList = (ListView) findViewById(R.id.drawer);
            image_walllet = (ImageView) findViewById(R.id.walllet);

            setupToolbar();

//------------------------------------------------------------------------------------------------------------------------
            image_walllet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(MainActivity.this,NotificationActivity.class);
                    startActivity(i);
                }
            });
            drawerItem = new DataModel[5];
            drawerItem[0] = new DataModel(R.drawable.home, "Home");
          //  drawerItem[1] = new DataModel(R.drawable.shopping, "Cart");
            drawerItem[1] = new DataModel(R.drawable.list, "Oreder History");
            drawerItem[2] = new DataModel(R.drawable.favorite, "My Family");
            drawerItem[3] = new DataModel(R.drawable.question, "Support");
            drawerItem[4] = new DataModel(R.drawable.share, "Share");


            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
            mDrawerList.setAdapter(adapter);
            mDrawerList.deferNotifyDataSetChanged();
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            profileImage();
            setupDrawerToggle();
            UnPaidSelectedItem(1);
            /*         socialFooter();*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            UnPaidSelectedItem(position);

        }

    }

    private void profileImage() {

        LayoutInflater myinflater = getLayoutInflater();
        ViewGroup myHeader = (ViewGroup) myinflater.inflate(R.layout.nav_header_main, mDrawerList, false);

        linearOffer = (LinearLayout) myHeader.findViewById(R.id.linearOffer);
        linearNotification = (LinearLayout) myHeader.findViewById(R.id.linearNotification);
        txt_name = (TextView) myHeader.findViewById(R.id.txt_name);
        txt_email = (TextView) myHeader.findViewById(R.id.txt_email);

        txt_email.setText(sd.getUserEmail());

        txt_name.setText("Name : "+sd.getUserName());

        linearOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,OfferActivity.class);
                startActivity(i);
            }
        });
        linearNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,NotificationActivity.class);
                startActivity(i);
            }
        });
        mDrawerList.addHeaderView(myHeader, null, false);
    }

    private void UnPaidSelectedItem(int position) {

     //   String title = getString(R.string.app_name);

        switch (position) {

            case 1:
                mDrawerLayout.closeDrawers();
                fragment = new HomeFragment();
                coutn1 = 1;
                count = 0;
                break;
        /*    case 2:
                mDrawerLayout.closeDrawers();
                fragment = new CartFragment();
                count = 1;
                coutn1 = 1;
                break;
*/

            case 2:
                mDrawerLayout.closeDrawers();
                fragment = new OerderFragment();
          //      Toast.makeText(MainActivity.this, "Working", Toast.LENGTH_SHORT).show();

                count = 2;
                coutn1 = 1;
                break;




            case 3:
                mDrawerLayout.closeDrawers();
                fragment = new AddFamily_Fragment();
                coutn1 = 1;
                count = 4;
                break;


            case 4:
                mDrawerLayout.closeDrawers();
                Toast.makeText(MainActivity.this, "Working", Toast.LENGTH_SHORT).show();
                coutn1 = 1;
                count = 5;
                break;


            case 5:
                mDrawerLayout.closeDrawers();
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.
                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
                share.putExtra(Intent.EXTRA_TEXT, "http://www.codeofaninja.com");

                startActivity(Intent.createChooser(share, "Share link!"));
                count = 6;
                coutn1 = 1;
                break;
                default:
                break;
        }

        if (fragment != null) {

            if (coutn1 == 1) {


                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
                Log.e("Count value", "" + count);


            }
            if (count == 0) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
                Log.e("Count value", "" + count);

            } else {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack(null);
                fragmentTransaction.commit();
                Log.e("Count value", "" + count);

                //getSupportActionBar().setTitle(title);
            }
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        switch (item.getItemId()){
            case R.id.logout:
                new AlertDialog.Builder(this)

                        .setTitle("intelliShopping")
                        .setMessage("Are you sure you want to Logout?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        sd.setLoginStatus("false");
                                        sd.setUserTrolleyId("USER_TROLLEY_ID");

                                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                        sd.setLoginStatus("false");
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                                        startActivity(i);
                                        overridePendingTransition(R.anim.fadein, R.anim.fade_out);
                                        ActivityCompat.finishAffinity(MainActivity.this);
                                        int pid = android.os.Process.myPid();
                                        android.os.Process.killProcess(pid);

                                    }

                                }).setNegativeButton("No", null).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try {
            mDrawerToggle.syncState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    void setupDrawerToggle() {
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
         getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


  /*  @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCount == 0) {

        } else {
            super.onBackPressed();
        }

    }*/



    @Override
    public void onBackPressed() {

        if (coutn1  == 1) {

            fragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();



            coutn1++;

        } else {
            int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
         if (backStackCount == 0) {

                new AlertDialog.Builder(this)

                        .setTitle("Intelli Shopping")
                        .setMessage("Are you sure you want to close?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        ActivityCompat.finishAffinity(MainActivity.this);
                                        int pid = android.os.Process.myPid();

                                        android.os.Process.killProcess(pid);
                                    }

                                }).setNegativeButton("No", null).show();
            } else {
                super.onBackPressed();
             getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

        }
    }
}