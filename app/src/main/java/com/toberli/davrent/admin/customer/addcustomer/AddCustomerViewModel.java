package com.toberli.davrent.admin.customer.addcustomer;

import android.content.Context;
import android.util.Log;

import com.toberli.davrent.admin.customertype.model.CustomerTypeModel;
import com.toberli.davrent.admin.customertype.model.Data;
import com.toberli.davrent.database.AppDatabase;
import com.toberli.davrent.helper.Helper;
import com.toberli.davrent.networking.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

public class AddCustomerViewModel extends ViewModel {

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showAlert = new MutableLiveData<>(false);
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>("");
    private final MutableLiveData<List<Data>> customerTypeData = new MutableLiveData<>();
    private final MutableLiveData<String> customerTypeID = new MutableLiveData<>();
    private final ApiService apiService;
    private final Context context;
    private Call<Void> callAddCustomer;
    private Call<CustomerTypeModel> callCustomerType;

    LiveData<List<Data>> getCustomerTypeData() {
        return customerTypeData;
    }

    LiveData<String> getCustomerTypeID() {
        return customerTypeID;
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    LiveData<Boolean> getShowAlert() {
        return showAlert;
    }

    LiveData<String> getAlertMessage() {
        return alertMessage;
    }

    void setCustomerTypeID(String id){
        customerTypeID.setValue(id);
    }

    @Inject
    public AddCustomerViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
        getCustomerType();
    }


    private void getCustomerType(){
        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        callCustomerType = apiService.getCustomerTypes(header);
        callCustomerType.enqueue(new Callback<CustomerTypeModel>() {
            @Override
            public void onResponse(Call<CustomerTypeModel> call, Response<CustomerTypeModel> response) {
                if (response.body() != null){

                    loading.setValue(false);
                    customerTypeData.setValue(response.body().data);
                    callCustomerType = null;

                }else{
                    loading.setValue(false);
                    callCustomerType = null;
                }

            }

            @Override
            public void onFailure(Call<CustomerTypeModel> call, Throwable t) {
                callCustomerType = null;
            }
        });
    }

    void addCustomer(String name,String number){
        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name_surname",name);
            jsonObject.put("phone","+90"+number);
            jsonObject.put("customer_type_id",customerTypeID.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,jsonObject.toString());


        callAddCustomer = apiService.addCustomer(header,body);
        callAddCustomer.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() >= 200 && response.code() < 400){
                    loading.setValue(false);
                    alertMessage.setValue("Müşteri başarı ile oluşturuldu");
                    showAlert.setValue(true);
                    callAddCustomer = null;
                }else{
                    loading.setValue(false);
                    try {
                        //noinspection ConstantConditions
                        String error = Helper.getApiBadRequestError(response.errorBody().string());
                        alertMessage.setValue(error);
                        showAlert.setValue(true);
                        callAddCustomer = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loading.setValue(false);
                alertMessage.setValue("Bağlantı Hatası");
                showAlert.setValue(true);
                callAddCustomer = null;
            }
        });

    }

    public void setShowAlert(boolean b) {
        showAlert.setValue(b);
    }
}
