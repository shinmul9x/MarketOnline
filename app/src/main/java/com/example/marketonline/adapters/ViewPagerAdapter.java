package com.example.marketonline.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketonline.R;
import com.example.marketonline.addproductscreen.AddProductScreen;
import com.example.marketonline.database.AppDatabase;
import com.example.marketonline.mainscreen.IMainContract;
import com.example.marketonline.splashscreen.SplashScreen;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    private Context mContext;
    private int mUserId;
    private IMainContract.IViewAction mMainScreen;

    public ViewPagerAdapter(Context context, int userId, IMainContract.IViewAction viewAction) {
        mContext = context;
        mUserId = userId;
        mMainScreen = viewAction;
    }

    public void onChange() {
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.viewpager_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        holder.recyclerView.setLayoutManager(layoutManager);
        if (position == 0) {
            holder.recyclerView.setAdapter(new ProductAdapter(mContext, AppDatabase.getDatabase().getAllProducts()));
            holder.floatingActionButton.setVisibility(View.INVISIBLE);
        } else if (position == 1) {
            if (mUserId == -1) {
                holder.floatingActionButton.setText("Dang nhap");
                holder.floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mContext.startActivity(new Intent(mContext, SplashScreen.class));
                        mMainScreen.finishActivity();
                    }
                });
            } else {
                holder.recyclerView.setAdapter(new ProductAdapter(mContext, AppDatabase.getDatabase().getAllProducts(mUserId)));
        }
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        ExtendedFloatingActionButton floatingActionButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.rv_products);
            floatingActionButton = itemView.findViewById(R.id.btn_add_product);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AddProductScreen.class);
                    intent.putExtra("USER_ID", mUserId);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
