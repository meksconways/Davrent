package com.toberli.davrent.admin.customer.customerdetail.rentproduct;

import android.content.Context;
import android.util.Log;

import com.toberli.davrent.admin.customer.customerdetail.rentproduct.model.CalculateRentModel;
import com.toberli.davrent.admin.product.model.Data;
import com.toberli.davrent.database.AppDatabase;
import com.toberli.davrent.helper.Helper;
import com.toberli.davrent.networking.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentProductViewModel extends ViewModel {

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Data> productData = new MutableLiveData<>();
    private final MutableLiveData<Integer> userID = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showAlert = new MutableLiveData<>();
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>();
    private final MutableLiveData<String> codes = new MutableLiveData<>(null);
    private final MutableLiveData<com.toberli.davrent.admin.customer.customerdetail.rentproduct.model.Data> calculateData =
            new MutableLiveData<>();
    private final ApiService apiService;
    private final Context context;
    private Call<ProductInfoModel> callProductData;
    private Call<CalculateRentModel> callCalRent;
    private Call<Void> callSaveRent;


    LiveData<com.toberli.davrent.admin.customer.customerdetail.rentproduct.model.Data> getCalculateData() {
        return calculateData;
    }


    void clearCodeAndData() {
        productData.setValue(null);
        codes.setValue(null);
        calculateData.setValue(null);
    }

    LiveData<String> getCodes() {
        return codes;
    }

    void addCode(String value){
        codes.setValue(value);
    }

    public MutableLiveData<Boolean> getShowAlert() {
        return showAlert;
    }

    LiveData<String> getAlertMessage() {
        return alertMessage;
    }

    void setShowAlert(Boolean value){
        showAlert.setValue(value);
    }

    public void setUserID(Integer id){
        userID.setValue(id);
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    LiveData<Data> getProductData() {
        return productData;
    }

    @Override
    protected void onCleared() {
        if (callProductData != null) {
            callProductData.cancel();
            callProductData = null;
        }
        if (callCalRent != null) {
            callCalRent.cancel();
            callCalRent = null;
        }
    }

    void saveRent(String hour){
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        if (codes.getValue() != null){
            if (codes.getValue().substring(0,1).equals("|")){
                codes.setValue(codes.getValue().substring(1));
            }

        }else{
            return;
        }
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("codes",codes.getValue());
            jsonObject.put("rent_hours",hour);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,jsonObject.toString());
        callSaveRent = apiService.saveRents(header,body,String.valueOf(userID.getValue()));
        callSaveRent.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() >= 200 && response.code() < 400){
                    alertMessage.setValue("Kira işlemi başarılı bir şekilde oluşturuldu");
                    showAlert.setValue(true);
                }else{
                    try {
                        //noinspection ConstantConditions
                        String error = Helper.getApiBadRequestError(response.errorBody().string());
                        alertMessage.setValue(error);
                        showAlert.setValue(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

    }

    void calculateRent(String hour){
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        if (codes.getValue() != null){
            if (codes.getValue().substring(0,1).equals("|")){
                codes.setValue(codes.getValue().substring(1));
            }

        }else{
            return;
        }
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("codes",codes.getValue());
            jsonObject.put("rent_hours",hour);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType,jsonObject.toString());

        Log.d( "***calculateRent: ", String.valueOf(userID));
        callCalRent = apiService.calculateRents(header,body,String.valueOf(userID.getValue()));
        callCalRent.enqueue(new Callback<CalculateRentModel>() {
            @Override
            public void onResponse(Call<CalculateRentModel> call, Response<CalculateRentModel> response) {
                if (response.code() >= 200 && response.code() < 400){

                    //noinspection ConstantConditions
                    calculateData.setValue(response.body().data);

                }else{

                    try {
                        //noinspection ConstantConditions
                        String error = Helper.getApiBadRequestError(response.errorBody().string());
                        alertMessage.setValue(error);
                        showAlert.setValue(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<CalculateRentModel> call, Throwable t) {

            }
        });

    }

    void getProductDataViaCode(String code){
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        callProductData = apiService.getProductViaCode(header,code);
        callProductData.enqueue(new Callback<ProductInfoModel>() {
            @Override
            public void onResponse(Call<ProductInfoModel> call, Response<ProductInfoModel> response) {
                Log.d( "***onResponse: ", String.valueOf(response.code()));
                if (response.code() >= 200 && response.code() < 400){
                    //noinspection ConstantConditions

                    productData.setValue(response.body().data);
//                    alertMessage.setValue("Kod Okundu");
//                    showAlert.setValue(true);

                }else{

                    try {
                        String error = Helper.getApiBadRequestError(response.errorBody().string());
                        alertMessage.setValue(error);
                        showAlert.setValue(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ProductInfoModel> call, Throwable t) {

            }
        });
    }



    @Inject
    RentProductViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }



}
