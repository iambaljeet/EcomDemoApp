package com.app.ecomdemoapp.activities;

import android.os.Bundle;
import android.support.design.card.MaterialCardView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.ecomdemoapp.MyApplication;
import com.app.ecomdemoapp.R;
import com.app.ecomdemoapp.adapters.CartListAdapter;
import com.app.ecomdemoapp.database.SqliteHelper;
import com.app.ecomdemoapp.models.CartModel;
import com.app.ecomdemoapp.models.ProductsModel;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    private SqliteHelper sqliteHelper;
    private ArrayList<CartModel> cartModelArrayList = new ArrayList<>();
    private AppCompatImageButton backImageButton;
    private AppCompatImageButton moreImageButton;
    private RecyclerView cartListRecyclerView;
    private AppCompatTextView grandTotalTextView;
    private LinearLayout noProductsContainer;
    private MaterialCardView cartItemsLayout;
    private AppCompatTextView titleText;

    private CartListAdapter cartListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();
    }

    private void initView() {
        backImageButton = findViewById(R.id.back_image_button);
        moreImageButton = findViewById(R.id.more_image_button);
        cartListRecyclerView = findViewById(R.id.cart_list_recycler_view);
        grandTotalTextView = findViewById(R.id.grand_total_text_view);
        noProductsContainer = findViewById(R.id.no_products_container);
        cartItemsLayout = findViewById(R.id.card_items_layout);
        titleText = findViewById(R.id.title_text);

        backImageButton.setOnClickListener(this);

        titleText.setText(getString(R.string.Cart));

        sqliteHelper = MyApplication.getSqliteHelper();

        cartListAdapter = new CartListAdapter(this, cartModelArrayList, sqliteHelper);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        cartListRecyclerView.setLayoutManager(linearLayoutManager);
        cartListRecyclerView.setAdapter(cartListAdapter);
        
        getCartList();
    }

    private void getCartList() {
        cartModelArrayList.addAll(sqliteHelper.getCartItems());
        cartListAdapter.notifyDataSetChanged();

        checkListEmpty();
    }

    private void checkListEmpty() {
        if (cartModelArrayList != null && cartModelArrayList.size() > 0) {
            showProductList();
        } else {
            hideProductList();
        }
    }


    public void hideProductList() {
        noProductsContainer.setVisibility(View.VISIBLE);
        cartItemsLayout.setVisibility(View.GONE);
    }


    public void showProductList() {
        cartItemsLayout.setVisibility(View.VISIBLE);
        noProductsContainer.setVisibility(View.GONE);
    }

    public void setGrandTotal(long grandTotal) {
        grandTotalTextView.setText(String.valueOf(grandTotal));
    }

    public void updateGrandTotal(long productPrice) {
        long currentGrandTotal = Long.parseLong(grandTotalTextView.getText().toString());
        grandTotalTextView.setText(String.valueOf(currentGrandTotal - productPrice));
    }

    public void removeFromCart(ProductsModel productsModel, int position) {
        sqliteHelper.removeProductFromCart(productsModel.getProduct_id());
        cartModelArrayList.remove(position);
        cartListAdapter.notifyItemRemoved(position);
        updateGrandTotal(productsModel.getProduct_discounted_price());
        checkListEmpty();
        Toast.makeText(this, "Product removed from cart.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_image_button:
                finish();
                break;
        }
    }
}