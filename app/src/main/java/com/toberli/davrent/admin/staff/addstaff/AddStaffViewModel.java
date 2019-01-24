package com.toberli.davrent.admin.staff.addstaff;

import android.content.Context;

import com.toberli.davrent.admin.staff.model.Data;
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

public class AddStaffViewModel extends ViewModel {

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showAlert = new MutableLiveData<>(false);
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>();
    private final ApiService apiService;
    private final Context context;
    private Call<Void> callAddStaff;


    LiveData<Boolean> getLoading() {
        return loading;
    }

    LiveData<Boolean> getShowAlert() {
        return showAlert;
    }

    LiveData<String> getAlertMessage() {
        return alertMessage;
    }

    @Inject
    public AddStaffViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    void addStaff(String name, String password, String password_con, String phone){
        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name_surname",name);
            jsonObject.put("phone","+90"+phone);
            jsonObject.put("password",password);
            jsonObject.put("password_confirmation",password_con);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,jsonObject.toString());

        callAddStaff = apiService.addStaff(header,body);
        callAddStaff.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() >= 200 && response.code() < 400){

                    loading.setValue(false);
                    alertMessage.setValue("Başarılı bir şekilde personel eklendi");
                    showAlert.setValue(true);
                    callAddStaff = null;

                }else{
                    if (response.errorBody() != null){
                        try {
                            String error = Helper.getApiBadRequestError(response.errorBody().string());
                            alertMessage.setValue(error);
                            showAlert.setValue(true);
                            loading.setValue(false);
                            callAddStaff = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callAddStaff = null;
            }
        });


    }

    @Override
    protected void onCleared() {
        if (callAddStaff != null) {
            callAddStaff.cancel();
            callAddStaff = null;
        }
    }

    void setShowAlert(boolean b) {
        showAlert.setValue(b);
    }
}
