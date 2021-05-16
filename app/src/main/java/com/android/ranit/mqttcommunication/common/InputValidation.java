package com.android.ranit.mqttcommunication.common;

import android.text.TextUtils;

public class InputValidation {

    public static boolean isEmpty(String input) {
        return TextUtils.isEmpty(input);
    }
}
