package com.example.marketonline.addproductscreen;

import android.content.Context;

import com.example.marketonline.database.AppDatabase;
import com.example.marketonline.database.Product;
import com.example.marketonline.database.User;

public class AddProductPresenter implements IAddProductContract.IPresenterAction {
    private Context mContext;

    AddProductPresenter(Context context) {
        mContext = context;
    }

    @Override
    public boolean addProduct(Product product) {
        if (product.getProductName().equals("") || product.getAddress().equals("") || product.getCost() == -1) {
            return false;
        }
        AppDatabase.getDatabase().addProduct(product);
        return true;
    }

    @Override
    public String getAddressUser(int userId) {
        return "";
    }

    @Override
    public Product getProduct(int productId) {
        return AppDatabase.getDatabase().getProduct(productId);
    }

    @Override
    public boolean updateProduct(Product product) {
        if (product.getProductName().equals("") || product.getAddress().equals("") || product.getCost() == -1) {
            return false;
        }
        AppDatabase.getDatabase().updateProduct(product);
        return true;
    }
}
