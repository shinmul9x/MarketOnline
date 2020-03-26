package com.example.marketonline.previewscreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.marketonline.R;
import com.example.marketonline.adapters.ImageAdapter;
import com.example.marketonline.addproductscreen.AddProductScreen;
import com.example.marketonline.database.Product;
import com.example.marketonline.profile2.ProfileScreen;
import com.example.marketonline.utils.AppConstants;
import com.example.marketonline.utils.AppUtils;
import com.example.marketonline.utils.PreferencesUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

public class PreviewScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private IPreviewContract.IPresenterAction mPresenter;
    private ViewPager2 mVpMedia;
    private ImageView mIvAvatar;
    private TextView mTvSellerName;
    private TextView mTvProductName;
    private TextView mTvDescription;
    private TextView mTvAddress;
    private TextView mTvCost;
    private BottomNavigationView mBottomNav;
    private String mSellerNumber;
    private FloatingActionButton mBtnCall;
    private boolean otherUser;
    private int mUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_screen2);
        initComponents();
        initActions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContent();
    }

    private void initComponents() {
        initToolbar();
        mPresenter = new PreviewPresenter(this);
        mVpMedia = findViewById(R.id.vp_media);
        mIvAvatar = findViewById(R.id.iv_avatar);
        mTvSellerName = findViewById(R.id.tv_seller_name);
        mTvProductName = findViewById(R.id.tv_product_name);
        mTvDescription = findViewById(R.id.tv_description);
        mTvAddress = findViewById(R.id.tv_address);
        mTvCost = findViewById(R.id.tv_cost);
//        mBottomNav = findViewById(R.id.nav_preview);
        mSellerNumber = "";
        mBtnCall = findViewById(R.id.btn_call);
        mUserId = 0;
//        setContent();
    }

    private void initActions() {
//        mBottomNav.setOnNavigationItemSelectedListener(this);
        mBtnCall.setOnClickListener(this);

        mTvSellerName.setOnClickListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.preview_toolbar);
        setSupportActionBar(toolbar);
        assert null != getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void setContent() {
        Intent intent = getIntent();
        int productId = intent.getIntExtra(AppConstants.PRODUCT_ID, 0);
        Product product = mPresenter.getProduct(productId);
        if (null == product) {
            //todo
            return;
        }
        mUserId = product.getUserId();
        int _userId = Integer.valueOf(PreferencesUtils.getString(this, AppConstants.USER_ID, AppConstants.NON_USER_ID));
        otherUser = _userId != product.getUserId();
        if (!otherUser) {
            mBtnCall.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit, getTheme()));
        }
        ImageAdapter adapter = new ImageAdapter(this, product.getImages(), AppConstants.IMAGE_ADAPTER_TYPE_2);
        mVpMedia.setAdapter(adapter);
        File imageFile = new File(mPresenter.getSellerAvatar(product.getUserId()));
        if (imageFile.exists()) {
            mIvAvatar.setImageBitmap(AppUtils.decodeFileToBitmap(imageFile, 70));
        }
        mTvSellerName.setText(mPresenter.getSellerName(product.getUserId()));
        mTvProductName.setText(product.getProductName());
        mTvDescription.setText(product.getDescription());
        mTvAddress.setText(product.getAddress());
        mTvCost.append(String.valueOf(product.getCost()));
        mSellerNumber = mPresenter.getSellerNumber(product.getUserId());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_call) {
            openDialogConfirmCallSeller();
        }
        return false;
    }

    private void openDialogConfirmCallSeller() {
        String title = "Thông báo!";
        String msg = "Bạn chắc rằng muốn gọi điện cho người bán để hẹn mua sản phẩm này chứ?";
        String buttonText = "Tôi muốn gọi điện cho họ";
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mSellerNumber.length() < 10) {
                    AppUtils.openAlertDialog(PreviewScreen.this, "Thất bại!",
                            "Xin lỗi bạn vì người này không cung cấp cho chúng tôi số điện thoại chính xác. Chúng tôi rất lấy làm tiếc vì điều này.",
                            "Tôi sẽ ổn thôi", null, false);
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mSellerNumber, null));
                    startActivity(intent);
                }
            }
        };
        String cancelText = "Tôi bị trượt tay";
        AppUtils.openAlertDialog(PreviewScreen.this, title, msg, buttonText, listener, true, cancelText);
    }

    void openDialogConfirmEditProduct() {
        String title = "Chú ý!";
        String msg = "Bạn muốn sửa thông tin về sản phẩm này ư?";
        String btnText = "Đúng thế";
        String btnCancelText = "Tôi bị trượt tay";
        final int productId = getIntent().getIntExtra(AppConstants.PRODUCT_ID, 0);
        AppUtils.log(this, productId+".");
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // todo open edit screen
                Intent intent = new Intent(PreviewScreen.this, AddProductScreen.class);
                intent.putExtra(AppConstants.EDIT_PRODUCT_ID, productId);
                startActivity(intent);
            }
        };
        AppUtils.openAlertDialog(this, title, msg, btnText, listener, true, btnCancelText);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_call) {
            if (otherUser) {
                openDialogConfirmCallSeller();
            } else {
                openDialogConfirmEditProduct();
            }
        } else if (view.getId() == R.id.tv_seller_name) {
            if (mUserId == 0)  return;
            Intent intent = new Intent(this, ProfileScreen.class);
            intent.putExtra(AppConstants.ID_IN_PROFILE, mUserId);
            startActivity(intent);
        }
    }
}
