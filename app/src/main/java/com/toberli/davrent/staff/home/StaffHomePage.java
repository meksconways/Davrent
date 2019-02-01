package com.toberli.davrent.staff.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toberli.davrent.R;
import com.toberli.davrent.admin.categories.AdminCategoriesFragment;
import com.toberli.davrent.admin.customer.CustomerFragment;
import com.toberli.davrent.admin.customertype.CustomerTypeFragment;
import com.toberli.davrent.admin.discount.DiscountFragment;
import com.toberli.davrent.admin.product.ProductFragment;
import com.toberli.davrent.admin.profile.ProfileFragment;
import com.toberli.davrent.admin.staff.AdminStaffFragment;
import com.toberli.davrent.base.MyApplication;
import com.toberli.davrent.database.AppDatabase;
import com.toberli.davrent.home.MainActivity;
import com.toberli.davrent.home.MainActivityViewModel;
import com.toberli.davrent.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StaffHomePage extends Fragment {


    private Context context;
    private Unbinder unbinder;
    @Inject
    ViewModelFactory factory;
    @BindView(R.id.card_categories)
    CardView card_categories;
    @BindView(R.id.card_profile)
    CardView card_profile;
    @BindView(R.id.card_goOut)
    CardView card_goOut;
    @BindView(R.id.card_customer)
    CardView card_customer;
    @BindView(R.id.card_product)
    CardView card_product;

    private MainActivityViewModel _viewmodel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    private void setFragment(Fragment fragment){
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.screen_container,fragment)
                    .commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_staffhome,container,false);
        unbinder = ButterKnife.bind(this,view);
        card_categories.setOnClickListener(v -> setFragment(new AdminCategoriesFragment()));
        card_profile.setOnClickListener(v -> setFragment(new ProfileFragment()));
        card_goOut.setOnClickListener(v -> {
            AppDatabase.deleteDatabase(context);
            _viewmodel.setRouter(1);
        });
        card_customer.setOnClickListener(v -> setFragment(new CustomerFragment()));
        card_product.setOnClickListener(v -> setFragment(new ProductFragment()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _viewmodel = ViewModelProviders.of(getActivity(),factory).get(MainActivityViewModel.class);
        _viewmodel.setShowToolbar(true);
        _viewmodel.setNeedBackButton(false);
        _viewmodel.setToolbarTitle("Personel Panel");



    }
}
