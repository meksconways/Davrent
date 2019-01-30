package com.toberli.davrent.admin.product.addproduct;

import android.content.Context;
import android.util.Log;

import com.toberli.davrent.admin.categories.model.CategoriesModel;
import com.toberli.davrent.admin.categories.model.Data;
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

public class AddProductViewModel extends ViewModel {


    private final MutableLiveData<List<Data>> categoryData = new MutableLiveData<>();
    private final MutableLiveData<String> category_id = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> showAlert = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isCodeAdded = new MutableLiveData<>(false);
    private final MutableLiveData<String> qrCodeValue = new MutableLiveData<>("");

    private final ApiService apiService;
    private final Context context;
    private Call<CategoriesModel> callCat;
    private Call<Void> callAddProduct;

    LiveData<String> getQrCodeValue() {
        return qrCodeValue;
    }

    void setQrCodeValue(String value){
        qrCodeValue.setValue(value);
    }

    void setIsCodeAdded(Boolean value){
        isCodeAdded.setValue(value);
    }

    LiveData<Boolean> getIsCodeAdded() {
        return isCodeAdded;
    }

    void setCategoryID(String id){
        category_id.setValue(id);
    }
    void setShowAlert(Boolean value){
        showAlert.setValue(value);
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

    LiveData<List<Data>> getCategoryData() {
        return categoryData;
    }

    LiveData<String> getCategory_id() {
        return category_id;
    }

    @Inject
    AddProductViewModel(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
        getCategories();
    }

    void addProduct(String pName, String pDesc, String price){
        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key",Helper.apikey);
        header.put("token",AppDatabase.getToken(context));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title",pName);
            jsonObject.put("description",pDesc);
            jsonObject.put("code",qrCodeValue.getValue());
            jsonObject.put("price",price);
            jsonObject.put("category_id",category_id.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,jsonObject.toString());

        callAddProduct = apiService.addProduct(header,body);
        callAddProduct.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() >= 200 && response.code() < 400){
                    loading.setValue(false);
                    alertMessage.setValue("Ürün başarı ile oluşturuldu");
                    showAlert.setValue(true);
                }else{
                    loading.setValue(false);
                    try {
                        String error = Helper.getApiBadRequestError(response.errorBody().string());
                        alertMessage.setValue(error);
                        showAlert.setValue(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loading.setValue(false);
                alertMessage.setValue("Bağlantı Hatası");
                showAlert.setValue(true);
            }
        });


    }

    void getCategories(){
        loading.setValue(true);
        Map<String,String> header = new HashMap<>();
        header.put("api-key", Helper.apikey);
        header.put("token", AppDatabase.getToken(context));

        callCat = apiService.getCategories(header);
        callCat.enqueue(new Callback<CategoriesModel>() {
            @Override
            public void onResponse(Call<CategoriesModel> call, Response<CategoriesModel> response) {
                if (response.code() >= 200 && response.code() < 400){
                    //noinspection ConstantConditions
                    loading.setValue(false);
                    //noinspection ConstantConditions
                    categoryData.setValue(response.body().data);
                    callCat = null;
                }else{
                    loading.setValue(false);
                    callCat = null;
                }
            }

            @Override
            public void onFailure(Call<CategoriesModel> call, Throwable t) {
                callCat = null;
            }
        });
    }



    @Override
    protected void onCleared() {

    }
}
