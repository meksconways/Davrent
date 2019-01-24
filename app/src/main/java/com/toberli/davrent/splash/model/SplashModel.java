package com.toberli.davrent.splash.model;

import com.squareup.moshi.Json;

public class SplashModel {

    @Json(name = "data")
    public final Data data;


    public SplashModel(Data data) {
        this.data = data;
    }
}
