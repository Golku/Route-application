package com.example.jason.route_application.data.models;

import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.features.container.addressListFragment.AddressListAdapter;
import com.example.jason.route_application.features.container.addressListFragment.AddressListPresenter;
import java.util.List;

public class AddressListHandler {

    private List<Address> addressList;
    private AddressListAdapter adapter;
    private AddressListPresenter callback;

    private boolean newAddress;
    private int changeAddressPosition;
    private int deletedItemPosition;
    private Address deletedAddress;

    public AddressListHandler(List<Address> addressList, AddressListPresenter callback) {
        this.addressList = addressList;
        this.callback = callback;
    }

    private void createAdapter(){
        adapter = new AddressListAdapter(callback, addressList);
    }

    public AddressListAdapter getAdapter(){
        return adapter;
    }

    public void updateList(List<Address> addressList) {
        this.addressList = addressList;
        createAdapter();
    }

    public int getListSize(){
        return addressList.size();
    }

}
