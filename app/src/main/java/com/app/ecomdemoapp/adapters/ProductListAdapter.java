package com.app.ecomdemoapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.app.ecomdemoapp.R;
import com.app.ecomdemoapp.activities.MainActivity;
import com.app.ecomdemoapp.callbacks.AlertOkCancelCallback;
import com.app.ecomdemoapp.helpers.Utility;
import com.app.ecomdemoapp.models.ProductsModel;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> implements Filterable {
    private Activity context;
    private ArrayList<ProductsModel> productsModelArrayList;
    private ArrayList<ProductsModel> filterList;
    private Filter filteredProducts;

    public ProductListAdapter(Activity context, ArrayList<ProductsModel> productsModelArrayList) {
        this.context = context;
        this.productsModelArrayList = productsModelArrayList;
        this.filterList = productsModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_list_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final ProductsModel productsModel = productsModelArrayList.get(viewHolder.getAdapterPosition());

        viewHolder.productNameTextView.setText(productsModel.getProduct_name());
        viewHolder.productDescriptionTextView.setText(productsModel.getProduct_description());
        viewHolder.productDiscountedPriceTextView.setText(productsModel.getProduct_currency() + productsModel.getProduct_discounted_price());
        viewHolder.productPriceTextView.setText(productsModel.getProduct_currency() + productsModel.getProduct_price());

        viewHolder.productItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.alertWithCustomLayoutYesNo(context, productsModel.getProduct_id(), 1, new AlertOkCancelCallback() {
                    @Override
                    public void onDone(int product_id) {
                        if (context instanceof MainActivity) {
                            ((MainActivity) context).addProductToCart(product_id);
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
        return productsModelArrayList == null ? 0 : productsModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView productNameTextView;
        private AppCompatTextView productDescriptionTextView;
        private AppCompatTextView productPriceTextView;
        private AppCompatTextView productDiscountedPriceTextView;
        private MaterialCardView productItemContainer;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            productNameTextView = itemView.findViewById(R.id.product_name_text_view);
            productDescriptionTextView = itemView.findViewById(R.id.product_description_text_view);
            productPriceTextView = itemView.findViewById(R.id.product_price_text_view);
            productDiscountedPriceTextView = itemView.findViewById(R.id.product_discounted_price_text_view);
            productItemContainer = itemView.findViewById(R.id.product_item_container);
        }
    }

    @Override
    public Filter getFilter() {
        if(filteredProducts == null) {
            filteredProducts = new ProductFilter();
        }
        return filteredProducts;
    }

    class ProductFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (TextUtils.isEmpty(constraint)) {
                results.values = filterList;
                results.count = filterList.size();
            } else {
                ArrayList<ProductsModel> resultArrayList = new ArrayList<>();

                for (ProductsModel productsModel : filterList) {
                    if (productsModel.getProduct_name().toUpperCase().trim().contains(constraint.toString().toUpperCase().trim())) {
                        resultArrayList.add(productsModel);
                    }
                }
                results.values = resultArrayList;
                results.count = resultArrayList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productsModelArrayList = (ArrayList<ProductsModel>) results.values;
            notifyDataSetChanged();
            if (productsModelArrayList.size() > 0) {
                if (context instanceof MainActivity) {
                    ((MainActivity) context).showProductList();
                }
            } else {
                if (context instanceof MainActivity) {
                    ((MainActivity) context).hideProductList();
                }
            }
        }
    }
}
