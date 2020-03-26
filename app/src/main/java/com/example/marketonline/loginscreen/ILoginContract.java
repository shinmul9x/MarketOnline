package com.example.marketonline.loginscreen;

public interface ILoginContract {
    interface IPresenterAction {
        int verifyAccount(String username, String password);
    }

    interface IViewAction {

    }
}
