package com.toberli.davrent.admin.customertype.addcustomertype;

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

public class AddCustomerTypeFragment extends Fragment {


    private Context context;
    private Unbinder unbinder;
    @Inject
    ViewModelFactory factory;
    @BindView(R.id.txt_header)
    EditText header;
    @BindView(R.id.txt_desc)
    EditText desc;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.lyt_edit)
    LinearLayout linearLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn)
    Button buttonSend;
    @BindView(R.id.layoutProgress)
    ProgressBar progressBarLayout;
    private AddCustomerTypeViewModel viewmodel;
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
        View view = inflater.inflate(R.layout.lay_addcustomertype,container,false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivityViewModel _viewmodel = ViewModelProviders.of(getActivity(),factory).get(MainActivityViewModel.class);
        _viewmodel.setShowToolbar(true);
        _viewmodel.setNeedBackButton(true);
        _viewmodel.setToolbarTitle("Müşteri Tipi Ekle");
        viewmodel = ViewModelProviders.of(this,factory).get(AddCustomerTypeViewModel.class);
        observeSpinner();
        observeViewModel();
        buttonSend.setOnClickListener(v -> {
            String _header = header.getText().toString().trim();
            String _desc = desc.getText().toString().trim();

            viewmodel.addCustomerType(_header,_desc);

        });
    }

    private void observeSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //noinspection ConstantConditions

                viewmodel.setDiscountId(viewmodel.getDiscountData().getValue().get(position).id);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //noinspection ConstantConditions
                viewmodel.setDiscountId(viewmodel.getDiscountData().getValue().get(0).id);
            }
        });

    }

    private void observeViewModel() {
        viewmodel.getDiscountData().observe(this, disData -> {
            linearLayout.setVisibility(View.VISIBLE);
            String [] discounts = new String[disData.size()];
            for (int i = 0; i<disData.size();i++){
                discounts[i] = disData.get(i).title+"  %"+String.valueOf(disData.get(i).percent);
            }
            //noinspection ConstantConditions
            adapterSpinner = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, discounts);
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterSpinner);
            observeSpinner();

        });
        viewmodel.getShowAlert().observe(this, show -> {
            if (show){
                showAlert(viewmodel.getBodyString().getValue(),true);
            }
        });
        viewmodel.getShowBottomLoading().observe(this, show -> {
            if (show){
                progressBarLayout.setVisibility(View.VISIBLE);
            }else{
                progressBarLayout.setVisibility(View.GONE);
            }
        });
        viewmodel.getLoading().observe(this, loading -> {
            if (loading){
                linearLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }else{
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    void showAlert(String msg,Boolean is_api){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Bilgilendirme");
        builder.setMessage(msg);
        builder.setPositiveButton("Tamam", (dialog, which) -> {

            if (is_api){

                if (getActivity() != null) {
                    viewmodel.setShowAlert(false);
                    getActivity().getSupportFragmentManager().popBackStack();
                }

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
