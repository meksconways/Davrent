package com.toberli.davrent.admin.customer.customerdetail.rentproduct;

import android.content.Context;
import android.widget.Toast;

import com.toberli.davrent.admin.product.model.Data;
import com.toberli.davrent.database.AppDatabase;
import com.toberli.davrent.helper.Helper;
import com.toberli.davrent.networking.ApiService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentProductViewModel extends ViewModel {

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Data> productData = new MutableLiveData<>();
    private final MutableLiveData<Integer> userID = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showAlert = new MutableLiveData<>();
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>();
    private final MutableLiveData<String> codes = new MutableLiveData<>("");
    private final ApiService apiService;
    private final Context context;
    private Call<ProductInfoModel> callProductData;

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

    void setUserID(Integer id){
        userID.setValue(id);
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    LiveData<Data> getProductData() {
        return productData;
    }

    void calculateRent(){
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));
        //callCalRent = apiService.calculateRents()

    }

    void getProductDataViaCode(String code){
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        callProductData = apiService.getProductViaCode(header,code);
        callProductData.enqueue(new Callback<ProductInfoModel>() {
            @Override
            public void onResponse(Call<ProductInfoModel> call, Response<ProductInfoModel> response) {
                if (response.code() >= 200 && response.code() < 400){

                    alertMessage.setValue("Kod Okundu");
                    //noinspection ConstantConditions
                    productData.setValue(response.body().data);
                    showAlert.setValue(true);

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
