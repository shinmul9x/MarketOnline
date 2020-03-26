package com.example.marketonline.mainscreen;

public interface IMainContract {
    interface IViewAction {
        void finishActivity();
    }

    interface IPresenterAction {
        String getAvatar(int userId);
        String getUserName(int userId);
    }
}
