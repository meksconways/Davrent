package com.toberli.davrent.admin.discount.model;

import com.squareup.moshi.Json;

import java.util.List;

public class DiscountModel {

    @Json(name = "data")
    public final List<Data> dataList;

    public DiscountModel(List<Data> dataList) {
        this.dataList = dataList;
    }
}
