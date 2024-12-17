package com.example.myapi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogHelper {
    public interface DialogCallback {
        void onResponse(boolean accepted);
    }

    public void showConfirmationDialog(Context context, String title, String textBtnAccept, String textBtnCancel, String message, DialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(textBtnAccept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onResponse(true);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(textBtnCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onResponse(false);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

