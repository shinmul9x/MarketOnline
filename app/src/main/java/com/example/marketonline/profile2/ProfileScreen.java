package com.example.marketonline.profile2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketonline.R;
import com.example.marketonline.adapters.ProductAdapter;
import com.example.marketonline.database.User;
import com.example.marketonline.utils.AppConstants;
import com.example.marketonline.utils.AppUtils;
import com.example.marketonline.utils.PreferencesUtils;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.Objects;

public class ProfileScreen extends AppCompatActivity implements View.OnClickListener {
    private IProfileContract.IPresenterAction mPresenter;
    private ImageView mIvAvatar;
    private TextView mTvUserName;
    private TextView mTvPhoneNum;
    private ProductAdapter mProductAdapter;
    private RecyclerView mRvProduct;
    private MaterialButton mBtnEditProfile;
    private int userId;
    private boolean otherUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen2);
        initComponents();
        initActions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContent();
    }

    private void initComponents() {
        mPresenter = new ProfilePresenter(this);
        mIvAvatar = findViewById(R.id.iv_avatar);
        mTvUserName = findViewById(R.id.tv_user_name);
        mTvPhoneNum = findViewById(R.id.tv_phone);
        mBtnEditProfile = findViewById(R.id.btn_edit_profile);
        int _userId = Integer.valueOf(PreferencesUtils.getString(this, AppConstants.USER_ID, AppConstants.NON_USER_ID));
        userId = getIntent().getIntExtra(AppConstants.ID_IN_PROFILE, _userId);
        otherUser = userId != _userId;
        mProductAdapter = new ProductAdapter(this, userId);
        mRvProduct = findViewById(R.id.rv_products);
        setContent();
    }

    @SuppressLint("SetTextI18n")
    private void setContent() {
        User user = mPresenter.getUser(userId);
        mIvAvatar.setImageBitmap(AppUtils.decodeFileToBitmap(new File(user.getAvatar()), 100));
        mTvUserName.setText(user.getName());
        mTvPhoneNum.setText("Tel: " + user.getPhoneNumber());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRvProduct.setLayoutManager(layoutManager);
        mRvProduct.setAdapter(mProductAdapter);
        initToolbar(user.getName());
    }

    private void initActions() {
        if (!otherUser) {
            mBtnEditProfile.setVisibility(View.VISIBLE);
            mBtnEditProfile.setOnClickListener(this);
        }
    }

    private void initToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_edit_profile) {
            startActivity(new Intent(this, com.example.marketonline.profilescreen.ProfileScreen.class));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
