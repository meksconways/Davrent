package com.toberli.davrent.admin.categories;

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
import com.toberli.davrent.admin.categories.addcategory.AddCategoryFragment;
import com.toberli.davrent.admin.categories.editcategory.EditCategoryFragment;
import com.toberli.davrent.admin.categories.editcategory.EditCategoryViewModel;
import com.toberli.davrent.admin.categories.model.Data;
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

public class AdminCategoriesFragment extends Fragment implements CategoriesSelectedListener {


    private MainActivityViewModel _viewmodel;
    private Unbinder unbinder;
    @BindView(R.id.rv_cat)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @Inject
    ViewModelFactory factory;
    private AdminCategoriesViewModel viewmodel;
    private Context context;

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
                            .addToBackStack(null)
                            .replace(R.id.screen_container,new AddCategoryFragment())
                            .commit();
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _viewmodel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        _viewmodel.setNeedBackButton(true);
        _viewmodel.setToolbarTitle("Kategoriler");
        _viewmodel.setShowToolbar(true);

        viewmodel = ViewModelProviders.of(this,factory).get(AdminCategoriesViewModel.class);

        recyclerView.setAdapter(new CatagoriesAdapter(viewmodel,this,this));
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
                recyclerView.setVisibility(View.VISIBLE);
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
    public void setSelectedCategory(Data data) {
        if (getActivity() != null) {
            EditCategoryViewModel viewmodel = ViewModelProviders.of(getActivity(),factory).get(EditCategoryViewModel.class);
            viewmodel.setData(data);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.screen_container,new EditCategoryFragment())
                    .commit();
        }

    }
}
