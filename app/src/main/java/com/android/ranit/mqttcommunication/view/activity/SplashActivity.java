package com.android.ranit.mqttcommunication.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.android.ranit.mqttcommunication.R;
import com.android.ranit.mqttcommunication.common.Constants;
import com.android.ranit.mqttcommunication.contract.SplashScreenContract;
import com.android.ranit.mqttcommunication.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity implements SplashScreenContract.View {
    private static final String TAG = SplashActivity.class.getSimpleName();

    private ActivitySplashBinding mBinding;
    private static final String SPLASH_SCREEN_ANIMATION = "splash.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        initializeUi();
        setupSplashHandler();
    }

    @Override
    public void initializeUi() {
        setupLottieAnimation(mBinding.lottieViewSplash, SPLASH_SCREEN_ANIMATION);
    }

    @Override
    public void setupLottieAnimation(LottieAnimationView animationView, String animationName) {
        Log.d(TAG, "setupLottieAnimation");
        animationView.setAnimation(animationName);
        animationView.playAnimation();
    }

    @Override
    public void launchActivity() {
        Log.d(TAG, "launchActivity() called");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void setupSplashHandler() {
        Handler mSplashScreenHandler = new Handler();

        mSplashScreenHandler.postDelayed((Runnable) () -> {
            launchActivity();
            finish();
        }, Constants.SPLASH_SCREEN_DURATION);
    }
}