package com.example.jason.route_application.features.addressDetails;

import com.example.jason.route_application.data.database.DatabaseCallback;
import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.Event;
import com.example.jason.route_application.data.pojos.Session;
import com.example.jason.route_application.data.pojos.database.AddressInformationResponse;
import com.example.jason.route_application.data.pojos.database.AddressTypeResponse;

import org.greenrobot.eventbus.EventBus;

import android.os.Handler;
import android.util.Log;

import javax.inject.Inject;

/**
 * Created by Jason on 07-Feb-18.
 */

public class AddressDetailsPresenter implements MvpAddressDetails.Presenter,
        DatabaseCallback.AddressInformationCallBack,
        DatabaseCallback.AddressTypeChangeCallback {

    private final String debugTag = "debugTag";

    private final MvpAddressDetails.View view;

    private MvpAddressDetails.Interactor interactor;

    private Session session;
    private Address address;

    @Inject
    AddressDetailsPresenter(MvpAddressDetails.View view, MvpAddressDetails.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void setInfo(Session session, Address address) {
        this.session = session;
        this.address = address;
    }

    @Override
    public void getAddressInformation() {
        view.networkOperationStarted();
        interactor.getAddressInformation(address, this);
    }

    @Override
    public String convertTime(int timeInMinutes) {

        double timeFraction = ((double)timeInMinutes) / 60;

        int hour = (int) timeFraction;
        int minute = (int) ((timeFraction - hour) * 60);

        String zero;
        int length = String.valueOf(minute).length();

        if(length < 2){
            zero = "0";
        }else{
            zero = "";
        }

        return String.valueOf(hour)+":"+(String.valueOf(minute)+zero);
    }

    @Override
    public void changeAddressType() {
        interactor.changeAddressType(session.getUsername(), address, this);
    }

    @Override
    public void changeOpeningHours(int hourOfDay, int minute, String workingHours) {

        int timeInMinutes = ((hourOfDay*60)+minute);
        Event event = new Event();

        switch (workingHours){
            case "open" :
                address.setOpeningTime(timeInMinutes);
                interactor.changeOpeningTime(address, timeInMinutes);
                event.setReceiver("addressFragment");
                event.setEventName("openingTimeChange");
                event.setAddress(address);
                break;
            case "close" :
                address.setClosingTime(timeInMinutes);
                interactor.changeClosingTime(address, timeInMinutes);
                event.setReceiver("addressFragment");
                event.setEventName("closingTimeChange");
                event.setAddress(address);
                break;
        }
        EventBus.getDefault().post(event);
    }

    @Override
    public void googleLinkClick() {
        view.showAddressInGoogle(address);
    }

    @Override
    public void addCommentButtonClick() {
        view.showCommentInput(address);
    }

    @Override
    public void onAddressInformationResponse(AddressInformationResponse response) {
        view.networkOperationFinish();
        view.updateMessageToUserTextView(response.getMessage());
        if (response.isInformationAvailable()) {
            if (response.getAddressInformation() != null) {
                if (response.getAddressInformation().getCommentsCount() > 0) {
                    view.setUpAdapter(response.getAddressInformation());
                }
            }
        }
    }

    @Override
    public void onAddressInformationResponseFailure() {
        view.networkOperationFinish();
        view.showToast("Unable to connect to the database");
    }

    @Override
    public void typeChangeResponse(AddressTypeResponse response) {
        if (response.isSuccess()) {

            if (address.isBusiness()) {
                address.setBusiness(false);
            } else {
                address.setBusiness(true);
            }

            Event event = new Event();
            event.setReceiver("all");
            event.setEventName("addressTypeChange");
            event.setAddress(address);

            EventBus.getDefault().post(event);
        }

        view.changeAddressType(address);
        view.networkOperationFinish();
    }

    @Override
    public void typeChangeResponseFailure() {
        view.networkOperationFinish();
        view.showToast("Fail to change address type");
    }
}
