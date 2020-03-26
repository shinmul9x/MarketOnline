package com.example.marketonline.adapters;

import android.content.Context;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.example.marketonline.mainscreen.MarketProductsFragment;
import com.example.marketonline.mainscreen.YourProductsFragment;
import com.example.marketonline.utils.AppUtils;

import java.util.List;

public class TabAdapter extends FragmentStateAdapter {
    private Context mContext;
    private MarketProductsFragment mMarketProductsFragment;
    private YourProductsFragment mYourProductsFragment;
    private Parcelable state;

    public TabAdapter(Context context, @NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        mContext = context;
        mMarketProductsFragment = new MarketProductsFragment(mContext);
        mYourProductsFragment = new YourProductsFragment(mContext);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return mMarketProductsFragment;
        }
        return mYourProductsFragment;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FragmentViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        AppUtils.log(mContext, "detach");
        mMarketProductsFragment.setChanged();
    }



    @Override
    public int getItemCount() {
        return 2;
    }

    public Fragment getFragment(int position) {
        if (position == 0) {
            return mMarketProductsFragment;
        }
        return mYourProductsFragment;
    }

    public void onChanged() {
        mMarketProductsFragment.setChanged();
        mYourProductsFragment.setChanged();
    }
}
