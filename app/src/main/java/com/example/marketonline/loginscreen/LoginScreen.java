package com.example.marketonline.loginscreen;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.marketonline.R;
import com.example.marketonline.database.AppDatabase;
import com.example.marketonline.database.User;
import com.example.marketonline.mainscreen.MainScreen;
import com.example.marketonline.registerscreen.RegisterScreen;
import com.example.marketonline.utils.AppConstants;
import com.example.marketonline.utils.AppUtils;
import com.example.marketonline.utils.PreferencesUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {
    private MaterialButton mBtnLogin;
    private MaterialButton mBtnSkip;
    private MaterialButton mBtnRegister;
    private TextInputEditText mEtUsername;
    private TextInputEditText mEtPassword;
    private ILoginContract.IPresenterAction mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        initComponents();
        initActions();
    }

    private void initComponents() {
        initToolBar();
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnSkip = findViewById(R.id.btn_skip_login);
        mBtnRegister = findViewById(R.id.btn_register);
        mEtUsername = findViewById(R.id.et_username);
        mEtPassword = findViewById(R.id.et_password);
        mPresenter = new LoginPresenter(this);
    }

    private void initActions() {
        mBtnLogin.setOnClickListener(this);
        mBtnSkip.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        setAutoHideKeyboard();
        setAutoClearError();
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEtUsername.setText("");
        mEtPassword.setText("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                for (User user : AppDatabase.getDatabase().getAllUsers()) {
                    AppUtils.log(this, user.getId() + " " + user.getUserName() + " " + user.toString());
                } // Log

                verifyAccount();
                break;
            case R.id.btn_skip_login:
                PreferencesUtils.putString(this, AppConstants.USER_ID, AppConstants.NON_USER_ID);
                startActivity(new Intent(LoginScreen.this, MainScreen.class));
                finish();
                break;
            case R.id.btn_register:
                startActivity(new Intent(LoginScreen.this, RegisterScreen.class));
                break;
        }
        clearFocus();
    }

    private void verifyAccount() {
        boolean hasBoxEmpty = false;
        String username = String.valueOf(mEtUsername.getText());
        if (username.equals("")) {
            hasBoxEmpty = true;
            AppUtils.setError(mEtUsername);
        }
        String password = String.valueOf(mEtPassword.getText());
        if (password.equals("")) {
            hasBoxEmpty = true;
            AppUtils.setError(mEtPassword);
        }
        int id = mPresenter.verifyAccount(username, password);
        AppUtils.log(this, id + "");

        if (hasBoxEmpty) return;
        if (id != Integer.valueOf(AppConstants.NON_USER_ID)) {
            PreferencesUtils.putString(this, AppConstants.USER_ID, id + "");
            Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);
            finish();
        } else {
            openDialogNotifyLoginFailed();
        }
    }

    private void openDialogNotifyLoginFailed() {
        String title = "Đăng nhập thất bại!";
        String msg = "Có vẻ như tài khoản bạn nhập không chính xác, hãy thử xem bạn có nhầm ở đâu không?";
        String buttonText = "Tôi muốn tạo tài khoản mới";
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(LoginScreen.this, RegisterScreen.class));
            }
        };
        String cancelText = "Để tôi xem lại";
        AppUtils.openAlertDialog(this, title, msg, buttonText, listener, true, cancelText);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_power) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void setAutoHideKeyboard() {
        View.OnTouchListener listener = new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtils.closeKeyBoard(LoginScreen.this, LoginScreen.this.getCurrentFocus()
                );
                clearFocus();
                return false;
            }
        };
        findViewById(R.id.layout_root).setOnTouchListener(listener);
//        findViewById(R.id.scroll_view).setOnTouchListener(listener);
        findViewById(R.id.login_toolbar).setOnTouchListener(listener);
    }

    private void clearFocus() {
        findViewById(R.id.layout_root).requestFocus();
    }

    private void setAutoClearError() {
        AppUtils.setAutoClearError(mEtUsername);
        AppUtils.setAutoClearError(mEtPassword);
    }
}
