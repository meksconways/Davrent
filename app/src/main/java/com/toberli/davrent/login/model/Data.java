package com.toberli.davrent.login.model;

import com.squareup.moshi.Json;

public class Data {


    @Json(name = "id")
    public final Integer id;
    @Json(name = "is_admin")
    public final Integer isAdmin;
    @Json(name = "token")
    public final String token;


    public Data(Integer id, Integer isAdmin, String token) {
        this.id = id;
        this.isAdmin = isAdmin;
        this.token = token;
    }
}
