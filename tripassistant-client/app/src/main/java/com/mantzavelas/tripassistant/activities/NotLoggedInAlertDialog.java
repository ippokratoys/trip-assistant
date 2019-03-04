package com.mantzavelas.tripassistant.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.mantzavelas.tripassistant.R;

public class NotLoggedInAlertDialog extends AlertDialog {

    private AlertDialog.Builder dialogBuilder;
    private FragmentManager fragmentManager;

    protected NotLoggedInAlertDialog(Context context) {
        super(context);
        this.dialogBuilder = new AlertDialog.Builder(context);
    }

    protected NotLoggedInAlertDialog(Context context, FragmentManager manager) {
        this(context);
        fragmentManager = manager;
    }

    protected void create(@NonNull String message) {
        dialogBuilder.setTitle("You are not logged-in");
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("Login", loginButtonListener);
        dialogBuilder.setNegativeButton("Cancel", cancelButtonListener);
    }

    @Override
    public void show() {
        dialogBuilder.create().show();
    }

    private OnClickListener loginButtonListener = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container_body, new LoginFragment(), "LoginFragment")
                    .commit();
            fragmentManager.popBackStack();
        }
    };

    private OnClickListener cancelButtonListener = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            fragmentManager.popBackStack();
        }
    };
}
