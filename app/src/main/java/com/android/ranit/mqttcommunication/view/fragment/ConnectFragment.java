package com.android.ranit.mqttcommunication.view.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ranit.mqttcommunication.R;
import com.android.ranit.mqttcommunication.common.MqttClientUtil;
import com.android.ranit.mqttcommunication.contract.ConnectContract;
import com.android.ranit.mqttcommunication.databinding.FragmentConnectBinding;
import com.android.ranit.mqttcommunication.viewModel.MqttClientViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

public class ConnectFragment extends Fragment implements ConnectContract.View {
    public static final String TAG = ConnectFragment.class.getSimpleName();

    private FragmentConnectBinding mBinding;
    private MqttClientViewModel mViewModel;
    private MqttClientUtil mMqttClientUtilInstance;

    private String mServerUri, mClientId, mUserName, mPassword;

    public ConnectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMqttClientUtilInstance = MqttClientUtil.getInstance();
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            // Making MainActivity at the ViewModelStoreOwner
            mViewModel = new ViewModelProvider(getActivity()).get(MqttClientViewModel.class);
        } else {
            Log.e(TAG, "ViewModel could not be initialized");
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_connect, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        onConnectButtonClicked();
        onClearButtonClicked();
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
        Log.d(TAG, "onConnectButtonClicked() called");
        // Click Listener
        mBinding.buttonConnect.setOnClickListener(view -> {
            prepareDataForBrokerConnection();

            if (!mServerUri.equals("") && !mClientId.equals("")) {
                // Initialize the MqttAndroidService in Utility
                mMqttClientUtilInstance.initializeMqttAndroidClient(getContext(), mServerUri, mClientId);

                if (mViewModel != null) {
                    mViewModel.connectToMqttBroker(mServerUri, mClientId, mUserName, mPassword);
                }
            } else {
                Snackbar.make(mBinding.layoutParent, "Fill necessary data to proceed",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public void onClearButtonClicked() {
        Log.d(TAG, "onClearButtonClicked() called");
        // Click Listener
        mBinding.buttonClearTextFields.setOnClickListener(view -> {

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
}