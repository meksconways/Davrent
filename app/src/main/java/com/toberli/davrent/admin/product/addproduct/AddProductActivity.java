package com.toberli.davrent.admin.product.addproduct;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
import com.toberli.davrent.home.MainActivity;
import com.toberli.davrent.home.MainActivityViewModel;
import com.toberli.davrent.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddProductActivity extends AppCompatActivity {

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
    private AddProductViewModel viewmodel;
    private ProgressDialog progressDialog;
    private ArrayAdapter<String> adapterSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MyApplication.getAppComponent(this).inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_addproduct);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        viewmodel = ViewModelProviders.of(AddProductActivity.this,factory).get(AddProductViewModel.class);
        observeViewModel();
        btnAddQr.setOnClickListener(v -> pickQR());

    }
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private AlertDialog mAlertDialog;
    private void pickQR() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.CAMERA,
                    "QR Kod okutabilmeniz için izin vermeniz gerekiyor",
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            goToQR();
        }
    }
    private void goToQR(){
        //todo

        Intent i = new Intent(getApplicationContext(), ReadQRActivity.class);
        startActivity(i);
    }
    private static final int REQUEST_CODE_QR_SCAN = 101;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {

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


    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showAlertDialog("İzin Gerekli", rationale,
                    (dialog, which) -> ActivityCompat.requestPermissions(this,
                            new String[]{permission}, requestCode), "Tamam",
                    null, "İptal");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bilgilendirme");
        builder.setMessage(msg);
        builder.setPositiveButton("Tamam", (dialog, which) -> {


            viewmodel.setShowAlert(false);
            onBackPressed();


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
            adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryArray);
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


}
