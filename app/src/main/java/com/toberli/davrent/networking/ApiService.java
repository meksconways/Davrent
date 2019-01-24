package com.toberli.davrent.networking;

import com.toberli.davrent.admin.customertype.model.CustomerTypeModel;
import com.toberli.davrent.admin.discount.model.DiscountModel;
import com.toberli.davrent.login.model.LoginModel;
import com.toberli.davrent.splash.model.SplashModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("common/getProfile")
    Call<SplashModel> checkToken(@Header("token") String token,
                                 @Header("api-key") String apikey);


    @POST("login")
    Call<LoginModel> login(@HeaderMap Map<String, String> map,
                           @Body RequestBody loginBody);

    @GET("{scope}/getDiscounts")
    Call<DiscountModel> getDiscount(@Path("scope") String scope,
                                    @HeaderMap Map<String,String> headerMap);

    @POST("{scope}/addDiscount")
    Call<Void> addDiscount(@Path("scope") String scope,
                           @HeaderMap Map<String,String> headerMap,
                           @Body RequestBody body);

    @PATCH("{scope}/updateDiscount/{id}")
    Call<Void> updateDiscount(@Path("scope") String scope,
                              @Path("id") String id,
                              @HeaderMap Map<String,String> headerMap,
                              @Body RequestBody body);


    @GET("admin/getCustomerTypes")
    Call<CustomerTypeModel> getCustomerTypes(@HeaderMap Map<String,String> map);

    @PATCH("admin/updateCustomerType/{customer_type_id}")
    Call<Void> updateCostumerType(@Path("customer_type_id") String id,
                                  @HeaderMap Map<String,String> map,
                                  @Body RequestBody body);

    @POST("admin/CustomerType")
    Call<Void> addCustomerType(@HeaderMap Map<String,String> map,
                               @Body RequestBody body);




}