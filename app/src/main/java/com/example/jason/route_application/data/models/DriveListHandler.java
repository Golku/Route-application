package com.example.jason.route_application.data.models;

import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.api.Drive;
import com.example.jason.route_application.features.container.driveListFragment.DriveListAdapter;
import com.example.jason.route_application.features.container.driveListFragment.DriveListPresenter;

import java.text.SimpleDateFormat;
import java.util.List;

public class DriveListHandler {

    private List<Drive> driveList;
    private DriveListAdapter adapter;
    private DriveListPresenter callback;

    public DriveListHandler(List<Drive> driveList, DriveListPresenter callback) {
        this.driveList = driveList;
        this.callback = callback;
    }

    public void createAdapter(){
        adapter = new DriveListAdapter(callback, driveList);
    }

    public DriveListAdapter getAdapter(){
        return adapter;
    }

    public void updateList(List<Drive> driveList) {
        this.driveList = driveList;
        createAdapter();
    }

    public int getListSize(){
        return driveList.size();
    }

    public void addressTypeChange(Address address) {

        for (Drive drive : driveList) {

            if (drive.getDestinationAddress().getAddress().equals(address.getAddress())) {

                if (address.isBusiness()) {
                    drive.setDestinationIsABusiness(true);
                    drive.getDestinationAddress().setBusiness(true);
                } else {
                    drive.setDestinationIsABusiness(false);
                    drive.getDestinationAddress().setBusiness(false);
                }

                break;
            }
        }
    }

    public void addDriveToList(Drive drive) {
        driveList.add(drive);

        long deliveryTime;
        long driveTime = drive.getDriveDurationInSeconds() * 1000;
        long PACKAGE_DELIVERY_TIME = 120000;
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");

        if (driveList.size() > 1) {
            Drive previousDrive = driveList.get(driveList.indexOf(drive) - 1);
            deliveryTime = previousDrive.getDeliveryTimeInMillis() + driveTime + PACKAGE_DELIVERY_TIME;
        } else {
            long date = System.currentTimeMillis();
            deliveryTime = date + driveTime + PACKAGE_DELIVERY_TIME;
        }

        String deliveryTimeString = sdf.format(deliveryTime);

        drive.setPosition(driveList.size());
        drive.setDeliveryTimeInMillis(deliveryTime);
        drive.setDeliveryTimeHumanReadable(deliveryTimeString);

        adapter.notifyItemInserted(driveList.indexOf(drive));
    }

    public void driveCompleted(Drive drive){
        drive.setDone(1);
    }

    public void removeDriveFromList() {
        int position = driveList.size() - 1;
        driveList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void removeMultipleDrive(String address) {

        for (Drive drive : driveList) {
            if (address.equals(drive.getDestinationAddress().getAddress())) {
                driveList.subList(driveList.indexOf(drive), driveList.size()).clear();
                break;
            }
        }
    }
}
