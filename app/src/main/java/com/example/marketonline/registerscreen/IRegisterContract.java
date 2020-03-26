package com.example.marketonline.registerscreen;

public interface IRegisterContract {
    interface IViewAction {

    }

    interface IPresenterAction {
        boolean registerAccount(String username, String password, String rePassword);
    }
}
