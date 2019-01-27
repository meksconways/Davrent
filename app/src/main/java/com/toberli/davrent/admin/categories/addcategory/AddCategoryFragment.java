package com.toberli.davrent.admin.categories.addcategory;

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

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddCategoryFragment extends Fragment {


    @Inject
    ViewModelFactory factory;

    @BindView(R.id.txt_catdesc)
    EditText catDesc;
    @BindView(R.id.txt_catname)
    EditText catName;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_send)
    Button btn;
    private Context context;
    private Unbinder unbinder;
    AddCategoryViewModel viewmodel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_addcategory,container,false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivityViewModel _viewmodel = ViewModelProviders.of(getActivity(),factory).get(MainActivityViewModel.class);
        _viewmodel.setShowToolbar(true);
        _viewmodel.setNeedBackButton(true);
        _viewmodel.setToolbarTitle("Kategori Ekle");

        viewmodel = ViewModelProviders.of(this,factory).get(AddCategoryViewModel.class);

        btn.setOnClickListener(v -> {

            viewmodel.addCategory(catName.getText().toString().trim(),catDesc.getText().toString().trim());
        });

        observeViewModel();
    }

    private void observeViewModel() {

        viewmodel.getLoading().observe(this, loading -> {
            if (loading){
                progressBar.setVisibility(View.VISIBLE);
            }else{
                progressBar.setVisibility(View.GONE);
            }
        });
        viewmodel.getShowAlert().observe(this, show -> {
            if (show){
                showAlert(viewmodel.getAlertMessage().getValue());
            }
        });

    }

    private void showAlert(String value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Bilgilendirme");
        builder.setMessage(value);
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
