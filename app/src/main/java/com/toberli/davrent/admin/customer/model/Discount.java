package com.toberli.davrent.admin.customer.model;

import com.squareup.moshi.Json;

public class Discount {

    @Json(name = "id")
    public final Integer id;
    @Json(name = "title")
    public final String title;
    @Json(name = "description")
    public final String description;
    @Json(name = "percent")
    public final Integer percent;
    @Json(name = "created_at")
    public final String createdAt;
    @Json(name = "updated_at")
    public final String updatedAt;

    public Discount(Integer id, String title, String description,
                    Integer percent, String createdAt, String updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.percent = percent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
