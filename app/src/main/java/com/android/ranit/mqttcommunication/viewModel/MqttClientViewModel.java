package com.android.ranit.mqttcommunication.viewModel;

import android.app.Application;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.ranit.mqttcommunication.common.MqttClientUtil;
import com.android.ranit.mqttcommunication.contract.ConnectContract;
import com.android.ranit.mqttcommunication.data.DataResponse;
import com.android.ranit.mqttcommunication.data.Error;
import com.android.ranit.mqttcommunication.data.States;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jetbrains.annotations.NotNull;

public class MqttClientViewModel extends AndroidViewModel implements ConnectContract.ViewModel {
    private static final String TAG = MqttClientViewModel.class.getSimpleName();

    private final MqttClientUtil mMqttClientUtilInstance;

    private MutableLiveData<DataResponse> mConnectBrokerMutableLiveData;

    // Constructor
    public MqttClientViewModel(@NonNull @NotNull Application application) {
        super(application);

        mMqttClientUtilInstance = MqttClientUtil.getInstance();

        mConnectBrokerMutableLiveData = new MutableLiveData<>();
    }

    @Override
    public void connectToMqttBroker(String serverUri, String clientId, String userName, String password) {
        Log.d(TAG, "connectToMqttBroker() called with: serverUri = [" + serverUri + "], " +
                "clientId = [" + clientId + "], userName = [" + userName + "], password = [" +
                password + "]");

        // Initially, Setting Status as LOADING
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mConnectBrokerMutableLiveData
                    .setValue(new DataResponse(States.EnumStates.LOADING, false, null));
        } else {
            mConnectBrokerMutableLiveData
                    .postValue(new DataResponse(States.EnumStates.LOADING, false, null));
        }

        mMqttClientUtilInstance.connectToBroker(userName, password,
                new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken iMqttToken) {
                        Log.d(TAG, "onSuccess: Connected to Broker");

                        if (Looper.myLooper() == Looper.getMainLooper()) {
                            mConnectBrokerMutableLiveData
                                    .setValue(new DataResponse(States.EnumStates.SUCCESS, true, null));
                        } else {
                            mConnectBrokerMutableLiveData
                                    .postValue(new DataResponse(States.EnumStates.SUCCESS, true, null));
                        }
                    }

                    @Override
                    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                        Log.e(TAG, "onFailure: Could not connect to Broker");

                        if (Looper.myLooper() == Looper.getMainLooper()) {
                            mConnectBrokerMutableLiveData
                                    .setValue(new DataResponse(States.EnumStates.ERROR, false,
                                            new Error("Could not connect to Broker")));
                        } else {
                            mConnectBrokerMutableLiveData
                                    .postValue(new DataResponse(States.EnumStates.ERROR, false,
                                            new Error("Could not connect to Broker")));
                        }
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

    /**
     * Get Live Data response from connectToBroker method
     */
    public LiveData<DataResponse> getConnectToBrokerLiveData() {
        return mConnectBrokerMutableLiveData;
    }
}
