package com.android.ranit.mqttcommunication.contract;

public interface MainActivityContract {
    // View (To be implemented by MainActivity)
    interface View {
        void launchConnectFragment();
        void launchClientFragment();
    }
}
