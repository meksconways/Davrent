package com.toberli.davrent.admin.discount;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.toberli.davrent.R;
import com.toberli.davrent.admin.discount.model.Data;
import com.toberli.davrent.base.MyApplication;
import com.toberli.davrent.home.MainActivityViewModel;
import com.toberli.davrent.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

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

public class DiscountFragment extends Fragment implements DiscountItemClickEvent{


    private Context context;
    private Unbinder unbinder;
    @BindView(R.id.rv_discount)
    RecyclerView recyclerView;
    @BindView(R.id.empty_lay)
    LinearLayout emptyLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private MainActivityViewModel _viewmodel;
    @Inject
    ViewModelFactory viewmodelFactory;
    private DiscountViewModel viewmodel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_discount,container,false);
        unbinder = ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _viewmodel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        _viewmodel.setShowToolbar(true);
        _viewmodel.setToolbarTitle("İndirimler");
        _viewmodel.setNeedBackButton(true);
        viewmodel = ViewModelProviders.of(this,viewmodelFactory).get(DiscountViewModel.class);
        recyclerView.setAdapter(new DiscountAdapter(viewmodel,this,this));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewmodel.fetchData();
        observeViewModel();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_discount, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.act_add:
                AddEditDiscountViewModel addEditDiscountViewModel = ViewModelProviders
                        .of(getActivity(),viewmodelFactory)
                        .get(AddEditDiscountViewModel.class);
                addEditDiscountViewModel.setIsEdit(false);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.screen_container,new AddEditDiscountFragment())
                        .commit();


                return true;
        }
        return super.onOptionsItemSelected(item);


    }

    private void observeViewModel() {
        viewmodel.getDataList().observe(this, data -> {
            recyclerView.setVisibility(View.VISIBLE);

        });
        viewmodel.getIsEmpty().observe(this, isEmpty -> {
            if (isEmpty){
                emptyLayout.setVisibility(View.VISIBLE);
            }else{
                emptyLayout.setVisibility(View.GONE);
            }
        });
        viewmodel.getLoading().observe(this, loading -> {
            if (loading){
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
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

    @Override
    public void onClickDiscountItem(Data data) {
        if (getActivity() != null) {
            AddEditDiscountViewModel addEditDiscountViewModel = ViewModelProviders
                    .of(getActivity(),viewmodelFactory)
                    .get(AddEditDiscountViewModel.class);
            addEditDiscountViewModel.setDatas(data);
            addEditDiscountViewModel.setIsEdit(true);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.screen_container,new AddEditDiscountFragment())
                    .commit();
        }
    }
}
