package com.toberli.davrent.home;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {


    /**
     * Router:
     * 0 -> Splash
     * 1 -> Login
     * 2 -> Admin
     * 3 -> Staff
     *
     */
    private final MutableLiveData<Integer> router = new MutableLiveData<>(0);
    /**
     * Back Button Görünümü
     * false -> görünmez
     * true -> görünür
     */
    private final MutableLiveData<Boolean> needBackButton = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showToolbar = new MutableLiveData<>(false);
    private final MutableLiveData<String> toolbarTitle = new MutableLiveData<>("Davrent");

    @Inject
    public MainActivityViewModel() {
    }



    public void setToolbarTitle(String data){
        toolbarTitle.setValue(data);
    }

    LiveData<String> getToolbarTitle(){
        return toolbarTitle;
    }

    public void setShowToolbar(Boolean data){
        showToolbar.setValue(data);
    }

    LiveData<Boolean> getShowToolbar(){
        return showToolbar;
    }

    LiveData<Boolean> getNeedBackButton(){
        return needBackButton;
    }

    public void setNeedBackButton(Boolean data){
        needBackButton.setValue(data);
    }


    LiveData<Integer> getRouter(){
        return router;
    }

    public void setRouter(Integer data){
        router.setValue(data);
    }


}
