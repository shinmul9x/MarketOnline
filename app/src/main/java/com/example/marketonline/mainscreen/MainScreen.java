package com.example.marketonline.mainscreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.marketonline.R;
import com.example.marketonline.adapters.ProductAdapter2;
import com.example.marketonline.addproductscreen.AddProductScreen;
import com.example.marketonline.database.AppDatabase;
import com.example.marketonline.loginscreen.LoginScreen;
import com.example.marketonline.profilescreen.ProfileScreen;
import com.example.marketonline.searchproductscreen.SearchProductScreen;
import com.example.marketonline.splashscreen.SplashScreen;
import com.example.marketonline.utils.AppConstants;
import com.example.marketonline.utils.AppUtils;
import com.example.marketonline.utils.PreferencesUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IMainContract.IViewAction {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mIvAvatar;
    private TextView mTvUserName;
    private IMainContract.IPresenterAction mPresenter;
    private RecyclerView mRvProduct;
    private ProductAdapter2 mProductAdapter;
    private TextView mTvNoData;

    private int mUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        mUserId = Integer.valueOf(PreferencesUtils.getString(this, AppConstants.USER_ID, AppConstants.NON_USER_ID));
        if (mUserId > 0 && "".equals(AppDatabase.getDatabase().getNameUser(mUserId))) {
            startActivity(new Intent(this, ProfileScreen.class));
        }
        initComponents();
        initActions();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mTabAdapter.onChanged();
        mProductAdapter.onDataChanged();
        if (mProductAdapter.getItemCount() == 0) {
            mTvNoData.setVisibility(View.VISIBLE);
        } else {
            mTvNoData.setVisibility(View.INVISIBLE);
        }

        if (mUserId > 0) {
            View navHeader = mNavigationView.getHeaderView(0);
            mIvAvatar = navHeader.findViewById(R.id.iv_avatar);
            File imageFile = new File(mPresenter.getAvatar(mUserId));
            if (imageFile.exists()) {
                mIvAvatar.setImageBitmap(AppUtils.decodeFileToBitmap(imageFile, 100));
            }
            mTvUserName = navHeader.findViewById(R.id.tv_user_name);
            mTvUserName.setText(mPresenter.getUserName(mUserId));
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRvProduct.setLayoutManager(layoutManager);
        mRvProduct.setAdapter(mProductAdapter);
        mRvProduct.setHasFixedSize(true);
    }

    private void initComponents() {
        mPresenter = new MainPresenter(this);
        initDrawer();
        mNavigationView = findViewById(R.id.nav_view);
        mRvProduct = findViewById(R.id.rv_products);
        mTvNoData = findViewById(R.id.tv_no_data);
        mProductAdapter = new ProductAdapter2(this, mUserId);
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        initRecyclerView();
    }

    private void initActions() {
        mNavigationView.setNavigationItemSelectedListener(this);
//        if (mUserId == 0) {
//            mBtnAddProduct.setText(R.string.btn_login);
//            mBtnAddProduct.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    startActivity(new Intent(MainScreen.this, SplashScreen.class));
//                    finish();
//                }
//            });
//        } else {
//            mBtnAddProduct.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(MainScreen.this, AddProductScreen.class);
//                    intent.putExtra("USER_ID", mUserId);
//                    startActivity(intent);
//                }
//            });
//        }
//        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                if (position == 1) {
//                    mBtnAddProduct.setVisibility(View.VISIBLE);
//                    return;
//                }
//                mBtnAddProduct.setVisibility(View.INVISIBLE);
//            }
//        });
        initTabsAction();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        // code refresh data
                    }
                }, 3000);
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initTabsAction() {
        TabLayout tabLayout = findViewById(R.id.main_tab);
//        new TabLayoutMediator(tabLayout, mViewPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
//            @Override
//            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                if (position == 0) {
//                    tab.setText(R.string.tab_market_products);
//                    tab.setIcon(R.drawable.ic_market2);
//                } else if (position == 1) {
//                    tab.setText(R.string.tab_your_products);
//                    tab.setIcon(R.drawable.ic_profile3);
//                }
//            }
//        }).attach();
    }

    private void initDrawer() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.main_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_profile: {
                if (mUserId == 0) {
                    openDialogConfirmLogin();
                    break;
                }
//                startActivity(new Intent(this, ProfileScreen.class));
                startActivity(new Intent(this, com.example.marketonline.profile2.ProfileScreen.class));
                break;
            }
            case R.id.nav_search: {
                startActivity(new Intent(this, SearchProductScreen.class));
                break;
            }
            case R.id.nav_add_product: {
                if (mUserId == 0) {
                    openDialogConfirmLogin();
                    break;
                }
                Intent intent = new Intent(MainScreen.this, AddProductScreen.class);
                intent.putExtra("USER_ID", mUserId);
                startActivity(intent);
                break;
            }
            case R.id.nav_logout: {
                PreferencesUtils.putString(this, AppConstants.USER_ID, "0");
                startActivity(new Intent(this, SplashScreen.class));
                finish();
                break;
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(this, SearchProductScreen.class));
        } else if (item.getItemId() == R.id.action_add) {
            if (mUserId == 0) {
                openDialogConfirmLogin();
            } else {
                Intent intent = new Intent(MainScreen.this, AddProductScreen.class);
                intent.putExtra("USER_ID", mUserId);
                startActivity(intent);
            }
        }


        return super.onOptionsItemSelected(item);
    }

    private void openDialogConfirmLogin() {
        String title = "Thông báo!";
        String msg = "Bạn cần có tài khoản và đăng nhập để có thể sử dụng chức năng này! Bạn muốn đăng nhập vào hệ thống của chúng tôi chứ?";
        String btnText = "Đăng nhập";
        String btnCancelText = "Tôi muốn xem tiếp";
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // todo open edit screen
                startActivity(new Intent(MainScreen.this, LoginScreen.class));
                finish();
            }
        };
        AppUtils.openAlertDialog(this, title, msg, btnText, listener, true, btnCancelText);
    }
}
