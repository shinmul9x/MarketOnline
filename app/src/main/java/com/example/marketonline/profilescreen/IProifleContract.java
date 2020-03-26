package com.example.marketonline.profilescreen;

import com.example.marketonline.database.User;

public interface IProifleContract {
    interface IViewAction {

    }

    interface IPresenterAction {
        void updateUserInfo(User user);
        User getUserInfo();
    }

}
