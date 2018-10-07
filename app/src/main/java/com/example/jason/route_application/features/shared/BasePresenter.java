package com.example.jason.route_application.features.shared;

import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.Event;
import com.example.jason.route_application.data.pojos.Session;
import com.example.jason.route_application.data.pojos.api.ChangeAddressRequest;
import com.example.jason.route_application.data.pojos.api.Drive;
import com.example.jason.route_application.data.pojos.api.DriveRequest;
import com.example.jason.route_application.features.container.MvpContainer;

import android.util.Log;

import java.util.List;

public abstract class BasePresenter {

    private final String debugTag = "debugTag";

    private final long TIME_OUT = 3600000; //60 min
//    private final long TIME_OUT = 120000; //120 sec
//    private final long TIME_OUT = 60000; //60 sec

    private Long currentTime;

    protected BasePresenter() {
        this.currentTime = System.currentTimeMillis();
    }

    protected void beginSession(String username, Session session){
        session.setActive(true);
        session.setUsername(username);
        session.setLoginTime(currentTime);
    }

    protected void endSession(Session session){
        session.setActive(false);
    }

    protected boolean verifySession(Session session){
        return session.getActive();
    }

    protected boolean verifySessionTimeOut(Session session){
        Log.d(debugTag, "Remaining seconds: "+ String.valueOf(((session.getLoginTime() + TIME_OUT) - currentTime)/1000));
        return currentTime < (session.getLoginTime() + TIME_OUT);
    }

    protected void createEvent(String receiver, String eventName, MvpBasePresenter callback){
        Event event = new Event();
        event.setReceiver(receiver);
        event.setEventName(eventName);
        callback.publishEvent(event);
    }

    protected void createEvent(String receiver, String eventName, String address, MvpBasePresenter callback){
        Event event = new Event();
        event.setReceiver(receiver);
        event.setEventName(eventName);
        event.setAddressString(address);
        callback.publishEvent(event);
    }

    protected void createEvent(String receiver, String eventName, Address address, MvpBasePresenter callback){
        Event event = new Event();
        event.setReceiver(receiver);
        event.setEventName(eventName);
        event.setAddress(address);
        callback.publishEvent(event);
    }

    protected void createEvent(String receiver, String eventName, Drive drive, MvpBasePresenter callback){
        Event event = new Event();
        event.setReceiver(receiver);
        event.setEventName(eventName);
        event.setDrive(drive);
        callback.publishEvent(event);
    }

    protected void createEvent(String receiver, String eventName, ChangeAddressRequest request, MvpBasePresenter callback){
        Event event = new Event();
        event.setReceiver(receiver);
        event.setEventName(eventName);
        event.setChangeAddressRequest(request);
        callback.publishEvent(event);
    }

    protected void createEvent(String receiver, String eventName, DriveRequest request, MvpBasePresenter callback){
        Event event = new Event();
        event.setReceiver(receiver);
        event.setEventName(eventName);
        event.setDriveRequest(request);
        callback.publishEvent(event);
    }

    protected void createEvent(String receiver, String eventName, List<Address> addressList, List<Drive> driveList, MvpBasePresenter callback){
        Event event = new Event();
        event.setReceiver(receiver);
        event.setEventName(eventName);
        event.setAddressList(addressList);
        event.setDriveList(driveList);
        callback.publishEvent(event);
    }
}
