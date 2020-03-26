package com.example.marketonline.addproductscreen;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketonline.R;
import com.example.marketonline.adapters.ImageAdapter;
import com.example.marketonline.database.Product;
import com.example.marketonline.loginscreen.LoginScreen;
import com.example.marketonline.utils.AppConstants;
import com.example.marketonline.utils.AppUtils;
import com.example.marketonline.utils.PreferencesUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

public class AddProductScreen extends AppCompatActivity implements View.OnClickListener {
    private IAddProductContract.IPresenterAction mPresenter;
    private TextInputEditText mEtProductName;
    private TextInputEditText mEtDescription;
    private TextInputEditText mEtCost;
    private MaterialButton mBtnAdd;
    private RecyclerView mRvImageList;
    private ImageButton mIBtnAddImages;
    private TextInputEditText mEtAddress;
    private ImageAdapter mImageAdapter;
    private ArrayList<String> mImageList;
    private int mUserId;
    private Product mProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_screen);
        initComponents();
        initActions();

//        mUserId = getIntent().getIntExtra("USER_ID", -1);
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.add_product_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initComponents() {
        initToolBar();
        initRecyclerView();
        mPresenter = new AddProductPresenter(this);
        mBtnAdd = findViewById(R.id.btn_add);
        mEtProductName = findViewById(R.id.et_product_name);
        mEtDescription = findViewById(R.id.et_description);
        mEtCost = findViewById(R.id.et_cost);
        mIBtnAddImages = findViewById(R.id.btn_add_images);
        mEtAddress = findViewById(R.id.et_address);
//        mEtAddress.append(mPresenter.getAddressUser(mUserId));
        mImageList = new ArrayList<>();

        int productId = getIntent().getIntExtra(AppConstants.EDIT_PRODUCT_ID, 0);
        AppUtils.log(this, productId+"");
        mProduct = mPresenter.getProduct(productId);
        if (null == mProduct) {
            mUserId = Integer.valueOf(PreferencesUtils.getString(this, AppConstants.USER_ID, AppConstants.NON_USER_ID));
        } else {
            setContent();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setContent() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Chỉnh sửa sản phẩm");
        mBtnAdd.setText("Cập nhật");
        mEtProductName.append(mProduct.getProductName());
        mEtAddress.append(mProduct.getAddress());
        mEtCost.append(mProduct.getCost() + "");
        mEtDescription.append(mProduct.getDescription());
        mImageList = mProduct.getImages();
        mImageAdapter.setImageList(mImageList);
    }

    private void initActions() {
        mBtnAdd.setOnClickListener(this);
        mIBtnAddImages.setOnClickListener(this);
        setAutoHideKeyboard();
        setAutoClearError();
    }

    private void initRecyclerView() {
        mRvImageList = findViewById(R.id.rv_image_list);
        mImageAdapter = new ImageAdapter(this, new ArrayList<String>(), AppConstants.IMAGE_ADAPTER_TYPE_1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mRvImageList.setLayoutManager(layoutManager);
        mRvImageList.setHasFixedSize(true);
        mRvImageList.setAdapter(mImageAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                if (mProduct == null) {
                    addProduct();
                } else {
                    updateProduct();
                }
                break;
            case R.id.btn_add_images:
                AppUtils.pickImagesFromGallery(this);
                break;
        }
        clearFocus();
    }

    private void openDialogConfirmAddProduct(final Product product) {
        String title = "Xác nhận muốn bán?";
        String msg = "Bạn chắc chắn rằng muốn đăng bán sản phẩm này lên trên hệ thống của chúng tôi?";
        String buttonText = "Đúng vậy";
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mPresenter.addProduct(product)) {
                    AppUtils.openAlertDialog(AddProductScreen.this, "Thành công!",
                            "Chúc mừng bạn đã đưa sản phẩm lên hệ thống thành công!", "Tuyệt",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    AddProductScreen.this.finish();
                                }
                            }, false);
                } else {
                    AppUtils.openAlertDialog(AddProductScreen.this, "Hỏng!",
                            "Thật tiếc vì đã có vấn đề xảy ra khi bạn đăng sản phẩm lên hệ thống của chúng tôi.",
                            "Tôi sẽ đăng lại sau", null, false);
                }
            }
        };
        AppUtils.openAlertDialog(this, title, msg, buttonText, listener, true);
    }

    private void openDialogConfirmUpdateProduct(final Product product) {
        String title = "Xác nhận chỉnh sửa?";
        String msg = "Bạn muốn chỉnh sửa thông sản phẩm này của bạn lên trên hệ thống của chúng tôi?";
        String buttonText = "Đúng vậy";
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mPresenter.updateProduct(product)) {
                    AppUtils.openAlertDialog(AddProductScreen.this, "Thành công!",
                            "Đã chỉnh sửa thông tin sản phẩm thành công!", "Tuyệt",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    AddProductScreen.this.finish();
                                }
                            }, false);
                } else {
                    AppUtils.openAlertDialog(AddProductScreen.this, "Hỏng!",
                            "Thật tiếc vì đã có vấn đề xảy ra khi sửa thông tin sản phẩm lên hệ thống của chúng tôi.",
                            "Tôi sẽ sửa lại sau", null, false);
                }
            }
        };
        AppUtils.openAlertDialog(this, title, msg, buttonText, listener, true);
    }

    private void addProduct() {
        boolean hasBoxEmpty = false;

        String productName = String.valueOf(mEtProductName.getText());
        if (productName.equals("")) {
            hasBoxEmpty = true;
            AppUtils.setError(mEtProductName);
        }
        String description = String.valueOf(mEtDescription.getText());

        String cost = String.valueOf(mEtCost.getText());
        if (cost.equals("")) {
            hasBoxEmpty = true;
            AppUtils.setError(mEtCost);
        }
        String status = "";
        String address = String.valueOf(mEtAddress.getText());
        if (address.equals("")) {
            hasBoxEmpty = true;
            AppUtils.setError(mEtAddress);
        }

        if (hasBoxEmpty) return;
        Product product = new Product(productName, mImageList, Integer.valueOf(cost), status, mUserId, description, address);
        openDialogConfirmAddProduct(product);
    }

    private void updateProduct() {
        boolean hasBoxEmpty = false;

        String productName = String.valueOf(mEtProductName.getText());
        if (productName.equals("")) {
            hasBoxEmpty = true;
            AppUtils.setError(mEtProductName);
        }
        String description = String.valueOf(mEtDescription.getText());

        String cost = String.valueOf(mEtCost.getText());
        if (cost.equals("")) {
            hasBoxEmpty = true;
            AppUtils.setError(mEtCost);
        }
        String status = "";
        String address = String.valueOf(mEtAddress.getText());
        if (address.equals("")) {
            hasBoxEmpty = true;
            AppUtils.setError(mEtAddress);
        }

        if (hasBoxEmpty) return;
        mProduct.setProductName(productName);
        mProduct.setImage(mImageList);
        mProduct.setCost(Integer.valueOf(cost));
        mProduct.setDescription(description);
        mProduct.setAddress(address);
        openDialogConfirmUpdateProduct(mProduct);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        assert null != data;
        mImageList = new ArrayList<>();
        if (null != data.getData()) {
            mImageList.add(AppUtils.getImagePath(this, data.getData()));
        } else if (null != data.getClipData()) {
            ClipData mClipData = data.getClipData();
            for (int i = 0; i < mClipData.getItemCount(); ++i) {
                ClipData.Item item = mClipData.getItemAt(i);
                mImageList.add(AppUtils.getImagePath(this, item.getUri()));
            }
        }
        mImageAdapter.setImageList(mImageList);
    }

    private void setAutoHideKeyboard() {
        View.OnTouchListener listener = new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtils.closeKeyBoard(AddProductScreen.this, AddProductScreen.this.getCurrentFocus());
                clearFocus();
                return false;
            }
        };
        findViewById(R.id.layout_root).setOnTouchListener(listener);
//        findViewById(R.id.scroll_view).setOnTouchListener(listener);
//        findViewById(R.id.scroll_view_images).setOnTouchListener(listener);
        findViewById(R.id.add_product_toolbar).setOnTouchListener(listener);
    }

    private void clearFocus() {
        findViewById(R.id.layout_root).requestFocus();
    }

    private void setAutoClearError() {
        AppUtils.setAutoClearError(mEtProductName);
        AppUtils.setAutoClearError(mEtCost);
        AppUtils.setAutoClearError(mEtAddress);
    }

}
