package com.toberli.davrent.admin.product.model;

import com.squareup.moshi.Json;

public class Code {


    @Json(name = "id")
    public final Integer id;
    @Json(name = "code")
    public final String code;
    @Json(name = "created_at")
    public final String createdAt;
    @Json(name = "updated_at")
    public final String updatedAt;


    public Code(Integer id, String code, String createdAt, String updatedAt) {
        this.id = id;
        this.code = code;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
