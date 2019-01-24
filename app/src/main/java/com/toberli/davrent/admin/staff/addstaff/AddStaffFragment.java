package com.toberli.davrent.admin.staff.addstaff;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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

public class AddStaffFragment extends Fragment {

    @BindView(R.id.txt_name)
    TextView name;
    @BindView(R.id.txt_phone)
    TextView phone;
    @BindView(R.id.txt_pass)
    TextView pass;
    @BindView(R.id.txt_pass_conf)
    TextView passConf;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private Context context;
    @Inject
    ViewModelFactory factory;
    private Unbinder unbinder;
    private AddStaffViewModel viewmodel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_addstaff,container,false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivityViewModel _viewmodel = ViewModelProviders.of(getActivity(),factory).get(MainActivityViewModel.class);
        _viewmodel.setShowToolbar(true);
        _viewmodel.setNeedBackButton(true);
        _viewmodel.setToolbarTitle("Personel Ekle");
        viewmodel = ViewModelProviders.of(this,factory).get(AddStaffViewModel.class);
        observeViewModel();

        btnSend.setOnClickListener(v -> {
            String _name = name.getText().toString().trim();
            String _phone = phone.getText().toString().trim();
            String _password = pass.getText().toString().trim();
            String _passConf = pass.getText().toString().trim();

            viewmodel.addStaff(_name,_password,_passConf,_phone);

        });


    }

    private void observeViewModel() {
        viewmodel.getLoading().observe(this, loading -> {
            if (loading){
                progressBar.setVisibility(View.VISIBLE);
            }else{
                progressBar.setVisibility(View.GONE);
            }
        });
        viewmodel.getShowAlert().observe(this, show -> {
            if (show){

                showAlert(viewmodel.getAlertMessage().getValue());

            }
        });
    }

    private void showAlert(String value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Bilgilendirme");
        builder.setMessage(value);
        builder.setPositiveButton("Tamam", (dialog, which) -> {

            viewmodel.setShowAlert(false);
            if (getActivity() != null) {
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
