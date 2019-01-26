package com.toberli.davrent.admin.staff.editstaff;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.toberli.davrent.R;
import com.toberli.davrent.base.MyApplication;
import com.toberli.davrent.home.MainActivityViewModel;
import com.toberli.davrent.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EditStaffFragment extends Fragment {


    private Context context;
    private Unbinder unbinder;
    @Inject
    ViewModelFactory factory;
    private EditStaffViewModel viewmodel;

    @BindView(R.id.txt_name)
    EditText name;
    @BindView(R.id.txt_staffphone)
    EditText phone;
    @BindView(R.id.btn_updatePass)
    Button btnPass;
    @BindView(R.id.btn_updateProfile)
    Button btnProfile;
    @BindView(R.id.txt_pass)
    EditText pass;
    ProgressDialog progressDialog;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_editstaff,container,false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Güncelleniyor ...");
        progressDialog.setCancelable(false);
        MainActivityViewModel _viewmodel = ViewModelProviders.of(getActivity(),factory).get(MainActivityViewModel.class);
        _viewmodel.setToolbarTitle("Personel Profil");
        _viewmodel.setNeedBackButton(true);
        _viewmodel.setShowToolbar(true);
        viewmodel = ViewModelProviders.of(getActivity(),factory).get(EditStaffViewModel.class);

        btnProfile.setOnClickListener(v -> {

            String _name = name.getText().toString().trim();
            String _phone = phone.getText().toString().trim();
            viewmodel.updateStaff(_name,_phone);

        });

        btnPass.setOnClickListener(v -> {
            String _password = pass.getText().toString().trim();
            viewmodel.changePasswordStaff(_password);
        });

        observeViewModel();

    }

    private void observeViewModel() {
        viewmodel.getData().observe(this, data -> {
            name.setText(data.nameSurname);
            phone.setText(data.phone.substring(3));
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

    private void showAlert(String alertMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Bilgilendirme");
        builder.setMessage(alertMessage);
        builder.setPositiveButton("Tamam", (dialog, which) -> {
            if (getActivity() != null) {
                viewmodel.setAlertShow(false);
                getActivity().getSupportFragmentManager().popBackStack();
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
