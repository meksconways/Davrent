package com.toberli.davrent.admin.customer.addcustomer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

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

public class AddCustomerFragment extends Fragment {


    @Inject
    ViewModelFactory factory;
    private Context context;
    private Unbinder unbinder;

    @BindView(R.id.txt_header)
    EditText header;
    @BindView(R.id.txt_phone)
    EditText phone;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.lyt_edit)
    LinearLayout linearLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn)
    Button buttonSend;
    private AddCustomerViewModel viewmodel;
    private ArrayAdapter<String> adapterSpinner;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_add_customer,container,false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivityViewModel _viewodel = ViewModelProviders.of(getActivity(),factory).get(MainActivityViewModel.class);
        _viewodel.setShowToolbar(true);
        _viewodel.setNeedBackButton(true);
        _viewodel.setToolbarTitle("Müşteri Ekle");
        viewmodel = ViewModelProviders.of(this,factory).get(AddCustomerViewModel.class);

        observeViewModel();
        buttonSend.setOnClickListener(v -> {
            viewmodel.addCustomer(header.getText().toString().trim(),phone.getText().toString().trim());
        });

    }

    private void observeViewModel() {
        viewmodel.getCustomerTypeData().observe(this, customerTypeData -> {

            String [] custType = new String[customerTypeData.size()];
            for (int i = 0; i < customerTypeData.size(); i++){
                custType[i] = customerTypeData.get(i).title;
            }
            //noinspection ConstantConditions
            adapterSpinner = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, custType);
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterSpinner);
            linearLayout.setVisibility(View.VISIBLE);
            observeSpinner();

        });
        viewmodel.getShowAlert().observe(this, show -> {
            if (show){
                showAlert(viewmodel.getAlertMessage().getValue());
            }
        });
        viewmodel.getLoading().observe(this, loading -> {
            if (loading){
                linearLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }else{
                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void observeSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //noinspection ConstantConditions

                viewmodel.setCustomerTypeID(String.valueOf(viewmodel.getCustomerTypeData().getValue().get(position).id));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //noinspection ConstantConditions
                viewmodel.setCustomerTypeID(String.valueOf(viewmodel.getCustomerTypeData().getValue().get(0).id));
            }
        });


    }

    void showAlert(String msg){
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }
}
