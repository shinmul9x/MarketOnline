package com.example.marketonline.registerscreen;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.marketonline.R;
import com.example.marketonline.database.AppDatabase;
import com.example.marketonline.utils.AppUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText mEtUsername;
    private TextInputEditText mEtPassword;
    private TextInputEditText mEtRePassword;
    private MaterialButton mBtnRegister;
    private MaterialButton mBtnLogin;
    private IRegisterContract.IPresenterAction mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        initComponents();
        initActions();
    }

    private void initComponents() {
        mEtUsername = findViewById(R.id.et_register_username);
        mEtPassword = findViewById(R.id.et_register_password);
        mEtRePassword = findViewById(R.id.et_register_re_password);
        mBtnRegister = findViewById(R.id.btn_register);
        mBtnLogin = findViewById(R.id.btn_login);
        mPresenter = new RegisterPresenter();
    }

    private void initActions() {
        initToolbar();
        mBtnRegister.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        setAutoHideKeyboard();
        setAutoClearError();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_register) {
            registerAccount();
        } else if (id == R.id.btn_login) {
            onBackPressed();
        }
        clearFocus();
    }

    private void registerAccount() {
        boolean hasBoxEmpty = false;

        String username = String.valueOf(mEtUsername.getText());
        if (username.equals("") || username.length() < 4) {
            hasBoxEmpty = true;
            AppUtils.setError(mEtUsername);
        }
        String password = String.valueOf(mEtPassword.getText());
        if (password.equals("") || username.length() < 4) {
            hasBoxEmpty = true;
            AppUtils.setError(mEtPassword);
        }
        String rePassword = String.valueOf(mEtRePassword.getText());
        if (!rePassword.equals(password)) {
            hasBoxEmpty = true;
            AppUtils.setError(mEtRePassword, "* nhập lại mật khẩu không đúng");
        }
        if (hasBoxEmpty) return;
        if (mPresenter.registerAccount(username, password, rePassword)) {
            openDialogNotifyRegisterSuccess();
        } else {
            openDialogNotifyRegisterFail();
        }
    }

    private void openDialogNotifyRegisterSuccess() {
        String title = "Thành công!";
        String msg = "Chúc mừng bạn đã khởi tạo tài khoản thành công. Chúc bạn sẽ có những trải nghiệm vui vẻ với hệ thống của chúng tôi.";
        String buttonText = "Tuyệt vời";
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RegisterScreen.this.onBackPressed();
            }
        };
        AppUtils.openAlertDialog(this, title, msg, buttonText, listener, false);
    }

    private void openDialogNotifyRegisterFail() {
        String title = "Thất bại!";
        String msg = "Dường như bạn đã có một tài khoản trên hệ thống của chúng tôi. Hoặc có ai đó đã sử tài khoản này trước đó rồi. Chúng tôi rất tiếc về sự cố này.";
        final String msg2 = "Xin lỗi chúng tôi chưa có chức năng lấy lại mật khẩu trên hệ thống.";
        String buttonText = "Tôi đã quên mật khẩu mất rồi";
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppUtils.openAlertDialog(RegisterScreen.this, "", msg2,
                        "Tôi sẽ tạo tài khoản khác", null, false);
            }
        };
        String cancelText = "Để tôi tạo tài khoản khác";
        AppUtils.openAlertDialog(this, title, msg, buttonText, listener, true, cancelText);
    }

    private void setAutoHideKeyboard() {
        View.OnTouchListener listener = new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtils.closeKeyBoard(RegisterScreen.this, RegisterScreen.this.getCurrentFocus());
                clearFocus();
                return false;
            }
        };
        findViewById(R.id.layout_root).setOnTouchListener(listener);
//        findViewById(R.id.scroll_view).setOnTouchListener(listener);
        findViewById(R.id.register_toolbar).setOnTouchListener(listener);
    }

    private void clearFocus() {
        findViewById(R.id.layout_root).requestFocus();
    }

    private void setAutoClearError() {
        AppUtils.setAutoClearError(mEtUsername);
        AppUtils.setAutoClearError(mEtPassword);
        AppUtils.setAutoClearError(mEtRePassword);
    }
}
