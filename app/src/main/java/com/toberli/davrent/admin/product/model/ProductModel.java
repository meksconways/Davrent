package com.toberli.davrent.admin.product.model;

import com.squareup.moshi.Json;

import java.util.List;

public class ProductModel {

    @Json(name = "data")
    public final List<Data> data;

    public ProductModel(List<Data> data) {
        this.data = data;
    }
}
