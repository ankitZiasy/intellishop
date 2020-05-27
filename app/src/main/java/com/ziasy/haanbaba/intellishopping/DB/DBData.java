package com.ziasy.haanbaba.intellishopping.DB;

/**
 * Created by ANDROID on 14-Sep-17.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.PRODUCT_LIST_TABLE_NAME;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.ID;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.PRODUCT_ID;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.PRODUCT_NAME;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.PRODUCT_PRICE;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.PRODUCT_QUANTITY;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.PRODUCT_RETAILR_ID;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.PRODUCT_WIEGTH;
import static com.ziasy.haanbaba.intellishopping.DB.DBConstants.PRODUCT_IMAGE;
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
 * @author "Sudar Muthu (http://sudarmuthu.com)"
 *
 */
public class DBData extends SQLiteOpenHelper {
    private static final String TAG = "DBData";

    private static final String DATABASE_NAME = "product.db";
    private static final int DATABASE_VERSION = 1;


    private static final String CREATE_PRODUCT_TABLE =
            "CREATE TABLE " + PRODUCT_LIST_TABLE_NAME + " ("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + PRODUCT_ID + " INTEGER,"
                    + PRODUCT_NAME + " TEXT,"
                    + PRODUCT_PRICE + " TEXT,"
                    + PRODUCT_QUANTITY + " TEXT,"
                    + PRODUCT_WIEGTH + " TEXT,"
                    + PRODUCT_IMAGE + " TEXT,"
                    + PRODUCT_RETAILR_ID + " TEXT"
                    + " );";

    private static final String SCAN_CREATE_PRODUCT_TABLE =
            "CREATE TABLE " + SCAN_PRODUCT_LIST_TABLE_NAME + " ("
                    + SCAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SCAN_PRODUCT_ID + " INTEGER,"
                    + SCAN_PRODUCT_NAME + " TEXT,"
                    + SCAN_PRODUCT_PRICE + " TEXT,"
                    + SCAN_PRODUCT_QUANTITY + " TEXT,"
                    + SCAN_PRODUCT_WIEGTH + " TEXT,"
                    + SCAN_PRODUCT_IMAGE + " TEXT,"
                    + SCAN_PRODUCT_RETAILR_ID + " TEXT"
                    + " );";

    private static final String CREATE_TROLLEY_TABLE =
            "CREATE TABLE " + TROLLEY_TABLE_NAME + " ("
                    + TROLLEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TROLLEY_QRCODE + " TEXT"
                    + " );";

    /**
     *
     * @param context
     */
    public DBData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // When the app is installed for the first time

        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(SCAN_CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_TROLLEY_TABLE);
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_PRODUCT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SCAN_CREATE_PRODUCT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TROLLEY_TABLE);
        onCreate(db);
    }
}
