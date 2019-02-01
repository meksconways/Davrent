package com.toberli.davrent.admin.customer.customerdetail;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.toberli.davrent.R;
import com.toberli.davrent.admin.customer.customerdetail.rentproduct.RentProductFragment;
import com.toberli.davrent.admin.customer.customerdetail.rentproduct.RentProductViewModel;
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

public class CustomerDetailFragment extends Fragment {


    private Context context;

    @Inject
    ViewModelFactory factory;
    private Unbinder unbinder;
    @BindView(R.id.btn_rentProduct)
    Button btnRentPro;
    @BindView(R.id.customerName)
    EditText customerName;
    @BindView(R.id.customerPhone)
    EditText customerPhone;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.btn_updateProfile)
    Button btnUpdateProfile;
    @BindView(R.id.btn_goRents)
    Button btnGoRents;
    @BindView(R.id.txt_customertype)
    TextView customerType;
    @BindView(R.id.btn_iade)
    Button btnIade;
    ProgressDialog progressDialog;
    private CustomerDetailViewModel viewmodel;
    private ArrayAdapter<String> adapterSpinner;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivityViewModel _viewmodel = ViewModelProviders.of(getActivity(),factory).get(MainActivityViewModel.class);
        _viewmodel.setToolbarTitle("Müşteri Detay");
        _viewmodel.setNeedBackButton(true);
        _viewmodel.setShowToolbar(true);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Güncelleniyor ...");

        viewmodel = ViewModelProviders.of(getActivity(),factory).get(CustomerDetailViewModel.class);
        observeViewModel();
        btnUpdateProfile.setOnClickListener(v -> {
            viewmodel.updateProfile(customerName.getText().toString().trim(),customerPhone.getText().toString().trim());
        });
        btnIade.setOnClickListener(v -> {
            viewmodel.returnProduct();
        });
    }

    private void showAlert(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Bilgilendirme");
        builder.setMessage(msg);
        builder.setPositiveButton("Tamam", (dialog, which) -> {
            if (getActivity() != null) {
                viewmodel.setShowAlert(false);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        builder.show();
    }

    private void observeViewModel() {

        viewmodel.getLoading().observe(this, loading -> {
            if (loading){
                progressDialog.show();
            }else{
                progressDialog.dismiss();
            }
        });

        viewmodel.getShowAlert().observe(this, show -> {
            if (show){
                showAlert(viewmodel.getAlertMessage().getValue());
            }
        });

        viewmodel.getData().observe(this, data -> {
            if (data.rents.size() == 0){
                btnGoRents.setVisibility(View.GONE);
            }
            customerName.setText(data.nameSurname);
            customerPhone.setText(data.phone.substring(3));
            customerType.setText(String.format("Müşteri Tipi : %s",data.type.title));
        });
        viewmodel.getCustomerTypeData().observe(this, customerTypeData -> {
            String [] custType = new String[customerTypeData.size()+1];
            custType[0] = "Müşteri Tipi Seç";
            for (int i = 0; i < customerTypeData.size(); i++){
                custType[i+1] = customerTypeData.get(i).title;
            }
            //noinspection ConstantConditions
            adapterSpinner = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, custType);
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterSpinner);
            observeSpinner();
        });



    }

    private void observeSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0){
                    //noinspection ConstantConditions
                    viewmodel.setCustomerTypeID(String.valueOf(viewmodel.getCustomerTypeData().getValue().get(position-1).id));
                }else{
                    //noinspection ConstantConditions
                    viewmodel.setCustomerTypeID(String.valueOf(viewmodel.getData().getValue().type.id));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //noinspection ConstantConditions
                viewmodel.setCustomerTypeID(String.valueOf(viewmodel.getData().getValue().type.id));
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_customerdetail,container,false);
        unbinder = ButterKnife.bind(this,view);

        btnRentPro.setOnClickListener(v -> {
            if (getActivity() != null) {
                //noinspection ConstantConditions
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.screen_container,RentProductFragment.newInstance(viewmodel.getData().getValue().id))
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
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
