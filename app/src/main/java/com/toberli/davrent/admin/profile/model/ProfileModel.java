package com.toberli.davrent.admin.profile.model;

import com.squareup.moshi.Json;

public class ProfileModel {


    @Json(name = "data")
    public final Data data;


    public ProfileModel(Data data) {
        this.data = data;
    }
}
