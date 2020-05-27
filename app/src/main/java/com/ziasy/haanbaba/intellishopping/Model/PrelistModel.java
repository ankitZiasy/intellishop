package com.ziasy.haanbaba.intellishopping.Model;

public class PrelistModel {
    String id;
    String name;
    String Image;
    String path;
    String weigth;
    String price;

    public PrelistModel(String id, String name, String image, String path, String price, String weigth) {
        this.id = id;
        this.name = name;
        this.Image = image;
        this.path = path;
        this.price = price;
        this.weigth = weigth;
    }

    public String getPrice() {

        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getWeigth() {
        return weigth;
    }

    public void setWeigth(String weigth) {
        this.weigth = weigth;
    }


}
