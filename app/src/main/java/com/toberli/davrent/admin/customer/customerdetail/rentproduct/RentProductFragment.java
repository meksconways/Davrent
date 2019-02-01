package com.toberli.davrent.admin.customer.customerdetail.rentproduct;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

    @BindView(R.id.txt_rentinghour)
    EditText hour;
    @BindView(R.id.rv_product)
    RecyclerView recyclerView;
    @BindView(R.id.btn_addCode)
    Button btnAddCode;
    @BindView(R.id.indirimliFiyat)
    TextView indirimliFiyat;
    @BindView(R.id.indirimsizFiyat)
    TextView indirimsizFiyat;
    @BindView(R.id.indirimTutari)
    TextView indirimTutari;
    @BindView(R.id.btn_calculate)
    Button calculate;
    @BindView(R.id.btn_addRent)
    Button addRent;

    @Inject
    ViewModelFactory factory;
    private RentProductViewModel viewmodel;
    private Integer user_id;


    public static RentProductFragment newInstance(Integer user_id) {

        Bundle args = new Bundle();
        args.putInt("user_id",user_id);
        RentProductFragment fragment = new RentProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
        if (getArguments() != null) {
            user_id = getArguments().getInt("user_id");
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        adapter.clearAll();
        viewmodel.clearCodeAndData();
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivityViewModel _viewmodel = ViewModelProviders.of(getActivity(),factory).get(MainActivityViewModel.class);
        _viewmodel.setToolbarTitle("Ürün Kirala");

        viewmodel = ViewModelProviders.of(this,factory).get(RentProductViewModel.class);

        observeViewModel();
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        adapter = new RentProductAdapter(viewmodel,this);
        recyclerView.setAdapter(adapter);
        btnAddCode.setOnClickListener(v -> {
            Intent i = new Intent(context, ReadQRActivity.class);
            startActivityForResult(i,1);
        });
        calculate.setOnClickListener(v -> {
            viewmodel.setUserID(user_id);
            viewmodel.calculateRent(hour.getText().toString().trim());
        });
        addRent.setOnClickListener(v -> {
            viewmodel.setUserID(user_id);
            viewmodel.saveRent(hour.getText().toString().trim());
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
                viewmodel.getProductDataViaCode(result);
                //Toast.makeText(context, "Ürün Eklendi", Toast.LENGTH_SHORT).show();

                if (viewmodel.getCodes().getValue() == null){
                    viewmodel.addCode(result);
                }else{
                    viewmodel.addCode("|"+viewmodel.getCodes().getValue()+"|"+result);
                }
            }
        }

    }

    private void observeViewModel() {

        viewmodel.getCalculateData().observe(this, calData -> {
            if (calData != null){
                indirimsizFiyat.setText(String.format("İndirimsiz Fiyatı: %s",calData.totalPrice));
                indirimTutari.setText(String.format("İndirim Tutarı: %s",calData.discountPrice));
                indirimliFiyat.setText(String.format("İndirimli Fiyatı: %s",calData.endPrice));
            }else{
                indirimliFiyat.setText(null);
                indirimsizFiyat.setText(null);
                indirimTutari.setText(null);
            }
        });

        viewmodel.getProductData().observe(this, data -> {
            if (data != null){
                recyclerView.setVisibility(View.VISIBLE);
                Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (vib != null) {
                    vib.vibrate(500);
                }
            }

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
