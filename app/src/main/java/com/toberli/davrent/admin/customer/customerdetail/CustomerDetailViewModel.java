package com.toberli.davrent.admin.customer.customerdetail;

import android.content.Context;
import android.util.Log;

import com.toberli.davrent.admin.customer.model.Data;
import com.toberli.davrent.admin.customertype.model.CustomerTypeModel;
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

public class CustomerDetailViewModel extends ViewModel {
    
    private final MutableLiveData<Data> data = new MutableLiveData<>();
    private final MutableLiveData<List<com.toberli.davrent.admin.customertype.model.Data>> customerTypeData = new MutableLiveData<>();
    private final ApiService apiService;
    private final Context context;
    private Call<CustomerTypeModel> callCustomerType;

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
    public CustomerDetailViewModel(ApiService apiService, Context context) {
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
}
