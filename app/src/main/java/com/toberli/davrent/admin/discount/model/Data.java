package com.toberli.davrent.admin.discount.model;

import com.squareup.moshi.Json;

public class Data {

    @Json(name = "id")
    public final Integer id;
    @Json(name = "title")
    public final String title;
    @Json(name = "description")
    public final String desc;
    @Json(name = "percent")
    public final Integer percent;


    public Data(Integer id, String title, String desc, Integer percent) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.percent = percent;
    }
}
