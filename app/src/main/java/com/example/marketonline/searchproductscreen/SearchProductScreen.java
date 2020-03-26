package com.example.marketonline.searchproductscreen;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketonline.R;
import com.example.marketonline.adapters.ProductAdapter;
import com.example.marketonline.adapters.ProductAdapter2;
import com.example.marketonline.database.Product;

import java.util.ArrayList;

public class SearchProductScreen extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private SearchView mSvContent;
    private RecyclerView mRvProduct;
    private ISearchProductContract.IPresenterAction mPresenter;
    private ProductAdapter2 mProductAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product_screen);
        initComponents();
        initActions();
    }

    private void initComponents() {
        initToolbar();
        mPresenter = new SearchProductPresenter(this);
        mSvContent = findViewById(R.id.sv_content);
        mRvProduct = findViewById(R.id.rv_products);
        mProductAdapter = new ProductAdapter2(this, new ArrayList<Product>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRvProduct.setLayoutManager(layoutManager);
        mRvProduct.setAdapter(mProductAdapter);
        mRvProduct.setHasFixedSize(true);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initActions() {
        mSvContent.setOnQueryTextListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mProductAdapter.onDataChanged(mPresenter.getProducts(query));
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
