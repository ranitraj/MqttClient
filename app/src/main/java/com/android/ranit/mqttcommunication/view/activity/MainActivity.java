package com.android.ranit.mqttcommunication.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;

import com.android.ranit.mqttcommunication.R;
import com.android.ranit.mqttcommunication.contract.MainActivityContract;
import com.android.ranit.mqttcommunication.databinding.ActivityMainBinding;
import com.android.ranit.mqttcommunication.view.fragment.ClientFragment;
import com.android.ranit.mqttcommunication.view.fragment.ConnectFragment;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        launchConnectFragment();
    }

    @Override
    public void launchConnectFragment() {
        Log.d(TAG, "launchConnectFragment() called");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layoutFragmentContainer, ConnectFragment.class, null)
                .commit();
    }

    @Override
    public void launchClientFragment() {
        Log.d(TAG, "launchClientFragment() called");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layoutFragmentContainer, ClientFragment.class, null)
                .commit();
    }
}