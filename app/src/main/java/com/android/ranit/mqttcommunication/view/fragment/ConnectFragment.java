package com.android.ranit.mqttcommunication.view.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ranit.mqttcommunication.R;
import com.android.ranit.mqttcommunication.contract.ConnectContract;
import com.android.ranit.mqttcommunication.databinding.FragmentConnectBinding;

public class ConnectFragment extends Fragment implements ConnectContract.View {
    public static final String TAG = ConnectFragment.class.getSimpleName();

    private FragmentConnectBinding mBinding;

    public ConnectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_connect, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onConnectButtonClicked() {
        Log.d(TAG, "onConnectButtonClicked() called");
    }

    @Override
    public void onClearButtonClicked() {
        Log.d(TAG, "onClearButtonClicked() called");
    }
}