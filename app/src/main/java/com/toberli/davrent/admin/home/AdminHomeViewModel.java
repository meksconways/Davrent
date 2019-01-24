package com.toberli.davrent.admin.home;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AdminHomeViewModel extends ViewModel {

    private final MutableLiveData<List<AdminHomeModel>> model = new MutableLiveData<>();


    void setModel(List<AdminHomeModel> model){
        this.model.setValue(model);
    }

    LiveData<List<AdminHomeModel>> getModel(){
        return model;
    }



}
