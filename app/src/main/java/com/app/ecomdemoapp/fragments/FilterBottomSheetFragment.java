package com.app.ecomdemoapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.app.ecomdemoapp.R;
import com.app.ecomdemoapp.callbacks.FilterCallback;

public class FilterBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private FilterCallback mListener;
    private AppCompatTextView selectedPriceTextView;
    private AppCompatSeekBar priceSeekbar;
    private AppCompatTextView minPriceTextView;
    private AppCompatTextView maxPriceTextView;
    private AppCompatButton resetButton;
    private AppCompatButton applyButton;
    private long selected_price;

    public static FilterBottomSheetFragment newInstance() {
        Bundle args = new Bundle();
        FilterBottomSheetFragment fragment = new FilterBottomSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_bottom_sheet, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (FilterCallback) parent;
        } else {
            mListener = (FilterCallback) context;
        }
    }


    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    private void initView(View view) {
        selectedPriceTextView = view.findViewById(R.id.selected_price_text_view);
        priceSeekbar = view.findViewById(R.id.price_seekbar);
        minPriceTextView = view.findViewById(R.id.min_price_text_view);
        maxPriceTextView = view.findViewById(R.id.max_price_text_view);
        resetButton = view.findViewById(R.id.reset_button);
        applyButton = view.findViewById(R.id.apply_button);

        selectedPriceTextView.setText(getString(R.string.selected_price) + 0);
        priceSeekbar.setMax(1000);

        priceSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selected_price = progress;
                selectedPriceTextView.setText(getString(R.string.selected_price) + selected_price);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        resetButton.setOnClickListener(this);
        applyButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_button:
                mListener.onFilterReset();
                break;
            case R.id.apply_button:
                mListener.onPriceFilterSelected(selected_price);
                break;
        }
    }
}
