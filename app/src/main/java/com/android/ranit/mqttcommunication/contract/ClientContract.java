package com.android.ranit.mqttcommunication.contract;

import com.android.ranit.mqttcommunication.data.request.PublishPojo;

public interface ClientContract {
    // View (To be implemented by ClientFragment)
    interface View {
        void prepareDataForPublishing();
        void onPublishButtonClicked();
        void onDisconnectButtonClicked();
        void attachObservers();
        void displayMessage(String message);
    }

    // View-Model
    interface ViewModel {
        void disconnectFromMqttBroker();
        void publishDataToMqttBroker(PublishPojo data);
    }
}
