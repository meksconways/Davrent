package com.toberli.davrent.base;

import com.toberli.davrent.admin.categories.AdminCategoriesFragment;
import com.toberli.davrent.admin.categories.addcategory.AddCategoryFragment;
import com.toberli.davrent.admin.categories.editcategory.EditCategoryFragment;
import com.toberli.davrent.admin.customer.CustomerFragment;
import com.toberli.davrent.admin.customer.addcustomer.AddCustomerFragment;
import com.toberli.davrent.admin.customer.customerdetail.CustomerDetailFragment;
import com.toberli.davrent.admin.customer.customerdetail.rentproduct.RentProductFragment;
import com.toberli.davrent.admin.customertype.CustomerTypeFragment;
import com.toberli.davrent.admin.customertype.addcustomertype.AddCustomerTypeFragment;
import com.toberli.davrent.admin.customertype.editcustomertype.EditCustomerTypeFragment;
import com.toberli.davrent.admin.discount.AddEditDiscountFragment;
import com.toberli.davrent.admin.discount.DiscountFragment;
import com.toberli.davrent.admin.product.ProductFragment;
import com.toberli.davrent.admin.product.addproduct.AddProductActivity;
import com.toberli.davrent.admin.product.addproduct.AddProductFragment;
import com.toberli.davrent.admin.product.addproduct.ReadQrFragment;
import com.toberli.davrent.admin.profile.ProfileFragment;
import com.toberli.davrent.admin.staff.AdminStaffFragment;
import com.toberli.davrent.admin.staff.addstaff.AddStaffFragment;
import com.toberli.davrent.admin.staff.editstaff.EditStaffFragment;
import com.toberli.davrent.home.MainActivity;
import com.toberli.davrent.login.LoginFragment;
import com.toberli.davrent.networking.NetworkModule;
import com.toberli.davrent.splash.SplashFragment;
import com.toberli.davrent.staff.home.StaffHomePage;
import com.toberli.davrent.viewmodel.ViewModelModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        ViewModelModule.class,
        NetworkModule.class,
})
public interface ApplicationComponent {

    void inject(SplashFragment splashFragment);

    void inject(LoginFragment loginFragment);

    void inject(MainActivity mainActivity);

    void inject(DiscountFragment discountFragment);

    void inject(AddEditDiscountFragment addEditDiscountFragment);

    void inject(CustomerTypeFragment customerTypeFragment);

    void inject(EditCustomerTypeFragment editCustomerTypeFragment);

    void inject(AddCustomerTypeFragment addCustomerTypeFragment);

    void inject(AdminStaffFragment adminStaffFragment);

    void inject(AddStaffFragment addStaffFragment);

    void inject(EditStaffFragment editStaffFragment);

    void inject(AdminCategoriesFragment adminCategoriesFragment);

    void inject(AddCategoryFragment addCategoryFragment);

    void inject(EditCategoryFragment editCategoryFragment);

    void inject(ProfileFragment profileFragment);

    void inject(StaffHomePage staffHomePage);

    void inject(CustomerFragment customerFragment);

    void inject(AddCustomerFragment addCustomerFragment);

    void inject(ProductFragment productFragment);

    void inject(AddProductFragment addProductFragment);

    void inject(AddProductActivity addProductActivity);

    void inject(ReadQrFragment readQrFragment);

    void inject(RentProductFragment rentProductFragment);

    void inject(CustomerDetailFragment customerDetailFragment);
}
