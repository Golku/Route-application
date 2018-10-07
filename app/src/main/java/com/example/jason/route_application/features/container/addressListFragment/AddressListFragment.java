package com.example.jason.route_application.features.container.addressListFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jason.route_application.R;
import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.Event;
import com.example.jason.route_application.data.pojos.RouteInfoHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressListFragment extends Fragment implements MvpAddressList.View{

    @BindView(R.id.address_list)
    RecyclerView recyclerView;

    private final String debugTag = "debugTag";

    private MvpAddressList.Presenter presenter;

    private AlertDialog alertDialog;
    private TextView dialogTitle;
    private EditText streetInput;
    private EditText postcodeLettersInput;
    private EditText postcodeNumbersInput;
    private EditText cityInput;
    private Button cancelDialogBtn;
    private Button addAddressBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        RouteInfoHolder routeInfoHolder = getArguments().getParcelable("routeInfoHolder");
        presenter = new AddressListPresenter(this, routeInfoHolder.getAddressList());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupAddressInputDialog();
        presenter.showAddressList();
    }

    @Override
    public void setupAdapter(AddressListAdapter adapter) {
        adapter.addContext(this.getContext());
        adapter.addTouchHelper(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    private void setupAddressInputDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_address_input, null);

        dialogTitle = view.findViewById(R.id.title_tv);
        streetInput = view.findViewById(R.id.street_input);
        postcodeNumbersInput = view.findViewById(R.id.postcode_numbers_input);
        postcodeLettersInput = view.findViewById(R.id.postcode_letters_input);
        cityInput = view.findViewById(R.id.city_input);
        addAddressBtn = view.findViewById(R.id.address_input_btn);
        cancelDialogBtn = view.findViewById(R.id.cancel_dialog_btn);

        alertDialogBuilder.setView(view);
        alertDialog = alertDialogBuilder.create();
    }

    @Override
    public void showAddressInputDialog(String title) {

        this.dialogTitle.setText(title);

        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String address = streetInput.getText().toString()+", "+
                        postcodeNumbersInput.getText().toString()+" "+
                        postcodeLettersInput.getText().toString()+" "+
                        cityInput.getText().toString();

                alertDialog.dismiss();
                presenter.processAddress(address);
            }
        });

        cancelDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void addressDeleted(final int position, final Address address) {
        Snackbar snackbar = Snackbar.make(recyclerView, "Address deleted", Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.restoreAddress();
                recyclerView.smoothScrollToPosition(position);
            }
        });
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                presenter.removeAddressFromContainer(address);
            }
        });
        snackbar.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEvent(Event event){
        presenter.eventReceived(event);
    }

    @Override
    public void postEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    @Override
    public void scrollToItem(int position) {
        recyclerView.smoothScrollToPosition(position);
    }

    @Override
    public void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
