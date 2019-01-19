package com.app.ecomdemoapp.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.ecomdemoapp.R;
import com.app.ecomdemoapp.callbacks.AlertOkCancelCallback;

public class Utility {

    public static void alertWithCustomLayoutYesNo(final Activity ctx, final int type,
                                                  final int product_id, final AlertOkCancelCallback callback) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);

        LayoutInflater inflater = ctx.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.round_dialog, null);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        AppCompatTextView textSubject = dialogView.findViewById(R.id.text_subject);
        AppCompatTextView textMessage = dialogView.findViewById(R.id.text_message);
        AppCompatButton mCancelButton = dialogView.findViewById(R.id.button_cancel);
        AppCompatButton mOkButton = dialogView.findViewById(R.id.button_ok);

        if (type == 0) {
            textSubject.setText(ctx.getResources().getString(R.string.remove_from_cart));
            textMessage.setText(ctx.getResources().getString(R.string.remove_from_cart_message));
        } else {
            textSubject.setText(ctx.getResources().getString(R.string.add_to_cart));
            textMessage.setText(ctx.getResources().getString(R.string.add_to_cart_message));
        }

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onDone(product_id);
                alertDialog.dismiss();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onCancel();
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.round_dialog_bg));
        }

        alertDialog.show();
    }

    public static void showAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
