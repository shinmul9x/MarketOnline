package com.example.marketonline.profilescreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.marketonline.R;
import com.example.marketonline.database.User;
import com.example.marketonline.loginscreen.LoginScreen;
import com.example.marketonline.utils.AppConstants;
import com.example.marketonline.utils.AppUtils;
import com.example.marketonline.utils.PreferencesUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

public class ProfileScreen extends AppCompatActivity {
    private IProifleContract.IPresenterAction mPresenter;
    private ImageView mIvAvatar;
    private TextInputEditText mEtName;
    private TextInputEditText mEtUserName;
    private TextInputEditText mEtPassword;
    private TextInputEditText mEtPhoneNumber;
    private TextView mBtnChangeAvatar;
    private User mUser;
    private String mImgPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        Log.d("MarketOnline", PreferencesUtils.getString(this, AppConstants.USER_ID, "0"));
        initComponents();
        initActions();
    }

    private void initComponents() {
        initToolBar();
        mPresenter = new ProfilePresenter(this);
        mUser = mPresenter.getUserInfo();
        mIvAvatar = findViewById(R.id.iv_avatar);
        mEtName = findViewById(R.id.et_name);
        mEtUserName = findViewById(R.id.et_username);
        mEtPassword = findViewById(R.id.et_password);
        mEtPhoneNumber = findViewById(R.id.et_phone_number);
        mBtnChangeAvatar = findViewById(R.id.btn_change_avatar);
        mImgPath = "";
        displayUserInfo();
    }

    private void displayUserInfo() {
        assert mUser != null;
        if (new File(mUser.getAvatar()).exists()) {
            mIvAvatar.setImageBitmap(AppUtils.decodeFileToBitmap(new File(mUser.getAvatar()), 100));
        }
        mEtName.setText(mUser.getName());
        mEtUserName.setText(mUser.getUserName());
        mEtPassword.setText(mUser.getPassword());
        mEtPhoneNumber.setText(mUser.getPhoneNumber());
    }

    private void initActions() {
        mEtName.setEnabled(true);
        mEtPassword.setEnabled(true);
        ((TextInputLayout) mEtPassword.getParent().getParent()).setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        mEtPhoneNumber.setEnabled(true);
        mBtnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });
        setAutoHideKeyboard();
        setAutoClearError();
    }

    private boolean updateUserInfo() {
        String name = String.valueOf(mEtName.getText());
        String password = String.valueOf(mEtPassword.getText());
        String phoneNumber = String.valueOf(mEtPhoneNumber.getText());

        boolean hasEmptyBox = false;
        if (name.length() == 0) {
            hasEmptyBox = true;
            AppUtils.setError(mEtName, "Chưa có tên người dùng");
        }
        if (password.length() == 0) {
            hasEmptyBox = true;
            AppUtils.setError(mEtPassword, "Chưa có mật khẩu");
        }
        if (phoneNumber.length() < 10) {
            hasEmptyBox = true;
            AppUtils.setError(mEtPhoneNumber, "Số điện thoại sai");
        }
        if (hasEmptyBox) {
            return false;
        }

        if (!mImgPath.equals("")) {
            mUser.setAvatar(mImgPath);
        }
        if (password.equals(mUser.getPassword())) {
            mUser.setName(name);
            mUser.setPhoneNumber(phoneNumber);
            mPresenter.updateUserInfo(mUser);
        } else {
            //todo: open dialog confirm new password
            mUser.setName(name);
            mUser.setPhoneNumber(phoneNumber);
            mUser.setPassword(password);
            mPresenter.updateUserInfo(mUser);
        }
        return true;
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            // submit
            if (updateUserInfo()) {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, AppConstants.GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AppConstants.GALLERY_REQUEST_CODE) {
                assert data != null;
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                assert selectedImage != null;
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mImgPath = cursor.getString(columnIndex);
                cursor.close();
                mIvAvatar.setImageBitmap(AppUtils.decodeFileToBitmap(new File(mImgPath), 100));
            }
        }
    }

    private void setAutoHideKeyboard() {
        View.OnTouchListener listener = new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtils.closeKeyBoard(ProfileScreen.this, ProfileScreen.this.getCurrentFocus()
                );
                clearFocus();
                return false;
            }
        };
        findViewById(R.id.layout_root).setOnTouchListener(listener);
        findViewById(R.id.profile_toolbar).setOnTouchListener(listener);
    }

    private void clearFocus() {
        findViewById(R.id.layout_root).requestFocus();
    }

    private void setAutoClearError() {
        AppUtils.setAutoClearError(mEtName);
        AppUtils.setAutoClearError(mEtPassword);
        AppUtils.setAutoClearError(mEtPhoneNumber);
    }
}
