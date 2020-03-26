package com.example.marketonline.mainscreen;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketonline.R;
import com.example.marketonline.adapters.ProductAdapter;
import com.example.marketonline.database.AppDatabase;
import com.example.marketonline.utils.AppConstants;
import com.example.marketonline.utils.AppUtils;
import com.example.marketonline.utils.PreferencesUtils;

public class YourProductsFragment extends Fragment {
    private Context mContext;
    private int mUserId;
    private ProductAdapter mProductAdapter;
    private TextView mTvNoData;

    public YourProductsFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_content, container, false);
        mTvNoData = view.findViewById(R.id.tv_no_data);
        RecyclerView recyclerView = view.findViewById(R.id.rv_products);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mUserId = Integer.valueOf(PreferencesUtils.getString(mContext, AppConstants.USER_ID, AppConstants.NON_USER_ID));
        if (mUserId != 0) {
            mProductAdapter = new ProductAdapter(mContext, mUserId);
            recyclerView.setAdapter(mProductAdapter);
            recyclerView.setHasFixedSize(true);
            displayNoData();
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        assert getView() != null;
//        mTvNoData = getView().findViewById(R.id.tv_no_data);
//        RecyclerView recyclerView = getView().findViewById(R.id.rv_products);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        mUserId = Integer.valueOf(PreferencesUtils.getString(mContext, AppConstants.USER_ID, AppConstants.NON_USER_ID));
//        if (mUserId == 0) return;
//        mProductAdapter = new ProductAdapter(mContext, mUserId);
//        recyclerView.setAdapter(mProductAdapter);
//        recyclerView.setHasFixedSize(true);
//        displayNoData();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        AppUtils.log(mContext, "resume");
//        setChanged();
//    }

    public void setChanged() {
        if (mUserId == 0) return;
        if (mProductAdapter == null) return;
        mProductAdapter.onDataChanged();
        displayNoData();
    }

    private void displayNoData() {
        if (mProductAdapter.getItemCount() == 0) {
            mTvNoData.setVisibility(View.VISIBLE);
        } else {
            mTvNoData.setVisibility(View.INVISIBLE);
        }
    }
}
