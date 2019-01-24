package com.toberli.davrent.admin.customertype;

import android.content.Context;
import android.util.Log;

import com.toberli.davrent.admin.customertype.model.CustomerTypeModel;
import com.toberli.davrent.admin.customertype.model.Data;
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

public class CustomerTypeViewModel extends ViewModel {

    private final MutableLiveData<List<Data>> data = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final ApiService apiService;
    private final Context context;
    private Call<CustomerTypeModel> callCustomerType;

    LiveData<Boolean> getLoading() {
        return loading;
    }

    LiveData<List<Data>> getData() {
        return data;
    }

    @Inject
    public CustomerTypeViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    void fetchData(){

        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        callCustomerType = apiService.getCustomerTypes(header);
        callCustomerType.enqueue(new Callback<CustomerTypeModel>() {
            @Override
            public void onResponse(Call<CustomerTypeModel> call, Response<CustomerTypeModel> response) {
                if (response.body() != null){

                    Log.d( "---onResponse: ",response.body().toString());
                    loading.setValue(false);
                    data.setValue(response.body().data);
                    callCustomerType = null;

                }else{
                    try {
                        Log.d( "---onResponse: ",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    @Override
    protected void onCleared() {
        if (callCustomerType != null) {
            callCustomerType.cancel();
            callCustomerType = null;
        }
    }
}
