package com.android.ranit.mqttcommunication.contract;

public interface ConnectContract {
    // View (To be implemented by ConnectFragment)
    interface View {
        void prepareDataForBrokerConnection();
        void onConnectButtonClicked();
        void onClearButtonClicked();
        void displayMessage(String message);
    }

    // View-Model
    interface ViewModel {
        void connectToMqttBroker(String serverUri, String clientId, String userName, String password);
    }
}
