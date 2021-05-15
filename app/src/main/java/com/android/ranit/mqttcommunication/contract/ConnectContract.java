package com.android.ranit.mqttcommunication.contract;

public interface ConnectContract {
    // View (To be implemented by ConnectFragment)
    interface View {
        void onConnectButtonClicked();
        void onClearButtonClicked();
    }
}
