package com.example.jason.route_application.data.pojos.database;

public class AddressInformationResponse {

    private boolean informationAvailable;
    private String message;
    private AddressInformation addressInformation;

    public boolean isInformationAvailable() {
        return informationAvailable;
    }

    public void setInformationAvailable(boolean informationAvailable) {
        this.informationAvailable = informationAvailable;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AddressInformation getAddressInformation() {
        return addressInformation;
    }

    public void setAddressInformation(AddressInformation addressInformation) {
        this.addressInformation = addressInformation;
    }
}
