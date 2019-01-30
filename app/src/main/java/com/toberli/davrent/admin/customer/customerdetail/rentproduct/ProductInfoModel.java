package com.toberli.davrent.admin.customer.customerdetail.rentproduct;

import com.squareup.moshi.Json;
import com.toberli.davrent.admin.product.model.Data;

public class ProductInfoModel {

    @Json(name = "data")
    public final Data data;


    public ProductInfoModel(Data data) {
        this.data = data;
    }
}
