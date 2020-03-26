package com.example.marketonline.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketonline.R;
import com.example.marketonline.database.AppDatabase;
import com.example.marketonline.database.Product;
import com.example.marketonline.previewscreen.PreviewScreen;
import com.example.marketonline.utils.AppConstants;
import com.example.marketonline.utils.AppUtils;
import com.example.marketonline.utils.PreferencesUtils;

import java.io.File;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private ArrayList<Product> mProducts;
    private Context mContext;
    private int mUserId;
    private boolean otherUser;

    public ProductAdapter(Context context, int userId) {
        this.mContext = context;
        mUserId = userId;
        int _userId = Integer.valueOf(PreferencesUtils.getString(mContext, AppConstants.USER_ID, AppConstants.NON_USER_ID));
        otherUser = _userId != userId;
        mProducts = AppDatabase.getDatabase().getAllProducts(mUserId);
    }

    ProductAdapter(Context context, ArrayList<Product> products) {
        this.mContext = context;
        mProducts = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_row3, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = AppDatabase.getDatabase().getProduct(mProducts.get(position).getId());
//        holder.id = product.getId();
//        Bitmap imageBitmap = AppUtils.decodeFileToBitmap(new File(product.getImage()), 400);
//        if (null != imageBitmap) {
//            holder.mIvMedia.setImageBitmap(imageBitmap);
//        }
//        holder.mTvProductName.setText(product.getProductName());
//        holder.mTvAddress.setText(product.getAddress());
//        holder.mTvCost.setText(product.getCost() + " VNĐ");
        holder.setContent(product, position);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public void onDataChanged() {
        mProducts = AppDatabase.getDatabase().getAllProducts(mUserId);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int id;
        ImageView mIvMedia;
        TextView mTvProductName;
        TextView mTvAddress;
        TextView mTvCost;
        ImageButton btnDelete;
        ImageButton btnEdit;
        CardView mCvProduct;
        int index;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // components
            id = 0;
            mIvMedia = itemView.findViewById(R.id.iv_media);
            mTvProductName = itemView.findViewById(R.id.tv_product_name);
            mTvAddress = itemView.findViewById(R.id.tv_address);
            mTvCost = itemView.findViewById(R.id.tv_cost);
            btnDelete = itemView.findViewById(R.id.btn_delete);
//            btnEdit = itemView.findViewById(R.id.btn_edit);
            mCvProduct = itemView.findViewById(R.id.cv_product);
            // actions
            btnDelete.setOnClickListener(this);
//            btnEdit.setOnClickListener(this);
            mCvProduct.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        void setContent(Product product, int index) {
            id = product.getId();
            Bitmap imageBitmap = AppUtils.decodeFileToBitmap(new File(product.getImage()), 400);
            if (null != imageBitmap) {
                mIvMedia.setImageBitmap(imageBitmap);
            }
            mTvProductName.setText(product.getProductName());
            mTvAddress.setText(product.getAddress());
            mTvCost.setText(product.getCost() + " VNĐ");
            this.index = index;
            if (!otherUser) {
                btnDelete.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_delete:
                    openDialogConfirmDelete();
                    break;
//                case R.id.btn_edit:
//                    openDialogConfirmEdit();
//                    break;
                case R.id.cv_product:
                    Intent intent = new Intent(mContext, PreviewScreen.class);
                    intent.putExtra(AppConstants.PRODUCT_ID, id);
                    mContext.startActivity(intent);
                    break;
            }
        }

        void openDialogConfirmDelete() {
            String title = "Chú ý!";
            String msg = "Bạn thực sự muốn dừng đăng thông tin về sản phẩm này cho người khác biết ư?";
            String btnText = "Tôi đã bán được nó rồi";
            String btnCancelText = "Tôi bị trượt tay";
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AppDatabase.getDatabase().deleteProduct(id);
                    onDataChanged();
                }
            };
            AppUtils.openAlertDialog(mContext, title, msg, btnText, listener, true, btnCancelText);
        }

        void openDialogConfirmEdit() {
            String title = "Chú ý!";
            String msg = "Bạn muốn sửa thông tin về sản phẩm này ư?";
            String btnText = "Đúng thế";
            String btnCancelText = "Tôi bị trượt tay";
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // todo open edit screen
                    onDataChanged();
                }
            };
            AppUtils.openAlertDialog(mContext, title, msg, btnText, listener, true, btnCancelText);
        }
    }
}
