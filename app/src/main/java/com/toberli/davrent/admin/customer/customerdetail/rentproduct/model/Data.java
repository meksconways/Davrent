package com.toberli.davrent.admin.customer.customerdetail.rentproduct.model;

import com.squareup.moshi.Json;

import java.util.List;

public class Data {

    @Json(name = "discount")
    public final Discount discount;
    @Json(name = "total_price")
    public final String totalPrice;
    @Json(name = "discount_price")
    public final String discountPrice;
    @Json(name = "end_price")
    public final String endPrice;
    @Json(name = "products")
    public final List<Product> products;

    public Data(Discount discount, String totalPrice, String discountPrice, String endPrice, List<Product> products) {
        this.discount = discount;
        this.totalPrice = totalPrice;
        this.discountPrice = discountPrice;
        this.endPrice = endPrice;
        this.products = products;
    }
}
