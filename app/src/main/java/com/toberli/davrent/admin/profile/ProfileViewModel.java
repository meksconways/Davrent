package com.toberli.davrent.admin.profile;

import android.content.Context;

import com.toberli.davrent.admin.profile.model.Data;
import com.toberli.davrent.admin.profile.model.ProfileModel;
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

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<Data> data = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> alertShow = new MutableLiveData<>(false);
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>("");
    private final ApiService apiService;
    private final Context context;
    private Call<Void> callUpdate;
    private Call<ProfileModel> callGetProfile;
    private Call<Void> callChangePass;

    @Inject
    ProfileViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
        getProfile();
    }
    void getProfile(){

        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        callGetProfile = apiService.getProfile(header);
        callGetProfile.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                if (response.code() >= 200 && response.code() < 400){
                    loading.setValue(false);
                    //noinspection ConstantConditions
                    data.setValue(response.body().data);
                }else{
                    try {
                        //noinspection ConstantConditions
                        String error = Helper.getApiBadRequestError(response.errorBody().string());
                        loading.setValue(false);
                        alertMessage.setValue(error);
                        alertShow.setValue(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                loading.setValue(false);
                alertMessage.setValue("Bağlantı hatası");
                alertShow.setValue(true);
            }
        });

    }

    LiveData<Data> getData() {
        return data;
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    LiveData<Boolean> getAlertShow() {
        return alertShow;
    }

    LiveData<String> getAlertMessage() {
        return alertMessage;
    }





    void updateProfile(String name,String phone){
        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name_surname",name);
            jsonObject.put("phone","+90"+phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,jsonObject.toString());

        callUpdate = apiService.updateProfile(header,body);
        callUpdate.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() >= 200 && response.code() < 400){
                    loading.setValue(false);
                    alertMessage.setValue("Profil başarı ile güncellendi");
                    alertShow.setValue(true);
                }else{
                    try {
                        //noinspection ConstantConditions
                        String error = Helper.getApiBadRequestError(response.errorBody().string());
                        loading.setValue(false);
                        alertMessage.setValue(error);
                        alertShow.setValue(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loading.setValue(false);
                alertMessage.setValue("Bağlantı hatası");
                alertShow.setValue(true);
            }
        });


    }

    void changePassword(String oldPass,String newPass,String passConf){
        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("current_password",oldPass);
            jsonObject.put("password_confirmation",passConf);
            jsonObject.put("password",newPass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,jsonObject.toString());

        callChangePass = apiService.changePassword(header,body);
        callChangePass.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() >= 200 && response.code() < 400){
                    loading.setValue(false);
                    alertMessage.setValue("Parola başarı ile güncellendi");
                    alertShow.setValue(true);
                }else{
                    try {
                        //noinspection ConstantConditions
                        String error = Helper.getApiBadRequestError(response.errorBody().string());
                        loading.setValue(false);
                        alertMessage.setValue(error);
                        alertShow.setValue(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loading.setValue(false);
                alertMessage.setValue("Bağlantı hatası");
                alertShow.setValue(true);
            }
        });

    }

    public void setShowAlert(boolean b) {
        alertShow.setValue(b);
    }

    @Override
    protected void onCleared() {
        if (callChangePass != null) {
            callChangePass.cancel();
            callChangePass = null;
        }
        if (callGetProfile != null) {
            callGetProfile.cancel();
            callGetProfile = null;
        }
        if (callUpdate != null) {
            callUpdate.cancel();
            callUpdate = null;
        }
    }
}
