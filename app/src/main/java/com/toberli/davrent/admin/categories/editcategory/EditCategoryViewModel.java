package com.toberli.davrent.admin.categories.editcategory;

import android.content.Context;

import com.toberli.davrent.admin.categories.model.Data;
import com.toberli.davrent.database.AppDatabase;
import com.toberli.davrent.helper.Helper;
import com.toberli.davrent.networking.ApiService;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCategoryViewModel extends ViewModel {

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showAlert = new MutableLiveData<>(false);
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>("");
    private final MutableLiveData<Data> data = new MutableLiveData<>();
    private final ApiService apiService;
    private final Context context;
    private Call<Void> callCat;


    @Inject
    public EditCategoryViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    LiveData<Data> getData() {
        return data;
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    LiveData<String> getAlertMessage() {
        return alertMessage;
    }

    LiveData<Boolean> getShowAlert() {
        return showAlert;
    }

    void setShowAlert(Boolean data){
        showAlert.setValue(data);
    }

    void updateCategory(String name,String desc){

        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name",name);
            jsonObject.put("description",desc);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,jsonObject.toString());

        //noinspection ConstantConditions
        callCat = apiService.updateCategory(header, String.valueOf(data.getValue().id),body);
        callCat.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() >= 200 && response.code() < 400){
                    loading.setValue(true);
                    alertMessage.setValue("Kategori başarı ile değiştirildi");
                    showAlert.setValue(true);
                    callCat = null;
                }else{
                    loading.setValue(true);
                    try {
                        //noinspection ConstantConditions
                        String error = Helper.getApiBadRequestError(response.errorBody().string());
                        alertMessage.setValue(error);
                        showAlert.setValue(true);
                        callCat = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                alertMessage.setValue("Bağlantı Hatası");
                showAlert.setValue(true);
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


    public void setData(Data value) {
        data.setValue(value);
    }

    public void setLoading(boolean b) {
        loading.setValue(false);
    }
}
