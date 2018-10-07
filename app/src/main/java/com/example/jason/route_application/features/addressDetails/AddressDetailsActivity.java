package com.example.jason.route_application.features.addressDetails;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.jason.route_application.R;
import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.Session;
import com.example.jason.route_application.data.pojos.database.AddressInformation;
import com.example.jason.route_application.data.pojos.CommentInformation;
import com.example.jason.route_application.features.commentDisplay.CommentDisplayActivity;
import com.example.jason.route_application.features.commentInput.CommentInputActivity;
import com.example.jason.route_application.data.models.DialogCreator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class AddressDetailsActivity extends DaggerAppCompatActivity implements
        MvpAddressDetails.View,
        AddressDetailsAdapter.CommentListFunctions,
        TimePickerDialog.OnTimeSetListener{

    @Inject
    MvpAddressDetails.Presenter presenter;

    @BindView(R.id.addressCommentsList)
    RecyclerView recyclerView;
    @BindView(R.id.streetTextView)
    TextView streetTextView;
    @BindView(R.id.postcodeTextView)
    TextView postcodeTextView;
    @BindView(R.id.cityTextView)
    TextView cityTextView;
    @BindView(R.id.addressTypeImageView)
    ImageView addressTypeImageView;
    @BindView(R.id.messageToUserTextView)
    TextView messageToUserTextView;
    @BindView(R.id.addCommentBtn)
    FloatingActionButton addCommentBtn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.typeChangeProgress_pb)
    ProgressBar typeChangeProgress_pb;
    @BindView(R.id.opening_time_tv)
    TextView openingTimeTv;
    @BindView(R.id.closing_time_tv)
    TextView closingTimeTv;
    @BindView(R.id.change_opening_time_tv)
    TextView changeOpeningTimeTv;
    @BindView(R.id.change_closing_time_tv)
    TextView changeClosingTimeTv;
    @BindView(R.id.opening_time_holder)
    TextView openingHoursHolder;
    @BindView(R.id.closing_time_holder)
    TextView closingHoursHolder;

    private String workingHours;
    private boolean returning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_details);
        ButterKnife.bind(this);
        returning = false;
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (returning) {
            presenter.getAddressInformation();
        }
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        Address address = getIntent().getParcelableExtra("address");

        streetTextView.setText(address.getStreet());
        postcodeTextView.setText(address.getPostCode());
        cityTextView.setText(address.getCity());

        addressTypeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeChangeConfirmation();
            }
        });

        if (address.isBusiness()) {
            openingTimeTv.setVisibility(View.VISIBLE);
            changeOpeningTimeTv.setVisibility(View.VISIBLE);
            closingTimeTv.setVisibility(View.VISIBLE);
            changeClosingTimeTv.setVisibility(View.VISIBLE);
            openingHoursHolder.setVisibility(View.VISIBLE);
            closingHoursHolder.setVisibility(View.VISIBLE);
            openingTimeTv.setText(presenter.convertTime(address.getOpeningTime()));
            closingTimeTv.setText(presenter.convertTime(address.getClosingTime()));
            addressTypeImageView.setImageResource(R.drawable.business_ic_white);
        }

        presenter.setInfo(new Session(this), address);
        presenter.getAddressInformation();
    }

    @Override
    public void setUpAdapter(AddressInformation addressInformation) {
        messageToUserTextView.setVisibility(View.GONE);
        AddressDetailsAdapter adapter = new AddressDetailsAdapter(addressInformation, this);
        recyclerView.setAdapter(adapter);
    }

    private void typeChangeConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setTitle("Change address type")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        addressTypeImageView.setVisibility(View.INVISIBLE);
                        typeChangeProgress_pb.setVisibility(View.VISIBLE);
                        presenter.changeAddressType();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void changeAddressType(Address address) {

        if(address.isBusiness()){
            addressTypeImageView.setImageResource(R.drawable.business_ic_white);
            openingTimeTv.setText(presenter.convertTime(address.getOpeningTime()));
            closingTimeTv.setText(presenter.convertTime(address.getClosingTime()));
            openingHoursHolder.setVisibility(View.VISIBLE);
            closingHoursHolder.setVisibility(View.VISIBLE);
            openingTimeTv.setVisibility(View.VISIBLE);
            changeOpeningTimeTv.setVisibility(View.VISIBLE);
            closingTimeTv.setVisibility(View.VISIBLE);
            changeClosingTimeTv.setVisibility(View.VISIBLE);
        }else{
            addressTypeImageView.setImageResource(R.drawable.home_ic_white);
            openingHoursHolder.setVisibility(View.GONE);
            closingHoursHolder.setVisibility(View.GONE);
            openingTimeTv.setVisibility(View.GONE);
            changeOpeningTimeTv.setVisibility(View.GONE);
            closingTimeTv.setVisibility(View.GONE);
            changeClosingTimeTv.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.googleSearchBtn)
    public void onGoogleLinkClick() {
        presenter.googleLinkClick();
    }

    @OnClick(R.id.change_opening_time_tv)
    public void onChangeOpeningHoursClick(){
        workingHours = "open";
        DialogFragment timePicker = new DialogCreator();
        timePicker.show(getSupportFragmentManager(), "Time Picker");
    }

    @OnClick(R.id.change_closing_time_tv)
    public void onOChangeClosingHoursClick(){
        workingHours = "close";
        DialogFragment timePicker = new DialogCreator();
        timePicker.show(getSupportFragmentManager(), "Time Picker");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        switch (workingHours){
            case "open" : openingTimeTv.setText(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
                break;
            case "close" : closingTimeTv.setText(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
        }
        presenter.changeOpeningHours(hourOfDay, minute, workingHours);
    }

    @Override
    public void onListItemClick(CommentInformation commentInformation) {
        showCommentDisplay(commentInformation);
    }

    @OnClick(R.id.addCommentBtn)
    public void onAddCommentButtonClick() {
        presenter.addCommentButtonClick();
    }

    @Override
    public void showAddressInGoogle(Address address) {
        String url = "http://www.google.com/search?q=" + address.getStreet() + " " + address.getCity();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void updateMessageToUserTextView(String message) {
        messageToUserTextView.setText(message);
    }

    @Override
    public void networkOperationStarted() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void networkOperationFinish() {
        progressBar.setVisibility(View.GONE);
        typeChangeProgress_pb.setVisibility(View.GONE);
        addressTypeImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCommentDisplay(CommentInformation commentInformation) {
        returning = false;
        Intent i = new Intent(this, CommentDisplayActivity.class);
        i.putExtra("commentInformation", commentInformation);
        startActivity(i);
    }

    @Override
    public void showCommentInput(Address address) {
        returning = true;
        Intent i = new Intent(this, CommentInputActivity.class);
        i.putExtra("address", address);
        startActivity(i);
    }

    @Override
    public void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void closeActivity() {
        finish();
    }
}
