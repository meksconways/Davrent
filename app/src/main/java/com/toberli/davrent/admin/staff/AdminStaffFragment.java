package com.toberli.davrent.admin.staff;

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
import com.toberli.davrent.admin.staff.addstaff.AddStaffFragment;
import com.toberli.davrent.admin.staff.editstaff.EditStaffFragment;
import com.toberli.davrent.admin.staff.editstaff.EditStaffViewModel;
import com.toberli.davrent.admin.staff.model.Data;
import com.toberli.davrent.base.MyApplication;
import com.toberli.davrent.home.MainActivityViewModel;
import com.toberli.davrent.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AdminStaffFragment extends Fragment implements StaffSelectedListener {


    private Context context;
    private Unbinder unbinder;

    @Inject
    ViewModelFactory factory;
    private AdminStaffViewModel viewmodel;
    @BindView(R.id.rv_staff)
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
        View view = inflater.inflate(R.layout.lay_adminstaff,container,false);
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
                            .beginTransaction().replace(R.id.screen_container,new AddStaffFragment())
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
        _viewmodel.setToolbarTitle("Personeller");
        _viewmodel.setNeedBackButton(true);
        _viewmodel.setShowToolbar(true);

        viewmodel = ViewModelProviders.of(this,factory).get(AdminStaffViewModel.class);


        recyclerView.setAdapter(new AdminStaffAdapter(viewmodel,this,this));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        observeViewModel();
        viewmodel.fetchData();
    }

    private void observeViewModel() {
        viewmodel.getLoading().observe(this, loading -> {
            if (loading){
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

            }else{
                progressBar.setVisibility(View.GONE);

            }
        });
        viewmodel.getData().observe(this, data -> {
            recyclerView.setVisibility(View.VISIBLE);
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
    public void setSelectedStaff(Data data) {
        EditStaffViewModel viewModel = ViewModelProviders.of(getActivity(),factory).get(EditStaffViewModel.class);
        viewModel.setData(data);
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.screen_container,new EditStaffFragment())
                .commit();
    }
}
