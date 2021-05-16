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
import com.android.ranit.mqttcommunication.common.MqttClientUtil;
import com.android.ranit.mqttcommunication.contract.ClientContract;
import com.android.ranit.mqttcommunication.data.DataResponse;
import com.android.ranit.mqttcommunication.data.States;
import com.android.ranit.mqttcommunication.databinding.FragmentClientBinding;
import com.android.ranit.mqttcommunication.view.activity.MainActivity;
import com.android.ranit.mqttcommunication.view.custom.CustomProgressbar;
import com.android.ranit.mqttcommunication.viewModel.MqttClientViewModel;
import com.google.android.material.snackbar.Snackbar;

public class ClientFragment extends Fragment implements ClientContract.View {
    public static final String TAG = ClientFragment.class.getSimpleName();

    private FragmentClientBinding mBinding;

    private MqttClientViewModel mViewModel;
    private CustomProgressbar mProgressBar;

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
                displayMessage("Something went wrong! Try again.");
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

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            mViewModel = new ViewModelProvider(this).get(MqttClientViewModel.class);
        } else {
            Log.e(TAG, "ViewModel could not be initialized");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.getDisconnectFromBrokerLiveData().removeObservers(this);
    }

    @Override
    public void onDisconnectButtonClicked() {
        mBinding.buttonDisconnect.setOnClickListener(view -> {
            Log.d(TAG, "onDisconnectButtonClicked() called");

            // Attaching Observer
            mViewModel.getDisconnectFromBrokerLiveData()
                    .observe(getViewLifecycleOwner(), observerDisconnectFromBroker);

            mViewModel.disconnectFromMqttBroker();
        });
    }

    @Override
    public void displayMessage(String message) {
        Snackbar.make(mBinding.layoutParent, message, Snackbar.LENGTH_SHORT)
                .show();
    }
}