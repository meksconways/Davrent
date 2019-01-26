package com.toberli.davrent.admin.staff.editstaff;

import android.content.Context;
import android.util.Log;

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

public class EditStaffViewModel extends ViewModel {

    private final MutableLiveData<Data> data = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>("Bilgi");
    private final MutableLiveData<Boolean> alertShow = new MutableLiveData<>(false);
    private Call<Void> callUpdateStaff;
    private Call<Void> callChangePass;


    LiveData<Boolean> getAlertShow() {
        return alertShow;
    }
    void setAlertShow(Boolean data){
        alertShow.setValue(data);
    }

    LiveData<String> getAlertMessage() {
        return alertMessage;
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    private final ApiService apiService;
    private final Context context;


    public void setData(Data dataX){
        data.setValue(dataX);
    }

    LiveData<Data> getData() {
        return data;
    }

    @Inject
    public EditStaffViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    void updateStaff(String _name, String _phone){
        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        JSONObject jsonObject = new JSONObject();

        Log.d( "updateStaff: ",_phone);

        try {
            jsonObject.put("name_surname",_name);
            jsonObject.put("phone","+90"+_phone);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,jsonObject.toString());

        //noinspection ConstantConditions
        callUpdateStaff = apiService.updateStaffProfile(header,body,String.valueOf(data.getValue().id));
        callUpdateStaff.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() >= 200 && response.code() < 400){
                    loading.setValue(false);
                    alertMessage.setValue("Bilgiler başarı ile güncellendi");
                    alertShow.setValue(true);
                    callUpdateStaff = null;
                }else{

                    try {
                        if (response.errorBody() != null) {
                            String error = Helper.getApiBadRequestError(response.errorBody().string());
                            loading.setValue(false);
                            alertMessage.setValue(error);
                            alertShow.setValue(true);
                            callUpdateStaff = null;
                        }else{
                            loading.setValue(false);
                            alertMessage.setValue("Sistemsel bir hata oluştu");
                            alertShow.setValue(true);
                            callUpdateStaff = null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loading.setValue(false);
                alertMessage.setValue("Sistemsel bir hata oluştu");
                alertShow.setValue(true);
                callUpdateStaff = null;
            }
        });



    }

    @Override
    protected void onCleared() {
        if (callChangePass != null) {
            callChangePass.cancel();
            callChangePass = null;
        }
        if (callUpdateStaff != null){
            callUpdateStaff.cancel();
            callUpdateStaff = null;
        }
    }

    void changePasswordStaff(String _password){
        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("password",_password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,jsonObject.toString());

        //noinspection ConstantConditions
        callChangePass = apiService.changeStaffPassword(header,body, String.valueOf(data.getValue().id));
        callChangePass.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() >= 200 && response.code() < 400){
                    loading.setValue(false);
                    alertMessage.setValue("Parola başarı ile güncellendi");
                    alertShow.setValue(true);
                    callChangePass = null;
                }else{
                    try {
                        if (response.errorBody() != null) {
                            String error = Helper.getApiBadRequestError(response.errorBody().string());
                            loading.setValue(false);
                            alertMessage.setValue(error);
                            alertShow.setValue(true);
                            callChangePass = null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loading.setValue(false);
                alertMessage.setValue("Sistemsel bir hata oluştu");
                alertShow.setValue(true);
                callChangePass = null;
            }
        });


    }
}
