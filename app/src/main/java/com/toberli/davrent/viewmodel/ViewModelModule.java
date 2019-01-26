package com.toberli.davrent.viewmodel;

import com.toberli.davrent.admin.customertype.CustomerTypeViewModel;
import com.toberli.davrent.admin.customertype.addcustomertype.AddCustomerTypeViewModel;
import com.toberli.davrent.admin.customertype.editcustomertype.EditCustomerTypeViewModel;
import com.toberli.davrent.admin.discount.AddEditDiscountViewModel;
import com.toberli.davrent.admin.discount.DiscountViewModel;
import com.toberli.davrent.admin.staff.AdminStaffViewModel;
import com.toberli.davrent.admin.staff.addstaff.AddStaffViewModel;
import com.toberli.davrent.admin.staff.editstaff.EditStaffViewModel;
import com.toberli.davrent.home.MainActivityViewModel;
import com.toberli.davrent.login.LoginViewModel;
import com.toberli.davrent.splash.SplashViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import retrofit2.http.Body;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    abstract ViewModel bindMainActivityVM(MainActivityViewModel mainActivityViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel.class)
    abstract ViewModel bindSplashVM(SplashViewModel splashViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginVM(LoginViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DiscountViewModel.class)
    abstract ViewModel bindDiscountVM(DiscountViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddEditDiscountViewModel.class)
    abstract ViewModel bindAddEditDiscountVM(AddEditDiscountViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CustomerTypeViewModel.class)
    abstract ViewModel bindCustomerTypeVM(CustomerTypeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EditCustomerTypeViewModel.class)
    abstract ViewModel bindEditCustomerTypeVM(EditCustomerTypeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddCustomerTypeViewModel.class)
    abstract ViewModel bindAddCustomerTypeVM(AddCustomerTypeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AdminStaffViewModel.class)
    abstract ViewModel bindstaffVM(AdminStaffViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddStaffViewModel.class)
    abstract ViewModel bindAddstaffVM(AddStaffViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EditStaffViewModel.class)
    abstract ViewModel bindEditstaffVM(EditStaffViewModel viewModel);



}
