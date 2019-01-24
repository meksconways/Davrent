package com.toberli.davrent.admin.discount;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.toberli.davrent.R;
import com.toberli.davrent.base.MyApplication;
import com.toberli.davrent.home.MainActivityViewModel;
import com.toberli.davrent.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddEditDiscountFragment extends Fragment {


    private Unbinder unbinder;
    private Context context;
    @BindView(R.id.txt_desc)
    EditText desc;
    @BindView(R.id.txt_header)
    EditText header;
    @BindView(R.id.txt_percent)
    EditText perc;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    ViewModelFactory viewModelFactory;
    private MainActivityViewModel _viewmodel;


    private AddEditDiscountViewModel viewmodel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_adddiscount,container,false);
        unbinder = ButterKnife.bind(this,view);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewmodel = ViewModelProviders.of(getActivity()).get(AddEditDiscountViewModel.class);
        _viewmodel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        _viewmodel.setNeedBackButton(true);
        //noinspection ConstantConditions
        if (viewmodel.getIsEdit().getValue()){
            _viewmodel.setToolbarTitle("İndirim Düzenle");

        }else{
            _viewmodel.setToolbarTitle("Yeni İndirim Ekle");

        }

        btnSend.setOnClickListener(v -> {
            if (header.getText().toString().trim().matches("")){
                showAlert("Başlık boş olamaz");
                return;
            }
            if (desc.getText().toString().trim().matches("")){
                showAlert("Açıklama boş olamaz");
                return;
            }
            if (perc.getText().toString().trim().matches("")){
                showAlert("Yüzde alanı boş olamaz");
                return;
            }

            if (viewmodel.getIsEdit().getValue()){

                String _header = header.getText().toString().trim();
                String _desc = desc.getText().toString().trim();
                String _perc = perc.getText().toString().trim();

                viewmodel.updateDiscount(_header,_desc,_perc);

            }else{
                String _header = header.getText().toString().trim();
                String _desc = desc.getText().toString().trim();
                String _perc = perc.getText().toString().trim();
                viewmodel.addDiscount(_header,_desc,_perc);

            }

        });

        observeViewModel();
    }

    private void showAlert(String mes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(mes);
        builder.setTitle("Hata");
        builder.setPositiveButton("Tamam", (dialog, which) -> {
            // nothing
        });
        builder.show();
    }

    private void observeViewModel() {
        viewmodel.getIsEdit().observe(this, isEdit -> {
            if (isEdit){

                //noinspection ConstantConditions
                header.setText(viewmodel.getDatas().getValue().title);
                desc.setText(viewmodel.getDatas().getValue().desc);
                perc.setText(String.valueOf(viewmodel.getDatas().getValue().percent));

            }else{
                header.setText(null);
                desc.setText(null);
                perc.setText(null);
            }
        });
        viewmodel.getLoading().observe(this, loading -> {
            if (loading != null) {
                if (loading){
                    progressBar.setVisibility(View.VISIBLE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().popBackStack();
                        viewmodel.setLoading(null);
                    }

                }
            }else{
                progressBar.setVisibility(View.GONE);
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
