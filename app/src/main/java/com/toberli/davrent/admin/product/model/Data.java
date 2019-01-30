package com.toberli.davrent.admin.product.model;

import com.squareup.moshi.Json;

public class Data {

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
    @Json(name = "code")
    public final Code code;
    @Json(name = "category")
    public final Category category;

    public Data(Integer id, String title, String description, String price, Integer priceSchedule,
                String createdAt, String updatedAt, Code code, Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.priceSchedule = priceSchedule;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.code = code;
        this.category = category;
    }
}
