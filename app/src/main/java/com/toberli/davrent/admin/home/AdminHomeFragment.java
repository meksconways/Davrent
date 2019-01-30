package com.toberli.davrent.admin.home;

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
import com.toberli.davrent.database.AppDatabase;
import com.toberli.davrent.home.MainActivityViewModel;
import com.toberli.davrent.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AdminHomeFragment extends Fragment {

    private Unbinder unbinder;

    @Inject
    ViewModelFactory factory;
    private AdminHomeViewModel viewmodel;
    private Context context;
    private MainActivityViewModel _viewmodel;
    @BindView(R.id.card_categories)
    CardView card_categories;
    @BindView(R.id.card_discount)
    CardView card_discounts;
    @BindView(R.id.card_customer_type)
    CardView card_customerType;
    @BindView(R.id.card_staffs)
    CardView card_staffs;
    @BindView(R.id.card_profile)
    CardView card_profile;
    @BindView(R.id.card_goOut)
    CardView card_goOut;
    @BindView(R.id.card_customer)
    CardView card_costumer;
    @BindView(R.id.card_product)
    CardView card_product;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
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
        View view = inflater.inflate(R.layout.lay_admin_main,container,false);
        unbinder = ButterKnife.bind(this,view);
        card_categories.setOnClickListener(v -> setFragment(new AdminCategoriesFragment()));
        card_discounts.setOnClickListener(v -> setFragment(new DiscountFragment()));
        card_customerType.setOnClickListener(v -> setFragment(new CustomerTypeFragment()));
        card_staffs.setOnClickListener(v -> setFragment(new AdminStaffFragment()));
        card_profile.setOnClickListener(v -> setFragment(new ProfileFragment()));
        card_costumer.setOnClickListener(v -> setFragment(new CustomerFragment()));
        card_product.setOnClickListener(v -> setFragment(new ProductFragment()));
        card_goOut.setOnClickListener(v -> {
            AppDatabase.deleteDatabase(context);
            _viewmodel.setRouter(1);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _viewmodel = ViewModelProviders.of(getActivity(),factory).get(MainActivityViewModel.class);
        _viewmodel.setShowToolbar(true);
        _viewmodel.setToolbarTitle("Admin Panel");
        _viewmodel.setNeedBackButton(false);
        viewmodel = ViewModelProviders.of(this).get(AdminHomeViewModel.class);
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
