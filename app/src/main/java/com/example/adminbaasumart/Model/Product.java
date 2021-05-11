package com.example.adminbaasumart.Model;

public class Product {
    private String Imageurl;
    private String productId;
    private String category;
    private String datetime;
    private String desc;
    private String discount;
    private String price;
    private String productName;
    private String rating;

    public Product(String Imageurl, String productId, String category,
                   String datetime, String desc, String discount, String price,
                   String productName, String rating) {
        this.Imageurl = Imageurl;
        this.productId = productId;
        this.category = category;
        this.datetime = datetime;
        this.desc = desc;
        this.discount = discount;
        this.price = price;
        this.productName = productName;
        this.rating = rating;
    }

    public Product() {
    }

    public String getImageurl() {
        return Imageurl;
    }

    public void setImageurl(String imageurl) {
        Imageurl = imageurl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
