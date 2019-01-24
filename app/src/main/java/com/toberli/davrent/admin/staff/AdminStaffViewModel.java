package com.toberli.davrent.admin.staff;

import android.content.Context;

import com.toberli.davrent.admin.staff.model.AdminStaffModel;
import com.toberli.davrent.admin.staff.model.Data;
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

public class AdminStaffViewModel extends ViewModel {


    private final MutableLiveData<List<Data>> data = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final ApiService apiService;
    private final Context context;
    private Call<AdminStaffModel> callStaff;

    LiveData<Boolean> getLoading() {
        return loading;
    }

    LiveData<List<Data>> getData() {
        return data;
    }

    @Inject
    public AdminStaffViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    void fetchData(){
        loading.setValue(true);
        Map<String,String> map = new HashMap<>();
        map.put("api-key", Helper.apikey);
        map.put("token", AppDatabase.getToken(context));

        callStaff = apiService.getStaffs(map);
        callStaff.enqueue(new Callback<AdminStaffModel>() {
            @Override
            public void onResponse(Call<AdminStaffModel> call, Response<AdminStaffModel> response) {

                loading.setValue(false);

                if (response.code() >= 200 && response.code() < 400){

                    //noinspection ConstantConditions
                    data.setValue(response.body().data);

                }
                callStaff = null;

            }

            @Override
            public void onFailure(Call<AdminStaffModel> call, Throwable t) {
                loading.setValue(false);
                callStaff = null;
            }
        });
    }

    @Override
    protected void onCleared() {
        if (callStaff != null) {
            callStaff.cancel();
            callStaff = null;
        }
    }
}
