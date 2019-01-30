package com.toberli.davrent.admin.customer.model;

import com.squareup.moshi.Json;

public class Type {

    @Json(name = "id")
    public final Integer id;
    @Json(name = "title")
    public final String title;
    @Json(name = "description")
    public final String description;
    @Json(name = "created_at")
    public final String createdAt;
    @Json(name = "updated_at")
    public final String updatedAt;
    @Json(name = "discount")
    public final Discount discount;

    public Type(Integer id, String title, String description,
                String createdAt, String updatedAt, Discount discount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.discount = discount;
    }
}
