package com.toberli.davrent.admin.product.addproduct;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.toberli.davrent.R;
import com.toberli.davrent.base.MyApplication;
import com.toberli.davrent.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ReadQrFragment extends Fragment {

    private CodeScanner mCodeScanner;

    @BindView(R.id.scanner_view)
    CodeScannerView scannerView;
    private Unbinder unbinder;
    private Context context;
    @Inject
    ViewModelFactory factory;
    private AddProductViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        Log.d( "onAttach: ","çalıştı");
        MyApplication.getAppComponent(context).inject(this);

    }
    final Activity activity = getActivity();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_readqr,container,false);
        Log.d( "onAttach:1 ","çalıştı");
        unbinder = ButterKnife.bind(this,view);
        Log.d( "onCreateView: ","g,d,");
        mCodeScanner = new CodeScanner(context, scannerView);
        Log.d( "***onDecodedXXX: ","girdik");
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                // işlemler result.gettext()
                Log.d( "***onDecoded: ","girdik");
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewModel.setQrCodeValue(result.getText());
                            viewModel.setIsCodeAdded(true);
                            getActivity().getSupportFragmentManager().popBackStack();
                            Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity(),factory).get(AddProductViewModel.class);
        Log.d( "onAttach:2 ","çalıştı");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d( "onAttach: 3","çalıştı");
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        Log.d( "onAttach: 5","çalıştı");
        super.onPause();
    }




}
