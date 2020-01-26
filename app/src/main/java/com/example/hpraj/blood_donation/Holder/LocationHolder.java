package com.example.hpraj.blood_donation.Holder;

public class LocationHolder {
    String location_name,latitude, longitude;

    public LocationHolder() {
    }

    public LocationHolder(String location_name, String latitude, String longitude) {
        this.location_name = location_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
