package com.toberli.davrent.admin.staff.model;

import com.squareup.moshi.Json;

import java.util.List;

public class AdminStaffModel {


    @Json(name = "data")
    public final List<Data> data;


    public AdminStaffModel(List<Data> data) {
        this.data = data;
    }
}
