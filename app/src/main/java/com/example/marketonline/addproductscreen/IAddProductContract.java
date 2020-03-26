package com.example.marketonline.addproductscreen;

import com.example.marketonline.database.Product;

public interface IAddProductContract {
    interface IViewAction {

    }

    interface IPresenterAction {
        boolean addProduct(Product product);

        boolean updateProduct(Product product);

        String getAddressUser(int userId);

        Product getProduct(int productId);
    }
}
