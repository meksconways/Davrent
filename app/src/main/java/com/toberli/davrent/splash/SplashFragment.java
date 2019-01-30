package com.toberli.davrent.splash;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.toberli.davrent.R;
import com.toberli.davrent.base.MyApplication;
import com.toberli.davrent.database.AppDatabase;
import com.toberli.davrent.home.MainActivity;
import com.toberli.davrent.home.MainActivityViewModel;
import com.toberli.davrent.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class SplashFragment extends Fragment {


    private MainActivityViewModel _viewmodel;
    private Context context;
    @Inject
    ViewModelFactory viewModelFactory;
    private SplashViewModel viewmodel;
    private String token;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lay_splash,container,false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _viewmodel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        _viewmodel.setNeedBackButton(false);
        _viewmodel.setShowToolbar(false);
        _viewmodel.setToolbarTitle("");
        viewmodel = ViewModelProviders.of(this,viewModelFactory).get(SplashViewModel.class);
        observeViewModel();

        if (isNetworkConnected()){


            if (AppDatabase.getToken(context) != null){

                token = AppDatabase.getToken(context);

                viewmodel.checkToken(token);

            }else{
                Log.d( "***onViewCreated: ","2");
                _viewmodel.setRouter(1);
            }

        }else{
            Toast.makeText(context, "Lütfen Bağlantınızı Kontrol Edin", Toast.LENGTH_SHORT).show();
        }

    }

    private void observeViewModel() {
        viewmodel.getCheckToken().observe(this, checkToken -> {

            if (checkToken){
                //noinspection ConstantConditions
                Integer isAdmin = viewmodel.getData().getValue().data.isAdmin;
                String scope;
                if (isAdmin == 0){
                    scope = "staff";
                    AppDatabase.addSharedWithKey(token,isAdmin,scope,context);
                    _viewmodel.setRouter(3);
                }else{
                    scope = "admin";
                    AppDatabase.addSharedWithKey(token,isAdmin,scope,context);
                    _viewmodel.setRouter(2);
                }

            }else{

                _viewmodel.setRouter(1);
            }
        });
    }

    private boolean isNetworkConnected() {
        if (getActivity() != null) {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm != null) {
                return cm.getActiveNetworkInfo() != null;
            }else{
                return false;
            }
        }else {
            return false;
        }

    }


}
