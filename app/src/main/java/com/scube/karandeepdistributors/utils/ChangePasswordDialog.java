package com.scube.karandeepdistributors.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.scube.karandeepdistributors.activities.SignInActivity;

/**
 * Change password dialog fragment for updating new password in case user forgot it
 */

public class ChangePasswordDialog extends DialogFragment {
    public ChangePasswordDialog() {
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("title"))
                .setMessage(getArguments().getString("message"))
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                        startActivity(new Intent(getActivity(), SignInActivity.class));
                    }
                });
        return builder.create();
    }
}
