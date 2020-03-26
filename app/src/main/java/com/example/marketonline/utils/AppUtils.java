package com.example.marketonline.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.marketonline.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

public class AppUtils {
    public static void log(Context context, String content) {
        Log.d("MarketOnlineDebug", content);
    }

    public static void closeKeyBoard(Context context, View currentFocus) {
//        View view = context.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
//            clearFocus();
        }
    }

    public static String getImagePath(Context context, Uri uri) {
        String imagePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);
        String id = wholeID.split(":")[1];
        String sel = MediaStore.Images.Media._ID + "=?";
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumn, sel, new String[]{id}, null);
        if (null != cursor && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imagePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return imagePath;
    }

    public static void pickImagesFromGallery(Context context) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((AppCompatActivity) context).startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                AppConstants.GALLERY_REQUEST_CODE
        );
    }

    public static Bitmap decodeFileToBitmap(File f, int requiredSize) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= requiredSize &&
                    o.outHeight / scale / 2 >= requiredSize) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException ignored) {
        }
        return null;
    }

    public static void openAlertDialog(Context context, int title, String message, int btnPosText
            , DialogInterface.OnClickListener listener, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnPosText, listener)
                .setCancelable(false);
        if (cancelable) {
            builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
        builder.create().show();
    }

    public static void openAlertDialog(Context context, String title, String message, String btnPosText
            , DialogInterface.OnClickListener listener, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnPosText, listener)
                .setCancelable(true);
        if (cancelable) {
            builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
        builder.create().show();
    }

    public static void openAlertDialog(Context context, String title, String message, String btnPosText
            , DialogInterface.OnClickListener listener, boolean cancelable, String cancelText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnPosText, listener)
                .setCancelable(true);
        if (cancelable) {
            builder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
        builder.create().show();
    }

    public static void setError(TextInputEditText textInputEditText) {
        getTextInputLayout(textInputEditText).setError("* chưa đúng yêu cầu");
    }

    public static void setError(TextInputEditText textInputEditText, String errorText) {
        getTextInputLayout(textInputEditText).setError(errorText);
    }

    public static void setAutoClearError(TextInputEditText textInputEditText) {
        final TextInputLayout inputLayout = getTextInputLayout(textInputEditText);
        textInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    inputLayout.setError("");
                }
            }
        });
    }

    private static TextInputLayout getTextInputLayout(TextInputEditText textInputEditText) {
        return (TextInputLayout) textInputEditText.getParent().getParent();
    }
}
