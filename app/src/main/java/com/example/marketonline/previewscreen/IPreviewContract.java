package com.example.marketonline.previewscreen;

import com.example.marketonline.database.Product;

public interface IPreviewContract {
    interface IPresenterAction {
        Product getProduct(int productId);

        String getSellerName(int userId);

        String getSellerNumber(int userId);

        String getSellerAvatar(int userId);
    }

    interface IViewAction {

    }
}
