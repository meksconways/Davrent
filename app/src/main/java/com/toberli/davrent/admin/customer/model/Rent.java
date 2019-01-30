package com.toberli.davrent.admin.customer.model;

import com.squareup.moshi.Json;

import java.util.List;

public class Rent {

    @Json(name = "id")
    public final Integer id;
    @Json(name = "rent_hours")
    public final Integer rentHours;
    @Json(name = "expiration_at")
    public final String expirationAt;
    @Json(name = "return_at")
    public final Object returnAt;
    @Json(name = "total_price")
    public final String totalPrice;
    @Json(name = "discount_price")
    public final String discountPrice;
    @Json(name = "end_price")
    public final String endPrice;
    @Json(name = "created_at")
    public final String createdAt;
    @Json(name = "updated_at")
    public final String updatedAt;
    @Json(name = "products")
    public final List<Product> products = null;


    public Rent(Integer id, Integer rentHours, String expirationAt, Object returnAt, String totalPrice,
                String discountPrice, String endPrice, String createdAt, String updatedAt) {
        this.id = id;
        this.rentHours = rentHours;
        this.expirationAt = expirationAt;
        this.returnAt = returnAt;
        this.totalPrice = totalPrice;
        this.discountPrice = discountPrice;
        this.endPrice = endPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
