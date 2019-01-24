package com.toberli.davrent.admin.staff.model;

import com.squareup.moshi.Json;

public class Data {

    @Json(name = "id")
    public final Integer id;
    @Json(name = "name_surname")
    public final String nameSurname;
    @Json(name = "phone")
    public final String phone;
    @Json(name = "is_admin")
    public final Integer isAdmin;
    @Json(name = "created_at")
    public final String createdAt;
    @Json(name = "updated_at")
    public final String updatedAt;
    @Json(name = "total_rents")
    public final Integer totalRents;
    @Json(name = "total_added_customer")
    public final Integer totalAddedCustomer;


    public Data(Integer id, String nameSurname, String phone, Integer isAdmin,
                String createdAt, String updatedAt, Integer totalRents, Integer totalAddedCustomer) {
        this.id = id;
        this.nameSurname = nameSurname;
        this.phone = phone;
        this.isAdmin = isAdmin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.totalRents = totalRents;
        this.totalAddedCustomer = totalAddedCustomer;
    }
}
