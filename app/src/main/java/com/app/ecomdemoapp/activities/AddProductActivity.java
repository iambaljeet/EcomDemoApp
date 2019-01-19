package com.app.ecomdemoapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.app.ecomdemoapp.MyApplication;
import com.app.ecomdemoapp.R;
import com.app.ecomdemoapp.database.SqliteHelper;
import com.app.ecomdemoapp.helpers.Utility;
import com.app.ecomdemoapp.models.ProductsModel;

import java.util.Objects;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {
    private SqliteHelper sqliteHelper;
    private AppCompatImageButton backImageButton;
    private AppCompatImageButton moreImageButton;
    private AppCompatEditText productNameEditText;
    private AppCompatSpinner productCategorySpinner;
    private AppCompatSpinner productCurrencySpinner;
    private AppCompatEditText productDescriptionEditText;
    private AppCompatEditText productPriceEditText;
    private AppCompatEditText productDiscountPriceEditText;
    private AppCompatTextView titleText;
    private AppCompatButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initView();
    }

    private void initView() {
        backImageButton = findViewById(R.id.back_image_button);
        moreImageButton = findViewById(R.id.more_image_button);
        productNameEditText = findViewById(R.id.product_name_edit_text);
        productCategorySpinner = findViewById(R.id.product_category_spinner);
        productCurrencySpinner = findViewById(R.id.currency_spinner);
        productDescriptionEditText = findViewById(R.id.product_description_edit_text);
        productPriceEditText = findViewById(R.id.product_price_edit_text);
        productDiscountPriceEditText = findViewById(R.id.product_discount_price_edit_text);
        titleText = findViewById(R.id.title_text);
        addButton = findViewById(R.id.add_button);

        titleText.setText(getString(R.string.add_product));

        addButton.setOnClickListener(this);
        sqliteHelper = MyApplication.getSqliteHelper();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.add_button:
                String productName = Objects.requireNonNull(productNameEditText.getText()).toString().trim();
                String productCategory = productCategorySpinner.getSelectedItem().toString();
                String productCurrency = productCurrencySpinner.getSelectedItem().toString();
                String productDescription = Objects.requireNonNull(productDescriptionEditText.getText()).toString().trim();
                String productPrice = Objects.requireNonNull(productPriceEditText.getText()).toString().trim();
                String productDiscountedPrice = Objects.requireNonNull(productDiscountPriceEditText.getText()).toString().trim();
                long productTime = System.currentTimeMillis();

                if (validateDetails(productName, productCategory, productCurrency, productDescription, productPrice, productDiscountedPrice)) {
                    addProduct(productName, productDescription, productCurrency, Long.parseLong(productPrice),
                            Long.parseLong(productDiscountedPrice), productTime);
                }
                break;
            case R.id.back_image_button:
                finish();
                break;
        }
    }

    boolean validateDetails(String productName, String productCategory, String productCurrency, String productDescription, String productPrice, String productDiscountedPrice) {

        if (TextUtils.isEmpty(productName)) {
            productNameEditText.setError("Please enter valid product name");
            return false;
        }
        if (TextUtils.isEmpty(productCategory) || productCategory.equalsIgnoreCase("--Select Category--")) {
            Utility.showAlert(this,"Error", "Please select product category");
            return false;
        }
        if (TextUtils.isEmpty(productCurrency) || productCurrency.equalsIgnoreCase("--Select Currency--")) {
            Utility.showAlert(this, "Error", "Please select currency");
            return false;
        }
        if (TextUtils.isEmpty(productDescription)) {
            productDescriptionEditText.setError("Please enter valid product description");
            return false;
        }
        if (TextUtils.isEmpty(productPrice)) {
            productPriceEditText.setError("Please enter valid product price");
            return false;
        }
        if (TextUtils.isEmpty(productDiscountedPrice)) {
            productDiscountPriceEditText.setError("Please enter product discounted price");
            return false;
        }

        return true;
    }

    void addProduct(String productName, String productDescription, String productCurrency, Long productPrice, Long productDiscountedPrice,
                    Long productTime) {
        ProductsModel productsModel = new ProductsModel();
        productsModel.setProduct_name(productName);
        productsModel.setProduct_currency(productCurrency);
        productsModel.setProduct_description(productDescription);
        productsModel.setProduct_discounted_price(productDiscountedPrice);
        productsModel.setProduct_price(productPrice);
        productsModel.setProduct_time(productTime);
        sqliteHelper.addNewProduct(productsModel);
    }
}
