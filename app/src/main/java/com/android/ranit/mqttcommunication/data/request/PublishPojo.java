package com.android.ranit.mqttcommunication.data.request;

public class PublishPojo {
    private String topic;
    private String payload;
    private int qosLevel;
    private boolean retainFlag;

    // Constructor
    private PublishPojo(String topic, String payload, int qosLevel, boolean retainFlag) {
        this.topic = topic;
        this.payload = payload;
        this.qosLevel = qosLevel;
        this.retainFlag = retainFlag;
    }

    // Getters and Setters
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getQosLevel() {
        return qosLevel;
    }

    public void setQosLevel(int qosLevel) {
        this.qosLevel = qosLevel;
    }

    public boolean isRetainFlag() {
        return retainFlag;
    }

    public void setRetainFlag(boolean retainFlag) {
        this.retainFlag = retainFlag;
    }
}
