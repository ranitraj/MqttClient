package com.android.ranit.mqttcommunication.viewModel;

import android.app.Application;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.ranit.mqttcommunication.common.MqttClientUtil;
import com.android.ranit.mqttcommunication.contract.ClientContract;
import com.android.ranit.mqttcommunication.contract.ConnectContract;
import com.android.ranit.mqttcommunication.data.response.DataResponse;
import com.android.ranit.mqttcommunication.data.response.Error;
import com.android.ranit.mqttcommunication.data.response.States;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.jetbrains.annotations.NotNull;

public class MqttClientViewModel extends AndroidViewModel
        implements ConnectContract.ViewModel, ClientContract.ViewModel {
    private static final String TAG = MqttClientViewModel.class.getSimpleName();

    private final MqttClientUtil mMqttClientUtilInstance;

    private final MutableLiveData<DataResponse> mConnectBrokerMutableLiveData;
    private final MutableLiveData<DataResponse> mDisconnectBrokerMutableLiveData;

    // Constructor
    public MqttClientViewModel(@NonNull @NotNull Application application) {
        super(application);

        mMqttClientUtilInstance = MqttClientUtil.getInstance();

        mConnectBrokerMutableLiveData = new MutableLiveData<>();
        mDisconnectBrokerMutableLiveData = new MutableLiveData<>();
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
                });
    }

    /**
     * Get Live Data response from connectToBroker method
     */
    public LiveData<DataResponse> getConnectToBrokerLiveData() {
        return mConnectBrokerMutableLiveData;
    }

    @Override
    public void disconnectFromMqttBroker() {
        Log.d(TAG, "disconnectFromMqttBroker() called");

        // Initially, Setting Status as LOADING
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mDisconnectBrokerMutableLiveData
                    .setValue(new DataResponse(States.EnumStates.LOADING, false, null));
        } else {
            mDisconnectBrokerMutableLiveData
                    .postValue(new DataResponse(States.EnumStates.LOADING, false, null));
        }

        mMqttClientUtilInstance.disconnectFromBroker(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "onSuccess: Disconnected from Broker");

                if (Looper.myLooper() == Looper.getMainLooper()) {
                    mDisconnectBrokerMutableLiveData
                            .setValue(new DataResponse(States.EnumStates.SUCCESS, true, null));
                } else {
                    mDisconnectBrokerMutableLiveData
                            .postValue(new DataResponse(States.EnumStates.SUCCESS, true, null));
                }
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.e(TAG, "onFailure: Could not disconnect from Broker");

                if (Looper.myLooper() == Looper.getMainLooper()) {
                    mDisconnectBrokerMutableLiveData
                            .setValue(new DataResponse(States.EnumStates.ERROR, false,
                                    new Error("Could not disconnect from Broker")));
                } else {
                    mDisconnectBrokerMutableLiveData
                            .postValue(new DataResponse(States.EnumStates.ERROR, false,
                                    new Error("Could not disconnect from Broker")));
                }
            }
        });
    }

    /**
     * Get Live Data response from disconnectFromBroker method
     */
    public LiveData<DataResponse> getDisconnectFromBrokerLiveData() {
        return mDisconnectBrokerMutableLiveData;
    }
}
