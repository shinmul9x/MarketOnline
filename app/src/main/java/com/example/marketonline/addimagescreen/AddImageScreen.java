package com.example.marketonline.addimagescreen;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.marketonline.R;
import com.example.marketonline.adapters.ImageAdapter;
import com.example.marketonline.database.AppDatabase;
import com.example.marketonline.utils.AppConstants;
import com.example.marketonline.utils.AppUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.marketonline.utils.AppConstants.GALLERY_REQUEST_CODE;

public class AddImageScreen extends AppCompatActivity {
    private RecyclerView mRecyclerViewImage;
    private ImageView mImageView;
    private Button mButton;
    private EditText editText1;
    private EditText editText2;
    private ViewPager2 viewPager2;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image_screen);
//        setContentView(R.layout.product_row2);
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
//                return true;
            } else {

                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
//            return true;
        }
//        initComponents();
//        initActions();
//
//        String bm = "/storage/9016-4EF8/DCIM/Camera/20191116_034115.jpg";
//        ImageView iv = findViewById(R.id.iv_media);
//        iv.setImageBitmap(BitmapFactory.decodeFile(new File(bm).getAbsolutePath()));
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initComponents() {
        initToolBar();
        mImageView = findViewById(R.id.iv_content);
        mButton = findViewById(R.id.btn_click);
        editText1 = findViewById(R.id.username);
        editText2 = findViewById(R.id.pass);
        viewPager2 = findViewById(R.id.vp2);
    }

    private void initActions() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pickFromGallery();
                getId();
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });
    }

    private void initToolBar() {

    }

    private void pickFromGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//        startActivityForResult(intent, GALLERY_REQUEST_CODE);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE: {
                    assert data != null;
                    Uri selectedImage = data.getData();
                    String ct = "/content:/media/external/images/media/15611";
                    String bm = "/storage/9016-4EF8/DCIM/Camera/20191116_034115.jpg";
//                    mImageView.setImageURI(Uri.fromFile(new File(ct)));
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                    assert selectedImage != null;
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                    assert cursor != null;
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(new File(bm).getAbsolutePath());
                    mImageView.setImageBitmap(bitmap);
//                    mImageView.setImageURI(selectedImage);
                    TextView textView = findViewById(R.id.content);
////                    assert selectedImage != null;
////                    String content = data.getDataString();
//                    File file = new File(Objects.requireNonNull(data.getDataString()));
//                    String content2 = file.getAbsolutePath();
                    textView.setText(imgDecodableString);
////                    ImageView imageView = findViewById(R.id.iv_content2);
////                    imageView.setImageURI(Uri.parse(content));
                    break;
                }

                case 2: {
                    assert data != null;
                    // Get the Image from data
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    if (data.getData() != null) {
                        Uri mImageUri = data.getData();
//                        String wholeID = DocumentsContract.getDocumentId(mImageUri);
//                        String id = wholeID.split(":")[1];
//                        String sel = MediaStore.Images.Media._ID + "=?";
//                        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                                filePathColumn, sel, new String[]{id}, null);
//                        cursor.moveToFirst();
//                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                        String imageEncoded = cursor.getString(columnIndex);
//                        cursor.close();
                        String imageEncoded = AppUtils.getImagePath(this, mImageUri);
                        Bitmap bitmap = BitmapFactory.decodeFile(new File(imageEncoded).getAbsolutePath());
                        mImageView.setImageBitmap(bitmap);

                    } else if (data.getClipData() != null) {

                        ArrayList<String> imagesEncodedList = new ArrayList<String>();
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
//                            mArrayUri.add(uri);
//                            // Get the cursor
//                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
//                            // Move to first row
//                            cursor.moveToFirst();
//
//                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                            String imageEncoded = cursor.getString(columnIndex);
////                            AppUtils.log(this, imageEncoded);
//                            imagesEncodedList.add(imageEncoded);
//                            cursor.close();
                            imagesEncodedList.add(AppUtils.getImagePath(this, uri));

                        }
//                            Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
//                        Bitmap bitmap = BitmapFactory.decodeFile(new File(imagesEncodedList.get(0)).getAbsolutePath());
//                        mImageView.setImageBitmap(bitmap);
                        AppUtils.log(this, "selected " + imagesEncodedList + " pic");
                        viewPager2.setAdapter(new ImageAdapter(this, imagesEncodedList,1));
                    }
                    break;

                }
            }
        }

    }

    private void getId() {
        String u = String.valueOf(editText1.getText());
        String p = String.valueOf(editText2.getText());
        int id = AppDatabase.getDatabase().getUserId(u, p);
        TextView textView = findViewById(R.id.content);
        textView.setText(id + "");
    }
}
