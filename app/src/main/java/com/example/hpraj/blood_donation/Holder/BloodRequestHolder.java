package com.example.hpraj.blood_donation.Holder;

import java.util.Date;

public class BloodRequestHolder {
    String id,blood_group, number_Of_Paints, req_date, status, req_user_id, report;
    String req_send_date;

    public BloodRequestHolder() {
    }

    public BloodRequestHolder(String id,String blood_group, String number_Of_Paints, String req_date, String status, String req_user_id, String req_send_date,String report) {
        this.blood_group = blood_group;
        this.number_Of_Paints = number_Of_Paints;
        this.req_date = req_date;
        this.status = status;
        this.req_user_id = req_user_id;
        this.req_send_date = req_send_date;
        this.id = id;
        this.report = report;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getNumber_Of_Paints() {
        return number_Of_Paints;
    }

    public void setNumber_Of_Paints(String number_Of_Paints) {
        this.number_Of_Paints = number_Of_Paints;
    }

    public String getReq_date() {
        return req_date;
    }

    public void setReq_date(String req_date) {
        this.req_date = req_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReq_user_id() {
        return req_user_id;
    }

    public void setReq_user_id(String req_user_id) {
        this.req_user_id = req_user_id;
    }

    public String getReq_send_date() {
        return req_send_date;
    }

    public void setReq_send_date(String req_send_date) {
        this.req_send_date = req_send_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
