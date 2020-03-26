package com.example.marketonline.searchproductscreen;

import android.content.Context;

import com.example.marketonline.database.AppDatabase;
import com.example.marketonline.database.Product;

import java.util.ArrayList;

public class SearchProductPresenter implements ISearchProductContract.IPresenterAction {
    private Context mContext;

    public SearchProductPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ArrayList<Product> getProducts(String content) {
        if (content.length() == 0) return null;
        return AppDatabase.getDatabase().getAllProducts(content);
    }
}
