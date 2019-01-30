package com.toberli.davrent.admin.customer;

import android.content.Context;
import android.util.Log;

import com.toberli.davrent.admin.customer.model.CustomerModel;
import com.toberli.davrent.admin.customer.model.Data;
import com.toberli.davrent.database.AppDatabase;
import com.toberli.davrent.helper.Helper;
import com.toberli.davrent.networking.ApiService;

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

public class CustomerViewModel extends ViewModel {


    private final MutableLiveData<List<Data>> data = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final ApiService apiService;
    private final Context context;
    private Call<CustomerModel> callCustomer;

    LiveData<List<Data>> getData() {
        return data;
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    @Inject
    CustomerViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    void fetchData(){
        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        callCustomer = apiService.getCustomers(header);
        callCustomer.enqueue(new Callback<CustomerModel>() {
            @Override
            public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
                Log.d( "***onResponse: ", String.valueOf(response.code()));
                if (response.code() >= 200 && response.code() < 400){
                    loading.setValue(false);
                    //noinspection ConstantConditions
                    data.setValue(response.body().data);
                    callCustomer = null;
                }else{
                    loading.setValue(false);
                    callCustomer = null;
                }
            }

            @Override
            public void onFailure(Call<CustomerModel> call, Throwable t) {
                callCustomer = null;
            }
        });

    }

    @Override
    protected void onCleared() {
        if (callCustomer != null) {
            callCustomer.cancel();
            callCustomer = null;
        }
    }
}
