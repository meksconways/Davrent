package com.toberli.davrent.admin.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toberli.davrent.R;
import com.toberli.davrent.admin.categories.AdminCategoriesFragment;
import com.toberli.davrent.admin.customertype.CustomerTypeFragment;
import com.toberli.davrent.admin.discount.DiscountFragment;
import com.toberli.davrent.home.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

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

    private AdminHomeViewModel viewmodel;
    private Context context;
    private MainActivityViewModel _viewmodel;
    @BindView(R.id.card_categories)
    CardView card_categories;
    @BindView(R.id.card_discount)
    CardView card_discounts;
    @BindView(R.id.card_customer_type)
    CardView card_customerType;

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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _viewmodel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        _viewmodel.setShowToolbar(true);
        _viewmodel.setToolbarTitle("Admin Panel");
        _viewmodel.setNeedBackButton(false);
        viewmodel = ViewModelProviders.of(this).get(AdminHomeViewModel.class);
        fillMenu();

    }

    private List<AdminHomeModel> menuModel = new ArrayList<>();


    private void fillMenu() {
        int p1 = this.getResources().getIdentifier("ic_categories", "drawable", context.getPackageName());
        int p2 = this.getResources().getIdentifier("ic_staffs", "drawable", context.getPackageName());
        int p3 = this.getResources().getIdentifier("ic_product", "drawable", context.getPackageName());
        int p4 = this.getResources().getIdentifier("ic_renting", "drawable", context.getPackageName());
        int p5 = this.getResources().getIdentifier("ic_customer_type", "drawable", context.getPackageName());
        int p6 = this.getResources().getIdentifier("ic_discount", "drawable", context.getPackageName());
        int p7 = this.getResources().getIdentifier("ic_stock", "drawable", context.getPackageName());
        int p8 = this.getResources().getIdentifier("ic_customer", "drawable", context.getPackageName());

        menuModel.add(new AdminHomeModel("Kategoriler",p1,1));
        menuModel.add(new AdminHomeModel("Personeller",p2,2));
        menuModel.add(new AdminHomeModel("Ürünler",p3,3));
        menuModel.add(new AdminHomeModel("Kiralama",p4,4));
        menuModel.add(new AdminHomeModel("Müşteri Tipi",p5,5));
        menuModel.add(new AdminHomeModel("İndirimler",p6,6));
        menuModel.add(new AdminHomeModel("Stok",p7,7));
        menuModel.add(new AdminHomeModel("Müşteri",p8,8));

        viewmodel.setModel(menuModel);

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
