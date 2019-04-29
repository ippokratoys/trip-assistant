package com.mantzavelas.tripassistant.activities.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SimpleDialogBuilder {

    private Context context;
    private AlertDialog.Builder dialogBuilder;
    private LinearLayout layout;

    public SimpleDialogBuilder(Context context, String title, String message) {
        this.context = context;

        dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);

        layout = new LinearLayout(context);
    }

    public void setLayout(int orientation) {
        layout.setOrientation(orientation);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void addTextView(String text) {
        TextView textView = new TextView(context);
        textView.setText(text);
        addView(textView);
    }

    public void addEditText(String hint) {
        addEditText(hint, 0);
    }

    public void addEditText(String hint, int inputType) {
        addEditText(hint, inputType, 0);
    }

    public void addEditText(String hint, int inputType, int viewId) {
        EditText editText = new EditText(context);
        editText.setHint(hint);

        if (inputType != 0) {
            editText.setInputType(inputType);
        }

        if (viewId != 0) {
            editText.setId(viewId);
        }

        addView(editText);

    }

    public void addView(View view) {
        addView(view, 0);
    }

    public void addView(View view, int viewId) {

        if (viewId != 0) {
            view.setId(viewId);
        }

        view.setPadding(40, 20, 40, 20);
        layout.addView(view);
    }

    public void addDefaultButtonListeners(DialogInterface.OnClickListener positive,
                                          DialogInterface.OnClickListener negative) {
        dialogBuilder.setPositiveButton("OK", positive);

        dialogBuilder.setNegativeButton("Cancel", negative != null ? negative :
                new DialogInterface.OnClickListener() {
                    @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                }
        );
    }

    public AlertDialog createDialog() {
        dialogBuilder.setView(layout);
        return dialogBuilder.create();
    }
}
