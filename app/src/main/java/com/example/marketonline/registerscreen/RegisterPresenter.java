package com.example.marketonline.registerscreen;

import com.example.marketonline.database.AppDatabase;
import com.example.marketonline.database.User;

public class RegisterPresenter implements IRegisterContract.IPresenterAction {
    @Override
    public boolean registerAccount(String username, String password, String rePassword) {
        return AppDatabase.getDatabase().addUser(new User(username, password));
    }
}
