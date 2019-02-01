package com.toberli.davrent.admin.customer.customerdetail;

import android.content.Context;
import android.util.Log;

import com.toberli.davrent.admin.customer.model.Data;
import com.toberli.davrent.admin.customertype.model.CustomerTypeModel;
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

public class CustomerDetailViewModel extends ViewModel {
    
    private final MutableLiveData<Data> data = new MutableLiveData<>();
    private final MutableLiveData<List<com.toberli.davrent.admin.customertype.model.Data>> customerTypeData = new MutableLiveData<>();
    private final MutableLiveData<String> customerTypeID = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showAlert = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>("");


    private final ApiService apiService;
    private final Context context;
    private Call<CustomerTypeModel> callCustomerType;
    private Call<Void> callUpdateProfile;
    private Call<Void> callReturnProduct;

    LiveData<Boolean> getLoading() {
        return loading;
    }

    void setShowAlert(Boolean value){
        showAlert.setValue(value);
    }

    LiveData<String> getAlertMessage() {
        return alertMessage;
    }

    LiveData<Boolean> getShowAlert() {
        return showAlert;
    }

    void setCustomerTypeID(String id){
        customerTypeID.setValue(id);
    }

    LiveData<String> getCustomerTypeID() {
        return customerTypeID;
    }

    LiveData<Data> getData() {
        return data;
    }

    public void setData(Data dataX){
        data.setValue(dataX);
    }

    LiveData<List<com.toberli.davrent.admin.customertype.model.Data>> getCustomerTypeData() {
        return customerTypeData;
    }

    @Inject
    CustomerDetailViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
        fetchCustomerType();
    }

    private void fetchCustomerType() {

        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        callCustomerType = apiService.getCustomerTypes(header);
        callCustomerType.enqueue(new Callback<CustomerTypeModel>() {
            @Override
            public void onResponse(Call<CustomerTypeModel> call, Response<CustomerTypeModel> response) {

                if (response.code() >= 200 && response.code() < 400){
                    //noinspection ConstantConditions
                    customerTypeData.setValue(response.body().data);
                    callCustomerType = null;
                }else{
                    callCustomerType = null;
                }

            }

            @Override
            public void onFailure(Call<CustomerTypeModel> call, Throwable t) {
                callCustomerType = null;
            }
        });
    }

    void updateProfile(String name, String phone) {
        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name_surname",name);
            jsonObject.put("phone","+90"+phone);
            jsonObject.put("customer_type_id",customerTypeID.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),jsonObject.toString());

        //noinspection ConstantConditions
        callUpdateProfile = apiService.updateCustomer(header, String.valueOf(data.getValue().id),body);
        callUpdateProfile.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() >= 200 && response.code() < 400){
                    loading.setValue(false);
                    alertMessage.setValue("Müşteri profili başarılı bir şekilde güncellendi");
                    showAlert.setValue(true);

                }else{
                    loading.setValue(false);
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
                loading.setValue(false);
                alertMessage.setValue("Bağlantı Hatası");
                showAlert.setValue(true);
            }
        });


    }

    void returnProduct() {
        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));
        //noinspection ConstantConditions
        callReturnProduct = apiService.returnProduct(header, String.valueOf(data.getValue().id));
        callReturnProduct.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() >= 200 && response.code() < 400){
                    loading.setValue(false);
                    alertMessage.setValue("Başarılı Bir Şekilde Geri İade İşlemi Yapıldı");
                    showAlert.setValue(true);

                }else{
                    loading.setValue(false);
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
}
