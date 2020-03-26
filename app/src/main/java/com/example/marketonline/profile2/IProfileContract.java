package com.example.marketonline.profile2;

import com.example.marketonline.database.User;

public interface IProfileContract {
    interface IPresenterAction {
        User getUser(int userId);
    }

    interface IViewAction {

    }
}
