package com.toberli.davrent.admin.discount;

import android.content.Context;
import android.util.Log;

import com.toberli.davrent.admin.discount.editdismodel.EditDiscountModel;
import com.toberli.davrent.admin.discount.model.Data;
import com.toberli.davrent.database.AppDatabase;
import com.toberli.davrent.helper.Helper;
import com.toberli.davrent.networking.ApiService;
import com.toberli.davrent.splash.SplashFragment;
import com.toberli.davrent.splash.model.SplashModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditDiscountViewModel extends ViewModel {

    private final MutableLiveData<Data> datas = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isEdit = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private final ApiService apiService;
    private final Context context;
    private Call<EditDiscountModel> callUpdate;
    private Call<Void> callAdd;

    void setLoading(Boolean value){
        loading.setValue(value);
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    LiveData<Boolean> getIsEdit() {
        return isEdit;
    }

    LiveData<Data> getDatas() {
        return datas;
    }

    void setIsEdit(Boolean value){
        isEdit.setValue(value);
    }

    void setDatas(Data data){
        datas.setValue(data);
    }

    @Inject
    AddEditDiscountViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    void updateDiscount(String _header, String _desc, String _perc){

        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token",AppDatabase.getToken(context));

        JSONObject jsonObject = new JSONObject();
        try {
            //noinspection ConstantConditions
            jsonObject.put("title",_header);
            jsonObject.put("description",_desc);
            jsonObject.put("percent",String.valueOf(_perc));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,jsonObject.toString());
        callUpdate = apiService.updateDiscount(AppDatabase.getScope(context), String.valueOf(datas.getValue().id),header,body);
        callUpdate.enqueue(new Callback<EditDiscountModel>() {
            @Override
            public void onResponse(Call<EditDiscountModel> call, Response<EditDiscountModel> response) {

                loading.setValue(false);

                if (response.code() >= 200 && response.code() < 400){

                }

            }

            @Override
            public void onFailure(Call<EditDiscountModel> call, Throwable t) {
                loading.setValue(false);
            }
        });

    }
    void addDiscount(String _header, String _desc, String _perc){
        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token",AppDatabase.getToken(context));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title",_header);
            jsonObject.put("description",_desc);
            jsonObject.put("percent",_perc);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,jsonObject.toString());

        callAdd = apiService.addDiscount(AppDatabase.getScope(context),header,body);
        callAdd.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loading.setValue(false);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loading.setValue(false);
            }
        });


    }

    @Override
    protected void onCleared() {
        if (callAdd != null) {
            callAdd.cancel();
            callAdd = null;
        }
        if (callUpdate != null){
            callUpdate.cancel();
            callUpdate = null;
        }
    }
}
