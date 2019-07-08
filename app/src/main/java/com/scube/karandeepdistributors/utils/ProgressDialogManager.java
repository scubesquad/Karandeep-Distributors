package com.scube.karandeepdistributors.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Custom Progress dialog box
 */
public class ProgressDialogManager {
    private ProgressDialog dialog;

    public ProgressDialogManager(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
    }

    public void showDialog() {
        try {
            if (!dialog.isShowing()) {
                dialog.show();
            } else {
                dialog.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideDialog() {
        try {
            if (dialog.isShowing()) {   //dialog.hide();
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
