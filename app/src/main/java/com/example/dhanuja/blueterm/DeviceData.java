package com.example.dhanuja.blueterm;

public class DeviceData {
    private String Name;
    private String Address;

    public DeviceData(String name, String address) {
        Name = name;
        Address = address;
    }

    public DeviceData(){

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}

