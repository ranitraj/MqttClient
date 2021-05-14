package com.android.ranit.mqttcommunication.contract;

import com.airbnb.lottie.LottieAnimationView;

public interface SplashScreenContract {
    // View (To be implemented by SplashActivity)
    interface View {
        void initializeUi();
        void setupLottieAnimation(LottieAnimationView animationView, String animationName);
        void launchActivity();
        void setupSplashHandler();
    }
}
