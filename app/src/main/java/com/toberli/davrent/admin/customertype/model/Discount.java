package com.toberli.davrent.admin.customertype.model;

public class Discount {

    public final Integer id;
    public final String title;
    public final Integer percent;


    public Discount(Integer id, String title, Integer percent) {
        this.id = id;
        this.title = title;
        this.percent = percent;
    }
}
