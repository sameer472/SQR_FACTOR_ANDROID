package com.hackerkernel.user.sqrfactor.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.hackerkernel.user.sqrfactor.R;

public class ViewUtils {
    static AlertDialog dialog;

    public static void showProgressBar(Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View v = LayoutInflater.from(ctx).inflate(R.layout.progresslay, null, false);
        builder.setView(v);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public static void dismissProgressBar() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
