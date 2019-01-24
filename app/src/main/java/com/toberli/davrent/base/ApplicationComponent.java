package com.toberli.davrent.base;

import com.toberli.davrent.admin.customertype.CustomerTypeFragment;
import com.toberli.davrent.admin.customertype.addcustomertype.AddCustomerTypeFragment;
import com.toberli.davrent.admin.customertype.editcustomertype.EditCustomerTypeFragment;
import com.toberli.davrent.admin.discount.AddEditDiscountFragment;
import com.toberli.davrent.admin.discount.DiscountFragment;
import com.toberli.davrent.admin.staff.AdminStaffFragment;
import com.toberli.davrent.admin.staff.addstaff.AddStaffFragment;
import com.toberli.davrent.home.MainActivity;
import com.toberli.davrent.login.LoginFragment;
import com.toberli.davrent.networking.NetworkModule;
import com.toberli.davrent.splash.SplashFragment;
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
}
