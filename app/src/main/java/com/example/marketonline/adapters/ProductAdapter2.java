package com.example.marketonline.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketonline.R;
import com.example.marketonline.database.AppDatabase;
import com.example.marketonline.database.Product;
import com.example.marketonline.loginscreen.LoginScreen;
import com.example.marketonline.mainscreen.MainScreen;
import com.example.marketonline.previewscreen.PreviewScreen;
import com.example.marketonline.utils.AppConstants;
import com.example.marketonline.utils.AppUtils;
import com.example.marketonline.utils.PreferencesUtils;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.util.ArrayList;

public class ProductAdapter2 extends RecyclerView.Adapter<ProductAdapter2.ViewHolder> {
    private ArrayList<Product> mProducts;
    private Context mContext;
    private int mUserId;

    public ProductAdapter2(Context context, int userId) {
        this.mContext = context;
        mUserId = userId;
        mProducts = AppDatabase.getDatabase().getAllProductsExcept(mUserId);
//        mProducts = AppDatabase.getDatabase().getAllProducts();
    }

    public ProductAdapter2(Context context, ArrayList<Product> products) {
        this.mContext = context;
        mProducts = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_row2, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = AppDatabase.getDatabase().getProduct(mProducts.get(position).getId());
//        holder.id = product.getId();
//        Bitmap imageBitmap = AppUtils.decodeFileToBitmap(new File(product.getImage()), 400);
//        if (null != imageBitmap) {
//            holder.ivMedia.setImageBitmap(imageBitmap);
//        }
//        holder.tvProductName.setText(product.getProductName());
//        holder.tvAddress.setText(product.getAddress());
//        holder.tvCost.setText(product.getCost() + " VNĐ");
        holder.setContent(product);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public void onDataChanged() {
//        mProducts = AppDatabase.getDatabase().getAllProducts();
        mProducts = AppDatabase.getDatabase().getAllProductsExcept(mUserId);
        notifyDataSetChanged();
    }

    public void onDataChanged(ArrayList<Product> products) {
        mProducts = products;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int id;
        ImageView ivMedia;
        TextView tvProductName;
        TextView tvAddress;
        TextView tvCost;
        MaterialCardView cvProduct;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // components
            ivMedia = itemView.findViewById(R.id.iv_media);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvCost = itemView.findViewById(R.id.tv_cost);
            cvProduct = itemView.findViewById(R.id.cv_product);
            // actions
            cvProduct.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.cv_product) {
                if (PreferencesUtils.getString(mContext, AppConstants.USER_ID, AppConstants.NON_USER_ID).equals(AppConstants.NON_USER_ID)) {
                    openDialogConfirmLogin();
                } else {
                    Intent intent = new Intent(mContext, PreviewScreen.class);
                    intent.putExtra(AppConstants.PRODUCT_ID, id);
                    mContext.startActivity(intent);
                }
            }
        }

        @SuppressLint("SetTextI18n")
        void setContent(Product product) {
            id = product.getId();
            Bitmap imageBitmap = AppUtils.decodeFileToBitmap(new File(product.getImage()), 400);
            if (null != imageBitmap) {
                ivMedia.setImageBitmap(imageBitmap);
            }
            tvProductName.setText(product.getProductName());
            tvAddress.setText(product.getAddress());
            tvCost.setText(product.getCost() + " VNĐ");
        }

        private void openDialogConfirmLogin() {
            String title = "Thông báo!";
            String msg = "Bạn cần có tài khoản và đăng nhập để có thể sử dụng chức năng này! Bạn muốn đăng nhập vào hệ thống của chúng tôi chứ?";
            String btnText = "Đăng nhập";
            String btnCancelText = "Tôi muốn xem tiếp";
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // todo open edit screen
                    mContext.startActivity(new Intent(mContext, LoginScreen.class));
                    ((Activity) mContext).finish();
                }
            };
            AppUtils.openAlertDialog(mContext, title, msg, btnText, listener, true, btnCancelText);
        }
    }
}
