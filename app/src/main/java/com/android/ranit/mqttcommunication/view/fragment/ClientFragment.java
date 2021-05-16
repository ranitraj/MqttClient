package com.android.ranit.mqttcommunication.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ranit.mqttcommunication.R;
import com.android.ranit.mqttcommunication.common.InputValidation;
import com.android.ranit.mqttcommunication.contract.ClientContract;
import com.android.ranit.mqttcommunication.data.request.PublishPojo;
import com.android.ranit.mqttcommunication.data.request.SubscribePojo;
import com.android.ranit.mqttcommunication.data.response.DataResponse;
import com.android.ranit.mqttcommunication.data.response.States;
import com.android.ranit.mqttcommunication.databinding.FragmentClientBinding;
import com.android.ranit.mqttcommunication.view.activity.MainActivity;
import com.android.ranit.mqttcommunication.view.custom.CustomProgressbar;
import com.android.ranit.mqttcommunication.viewModel.MqttClientViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

public class ClientFragment extends Fragment implements ClientContract.View {
    public static final String TAG = ClientFragment.class.getSimpleName();

    private FragmentClientBinding mBinding;

    private MqttClientViewModel mViewModel;
    private CustomProgressbar mProgressBar;

    private String mPublishTopic, mPublishPayload, mSubscribeTopic;
    private int mPublishQosLevel, mSubscribeQosLevel;
    private boolean mRetentionFlag;

    public ClientFragment() {
        // Required empty public constructor
    }

