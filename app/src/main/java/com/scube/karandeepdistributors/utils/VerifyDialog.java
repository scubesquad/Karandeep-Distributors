package com.scube.karandeepdistributors.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.scube.karandeepdistributors.activities.VerfiyOTPActivity;

/**
 * Verify Dialog Fragment for Sign in and Sign up
 */
public class VerifyDialog extends DialogFragment {
    public VerifyDialog() {
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
                        Intent intent = new Intent(getActivity(), VerfiyOTPActivity.class);
                        startActivity(new Intent(intent));
                        getActivity().finish();
                    }
                });
        return builder.create();
    }
}
