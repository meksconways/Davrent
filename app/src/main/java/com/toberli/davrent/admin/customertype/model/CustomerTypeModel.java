package com.toberli.davrent.admin.customertype.model;

import com.squareup.moshi.Json;

import java.util.List;

public class CustomerTypeModel {

    @Json(name = "data")
    public final List<Data> data;


    public CustomerTypeModel(List<Data> data) {
        this.data = data;
    }
}
