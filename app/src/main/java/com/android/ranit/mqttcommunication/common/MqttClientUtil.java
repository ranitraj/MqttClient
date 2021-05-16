package com.android.ranit.mqttcommunication.common;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Utility Class which facilitates all Mqtt Client operations
 *
 * Created by: Ranit Raj Ganguly on 15/05/2021
 */
public class MqttClientUtil {
    private static final String TAG = MqttClientUtil.class.getSimpleName();

    private static MqttClientUtil INSTANCE;
    private MqttAndroidClient mMqttClient;

    /**
     * Returns a Singleton Instance of this class
     *
     * @return singleton Instance
     */
    public static synchronized MqttClientUtil getInstance() {
        if (INSTANCE == null) {
            Log.d(TAG, "getInstance() called with New Instance");
            INSTANCE = new MqttClientUtil();
        }
        return INSTANCE;
    }

    // Constructors
    private MqttClientUtil() { }

    /**
     * Initialize MqttAndroidClient (From View)
     */
    public void initializeMqttAndroidClient(Context context, String serverUri, String clientId) {
        Log.d(TAG, "initializeMqttAndroidClient() called with: serverUri = [" + serverUri + "], " +
                "clientId = [" + clientId + "]");

        mMqttClient = new MqttAndroidClient(context, serverUri, clientId);
    }

    /**
     * Connect to MQTT Broker
     */
    public void connectToBroker(String userName, String password,
                                IMqttActionListener listener) {
        Log.d(TAG, "connectToBroker() called");

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(userName);
        options.setPassword(password.toCharArray());

        try {
            Log.d(TAG, "connectToBroker Connecting..");
            mMqttClient.connect(options, null, listener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnect From MQTT Broker
     */
    public void disconnectFromBroker(IMqttActionListener listener) {
        try {
            Log.d(TAG, "disconnectFromBroker Disconnecting..");
            mMqttClient.disconnect(null, listener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Publish Message to MQTT Broker
     */

}
