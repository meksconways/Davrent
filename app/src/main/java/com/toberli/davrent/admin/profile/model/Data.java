package com.toberli.davrent.admin.profile.model;

import com.squareup.moshi.Json;

public class Data {


    @Json(name = "name_surname")
    public final String name;
    @Json(name = "phone")
    public final String phone;
    @Json(name = "total_added_customer")
    public final String totalCustomer;
    @Json(name = "total_rents")
    public final String totalRents;


    public Data(String name, String phone, String totalCustomer, String totalRents) {
        this.name = name;
        this.phone = phone;
        this.totalCustomer = totalCustomer;
        this.totalRents = totalRents;
    }
}
