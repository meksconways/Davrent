package com.toberli.davrent.viewmodel;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {


    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModels;

    @Inject
    ViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModels){
        this.viewModels = viewModels;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        try {

            //noinspection unchecked,ConstantConditions
            return ((T) viewModels.get(modelClass).get());

        }catch (Exception e){
            throw new RuntimeException(modelClass.getSimpleName(),e);
        }


    }
}