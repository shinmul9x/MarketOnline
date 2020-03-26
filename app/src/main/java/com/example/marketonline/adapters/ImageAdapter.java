package com.example.marketonline.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketonline.R;
import com.example.marketonline.utils.AppConstants;
import com.example.marketonline.utils.AppUtils;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> mListImage;
    private int type;

    public ImageAdapter(Context context, ArrayList<String> mListImage, int type) {
        this.context = context;
        this.mListImage = mListImage;
        this.type = type;
    }

    public void setImageList(ArrayList<String> mListImage) {
        this.mListImage = mListImage;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (type == AppConstants.IMAGE_ADAPTER_TYPE_1) {
            view = inflater.inflate(R.layout.image_row, parent, false);
        } else {
            view = inflater.inflate(R.layout.image_row2, parent, false);
        }
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bitmap;
        File fileImage = new File(mListImage.get(position));
        if (fileImage.exists()) {
            if (type == AppConstants.IMAGE_ADAPTER_TYPE_1) {
                bitmap = AppUtils.decodeFileToBitmap(new File(mListImage.get(position)), 300);
            } else {
                bitmap = AppUtils.decodeFileToBitmap(fileImage, 500);
            }
            holder.imageView.setImageBitmap(bitmap);
        } else {
            holder.imageView.setImageResource(R.drawable.noimage);
        }
        holder.imageCounter.setText(position + 1 + "/" + getItemCount());
    }

    @Override
    public int getItemCount() {
        return mListImage.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView imageCounter;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_content);
            imageCounter = itemView.findViewById(R.id.image_counter);
        }
    }
}
