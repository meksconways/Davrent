package com.toberli.davrent.admin.customer.model;

import com.squareup.moshi.Json;

import java.util.List;

public class CustomerModel {


    @Json(name = "data")
    public final List<Data> data;


    public CustomerModel(List<Data> data) {
        this.data = data;
    }
}
