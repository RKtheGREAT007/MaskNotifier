package com.example.masknotifier.model;

public class HistoryData {
    private String reply;
    private String timeStamp;

    public HistoryData(String reply, String timeStamp) {
        this.reply = reply;
        this.timeStamp = timeStamp;
    }

    public HistoryData() {
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
