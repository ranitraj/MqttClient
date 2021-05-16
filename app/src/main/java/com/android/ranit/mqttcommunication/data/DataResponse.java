package com.android.ranit.mqttcommunication.data;

/**
 * Data Response object to be returned
 */
public class DataResponse {
    private States.EnumStates state;
    private boolean status;
    private Error errorObject;

    // Constructor
    public DataResponse(States.EnumStates state, boolean status, Error errorObject) {
        this.state = state;
        this.status = status;
        this.errorObject = errorObject;
    }

    // Getters and Setters
    public States.EnumStates getState() {
        return state;
    }

    public void setState(States.EnumStates state) {
        this.state = state;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Error getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(Error errorObject) {
        this.errorObject = errorObject;
    }
}
