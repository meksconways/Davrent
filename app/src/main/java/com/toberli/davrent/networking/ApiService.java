package com.toberli.davrent.networking;

import com.toberli.davrent.admin.categories.model.CategoriesModel;
import com.toberli.davrent.admin.customer.customerdetail.rentproduct.ProductInfoModel;
import com.toberli.davrent.admin.customer.model.CustomerModel;
import com.toberli.davrent.admin.customertype.model.CustomerTypeModel;
import com.toberli.davrent.admin.discount.editdismodel.EditDiscountModel;
import com.toberli.davrent.admin.discount.model.DiscountModel;
import com.toberli.davrent.admin.product.model.ProductModel;
import com.toberli.davrent.admin.profile.model.ProfileModel;
import com.toberli.davrent.admin.staff.model.AdminStaffModel;
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
import retrofit2.http.Query;

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
    Call<EditDiscountModel> updateDiscount(@Path("scope") String scope,
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






    @GET("admin/getStaffs")
    Call<AdminStaffModel> getStaffs(@HeaderMap Map<String,String> map);

    @POST("admin/addStaff")
    Call<Void> addStaff(@HeaderMap Map<String,String> headerMap,
                           @Body RequestBody body);

    @PATCH("admin/updateStaffProfile/{id}")
    Call<Void> updateStaffProfile(@HeaderMap Map<String,String> headerMap,
                                  @Body RequestBody body,
                                  @Path("id") String user_id);

    @PATCH("admin/changeStaffPassword/{id}")
    Call<Void> changeStaffPassword(@HeaderMap Map<String,String> headerMap,
                                   @Body RequestBody body,
                                   @Path("id") String user_id);



    @GET("common/getCategories")
    Call<CategoriesModel> getCategories(@HeaderMap Map<String,String> map);

    @PATCH("common/updateCategory/{category_id}")
    Call<Void> updateCategory(@HeaderMap Map<String,String> map,
                              @Path("category_id") String id,
                              @Body RequestBody body);

    @POST("common/addCategory")
    Call<Void> addCategory(@HeaderMap Map<String,String> headerMap,
                           @Body RequestBody body);




    @GET("common/getProfile")
    Call<ProfileModel> getProfile(@HeaderMap Map<String,String> headerMap);

    @PATCH("common/updateProfile")
    Call<Void> updateProfile(@HeaderMap Map<String,String> headerMap,
                             @Body RequestBody body);

    @PATCH("common/changePassword")
    Call<Void> changePassword(@HeaderMap Map<String,String> headerMap,
                             @Body RequestBody body);


    /**
     *
     * Customer
     *
     * */
    @GET("common/getCustomers")
    Call<CustomerModel> getCustomers(@HeaderMap Map<String,String> map);

    @PATCH("common/updateCustomerProfile/{customer_id}")
    Call<Void> updateCustomer(@HeaderMap Map<String,String> map,
                              @Path("customer_id") String customerID,
                              @Body RequestBody body);

    @POST("common/addCustomer")
    Call<Void> addCustomer(@HeaderMap Map<String,String> map,
                           @Body RequestBody body);


    /**
     *
     * Products
     */
    @GET("common/getProducts")
    Call<ProductModel> getProducts(@HeaderMap Map<String,String> map);

    @PATCH("common/updateProduct/{product_id}")
    Call<Void> updateProduct(@HeaderMap Map<String,String> map,
                             @Path("product_id") String productID,
                             @Body RequestBody body);

    /**
     *
     * @param map
     * @param body
     * @return
     */
    @POST("common/addProduct")
    Call<Void> addProduct(@HeaderMap Map<String,String> map,
                          @Body RequestBody body);

    @GET("common/getProductInfoViaCode")
    Call<ProductInfoModel> getProductViaCode(@HeaderMap Map<String,String> map,
                                             @Query("code") String code);

    /**
     * REnts
     * @param map
     * @param body
     * @param customerID
     * @return
     */
    @POST("common/calculateRents/{customer_id}")
    Call<Void> calculateRents(@HeaderMap Map<String,String> map,
                              @Body RequestBody body,
                              @Path("customer_id") String customerID);

    @POST("common/saveRents/{customer_id}")
    Call<Void> saveRents(@HeaderMap Map<String,String> map,
                              @Body RequestBody body,
                              @Path("customer_id") String customerID);





//    //calculateRents verileri : codes (1236547891|3256985236 vs. seklinde olmali) | rent_hours(kirada kalacağı süre saat cinsinden)
//    $router->post("calculateRents/{customer_id}","RentsControllers@calculateRents");
//    $router->post("saveRents/{customer_id}","RentsControllers@saveRents"); //bu veriler calculateRents ile aynı - bu servis üstteki ile aynı sadece arka işlevi farklı
//    $router->get("returnRents/{customer_id}","RentsControllers@returnRents");






}
