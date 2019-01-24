package com.toberli.davrent.login.model;

import com.squareup.moshi.Json;


public class LoginModel {

    @Json(name = "data")
    public final Data data;

    public LoginModel(Data data) {
        this.data = data;
    }
}
