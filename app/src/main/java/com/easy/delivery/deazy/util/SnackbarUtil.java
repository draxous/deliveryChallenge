package com.easy.delivery.deazy.util;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarUtil {

    private static SnackbarActions listener;

    public static void showSnackbar(View rootView, final Activity activity) {

        listener = (SnackbarActions)activity;
        Snackbar.make(rootView, "NoInternetConnectivity", Snackbar.LENGTH_LONG)
                .setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onPositiveButtonClick();
                    }
                })
                .setActionTextColor(Color.CYAN)
                .show();

    }
}
