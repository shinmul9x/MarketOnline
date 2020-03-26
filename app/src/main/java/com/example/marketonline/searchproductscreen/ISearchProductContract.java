package com.example.marketonline.searchproductscreen;

import com.example.marketonline.database.Product;

import java.util.ArrayList;

public interface ISearchProductContract {
    interface IPresenterAction {
        ArrayList<Product> getProducts(String content);
    }

    interface IViewAction {

    }
}
