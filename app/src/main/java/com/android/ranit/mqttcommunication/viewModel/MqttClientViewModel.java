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
import com.android.ranit.mqttcommunication.data.request.PublishPojo;
import com.android.ranit.mqttcommunication.data.request.SubscribePojo;
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
    private final MutableLiveData<DataResponse> mPublishMutableLiveData;
    private final MutableLiveData<DataResponse> mSubscribeToTopicMutableLiveData;
    private final MutableLiveData<DataResponse> mUnSubscribeFromTopicMutableLiveData;

    // Constructor
    public MqttClientViewModel(@NonNull @NotNull Application application) {
        super(application);

        mMqttClientUtilInstance = MqttClientUtil.getInstance();

        mConnectBrokerMutableLiveData = new MutableLiveData<>();
        mDisconnectBrokerMutableLiveData = new MutableLiveData<>();
        mPublishMutableLiveData = new MutableLiveData<>();
        mSubscribeToTopicMutableLiveData = new MutableLiveData<>();
        mUnSubscribeFromTopicMutableLiveData = new MutableLiveData<>();
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

    @Override
    public void publishDataToMqttBroker(PublishPojo data) {
        Log.d(TAG, "publishDataToMqttBroker() called");

        // Initially, Setting Status as LOADING
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mPublishMutableLiveData
                    .setValue(new DataResponse(States.EnumStates.LOADING, false, null));
        } else {
            mPublishMutableLiveData
                    .postValue(new DataResponse(States.EnumStates.LOADING, false, null));
        }

        mMqttClientUtilInstance.publish(data, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "onSuccess: Published data to Broker");

                if (Looper.myLooper() == Looper.getMainLooper()) {
                    mPublishMutableLiveData
                            .setValue(new DataResponse(States.EnumStates.SUCCESS, true, null));
                } else {
                    mPublishMutableLiveData
                            .postValue(new DataResponse(States.EnumStates.SUCCESS, true, null));
                }
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.e(TAG, "onFailure: Could not publish data to Broker");

                if (Looper.myLooper() == Looper.getMainLooper()) {
                    mPublishMutableLiveData
                            .setValue(new DataResponse(States.EnumStates.ERROR, false,
                                    new Error("Could not publish data to Broker")));
                } else {
                    mPublishMutableLiveData
                            .postValue(new DataResponse(States.EnumStates.ERROR, false,
                                    new Error("Could not publish data to Broker")));
                }
            }
        });
    }

    @Override
    public void subscribeToTopic(SubscribePojo data) {
        Log.d(TAG, "subscribeToTopic() called");

        // Initially, Setting Status as LOADING
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mSubscribeToTopicMutableLiveData
                    .setValue(new DataResponse(States.EnumStates.LOADING, false, null));
        } else {
            mSubscribeToTopicMutableLiveData
                    .postValue(new DataResponse(States.EnumStates.LOADING, false, null));
        }

        mMqttClientUtilInstance.subscribe(data, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "onSuccess: Subscribed to topic via Broker");

                if (Looper.myLooper() == Looper.getMainLooper()) {
                    mSubscribeToTopicMutableLiveData
                            .setValue(new DataResponse(States.EnumStates.SUCCESS, true, null));
                } else {
                    mSubscribeToTopicMutableLiveData
                            .postValue(new DataResponse(States.EnumStates.SUCCESS, true, null));
                }
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.e(TAG, "onFailure: Could not subscribe to topic via Broker");

                if (Looper.myLooper() == Looper.getMainLooper()) {
                    mSubscribeToTopicMutableLiveData
                            .setValue(new DataResponse(States.EnumStates.ERROR, false,
                                    new Error("Could not subscribe to topic via Broker")));
                } else {
                    mSubscribeToTopicMutableLiveData
                            .postValue(new DataResponse(States.EnumStates.ERROR, false,
                                    new Error("Could not subscribe to topic via Broker")));
                }
            }
        });
    }

    @Override
    public void unSubscribeFromTopic(String topicName) {
        Log.d(TAG, "unSubscribeFromTopic() called");

        // Initially, Setting Status as LOADING
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mUnSubscribeFromTopicMutableLiveData
                    .setValue(new DataResponse(States.EnumStates.LOADING, false, null));
        } else {
            mUnSubscribeFromTopicMutableLiveData
                    .postValue(new DataResponse(States.EnumStates.LOADING, false, null));
        }

        mMqttClientUtilInstance.unSubscribe(topicName, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "onSuccess: Un-Subscribed from topic via Broker");

                if (Looper.myLooper() == Looper.getMainLooper()) {
                    mUnSubscribeFromTopicMutableLiveData
                            .setValue(new DataResponse(States.EnumStates.SUCCESS, true, null));
                } else {
                    mUnSubscribeFromTopicMutableLiveData
                            .postValue(new DataResponse(States.EnumStates.SUCCESS, true, null));
                }
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.e(TAG, "onFailure: Could not un-subscribe from topic via Broker");

                if (Looper.myLooper() == Looper.getMainLooper()) {
                    mUnSubscribeFromTopicMutableLiveData
                            .setValue(new DataResponse(States.EnumStates.ERROR, false,
                                    new Error("Could not un-subscribe from topic")));
                } else {
                    mUnSubscribeFromTopicMutableLiveData
                            .postValue(new DataResponse(States.EnumStates.ERROR, false,
                                    new Error("Could not un-subscribe from topic")));
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

    /**
     * Get Live Data response from disconnectFromBroker method
     */
    public LiveData<DataResponse> getDisconnectFromBrokerLiveData() {
        return mDisconnectBrokerMutableLiveData;
    }

    /**
     * Get Live Data response from publish method
     */
    public LiveData<DataResponse> getPublishLiveData() {
        return mPublishMutableLiveData;
    }

    /**
     * Get Live Data response from subscribe method
     */
    public LiveData<DataResponse> getSubscribeToTopicLiveData() {
        return mSubscribeToTopicMutableLiveData;
    }

    /**
     * Get Live Data response from unSubscribe method
     */
    public LiveData<DataResponse> getUnSubscribeFromTopicLiveData() {
        return mUnSubscribeFromTopicMutableLiveData;
    }
}
