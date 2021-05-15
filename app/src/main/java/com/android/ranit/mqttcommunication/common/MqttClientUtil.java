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

    private Context mContext;
    private String mServerUri;
    private String mClientId;

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
        Log.d(TAG, "initializeMqttAndroidClient() called with: context = [" + context + "], " +
                "serverUri = [" + serverUri + "], clientId = [" + clientId + "]");

        this.mContext = context;
        this.mServerUri = serverUri;
        this.mClientId = clientId;

        mMqttClient = new MqttAndroidClient(context, serverUri, clientId);
    }

    /**
     * Connect to Mqtt Broker
     */
    public void connectToBroker(String userName, String password,
                                IMqttActionListener mqttActionListener, MqttCallback mqttCallback) {
        Log.d(TAG, "connectToBroker() called with: userName = [" + userName + "], " +
                "password = [" + password + "], mqttActionListener = [" + mqttActionListener + "], " +
                "mqttCallback = [" + mqttCallback + "]");

        mMqttClient.setCallback(mqttCallback);

        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setUserName(userName);
        connectOptions.setPassword(password.toCharArray());

        try {
            mMqttClient.connect(connectOptions, null, mqttActionListener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
