package com.app.ecomdemoapp.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;

import com.app.ecomdemoapp.R;
import com.app.ecomdemoapp.activities.CartActivity;
import com.app.ecomdemoapp.callbacks.AlertOkCancelCallback;
import com.app.ecomdemoapp.database.SqliteHelper;
import com.app.ecomdemoapp.helpers.Utility;
import com.app.ecomdemoapp.models.CartModel;
import com.app.ecomdemoapp.models.ProductsModel;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<CartModel> cartModelArrayList;
    private Filter filteredProducts;
    private SqliteHelper sqliteHelper;

    private long grand_total = 0;

    public CartListAdapter(Activity context, ArrayList<CartModel> cartModelArrayList, SqliteHelper sqliteHelper) {
        this.context = context;
        this.cartModelArrayList = cartModelArrayList;
        this.sqliteHelper = sqliteHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cart_list_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final ViewHolder itemHolder = viewHolder;
        int product_id = cartModelArrayList.get(itemHolder.getAdapterPosition()).getProduct_id();

        final ProductsModel productsModel = sqliteHelper.getProductById(product_id);

        itemHolder.productNameTextView.setText(productsModel.getProduct_name());
        itemHolder.productDescriptionTextView.setText(productsModel.getProduct_description());
        itemHolder.productDiscountedPriceTextView.setText(productsModel.getProduct_currency() + productsModel.getProduct_discounted_price());
        itemHolder.productPriceTextView.setText(productsModel.getProduct_currency() + productsModel.getProduct_price());

        itemHolder.productItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.alertWithCustomLayoutYesNo(context, productsModel.getProduct_id(), 0, new AlertOkCancelCallback() {
                    @Override
                    public void onDone(int product_id) {
                        if (context instanceof CartActivity) {
                            ((CartActivity) context).removeFromCart(productsModel, itemHolder.getAdapterPosition());
                        }
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartModelArrayList == null ? 0 : cartModelArrayList.size();
    }

    public void updateGrandTotal(long productPrice) {
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView productNameTextView;
        private AppCompatTextView productDescriptionTextView;
        private AppCompatTextView productPriceTextView;
        private AppCompatTextView productDiscountedPriceTextView;
        private LinearLayout productItemContainer;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            productNameTextView = itemView.findViewById(R.id.product_name_text_view);
            productDescriptionTextView = itemView.findViewById(R.id.product_description_text_view);
            productPriceTextView = itemView.findViewById(R.id.product_price_text_view);
            productDiscountedPriceTextView = itemView.findViewById(R.id.product_discounted_price_text_view);
            productItemContainer = itemView.findViewById(R.id.product_item_container);
        }
    }
}
