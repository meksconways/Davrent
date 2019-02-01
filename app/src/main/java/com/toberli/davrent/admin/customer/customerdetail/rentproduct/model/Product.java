package com.toberli.davrent.admin.customer.customerdetail.rentproduct.model;

import com.squareup.moshi.Json;

public class Product {

    @Json(name = "id")
    public final Integer id;
    @Json(name = "title")
    public final String title;
    @Json(name = "description")
    public final String description;
    @Json(name = "price")
    public final String price;
    @Json(name = "price_schedule")
    public final Integer priceSchedule;
    @Json(name = "created_at")
    public final String createdAt;
    @Json(name = "updated_at")
    public final String updatedAt;
    @Json(name = "category")
    public final Category category;
    @Json(name = "code")
    public final String code;
    @Json(name = "calculated_price")
    public final String calculatedPrice;

    public Product(Integer id, String title, String description, String price, Integer priceSchedule,
                   String createdAt, String updatedAt, Category category, String code, String calculatedPrice) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.priceSchedule = priceSchedule;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
        this.code = code;
        this.calculatedPrice = calculatedPrice;
    }
}
