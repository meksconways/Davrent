package com.toberli.davrent.admin.discount;

import android.content.Context;
import android.util.Log;

import com.toberli.davrent.admin.discount.model.Data;
import com.toberli.davrent.admin.discount.model.DiscountModel;
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

public class DiscountViewModel extends ViewModel {


    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isEmpty = new MutableLiveData<>();
    private final MutableLiveData<List<Data>> dataList = new MutableLiveData<>();
    private final ApiService apiService;
    private final Context context;
    private Call<DiscountModel> callDiscount;

    LiveData<Boolean> getLoading() {
        return loading;
    }

    LiveData<Boolean> getIsEmpty() {
        return isEmpty;
    }

    LiveData<List<Data>> getDataList() {
        return dataList;
    }

    @Inject
    DiscountViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;

    }

    void fetchData() {
        loading.setValue(true);
        String scope = AppDatabase.getScope(context);
        Map<String,String> map = new HashMap<>();
        map.put("api-key", Helper.apikey);
        map.put("token",AppDatabase.getToken(context));

        callDiscount = apiService.getDiscount(scope,map);
        callDiscount.enqueue(new Callback<DiscountModel>() {
            @Override
            public void onResponse(Call<DiscountModel> call, Response<DiscountModel> response) {
                if (response.body() != null){
                    Log.d( "***onResponse:",response.body().dataList.toString());
                    dataList.setValue(response.body().dataList);
                    loading.setValue(false);
                    if (response.body().dataList.size() == 0){
                        isEmpty.setValue(true);
                    }else{
                        isEmpty.setValue(false);
                    }
                }else{
                    try {
                        Log.d( "***onResponse: ",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DiscountModel> call, Throwable t) {
                Log.d( "***onResponse: ",t.getLocalizedMessage());
                loading.setValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        if (callDiscount != null) {
            callDiscount.cancel();
            callDiscount = null;
        }

    }
}
