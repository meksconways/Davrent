package com.toberli.davrent.admin.customertype.editcustomertype;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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

public class EditCustomerTypeFragment extends Fragment {


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
    @BindView(R.id.txt_indirim)
    TextView seciliIndirim;
    @BindView(R.id.layoutProgress)
    ProgressBar progressBarLayout;
    private EditCustomerTypeViewModel viewmodel;
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
        View view = inflater.inflate(R.layout.lay_editcustomertype,container,false);
        unbinder = ButterKnife.bind(this,view);
        return view;

    }

    private void observeSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //noinspection ConstantConditions
                if (position != 0){
                    viewmodel.setDiscountId(viewmodel.getDiscountData().getValue().get(position-1).id);
                }else{
                    //noinspection ConstantConditions
                    viewmodel.setDiscountId(viewmodel.getData().getValue().discount.id);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //noinspection ConstantConditions
                viewmodel.setDiscountId(viewmodel.getData().getValue().discount.id);
            }
        });

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //noinspection ConstantConditions
        MainActivityViewModel _viewmodel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        _viewmodel.setShowToolbar(true);
        _viewmodel.setNeedBackButton(true);
        _viewmodel.setToolbarTitle("Müşteri Tipi Düzenle");
        viewmodel = ViewModelProviders.of(getActivity(),factory).get(EditCustomerTypeViewModel.class);

        buttonSend.setOnClickListener(v -> {

            String _header = header.getText().toString().trim();
            String _desc = header.getText().toString().trim();

            viewmodel.updateCustomerType(_header,_desc);

        });

        observeViewModel();
    }

    void showAlert(String msg,Boolean is_api){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Bilgilendirme");
        builder.setMessage(msg);
        builder.setPositiveButton("Tamam", (dialog, which) -> {

            if (is_api){

                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }

            }


        });
    }

    private void observeViewModel() {
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

        viewmodel.getDiscountData().observe(this, disData -> {
            linearLayout.setVisibility(View.VISIBLE);
            String [] discounts = new String[disData.size()+1];
            discounts[0] = "İndirim Seç";
            for (int i = 0; i<disData.size();i++){
                discounts[i+1] = disData.get(i).title+"  %"+String.valueOf(disData.get(i).percent);
            }
            //noinspection ConstantConditions
            adapterSpinner = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, discounts);
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterSpinner);
            observeSpinner();

        });
        viewmodel.getLoading().observe(this, loading -> {
            if (loading){
                linearLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }else{
                progressBar.setVisibility(View.GONE);
            }
        });
        viewmodel.getData().observe(this, data -> {
            header.setText(data.title);
            desc.setText(data.description);
            seciliIndirim.setText(String.format("Önceden Seçili İndirim: %s",data.discount.title));
        });
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
