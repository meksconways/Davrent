package com.toberli.davrent.admin.customertype.model;

import com.squareup.moshi.Json;

public class Data {

    public final Integer id;
    public final String title;
    public final String description;
    @Json(name = "discount")
    public final Discount discount;


    public Data(Integer id, String title, String description, Discount discount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.discount = discount;
    }
}
