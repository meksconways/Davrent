package com.toberli.davrent.admin.customer.model;

import com.squareup.moshi.Json;

import java.util.List;

public class Data {

    @Json(name = "id")
    public final Integer id;
    @Json(name = "name_surname")
    public final String nameSurname;
    @Json(name = "phone")
    public final String phone;
    @Json(name = "user_id")
    public final Integer userId;
    @Json(name = "created_at")
    public final String createdAt;
    @Json(name = "updated_at")
    public final String updatedAt;
    @Json(name = "type")
    public final Type type;
    @Json(name = "rents")
    public final List<Rent> rents;

    public Data(Integer id, String nameSurname, String phone,
                Integer userId, String createdAt,
                String updatedAt, Type type, List<Rent> rents) {
        this.id = id;
        this.nameSurname = nameSurname;
        this.phone = phone;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.type = type;
        this.rents = rents;
    }
}
