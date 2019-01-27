package com.toberli.davrent.admin.categories.model;

import com.squareup.moshi.Json;

import java.util.List;

public class CategoriesModel {


    @Json(name = "data")
    public final List<Data> data;


    public CategoriesModel(List<Data> data) {
        this.data = data;
    }
}
