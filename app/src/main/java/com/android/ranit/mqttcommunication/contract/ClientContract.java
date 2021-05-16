package com.android.ranit.mqttcommunication.contract;

public interface ClientContract {
    // View (To be implemented by ClientFragment)
    interface View {
        void onDisconnectButtonClicked();
        void displayMessage(String message);
    }

    // View-Model
    interface ViewModel {
        void disconnectFromMqttBroker();
    }
}
