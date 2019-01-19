package com.app.ecomdemoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.ecomdemoapp.MyApplication;
import com.app.ecomdemoapp.R;
import com.app.ecomdemoapp.adapters.ProductListAdapter;
import com.app.ecomdemoapp.callbacks.FilterCallback;
import com.app.ecomdemoapp.database.SqliteHelper;
import com.app.ecomdemoapp.fragments.FilterBottomSheetFragment;
import com.app.ecomdemoapp.models.ProductsModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, FilterCallback {
    private SqliteHelper sqliteHelper;
    private MaterialCardView searchMaterialCard;
    private AppCompatEditText searchProductEditText;
    private AppCompatImageButton filterImageButton;
    private AppCompatImageButton cartItemImageButton;
    private RecyclerView productsRecyclerView;
    private LinearLayout noProductsContainer;
    private FloatingActionButton addProductFab;
    private ArrayList<ProductsModel> productsModelArrayList = new ArrayList<>();
    private FilterBottomSheetFragment filterBottomSheetFragment;

    private ProductListAdapter productListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProductList();
    }

    private void getProductList() {
        productsModelArrayList.clear();
        productsModelArrayList.addAll(sqliteHelper.getAllProducts());
        productListAdapter.notifyDataSetChanged();

        checkListEmpty();
    }

    private void getFilterProductList(long price) {
        for (Iterator<ProductsModel> productsModelIterator = productsModelArrayList.iterator(); productsModelIterator.hasNext();) {
            if (productsModelIterator.next().getProduct_discounted_price() > price) {
                productsModelIterator.remove();
            }
        }
        productListAdapter.notifyDataSetChanged();

        checkListEmpty();
    }

    public void checkListEmpty() {
        if (productsModelArrayList != null && productsModelArrayList.size() > 0) {
            showProductList();
        } else {
            hideProductList();
        }
    }

    public void hideProductList() {
        noProductsContainer.setVisibility(View.VISIBLE);
        productsRecyclerView.setVisibility(View.GONE);
    }


    public void showProductList() {
        noProductsContainer.setVisibility(View.GONE);
        productsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void initView() {
        searchMaterialCard = findViewById(R.id.search_material_card);
        searchProductEditText = findViewById(R.id.search_product_edit_text);
        filterImageButton = findViewById(R.id.filter_image_button);
        productsRecyclerView = findViewById(R.id.products_recycler_view);
        noProductsContainer = findViewById(R.id.no_products_container);
        addProductFab = findViewById(R.id.add_product_fab);
        cartItemImageButton = findViewById(R.id.cart_item_image_button);

        filterImageButton.setOnClickListener(this);
        addProductFab.setOnClickListener(this);
        cartItemImageButton.setOnClickListener(this);

        sqliteHelper = MyApplication.getSqliteHelper();

        productListAdapter = new ProductListAdapter(this, productsModelArrayList);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsRecyclerView.setAdapter(productListAdapter);

        searchProductEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged", "s: " + s);
                productListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    void startAddProductActivity() {
        Intent addProductIntent = new Intent(this, AddProductActivity.class);
        startActivity(addProductIntent);
    }

    void startCartActivity() {
        Intent cartIntent = new Intent(this, CartActivity.class);
        startActivity(cartIntent);
    }

    public void addProductToCart(int product_id) {
        if (sqliteHelper.addProductToCart(product_id) > 0) {
            Toast.makeText(this, "Product added to cart.", Toast.LENGTH_SHORT).show();
        }
    }

    public void showFilterBottomSheet() {
        filterBottomSheetFragment = FilterBottomSheetFragment.newInstance();
        filterBottomSheetFragment.show(getSupportFragmentManager(), filterBottomSheetFragment.getTag());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filter_image_button:
                showFilterBottomSheet();
                break;
            case R.id.add_product_fab:
                startAddProductActivity();
                break;
            case R.id.cart_item_image_button:
                startCartActivity();
                break;
        }
    }

    @Override
    public void onPriceFilterSelected(long product_price) {
        getProductList();

        getFilterProductList(product_price);
        filterBottomSheetFragment.dismiss();
    }

    @Override
    public void onFilterReset() {
        getProductList();
        filterBottomSheetFragment.dismiss();
    }
}