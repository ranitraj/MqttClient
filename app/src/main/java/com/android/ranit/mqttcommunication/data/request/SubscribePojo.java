package com.android.ranit.mqttcommunication.data.request;

public class SubscribePojo {
    private String topic;
    private int qosLevel;

    // Constructor
    public SubscribePojo(String topic, int qosLevel) {
        this.topic = topic;
        this.qosLevel = qosLevel;
    }

    // Getters-Setters
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getQosLevel() {
        return qosLevel;
    }

    public void setQosLevel(int qosLevel) {
        this.qosLevel = qosLevel;
    }
}
