package com.toberli.davrent.admin.customer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.toberli.davrent.R;
import com.toberli.davrent.admin.customer.addcustomer.AddCustomerFragment;
import com.toberli.davrent.admin.customer.customerdetail.CustomerDetailFragment;
import com.toberli.davrent.admin.customer.customerdetail.CustomerDetailViewModel;
import com.toberli.davrent.admin.customer.model.Data;
import com.toberli.davrent.base.MyApplication;
import com.toberli.davrent.home.MainActivityViewModel;
import com.toberli.davrent.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CustomerFragment extends Fragment implements CustomerSelectedListener{

    private Context context;
    private Unbinder unbinder;

    @BindView(R.id.rv_cat)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @Inject
    ViewModelFactory factory;
    private CustomerViewModel viewmodel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_categories,container,false);
        unbinder = ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_discount,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.act_add:
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.screen_container,new AddCustomerFragment())
                            .addToBackStack(null)
                            .commit();
                }
                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivityViewModel _viewmodel = ViewModelProviders.of(getActivity(),factory).get(MainActivityViewModel.class);
        _viewmodel.setToolbarTitle("Müşteriler");
        _viewmodel.setNeedBackButton(true);
        _viewmodel.setShowToolbar(true);
        viewmodel = ViewModelProviders.of(this,factory).get(CustomerViewModel.class);
        observeViewModel();
        recyclerView.setAdapter(new CustomerAdapter(this,viewmodel,this));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewmodel.fetchData();
    }

    private void observeViewModel() {
        viewmodel.getLoading().observe(this, loading -> {
            if (loading){
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else{
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
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

    @Override
    public void customerSelectedListener(Data data) {
        if (getActivity() != null) {
            CustomerDetailViewModel viewModel = ViewModelProviders.of(getActivity(),factory).get(CustomerDetailViewModel.class);
            viewModel.setData(data);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.screen_container,new CustomerDetailFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }
}
