package com.toberli.davrent.admin.customertype.addcustomertype;

import android.content.Context;

import com.toberli.davrent.admin.discount.model.Data;
import com.toberli.davrent.admin.discount.model.DiscountModel;
import com.toberli.davrent.database.AppDatabase;
import com.toberli.davrent.helper.Helper;
import com.toberli.davrent.networking.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

public class AddCustomerTypeViewModel extends ViewModel {

    private final MutableLiveData<List<Data>> discountData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Integer> discount_id = new MutableLiveData<>();
    private final MutableLiveData<String> bodyString = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showAlert = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showBottomLoading = new MutableLiveData<>(false);


    private final ApiService apiService;
    private final Context context;
    private Call<DiscountModel> callDiscont;
    private Call<Void> callCustomer;

    void setDiscountId(Integer id){
        discount_id.setValue(id);
    }

    void setShowAlert(Boolean value){
        showAlert.setValue(value);
    }

    LiveData<String> getBodyString() {
        return bodyString;
    }

    LiveData<Boolean> getShowAlert() {
        return showAlert;
    }

    LiveData<Boolean> getShowBottomLoading() {
        return showBottomLoading;
    }

    LiveData<List<Data>> getDiscountData() {
        return discountData;
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    @Inject
    AddCustomerTypeViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
        getDiscounts();
    }

    private void getDiscounts() {
        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        callDiscont = apiService.getDiscount(AppDatabase.getScope(context),header);
        callDiscont.enqueue(new Callback<DiscountModel>() {
            @Override
            public void onResponse(Call<DiscountModel> call, Response<DiscountModel> response) {
                if (response.body() != null){
                    discountData.setValue(response.body().dataList);
                    loading.setValue(false);

                }else{
                    loading.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<DiscountModel> call, Throwable t) {

            }
        });
    }

    void addCustomerType(String header, String desc) {
        showBottomLoading.setValue(true);
        Map<String,String> map = new HashMap<>();
        map.put("api-key",Helper.apikey);
        map.put("token",AppDatabase.getToken(context));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title",header);
            jsonObject.put("description",desc);
            jsonObject.put("discount_id",discount_id.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType =  MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,jsonObject.toString());


        callCustomer = apiService.addCustomerType(map,body);
        callCustomer.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.body() != null){
                    bodyString.setValue("Müşteri Tipi Başarıyla Eklendi");
                    showAlert.setValue(true);
                    showBottomLoading.setValue(false);
                }else{
                    try {
                        if (response.errorBody() != null) {
                            String error = Helper.getApiBadRequestError(response.errorBody().string());
                            bodyString.setValue(error);
                            showAlert.setValue(true);
                            showBottomLoading.setValue(false);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onCleared() {
        if (callCustomer != null) {
            callCustomer.cancel();
            callCustomer = null;
        }
        if (callDiscont != null){
            callDiscont.cancel();
            callDiscont = null;
        }
    }
}
