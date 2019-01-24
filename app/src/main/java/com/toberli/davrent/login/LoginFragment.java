package com.toberli.davrent.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.toberli.davrent.R;
import com.toberli.davrent.base.MyApplication;
import com.toberli.davrent.database.AppDatabase;
import com.toberli.davrent.home.MainActivityViewModel;
import com.toberli.davrent.viewmodel.ViewModelFactory;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginFragment extends Fragment {


    private MainActivityViewModel _viewmodel;

    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_password)
    EditText edtPassword;

    @Inject
    ViewModelFactory viewModelFactory;
    private Context context;
    private LoginViewModel viewmodel;
    private ProgressDialog progressDialog;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_login,container,false);
        ButterKnife.bind(this,view);
        btn.setOnClickListener(v -> {
            
            String phone = edtPhone.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            
            if (phone.matches("")){
                showAlert("Lütfen Bir Telefon Numaranızı Girin");
                return;
            }
            if (password.matches("")){
                showAlert("Parola Boş Olamaz");
                return;
            }

            phone = "+90"+phone;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phone",phone);
                jsonObject.put("password",password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d( "***JSON:",jsonObject.toString());

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,jsonObject.toString());

            viewmodel.login(body);
            
            
        });

        return view;
    }

    private void showAlert(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(data);
        builder.setTitle("Hata");
        builder.setPositiveButton("Tamam", (dialog, which) -> {
            // nothing
        });
        builder.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _viewmodel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        _viewmodel.setShowToolbar(false);
        viewmodel = ViewModelProviders.of(this,viewModelFactory).get(LoginViewModel.class);
        observeViewModel();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Giriş Yapılıyor...");
        progressDialog.setCancelable(false);

    }

    private void observeViewModel() {
        viewmodel.getData().observe(this, data -> {

            String token = data.data.token;
            Integer isAdmin = data.data.isAdmin;
            String scope;
            if (isAdmin == 0){
                scope = "staff";
            }else{
                scope = "admin";
            }
            AppDatabase.addSharedWithKey(token,isAdmin,scope,context);

            if (isAdmin == 0){
                _viewmodel.setRouter(3);
            }else{
                _viewmodel.setRouter(2);
            }

        });
        viewmodel.getErrorBody().observe(this, errorString -> {
            //noinspection Convert2MethodRef
            showAlert(errorString);
        });
        viewmodel.getLoading().observe(this, loading -> {
            if (loading){
                progressDialog.show();
            }else {
                progressDialog.dismiss();
            }
        });
    }

}
