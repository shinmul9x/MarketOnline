package com.example.marketonline.loginscreen;

import android.content.Context;

import com.example.marketonline.database.AppDatabase;

public class LoginPresenter implements ILoginContract.IPresenterAction {
    private Context mContext;

    LoginPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int verifyAccount(String username, String password) {
        if (username.equals("") || password.equals("")) return 0;
        return AppDatabase.getDatabase().getUserId(username, password);
    }
}
