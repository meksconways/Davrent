package com.toberli.davrent.admin.customertype;

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
import com.toberli.davrent.admin.customertype.addcustomertype.AddCustomerTypeFragment;
import com.toberli.davrent.admin.customertype.editcustomertype.EditCustomerTypeFragment;
import com.toberli.davrent.admin.customertype.editcustomertype.EditCustomerTypeViewModel;
import com.toberli.davrent.admin.customertype.model.Data;
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

public class CustomerTypeFragment extends Fragment implements CustomerTypeSelectedListener {


    private Context context;
    private Unbinder unbinder;
    @Inject
    ViewModelFactory factory;
    private CustomerTypeViewModel viewmodel;
    @BindView(R.id.rv_customertype)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_customer_type,container,false);
        unbinder = ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //noinspection ConstantConditions
        MainActivityViewModel _viewmodel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        _viewmodel.setToolbarTitle("Müşteri Tipleri");
        _viewmodel.setNeedBackButton(true);
        _viewmodel.setShowToolbar(true);
        viewmodel = ViewModelProviders.of(this,factory).get(CustomerTypeViewModel.class);
        recyclerView.setAdapter(new CustomerTypeAdapter(viewmodel,this,this));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        observeViewModel();
        viewmodel.fetchData();

    }

    private void observeViewModel() {
        viewmodel.getData().observe(this, data -> {
            recyclerView.setVisibility(View.VISIBLE);
        });
        viewmodel.getLoading().observe(this, loading -> {
            if (loading){
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }else{
                progressBar.setVisibility(View.GONE);
            }
        });
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
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.screen_container,new AddCustomerTypeFragment())
                            .commit();
                }
                return true;

        }


        return super.onOptionsItemSelected(item);
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
    public void setSelectedCustomerType(Data data) {
        //noinspection ConstantConditions
        EditCustomerTypeViewModel viewModel = ViewModelProviders.of(getActivity(),factory).get(EditCustomerTypeViewModel.class);
        viewModel.setData(data);
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.screen_container,new EditCustomerTypeFragment())
                .commit();
    }
}
