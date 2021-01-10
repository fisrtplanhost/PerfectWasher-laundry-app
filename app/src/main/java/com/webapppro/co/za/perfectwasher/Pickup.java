package com.webapppro.co.za.perfectwasher;

public class Pickup {
    private String name;
    private String telephone;
    private String address;
    private String timePick;
    private String datePick;
    private String instruction;


    public Pickup() {

    }

    public Pickup(String name, String telephone, String address, String timePick, String datePick, String instruction) {
        this.name = name;
        this.telephone = telephone;
        this.address = address;
        this.timePick = timePick;
        this.datePick = datePick;
        this.instruction = instruction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimePick() {
        return timePick;
    }

    public void setTimePick(String timePick) {
        this.timePick = timePick;
    }

    public String getDatePick() {
        return datePick;
    }

    public void setDatePick(String datePick) {
        this.datePick = datePick;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
