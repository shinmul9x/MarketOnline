package com.example.marketonline.mainscreen;

import android.content.Context;
import android.os.Bundle;
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
import com.example.marketonline.adapters.ProductAdapter2;
import com.example.marketonline.utils.AppConstants;
import com.example.marketonline.utils.AppUtils;
import com.example.marketonline.utils.PreferencesUtils;


public class MarketProductsFragment extends Fragment {
    private Context mContext;
    private ProductAdapter2 mProductAdapter;
    private TextView mTvNoData;
    private RecyclerView mRecyclerView;

    public MarketProductsFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_market, container, false);
        mTvNoData = view.findViewById(R.id.tv_no_data);
        mRecyclerView = view.findViewById(R.id.rv_market_products);
        setRecyclerView();
        displayNoData();
        AppUtils.log(mContext, "create");
        return view;
    }

    private void setRecyclerView() {
        int mUserId = Integer.valueOf(PreferencesUtils.getString(mContext, AppConstants.USER_ID, AppConstants.NON_USER_ID));
        mProductAdapter = new ProductAdapter2(mContext, mUserId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mProductAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        AppUtils.log(mContext, "resume");
//        setChanged();
    }

    public void setChanged() {
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
