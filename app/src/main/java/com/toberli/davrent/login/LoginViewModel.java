package com.toberli.davrent.login;

import com.toberli.davrent.helper.Helper;
import com.toberli.davrent.login.model.LoginModel;
import com.toberli.davrent.networking.ApiService;
import com.toberli.davrent.splash.model.SplashModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HEAD;

public class LoginViewModel extends ViewModel {


     private final MutableLiveData<LoginModel> data = new MutableLiveData<>();
     private final MutableLiveData<String> errorBody = new MutableLiveData<>();
     private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
     private final ApiService apiService;
     private Call<LoginModel> callLogin;


     @Inject
     public LoginViewModel(ApiService apiService) {
         this.apiService = apiService;
     }

     LiveData<LoginModel> getData() {
         return data;
     }

     LiveData<String> getErrorBody() {
         return errorBody;
     }

     LiveData<Boolean> getLoading() {
         return loading;
     }

     void login(RequestBody body) {
         loading.setValue(true);
         Map<String,String> header = new HashMap<>();
         header.put("api-key",Helper.apikey);

        callLogin = apiService.login(header,body);
        callLogin.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.body() != null){
                    data.setValue(response.body());
                    loading.setValue(false);
                    callLogin = null;
                }else{
                    try {
                        if (response.errorBody() != null) {
                            errorBody.setValue(Helper.getApiBadRequestError(response.errorBody().string()));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    loading.setValue(false);
                    callLogin = null;


                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                loading.setValue(false);
                    callLogin = null;
            }
        });

     }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (callLogin != null) {
            callLogin.cancel();
            callLogin = null;
        }
    }
}
