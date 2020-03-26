package com.example.marketonline.profile2;

import android.content.Context;

import com.example.marketonline.database.AppDatabase;
import com.example.marketonline.database.User;

public class ProfilePresenter implements IProfileContract.IPresenterAction {
    private Context mContext;

    public ProfilePresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public User getUser(int userId) {
        return AppDatabase.getDatabase().getUser(userId);
    }
}
