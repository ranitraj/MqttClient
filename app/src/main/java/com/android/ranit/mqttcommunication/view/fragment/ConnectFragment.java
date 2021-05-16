package com.android.ranit.mqttcommunication.view.fragment;

import android.os.Bundle;

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
import com.android.ranit.mqttcommunication.common.MqttClientUtil;
import com.android.ranit.mqttcommunication.contract.ConnectContract;
import com.android.ranit.mqttcommunication.data.response.DataResponse;
import com.android.ranit.mqttcommunication.data.response.States;
import com.android.ranit.mqttcommunication.databinding.FragmentConnectBinding;
import com.android.ranit.mqttcommunication.view.activity.MainActivity;
import com.android.ranit.mqttcommunication.view.custom.CustomProgressbar;
import com.android.ranit.mqttcommunication.viewModel.MqttClientViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

public class ConnectFragment extends Fragment implements ConnectContract.View {
    public static final String TAG = ConnectFragment.class.getSimpleName();

    private FragmentConnectBinding mBinding;
    private MqttClientViewModel mViewModel;
    private MqttClientUtil mMqttClientUtilInstance;
    private CustomProgressbar mProgressBar;

    private String mServerUri, mClientId, mUserName, mPassword;

    public ConnectFragment() {
        // Required empty public constructor
    }

    /**
     * Observer for connectToBroker LiveData Response
     */
    private final Observer<DataResponse> observerConnectToBroker = new Observer<DataResponse>() {
        @Override
        public void onChanged(DataResponse dataResponse) {
            Log.d(TAG, "onChanged() called");

            if (dataResponse.getState() == States.EnumStates.SUCCESS) {
                mProgressBar.dismiss();
                // Launch Client Fragment
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).launchClientFragment();
                }
                mViewModel.getConnectToBrokerLiveData().removeObserver(this);

            } else if (dataResponse.getState() == States.EnumStates.ERROR) {
                mProgressBar.dismiss();
                displayMessage("Something went wrong! Try again.");

                mViewModel.getConnectToBrokerLiveData().removeObserver(this);
            } else {
                mProgressBar.show();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMqttClientUtilInstance = MqttClientUtil.getInstance();
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            mViewModel = new ViewModelProvider(this).get(MqttClientViewModel.class);
            attachObserver();
        } else {
            Log.e(TAG, "ViewModel could not be initialized");
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_connect, container, false);
        mProgressBar = new CustomProgressbar(getContext());

        onConnectButtonClicked();
        onClearButtonClicked();

        return mBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.getConnectToBrokerLiveData().removeObservers(this);
    }

    @Override
    public void prepareDataForBrokerConnection() {
        Log.d(TAG, "prepareDataForBrokerConnection() called");
        // Get Server-URI Input
        if (mBinding.editTvServerUri.getEditText() != null) {
            mServerUri = mBinding.editTvServerUri.getEditText().getText().toString();
        }

        // Get Client-ID Input
        if (mBinding.editTvClientId.getEditText() != null) {
            mClientId = mBinding.editTvClientId.getEditText().getText().toString();
        }

        // Get User-Name Input
        if (mBinding.editTvUsername.getEditText() != null) {
            mUserName = mBinding.editTvUsername.getEditText().getText().toString();
        }

        // Get Password Input
        if (mBinding.editTvPassword.getEditText() != null) {
            mPassword = mBinding.editTvPassword.getEditText().getText().toString();
        }
    }

    @Override
    public void onConnectButtonClicked() {
        // Click Listener
        mBinding.buttonConnect.setOnClickListener(view -> {
            Log.d(TAG, "onConnectButtonClicked() called");

            prepareDataForBrokerConnection();

            if (InputValidation.isEmpty(mServerUri) || InputValidation.isEmpty(mClientId)) {
                displayMessage("Fill Server-URI and Client-ID before connecting");
            } else {
                // Initialize the MqttAndroidService in Utility
                mMqttClientUtilInstance.initializeMqttAndroidClient(getContext(), mServerUri, mClientId);

                if (mViewModel != null) {
                    mViewModel.connectToMqttBroker(mServerUri, mClientId, mUserName, mPassword);
                }
            }
        });
    }

    @Override
    public void onClearButtonClicked() {
        // Click Listener
        mBinding.buttonClearTextFields.setOnClickListener(view -> {
            Log.d(TAG, "onClearButtonClicked() called");

            if (mBinding.editTvServerUri.getEditText() != null) {
                mBinding.editTvServerUri.getEditText().setText("");
            }

            if (mBinding.editTvClientId.getEditText() != null) {
                mBinding.editTvClientId.getEditText().setText("");
            }

            if (mBinding.editTvUsername.getEditText() != null) {
                mBinding.editTvUsername.getEditText().setText("");
            }

            if (mBinding.editTvPassword.getEditText() != null) {
                mBinding.editTvPassword.getEditText().setText("");
            }
        });
    }

    @Override
    public void attachObserver() {
        mViewModel.getConnectToBrokerLiveData()
                .observe(getViewLifecycleOwner(), observerConnectToBroker);
    }

    @Override
    public void displayMessage(String message) {
        Snackbar.make(mBinding.layoutParent, message, Snackbar.LENGTH_SHORT)
                .show();
    }
}