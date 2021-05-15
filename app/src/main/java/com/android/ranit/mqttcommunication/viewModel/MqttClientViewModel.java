package com.android.ranit.mqttcommunication.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.ranit.mqttcommunication.common.MqttClientUtil;
import com.android.ranit.mqttcommunication.contract.ConnectContract;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jetbrains.annotations.NotNull;

public class MqttClientViewModel extends AndroidViewModel implements ConnectContract.ViewModel {
    private static final String TAG = MqttClientViewModel.class.getSimpleName();

    private final MqttClientUtil mMqttClientUtilInstance;

    // Constructor
    public MqttClientViewModel(@NonNull @NotNull Application application) {
        super(application);

        mMqttClientUtilInstance = MqttClientUtil.getInstance();
    }

    @Override
    public void connectToMqttBroker(String serverUri, String clientId, String userName, String password) {
        Log.d(TAG, "connectToMqttBroker() called with: serverUri = [" + serverUri + "], " +
                "clientId = [" + clientId + "], userName = [" + userName + "], password = [" +
                password + "]");

        mMqttClientUtilInstance.connectToBroker(userName, password,
                new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken iMqttToken) {
                        Log.d(TAG, "onSuccess: Connected to Broker");
                    }

                    @Override
                    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                        Log.e(TAG, "onFailure: Could not connect to Broker");
                    }
                },
                
                new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable throwable) {
                        Log.e(TAG, "connectionLost: Could not connect due to "+throwable.toString());
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                        String message = "Received message: "+mqttMessage.toString()+" from topic: "+topic;
                        Log.d(TAG, message);
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                        Log.d(TAG, "deliveryComplete: ");
                    }
                });
    }
}
