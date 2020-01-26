package com.example.hpraj.blood_donation.Holder;

public class DonationHistoryHolder {
    String date, location, number_of_paints;


    public DonationHistoryHolder(String date, String location, String number_of_paints) {
        this.date = date;
        this.location = location;
        this.number_of_paints = number_of_paints;
    }

    public DonationHistoryHolder() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNumber_of_paints() {
        return number_of_paints;
    }

    public void setNumber_of_paints(String number_of_paints) {
        this.number_of_paints = number_of_paints;
    }
}