    /**
     * Observer for disconnectFromBroker LiveData Response
     */
    private final Observer<DataResponse> observerDisconnectFromBroker = new Observer<DataResponse>() {
        @Override
        public void onChanged(DataResponse dataResponse) {
            Log.d(TAG, "onChanged() called");
            if (dataResponse.getState() == States.EnumStates.SUCCESS) {
                mProgressBar.hide();
                // Redirect to Connect Fragment
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).launchConnectFragment();
                }
            } else if (dataResponse.getState() == States.EnumStates.ERROR) {
                mProgressBar.dismiss();
                displayMessage(dataResponse.getErrorObject().getErrorMessage());
            } else {
                mProgressBar.show();
            }
        }
    };

    /**
     * Observer for publish LiveData Response
     */
    private final Observer<DataResponse> observerPublish = new Observer<DataResponse>() {
        @Override
        public void onChanged(DataResponse dataResponse) {
            Log.d(TAG, "onChanged() called");

            if (dataResponse.getState() == States.EnumStates.SUCCESS) {
                mProgressBar.hide();
                displayMessage("Published Data to broker successfully.");
            } else if (dataResponse.getState() == States.EnumStates.ERROR) {
                mProgressBar.dismiss();
                displayMessage(dataResponse.getErrorObject().getErrorMessage());
            } else {
                mProgressBar.show();
            }
        }
    };

    /**
     * Observer for subscribeToTopic Live Data
     */
    private final Observer<DataResponse> observerSubscribeToTopic = new Observer<DataResponse>() {
        @Override
        public void onChanged(DataResponse dataResponse) {
            Log.d(TAG, "onChanged() called");

            if (dataResponse.getState() == States.EnumStates.SUCCESS) {
                mProgressBar.hide();
                // Enable un-subscribe button and disable subscribe button
                enableUiComponent(mBinding.buttonUnSubscribe);
                disableUiComponent(mBinding.buttonSubscribe);

                // Change visibility for QoS-Level Header and Slider
                changeVisibility(mBinding.tvQosLevelSubHeader1, View.INVISIBLE);
                changeVisibility(mBinding.sliderSubscribeQosLevel, View.INVISIBLE);

                displayMessage("Subscribed Data to broker successfully.");
            } else if (dataResponse.getState() == States.EnumStates.ERROR) {
                mProgressBar.dismiss();
                displayMessage(dataResponse.getErrorObject().getErrorMessage());
            } else {
                mProgressBar.show();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_client, container, false);
        mProgressBar = new CustomProgressbar(getContext());

        onDisconnectButtonClicked();
        onPublishButtonClicked();
        onSubscribeButtonClicked();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        disableUiComponent(mBinding.buttonUnSubscribe);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            mViewModel = new ViewModelProvider(this).get(MqttClientViewModel.class);
            attachObservers();
        } else {
            Log.e(TAG, "ViewModel could not be initialized");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.getDisconnectFromBrokerLiveData().removeObservers(this);
        mViewModel.getPublishLiveData().removeObservers(this);
        mViewModel.getSubscribeToTopicLiveData().removeObservers(this);
    }

    @Override
    public void prepareDataForPublishing() {
        Log.d(TAG, "prepareDataForPublishing() called");

        // Get Publishing topic
        if (mBinding.editTvPublishTopic.getEditText() != null) {
            mPublishTopic = mBinding.editTvPublishTopic.getEditText().getText().toString();
        }

        // Get Publishing payload
        if (mBinding.editTvPublishMessage.getEditText() != null) {
            mPublishPayload = mBinding.editTvPublishMessage.getEditText().getText().toString();
        }

        // Get Publishing QoS Level
        mPublishQosLevel = (int) mBinding.sliderPublishQosLevel.getValue();

        // Get Retention-Flag Value
        mBinding.toggleGroupRetainFlag.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    if (checkedId == R.id.buttonEnable) {
                        mRetentionFlag = true;
                    } else {
                        mRetentionFlag = false;
                    }
                }
            }
        });
    }

    @Override
    public void prepareDataForSubscribing() {
        Log.d(TAG, "prepareDataForSubscribing() called");

        // Get Subscription topic
        if (mBinding.editTvSubscribeTopic.getEditText() != null) {
            mSubscribeTopic = mBinding.editTvSubscribeTopic.getEditText().getText().toString();
        }

        // Get Subscription QoS Level
        mSubscribeQosLevel = (int) mBinding.sliderSubscribeQosLevel.getValue();
    }

    @Override
    public void onPublishButtonClicked() {
        mBinding.buttonPublish.setOnClickListener(view -> {
            Log.d(TAG, "onPublishButtonClicked called()");
            prepareDataForPublishing();

            if (InputValidation.isEmpty(mPublishTopic) || InputValidation.isEmpty(mPublishPayload)) {
                displayMessage("Enter Topic and Payload before publishing.");
            } else {
                mViewModel.publishDataToMqttBroker(new PublishPojo(mPublishTopic, mPublishPayload,
                        mPublishQosLevel, mRetentionFlag));
            }
        });
    }

    @Override
    public void onSubscribeButtonClicked() {
        mBinding.buttonSubscribe.setOnClickListener(view -> {
            Log.d(TAG, "onSubscribeButtonClicked() called");
            prepareDataForSubscribing();

            if (InputValidation.isEmpty(mSubscribeTopic)) {
                displayMessage("Enter Topic before subscribing.");
            } else {
                mViewModel.subscribeToTopic(new SubscribePojo(mSubscribeTopic, mSubscribeQosLevel));
            }
        });
    }

    @Override
    public void onDisconnectButtonClicked() {
        mBinding.buttonDisconnect.setOnClickListener(view -> {
            Log.d(TAG, "onDisconnectButtonClicked() called");

            mViewModel.disconnectFromMqttBroker();
        });
    }

    @Override
    public void attachObservers() {
        Log.d(TAG, "attachObservers() called");
        mViewModel.getDisconnectFromBrokerLiveData()
                .observe(getViewLifecycleOwner(), observerDisconnectFromBroker);

        mViewModel.getPublishLiveData()
                .observe(getViewLifecycleOwner(), observerPublish);

        mViewModel.getSubscribeToTopicLiveData()
                .observe(getViewLifecycleOwner(), observerSubscribeToTopic);
    }

    @Override
    public void displayMessage(String message) {
        Snackbar.make(mBinding.layoutParent, message, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void changeVisibility(View view, int visibility) {
        view.setVisibility(visibility);
    }

    @Override
    public void enableUiComponent(View componentName) {
        componentName.setEnabled(true);
        componentName.setAlpha(1);
    }

    @Override
    public void disableUiComponent(View componentName) {
        componentName.setEnabled(false);
        componentName.setAlpha((float) 0.8);
    }
}