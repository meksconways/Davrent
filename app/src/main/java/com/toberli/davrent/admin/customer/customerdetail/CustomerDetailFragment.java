package com.toberli.davrent.admin.customer.customerdetail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.toberli.davrent.R;
import com.toberli.davrent.admin.customer.customerdetail.rentproduct.RentProductFragment;
import com.toberli.davrent.admin.customertype.CustomerTypeViewModel;
import com.toberli.davrent.base.MyApplication;
import com.toberli.davrent.home.MainActivityViewModel;
import com.toberli.davrent.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CustomerDetailFragment extends Fragment {


    private Context context;

    @Inject
    ViewModelFactory factory;
    private Unbinder unbinder;
    @BindView(R.id.btn_rentProduct)
    Button btnRentPro;
    private CustomerDetailViewModel viewmodel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivityViewModel _viewmodel = ViewModelProviders.of(getActivity(),factory).get(MainActivityViewModel.class);
        _viewmodel.setToolbarTitle("Müşteri Detay");
        _viewmodel.setNeedBackButton(true);
        _viewmodel.setShowToolbar(true);

        viewmodel = ViewModelProviders.of(getActivity(),factory).get(CustomerDetailViewModel.class);
        observeViewModel();
    }

    private void observeViewModel() {
        viewmodel.getCustomerTypeData().observe(this, customerTypeData -> {

        });



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_customerdetail,container,false);
        unbinder = ButterKnife.bind(this,view);

        btnRentPro.setOnClickListener(v -> {
            if (getActivity() != null) {

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.screen_container,new RentProductFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
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
