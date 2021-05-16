package com.android.ranit.mqttcommunication.data;

/**
 * Custom error class which can be fully customized
 */
public class Error {
    private String mErrorMessage;

    // Constructor
    public Error(String errorMessage) {
        this.mErrorMessage = errorMessage;
    }

    // Getter-Setter

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.mErrorMessage = errorMessage;
    }
}
