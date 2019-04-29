package com.mantzavelas.tripassistant.activities.listeners;

import android.view.View;

public interface IClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
