package com.toberli.davrent.admin.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.toberli.davrent.R;
import com.toberli.davrent.base.MyApplication;
import com.toberli.davrent.home.MainActivityViewModel;
import com.toberli.davrent.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment {


    private Context context;
    @Inject
    ViewModelFactory factory;
    @BindView(R.id.txt_name)
    EditText name;
    @BindView(R.id.txt_phone)
    EditText phone;
    @BindView(R.id.txt_totaladdedcustomer)
    TextView total_customer;
    @BindView(R.id.txt_totalrenting)
    TextView total_rent;
    @BindView(R.id.btn_send)
    Button updateProfile;
    @BindView(R.id.txt_oldpass)
    EditText oldPass;
    @BindView(R.id.txt_newpass)
    EditText newPass;
    @BindView(R.id.txt_newpassagain)
    EditText newPassAgain;
    @BindView(R.id.btn_passUpdate)
    Button passUpdate;
    private Unbinder unbinder;
    private ProfileViewModel viewmodel;
    ProgressDialog progressDialog;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_profile,container,false);
        unbinder = ButterKnife.bind(this,view);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivityViewModel _viewmodel = ViewModelProviders.of(getActivity(),factory).get(MainActivityViewModel.class);
        _viewmodel.setToolbarTitle("Profil");
        _viewmodel.setNeedBackButton(true);
        _viewmodel.setShowToolbar(true);
        viewmodel = ViewModelProviders.of(this,factory).get(ProfileViewModel.class);
        observeViewModel();
        updateProfile.setOnClickListener(v -> viewmodel.updateProfile(name.getText().toString().trim(),
                phone.getText().toString().trim()));
        passUpdate.setOnClickListener(v -> viewmodel.changePassword(oldPass.getText().toString().trim(),
                newPass.getText().toString().trim(),
                newPassAgain.getText().toString().trim()));

    }

    private void observeViewModel() {
        viewmodel.getData().observe(this, data -> {
            if (data != null){
                name.setText(data.name);
                phone.setText(data.phone.substring(3));
                total_customer.setText(String.format("Eklenen Toplam Müşteri : %s",data.totalCustomer));
                total_rent.setText(String.format("Eklenen Toplam Kiralama : %s",data.totalRents));
            }
        });
        viewmodel.getAlertShow().observe(this, show -> {
            if (show){
                showAlert(viewmodel.getAlertMessage().getValue());
            }
        });
        viewmodel.getLoading().observe(this, loading -> {
            if (loading){
                progressDialog.show();
            }else{
                progressDialog.dismiss();
            }
        });
    }

    private void showAlert(String value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Bilgilendirme");
        builder.setMessage(value);
        builder.setPositiveButton("Tamam", (dialog, which) -> {

            if (getActivity() != null) {
                viewmodel.setShowAlert(false);
                viewmodel.getProfile();
                oldPass.setText("");
                newPass.setText("");
                newPassAgain.setText("");
            }

        });

        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }
}
