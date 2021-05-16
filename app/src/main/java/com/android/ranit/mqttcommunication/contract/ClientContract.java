package com.android.ranit.mqttcommunication.contract;

import com.android.ranit.mqttcommunication.data.request.PublishPojo;
import com.android.ranit.mqttcommunication.data.request.SubscribePojo;

public interface ClientContract {
    // View (To be implemented by ClientFragment)
    interface View {
        void prepareDataForPublishing();
        void prepareDataForSubscribing();
        void onPublishButtonClicked();
        void onSubscribeButtonClicked();
        void onDisconnectButtonClicked();
        void attachObservers();
        void displayMessage(String message);
        void changeVisibility(android.view.View view, int visibility);
        void enableUiComponent(android.view.View componentName);
        void disableUiComponent(android.view.View componentName);
    }

    // View-Model
    interface ViewModel {
        void disconnectFromMqttBroker();
        void publishDataToMqttBroker(PublishPojo data);
        void subscribeToTopic(SubscribePojo data);
    }
}
