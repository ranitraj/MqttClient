package com.android.ranit.mqttcommunication.common;

import android.content.Context;
import android.util.Log;

import com.android.ranit.mqttcommunication.data.request.PublishPojo;
import com.android.ranit.mqttcommunication.data.request.SubscribePojo;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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
    public void publish(PublishPojo data, IMqttActionListener listener) {
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(data.getPayload().getBytes());
            message.setQos(data.getQosLevel());
            message.setRetained(data.isRetainFlag());

            Log.d(TAG, "publish: Publishing data to broker with Topic = [" +data.getTopic()+ "], " +
                            "Payload = [" +data.getPayload()+ "], QoS Level = [" + data.getQosLevel() + "], Retention Flag = [" +
                            data.isRetainFlag() + "]");

            mMqttClient.publish(data.getTopic(), message, null, listener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Subscribe to an MQTT Topic (through Broker)
     */
    public void subscribe(SubscribePojo data, IMqttActionListener listener) {
        try {
            Log.d(TAG, "subscribe: Subscribing to broker with Topic = [" +data.getTopic()+ "], " +
                    " QoS Level = [" + data.getQosLevel() + "]");

            mMqttClient.subscribe(data.getTopic(), data.getQosLevel(), null, listener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Un-Subscribe from MQTT Topic (through Broker)
     */
    public void unSubscribe(String topicName, IMqttActionListener listener) {
        try {
            Log.d(TAG, "unSubscribe: Un-Subscribing from broker with Topic = [" +topicName+ "]");

            mMqttClient.unsubscribe(topicName, null, listener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
