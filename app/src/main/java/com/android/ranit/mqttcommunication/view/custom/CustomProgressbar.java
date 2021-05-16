package com.android.ranit.mqttcommunication.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.android.ranit.mqttcommunication.R;

public class CustomProgressbar extends Dialog {

    // Constructor
    public CustomProgressbar(@NonNull Context context) {
        super(context);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        getWindow().setAttributes(layoutParams);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);

        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_progress_bar, null);
        setContentView(view);
    }
}
