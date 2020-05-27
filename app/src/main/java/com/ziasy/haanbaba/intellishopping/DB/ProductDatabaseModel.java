package com.ziasy.haanbaba.intellishopping.DB;

/**
 * Created by ANDROID on 14-Sep-17.
 */

public class ProductDatabaseModel {
    int id;

    public int getId() {
        return id;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public ProductDatabaseModel(int id, int productId, String productName, String productQuantity, String productWeigth, String productImage, String productPrice, String product_retailer_id) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productWeigth = productWeigth;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.product_retailer_id = product_retailer_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductWeigth() {
        return productWeigth;
    }

    public void setProductWeigth(String productWeigth) {
        this.productWeigth = productWeigth;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    int productId;
    String productName;
    String productQuantity;
    String productWeigth;
    String productImage;
    String productPrice;

    public String getProduct_retailer_id() {
        return product_retailer_id;
    }

    public void setProduct_retailer_id(String product_retailer_id) {
        this.product_retailer_id = product_retailer_id;
    }

    String product_retailer_id;
  }
