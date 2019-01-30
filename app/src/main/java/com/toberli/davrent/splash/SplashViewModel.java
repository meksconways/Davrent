package com.toberli.davrent.splash;

import android.util.Log;

import com.toberli.davrent.helper.Helper;
import com.toberli.davrent.networking.ApiService;
import com.toberli.davrent.splash.model.SplashModel;

import java.io.IOException;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashViewModel extends ViewModel {

    private final MutableLiveData<Boolean> checkToken = new MutableLiveData<>();
    private final MutableLiveData<SplashModel> data = new MutableLiveData<>();
    private final MutableLiveData<String> errorBody = new MutableLiveData<>();
    private final ApiService apiService;
    private Call<SplashModel> callProfile;

    LiveData<Boolean> getCheckToken(){
        return checkToken;
    }

    LiveData<SplashModel> getData() {
        return data;
    }


    @Inject
    SplashViewModel(ApiService apiService){
        this.apiService = apiService;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (callProfile != null) {
            callProfile.cancel();
            callProfile = null;
        }
    }

    void checkToken(String token) {

        callProfile = apiService.checkToken(token, Helper.apikey);
        callProfile.enqueue(new Callback<SplashModel>() {
            @Override
            public void onResponse(Call<SplashModel> call, Response<SplashModel> response) {
                if (response.body() != null){
                    data.setValue(response.body());
                    checkToken.setValue(true);
                    callProfile = null;
                }else{
                    checkToken.setValue(false);

                    try {

                        if (response.errorBody() != null) {
                            errorBody.setValue(Helper.getApiBadRequestError(response.errorBody().string()));
                            Log.d( "***onResponse: ",errorBody.getValue());
                            callProfile = null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SplashModel> call, Throwable t) {

            }
        });


    }
}
