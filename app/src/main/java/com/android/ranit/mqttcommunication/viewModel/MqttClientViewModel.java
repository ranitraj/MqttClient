package com.android.ranit.mqttcommunication.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.ranit.mqttcommunication.contract.ConnectContract;

import org.jetbrains.annotations.NotNull;

public class MqttClientViewModel extends AndroidViewModel implements ConnectContract.ViewModel {
    private static final String TAG = MqttClientViewModel.class.getSimpleName();

    // Constructor
    public MqttClientViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    @Override
    public void connectToMqttBroker(String serverUri, String clientId, String userName, String password) {
        Log.d(TAG, "connectToMqttBroker() called with: serverUri = [" + serverUri + "], " +
                "clientId = [" + clientId + "], userName = [" + userName + "], password = [" +
                password + "]");


    }
}
