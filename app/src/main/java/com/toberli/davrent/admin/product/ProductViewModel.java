package com.toberli.davrent.admin.product;

import android.content.Context;

import com.toberli.davrent.admin.product.model.Data;
import com.toberli.davrent.admin.product.model.ProductModel;
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

public class ProductViewModel extends ViewModel {


    private final MutableLiveData<List<Data>> data = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final ApiService apiService;
    private final Context context;
    private Call<ProductModel> callProduct;

    LiveData<Boolean> getLoading() {
        return loading;
    }

    LiveData<List<Data>> getData() {
        return data;
    }

    @Inject
    public ProductViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    void fetchData(){
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        callProduct = apiService.getProducts(header);
        callProduct.enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                if (response.code() >= 200 && response.code() < 400){
                    loading.setValue(false);
                    //noinspection ConstantConditions
                    data.setValue(response.body().data);
                    callProduct = null;
                }else{
                    loading.setValue(false);
                    callProduct = null;
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                loading.setValue(false);
                callProduct = null;
            }
        });

    }

    @Override
    protected void onCleared() {
        if (callProduct != null) {
            callProduct.cancel();
            callProduct = null;
        }
    }
}
