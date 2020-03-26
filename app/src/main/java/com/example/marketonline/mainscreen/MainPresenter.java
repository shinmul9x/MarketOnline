package com.example.marketonline.mainscreen;

import android.content.Context;

import com.example.marketonline.database.AppDatabase;
import com.example.marketonline.database.User;

public class MainPresenter implements IMainContract.IPresenterAction {
    private Context mContext;

    MainPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public String getAvatar(int userId) {
        User user = AppDatabase.getDatabase().getUser(userId);
        return user.getAvatar();
    }

    @Override
    public String getUserName(int userId) {
        User user = AppDatabase.getDatabase().getUser(userId);
        return user.getName();
    }
}
