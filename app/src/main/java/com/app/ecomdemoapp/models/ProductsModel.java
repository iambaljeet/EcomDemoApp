package com.app.ecomdemoapp.models;

public class ProductsModel {
    int product_id;
    String product_name;
    String product_category;
    String product_description;
    String product_currency;
    Long product_price;
    Long product_discounted_price;
    Long product_time;

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_currency() {
        return product_currency;
    }

    public void setProduct_currency(String product_currency) {
        this.product_currency = product_currency;
    }

    public Long getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Long product_price) {
        this.product_price = product_price;
    }

    public Long getProduct_discounted_price() {
        return product_discounted_price;
    }

    public void setProduct_discounted_price(Long product_discounted_price) {
        this.product_discounted_price = product_discounted_price;
    }

    public Long getProduct_time() {
        return product_time;
    }

    public void setProduct_time(Long product_time) {
        this.product_time = product_time;
    }
}