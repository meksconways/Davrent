package com.toberli.davrent.admin.categories;

import android.content.Context;
import android.util.Log;

import com.toberli.davrent.admin.categories.model.CategoriesModel;
import com.toberli.davrent.admin.categories.model.Data;
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

public class AdminCategoriesViewModel extends ViewModel {


    private final MutableLiveData<List<Data>> data = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final ApiService apiService;
    private final Context context;
    private Call<CategoriesModel> callCat;


    LiveData<Boolean> getLoading() {
        return loading;
    }

    LiveData<List<Data>> getData() {
        return data;
    }

    @Inject
    AdminCategoriesViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    void fetchData(){
        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        callCat = apiService.getCategories(header);
        callCat.enqueue(new Callback<CategoriesModel>() {
            @Override
            public void onResponse(Call<CategoriesModel> call, Response<CategoriesModel> response) {
                if (response.code() >= 200 && response.code() < 400){
                    //noinspection ConstantConditions
                    loading.setValue(false);
                    data.setValue(response.body().data);
                    callCat = null;
                }else{
                    loading.setValue(false);
                    callCat = null;
                }
            }

            @Override
            public void onFailure(Call<CategoriesModel> call, Throwable t) {
                Log.d( "onFailure: ",t.getLocalizedMessage());
                callCat = null;
            }
        });

    }


    @Override
    protected void onCleared() {
        if (callCat != null) {
            callCat.cancel();
            callCat = null;
        }
    }
}
