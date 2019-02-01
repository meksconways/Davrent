package com.toberli.davrent.admin.customer.customerdetail.rentproduct.model;

import com.squareup.moshi.Json;

public class CalculateRentModel {

    @Json(name = "data")
    public final Data data;


    public CalculateRentModel(Data data) {
        this.data = data;
    }
}
