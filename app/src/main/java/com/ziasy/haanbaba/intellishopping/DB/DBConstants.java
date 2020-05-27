package com.ziasy.haanbaba.intellishopping.DB;

import android.provider.BaseColumns;

/**
 * Created by ANDROID on 14-Sep-17.
 */

public interface DBConstants extends BaseColumns {
    //product prelist  Category table
    public static final String PRODUCT_LIST_TABLE_NAME = "product_list";
    //columns in the entry table
    public static final String ID = "ID";
    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String PRODUCT_PRICE = "PRODUCT_PRICE";
    public static final String PRODUCT_QUANTITY = "PRODUCT_QUANTITY";
    public static final String PRODUCT_WIEGTH = "PRODUCT_WIEGTH";
    public static final String PRODUCT_IMAGE = "PRODUCT_IMAGE";
    public static final String PRODUCT_RETAILR_ID = "PRODUCT_RETAILR_ID";

//Exercise  Category table
    public static final String SCAN_PRODUCT_LIST_TABLE_NAME = "scan_product_list";
    //columns in the entry table
    public static final String SCAN_ID = "ID";
    public static final String SCAN_PRODUCT_ID = "PRODUCT_ID";
    public static final String SCAN_PRODUCT_NAME = "PRODUCT_NAME";
    public static final String SCAN_PRODUCT_PRICE = "PRODUCT_PRICE";
    public static final String SCAN_PRODUCT_QUANTITY = "PRODUCT_QUANTITY";
    public static final String SCAN_PRODUCT_WIEGTH = "PRODUCT_WIEGTH";
    public static final String SCAN_PRODUCT_IMAGE = "PRODUCT_IMAGE";
    public static final String SCAN_PRODUCT_RETAILR_ID = "SCAN_PRODUCT_RETAILR_ID";

    public static final String TROLLEY_TABLE_NAME="TROLLEY_TABLE_NAME";

    public static final String TROLLEY_ID="TROLLEY_ID";
    public static final String TROLLEY_QRCODE="TROLLEY_QRCODE";



}