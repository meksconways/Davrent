package com.toberli.davrent.admin.product.addproduct;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.toberli.davrent.R;
import com.toberli.davrent.base.MyApplication;
import com.toberli.davrent.home.MainActivityViewModel;
import com.toberli.davrent.home.MainActivity_ViewBinding;
import com.toberli.davrent.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddProductFragment extends Fragment {


    private static final String LOGTAG = "***";
    @Inject
    ViewModelFactory factory;
    @BindView(R.id.productName)
    EditText productName;
    @BindView(R.id.productDesc)
    EditText productDesc;
    @BindView(R.id.productprice)
    EditText productPrice;
    @BindView(R.id.txt_isAdded)
    TextView txt_isAdded;
    @BindView(R.id.btnAddQR)
    Button btnAddQr;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.spinner)
    Spinner spinner;
    private Context context;
    private Unbinder unbinder;
    private AddProductViewModel viewmodel;
    private ProgressDialog progressDialog;
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
        View view = inflater.inflate(R.layout.lay_addproduct,container,false);
        unbinder = ButterKnife.bind(this,view);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivityViewModel _viewModel = ViewModelProviders.of(getActivity(),factory).get(MainActivityViewModel.class);
        _viewModel.setShowToolbar(true);
        _viewModel.setNeedBackButton(true);
        _viewModel.setToolbarTitle("Ürün Ekle");
        viewmodel = ViewModelProviders.of(this,factory).get(AddProductViewModel.class);
        observeViewModel();
        btnAddQr.setOnClickListener(v -> {
            pickQR();
        });
        btnSend.setOnClickListener(v -> {
            viewmodel.addProduct(productName.getText().toString().trim(),
                                    productDesc.getText().toString().trim(),
                                    productPrice.getText().toString().trim());
        });
    }

    private void pickQR() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.CAMERA,
                    "QR Kod okutabilmeniz için izin vermeniz gerekiyor",
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            goToQR();
        }
    }

    private void goToQR(){
        Intent i = new Intent(context, ReadQRActivity.class);
        startActivityForResult(i, 1);
//        if (getActivity() != null) {
//            getActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.screen_container,new ReadQrFragment())
//                    .addToBackStack(null)
//                    .commit();
//        }
    }

    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        //noinspection ConstantConditions
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
            showAlertDialog("İzin Gerekli", rationale,
                    (dialog, which) -> ActivityCompat.requestPermissions(getActivity(),
                            new String[]{permission}, requestCode), "Tamam",
                    null, "İptal");
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d( "onRequestPermissions","izin verildi");
                    pickQR();
                }else{

                }
                return;
        }

    }

    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
    }

    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private AlertDialog mAlertDialog;
    private static final int REQUEST_CODE_QR_SCAN = 101;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK)
        {
            Log.d(LOGTAG,"COULD NOT GET A GOOD RESULT.");
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
            Log.d(LOGTAG,"Have scan result in your app activity :"+ result);
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
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                viewmodel.setIsCodeAdded(true);
                viewmodel.setQrCodeValue(result);
            }
        }

    }

    private void observeSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //noinspection ConstantConditions
                viewmodel.setCategoryID(String.valueOf(viewmodel.getCategoryData().getValue().get(position).id));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //noinspection ConstantConditions
                viewmodel.setCategoryID(String.valueOf(viewmodel.getCategoryData().getValue().get(0).id));
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

    private void observeViewModel() {
        viewmodel.getIsCodeAdded().observe(this, isAdded -> {
            if (isAdded){
                txt_isAdded.setText("Kod Eklenme Durumu: Eklendi");
                txt_isAdded.setTextColor(getResources().getColor(R.color.colorAndroid));
            }else{
                txt_isAdded.setText("Kod Eklenme Durumu: Eklenmedi");
                txt_isAdded.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });
        viewmodel.getCategoryData().observe(this, categoryData -> {
            String [] categoryArray = new String[categoryData.size()];
            for (int i = 0; i<categoryData.size();i++){
                categoryArray[i] = categoryData.get(i).name;
            }
            //noinspection ConstantConditions
            adapterSpinner = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categoryArray);
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterSpinner);
            observeSpinner();

        });
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
