package com.example.hpraj.blood_donation.Holder;

public class NotificationHolder {

    String not_id, header, body, send_time, read_status,sender_id;

    public NotificationHolder() {

    }

    public NotificationHolder(String not_id, String header, String body, String send_time, String read_status, String send_id) {
        this.header = header;
        this.body = body;
        this.send_time = send_time;
        this.read_status = read_status;
        this.not_id = not_id;
        this.sender_id = send_id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    public String getNot_id() {
        return not_id;
    }

    public void setNot_id(String not_id) {
        this.not_id = not_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }
}
