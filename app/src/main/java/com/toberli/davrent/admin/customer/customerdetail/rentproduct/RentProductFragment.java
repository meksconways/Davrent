package com.toberli.davrent.admin.customer.customerdetail.rentproduct;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.toberli.davrent.R;
import com.toberli.davrent.admin.product.addproduct.ReadQRActivity;
import com.toberli.davrent.base.MyApplication;
import com.toberli.davrent.home.MainActivityViewModel;
import com.toberli.davrent.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RentProductFragment extends Fragment {


    private static final int REQUEST_CODE_QR_SCAN = 101;
    private Context context;
    private Unbinder unbinder;

    @BindView(R.id.rv_product)
    RecyclerView recyclerView;
    @BindView(R.id.btn_addCode)
    Button btnAddCode;

    @Inject
    ViewModelFactory factory;
    private RentProductViewModel viewmodel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }
    RentProductAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_rentproduct,container,false);
        unbinder = ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivityViewModel _viewmodel = ViewModelProviders.of(getActivity(),factory).get(MainActivityViewModel.class);
        _viewmodel.setToolbarTitle("Ürün Kirala");

        viewmodel = ViewModelProviders.of(getActivity(),factory).get(RentProductViewModel.class);

        observeViewModel();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RentProductAdapter(viewmodel,this);
        recyclerView.setAdapter(adapter);
        btnAddCode.setOnClickListener(v -> {
            Intent i = new Intent(context, ReadQRActivity.class);
            startActivityForResult(i,1);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK)
        {
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if( result!=null)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }
            return;

        }
        if(requestCode == REQUEST_CODE_QR_SCAN)
        {
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Scan result");
            alertDialog.setMessage(result);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> {
                        dialog.dismiss();


                    });
            alertDialog.show();

        }
        if (requestCode == 1){
            if (data != null) {
                String result=data.getStringExtra("result");
                //viewmodel.getProductDataViaCode(result);
                Toast.makeText(context, "Ürün Eklendi", Toast.LENGTH_SHORT).show();
                //noinspection ConstantConditions
                if (viewmodel.getCodes().getValue().matches("")){
                    viewmodel.addCode(result);
                }else{
                    viewmodel.addCode("|"+viewmodel.getCodes().getValue()+"|"+result);
                }
            }
        }

    }

    private void observeViewModel() {
        viewmodel.getProductData().observe(this, data -> {
            adapter.setdata(data);
            adapter.notifyDataSetChanged();
        });
        viewmodel.getShowAlert().observe(this , show -> {
            if (show){
                showAlert(viewmodel.getAlertMessage().getValue());
            }
        });
    }

    void showAlert(String value){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(value);

        builder.setPositiveButton("Tamam", (dialog, which) -> {
           viewmodel.setShowAlert(false);
        });
        builder.show();
    }
}
