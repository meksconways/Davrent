package com.toberli.davrent.admin.discount.editdismodel;

import com.squareup.moshi.Json;

import java.util.List;

public class EditDiscountModel {

    @Json(name = "data")
    public final List<String> data;


    public EditDiscountModel(List<String> data) {
        this.data = data;
    }
}
