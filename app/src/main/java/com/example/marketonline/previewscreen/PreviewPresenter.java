package com.example.marketonline.previewscreen;

import android.content.Context;

import com.example.marketonline.database.AppDatabase;
import com.example.marketonline.database.Product;
import com.example.marketonline.database.User;

public class PreviewPresenter implements IPreviewContract.IPresenterAction {
    private Context mContext;

    public PreviewPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Product getProduct(int productId) {
        return AppDatabase.getDatabase().getProduct(productId);
    }

    @Override
    public String getSellerName(int userId) {
        User user = AppDatabase.getDatabase().getUser(userId);
        return user.getName();
    }

    @Override
    public String getSellerNumber(int userId) {
        User user = AppDatabase.getDatabase().getUser(userId);
        return user.getPhoneNumber();
    }

    @Override
    public String getSellerAvatar(int userId) {
        User user = AppDatabase.getDatabase().getUser(userId);
        return user.getAvatar();
    }
}
