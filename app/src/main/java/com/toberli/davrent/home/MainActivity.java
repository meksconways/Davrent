package com.toberli.davrent.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Binds;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.toberli.davrent.R;
import com.toberli.davrent.admin.home.AdminHomeFragment;
import com.toberli.davrent.base.MyApplication;
import com.toberli.davrent.login.LoginFragment;
import com.toberli.davrent.splash.SplashFragment;
import com.toberli.davrent.viewmodel.ViewModelFactory;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {


    private MainActivityViewModel viewmodel;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.screen_container)
    FrameLayout screenContainer;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.getAppComponent(this).inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        viewmodel = ViewModelProviders.of(MainActivity.this,viewModelFactory).get(MainActivityViewModel.class);

        observeViewModel();
        setSupportActionBar(toolbar);
    }


    public void updateToolbarTitle(String title) {
        if (title != null) {
            toolbar.setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();
                return true;
        }


        return super.onOptionsItemSelected(item);

    }

    private void observeViewModel() {
        viewmodel.getToolbarTitle().observe(this, titleX -> {
            updateToolbarTitle(titleX);
        });

        viewmodel.getShowToolbar().observe(this, show -> {
            if (show){
                if (toolbar != null) {
                    toolbar.setVisibility(View.VISIBLE);
                }
            }else{
                if (toolbar != null) {
                    toolbar.setVisibility(View.GONE);
                }
            }
        });

        viewmodel.getNeedBackButton().observe(this, nbb -> {
            if (nbb){
                if (getSupportActionBar() != null) {

                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }else{
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            }
        });

        viewmodel.getRouter().observe(this, router -> {

            switch (router){
                case 0:
                    // splash
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.screen_container,new SplashFragment())
                            .disallowAddToBackStack()
                            .commit();
                    break;
                case 1:
                    // login
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.screen_container,new LoginFragment())
                            .disallowAddToBackStack()
                            .commit();
                    break;
                case 2:
                    //admin
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.screen_container,new AdminHomeFragment())
                            .disallowAddToBackStack()
                            .commit();
                    break;
                case 3:
                    // staff
                    Toast.makeText(this, "Staff", Toast.LENGTH_SHORT).show();
                    break;
            }

        });
    }


}
