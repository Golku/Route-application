package com.example.jason.route_application.features.container.driveListFragment;

import com.example.jason.route_application.data.models.DriveListHandler;
import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.Event;
import com.example.jason.route_application.data.pojos.api.Drive;
import com.example.jason.route_application.features.shared.BasePresenter;
import com.example.jason.route_application.features.shared.MvpBasePresenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 03-Apr-18.
 */

public class DriveListPresenter extends BasePresenter implements
        MvpBasePresenter,
        MvpDriveList.Presenter,
        DriveListAdapter.AdapterCallback {

    private final String debugTag = "debugTag";

    private MvpDriveList.View view;

    private DriveListHandler listHandler;

    DriveListPresenter(MvpDriveList.View view, List<Drive> driveList) {
        this.view = view;
        listHandler = new DriveListHandler(driveList, this);
        listHandler.createAdapter();
    }

    @Override
    public void showDriveList() {
        view.setupAdapter(listHandler.getAdapter());
    }

    @Override
    public void itemClick(Address address) {
        createEvent("container", "itemClick", address, this);
    }

    @Override
    public void goButtonClick(String address) {
        createEvent("container", "driveDirections", address, this);
    }

    @Override
    public void completeDrive(Drive drive) {
        listHandler.driveCompleted(drive);
        showDriveList();
        createEvent("mapFragment","driveCompleted", drive.getDestinationAddress(),this);
    }

    @Override
    public void publishEvent(Event event) {
        view.postEvent(event);
    }

    @Override
    public void eventReceived(Event event) {

        if (!(event.getReceiver().equals("driveFragment") || event.getReceiver().equals("all"))) {
            return;
        }

        Log.d(debugTag, "Event received on driveFragment: " + event.getEventName());

        switch (event.getEventName()) {
            case "addressTypeChange":
                listHandler.addressTypeChange(event.getAddress());
                showDriveList();
                break;
            case "updateList":
                listHandler.updateList(event.getDriveList());
                showDriveList();
                break;
            case "addDrive":
                listHandler.addDriveToList(event.getDrive());
                view.scrollToItem(listHandler.getListSize());
                createEvent("mapFragment", "driveSuccess", event.getDrive(), this);
                createEvent("container", "updateEndTime", this);
                break;
            case "removeDrive":
                listHandler.removeDriveFromList();
                view.scrollToItem(listHandler.getListSize());
                createEvent("container", "updateEndTime", this);
                break;
            case "RemoveMultipleDrive":
                listHandler.removeMultipleDrive(event.getAddressString());
                view.scrollToItem(listHandler.getListSize());
                showDriveList();
                createEvent("container", "updateEndTime", this);
                break;
        }
    }
}