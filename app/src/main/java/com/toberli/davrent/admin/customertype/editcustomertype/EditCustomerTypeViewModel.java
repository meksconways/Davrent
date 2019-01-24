package com.toberli.davrent.admin.customertype.editcustomertype;

import android.content.Context;
import android.util.Log;

import com.toberli.davrent.admin.customertype.model.Data;
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

public class EditCustomerTypeViewModel extends ViewModel {


    private final MutableLiveData<Data> data = new MutableLiveData<>();
    private final MutableLiveData<List<com.toberli.davrent.admin.discount.model.Data>> discountData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Integer> discount_id = new MutableLiveData<>();
    private final MutableLiveData<String> bodyString = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showAlert = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showBottomLoading = new MutableLiveData<>(false);

    private final ApiService apiService;
    private final Context context;
    private Call<DiscountModel> callDiscont;
    private Call<Void> callCostumer;

    void setDiscountId(Integer id){
        discount_id.setValue(id);
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

    public void setData(Data dataX){
        data.setValue(dataX);
    }

    LiveData<Data> getData() {
        return data;
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    LiveData<List<com.toberli.davrent.admin.discount.model.Data>> getDiscountData() {
        return discountData;
    }

    @Inject
    EditCustomerTypeViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
        getDiscounts();
    }

    void updateCustomerType(String title,String description){
        showBottomLoading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key",Helper.apikey);
        header.put("token",AppDatabase.getToken(context));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title",title);
            jsonObject.put("description",description);
            jsonObject.put("discount_id",String.valueOf(discount_id.getValue()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,jsonObject.toString());

        Log.d( "---updateCustomerType: ",jsonObject.toString());


        //noinspection ConstantConditions
        callCostumer = apiService.updateCostumerType(String.valueOf(data.getValue().id),header,body);
        callCostumer.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Log.d("onResponse: ", String.valueOf(response.code()));

                if (response.code() >= 200 && response.code() < 400){
                    bodyString.setValue("Müşteri Tipi Başarıyla Güncellendi");
                    showAlert.setValue(true);
                    showBottomLoading.setValue(false);
                    callCostumer = null;
                }else{


                    try {


                        if (response.errorBody() != null) {
                            Log.d( "---updateCustomerType: ","error");
                            String error = Helper.getApiBadRequestError(response.errorBody().string());
                            bodyString.setValue(error);
                            showAlert.setValue(true);
                            showBottomLoading.setValue(false);
                            callCostumer = null;
                        }else{
                            bodyString.setValue("Bir Hata Oluştu");
                            showAlert.setValue(true);
                            showBottomLoading.setValue(false);
                            callCostumer = null;

                            Log.d( "---updateCustomerType: ",response.toString());
                        }
                    } catch (IOException e) {
                        Log.d( "---updateCustomerType: ","errorX");
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d( "---updateCustomerType: ","fail");
                callCostumer = null;
            }
        });
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

    @Override
    protected void onCleared() {
        if (callDiscont != null) {
            callDiscont.cancel();
            callDiscont = null;
        }
        if (callCostumer != null){
            callCostumer.cancel();
            callCostumer = null;
        }
    }

     void setAlertValue(boolean b) {
        showAlert.setValue(b);
    }
}
