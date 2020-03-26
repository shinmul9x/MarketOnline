package com.example.marketonline.profilescreen;

import android.content.Context;

import com.example.marketonline.database.AppDatabase;
import com.example.marketonline.database.User;
import com.example.marketonline.utils.AppConstants;
import com.example.marketonline.utils.PreferencesUtils;

public class ProfilePresenter implements IProifleContract.IPresenterAction {
    private Context mContext;

    ProfilePresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void updateUserInfo(User user) {
        AppDatabase.getDatabase().updateUser(user);
    }

    @Override
    public User getUserInfo() {
        int userId = Integer.valueOf(PreferencesUtils.getString(mContext, AppConstants.USER_ID, "0"));
        if (userId > 0) {
            return AppDatabase.getDatabase().getUser(userId);
        }
        return null;
    }
}
