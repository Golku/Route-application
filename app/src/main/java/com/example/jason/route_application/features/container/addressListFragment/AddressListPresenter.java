package com.example.jason.route_application.features.container.addressListFragment;

import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.Event;
import com.example.jason.route_application.data.pojos.api.ChangeAddressRequest;
import com.example.jason.route_application.features.shared.BasePresenter;
import com.example.jason.route_application.features.shared.MvpBasePresenter;

import android.util.Log;

import java.util.List;

public class AddressListPresenter extends BasePresenter implements
        MvpBasePresenter,
        MvpAddressList.Presenter,
        AddressListAdapter.AdapterCallback{

    private final String debugTag = "debugTag";

    private MvpAddressList.View view;

    private List<Address> addressList;
    private AddressListAdapter adapter;

    private boolean newAddress;
    private int changeAddressPosition;

    private int deletedItemPosition;
    private Address deletedAddress;

    AddressListPresenter(MvpAddressList.View view, List<Address> addressList) {
        this.view = view;
        this.addressList = addressList;
    }

    @Override
    public void showAddressList() {
        adapter = new AddressListAdapter(this, addressList);
        view.setupAdapter(adapter);
    }

    @Override
    public void showDialog(String title) {
        view.showAddressInputDialog(title);
    }

    @Override
    public void itemClick(Address address) {
        if (address.isValid()) {
            createEvent("container", "itemClick", address,this);
        }else{
            newAddress = false;
            changeAddressPosition = addressList.indexOf(address);
            showDialog(address.getAddress());
        }
    }

    @Override
    public void processAddress(String addressString) {
        if(addressString.isEmpty()){//not working FIX THIS!!
            view.showToast("Fill in a address");
            return;
        }

        if (newAddress) {
            getAddress(addressString+", Netherlands");
        }else{
            changeAddress(addressString+", Netherlands");
        }
    }

    @Override
    public void showAddress(Address address) {
        createEvent("container", "showMap", this);
        createEvent("mapFragment", "showMarker", address, this);
    }

    private void getAddress(String address) {
        createEvent("container", "getAddress", address,this);
    }

    private void changeAddress(String address){
        ChangeAddressRequest request = new ChangeAddressRequest(addressList.get(changeAddressPosition).getAddress(), address);
        createEvent("container", "changeAddress", request,this);
    }

    @Override
    public void removeAddress(Address address) {
        deletedItemPosition = addressList.indexOf(address);
        deletedAddress = address;
        adapter.notifyItemRemoved(addressList.indexOf(address));
        addressList.remove(address);
        view.addressDeleted(deletedItemPosition, address);
        createEvent("mapFragment", "removeMarker", address,this);
    }

    @Override
    public void restoreAddress() {
        addressList.add(deletedItemPosition, deletedAddress);
        adapter.notifyItemInserted(deletedItemPosition);
    }

    @Override
    public void removeAddressFromContainer(Address address) {
        createEvent("container", "removeAddress", address,this);
    }

    @Override
    public void eventReceived(Event event) {

        if(!(event.getReceiver().equals("addressFragment") || event.getReceiver().equals("all"))){
            return;
        }

        Log.d(debugTag, "Event received on addressFragment: "+ event.getEventName());

        switch (event.getEventName()) {
            case "showDialog" :
                newAddress = true;
                showDialog("New Address");
                break;
            case "addressTypeChange" : addressTypeChange(event.getAddress());
                break;
            case "openingTimeChange" : openingTimeChange(event.getAddress());
                break;
            case "closingTimeChange" : closingTimeChange(event.getAddress());
                break;
            case "updateList" : updateList(event.getAddressList());
                break;
            case "addAddress" : addAddress(event.getAddress());
                break;
            case "replaceAddress" : replaceAddress(event.getAddress());
                break;
        }
    }

    private void addressTypeChange(Address address){
        for(Address it: addressList){
            if(it.getAddress().equals(address.getAddress())){
                if(address.isBusiness()){
                    it.setBusiness(true);
                }else{
                    it.setBusiness(false);
                }
                showAddressList();
                break;
            }
        }
    }

    private void openingTimeChange(Address address) {
        for(Address it: addressList){
            if(it.getAddress().equals(address.getAddress())){
                it.setOpeningTime(address.getOpeningTime());
                break;
            }
        }
    }

    private void closingTimeChange(Address address) {
        for(Address it: addressList){
            if(it.getAddress().equals(address.getAddress())){
                it.setClosingTime(address.getClosingTime());
                break;
            }
        }
    }

    private void updateList(List<Address> addressList){
        this.addressList = addressList;
        showAddressList();
    }

    private void addAddress(Address address){
        boolean notFound = true;
        for(Address it : addressList){
            if(it.getAddress().equals(address.getAddress())){
                it.setPackageCount(it.getPackageCount()+1);
                notFound = false;
                break;
            }
        }
        if(notFound){
            addressList.add(address);
            adapter.notifyItemInserted(addressList.indexOf(address));
            view.scrollToItem(addressList.size());
            createEvent("mapFragment","markAddress", address, this);
        }
    }

    private void replaceAddress(Address address){
        boolean notFound = true;
        for(Address it : addressList){
            if(it.getAddress().equals(address.getAddress())){
                it.setPackageCount(it.getPackageCount()+1);
                addressList.remove(changeAddressPosition);
                adapter.notifyItemRemoved(changeAddressPosition);
                notFound = false;
                break;
            }
        }
        if(notFound){
            addressList.set(changeAddressPosition, address);
            adapter.notifyItemChanged(changeAddressPosition);
            view.scrollToItem(changeAddressPosition);
            createEvent("mapFragment","markAddress", address, this);
        }
    }

    @Override
    public void publishEvent(Event event) {
        view.postEvent(event);
    }
}
