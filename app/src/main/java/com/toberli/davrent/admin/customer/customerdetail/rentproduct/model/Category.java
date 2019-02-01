package com.toberli.davrent.admin.customer.customerdetail.rentproduct.model;

import com.squareup.moshi.Json;

public class Category {

    @Json(name = "id")
    public final Integer id;
    @Json(name = "name")
    public final String name;
    @Json(name = "description")
    public final String description;
    @Json(name = "created_at")
    public final String createdAt;
    @Json(name = "updated_at")
    public final String updatedAt;

    public Category(Integer id, String name, String description, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
