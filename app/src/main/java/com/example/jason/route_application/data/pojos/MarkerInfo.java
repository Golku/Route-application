package com.example.jason.route_application.data.pojos;

/**
 * Created by Jason on 12-Apr-18.
 */

public class MarkerInfo {

    private boolean selected;
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
