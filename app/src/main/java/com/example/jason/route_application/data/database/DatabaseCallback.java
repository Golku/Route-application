package com.example.jason.route_application.data.database;

import com.example.jason.route_application.data.pojos.database.AddressInformationResponse;
import com.example.jason.route_application.data.pojos.database.AddressTypeResponse;
import com.example.jason.route_application.data.pojos.database.CommentInputResponse;
import com.example.jason.route_application.data.pojos.database.LoginResponse;

/**
 * Created by Jason on 19-Feb-18.
 */

public interface DatabaseCallback {

    interface LoginCallBack{
        void onLoginResponse(LoginResponse response);
        void onLoginResponseFailure();
    }

    interface AddressInformationCallBack{
        void onAddressInformationResponse(AddressInformationResponse response);
        void onAddressInformationResponseFailure();
    }

    interface AddressTypeChangeCallback{
        void typeChangeResponse(AddressTypeResponse response);
        void typeChangeResponseFailure();
    }

    interface CommentInputCallBack{
        void onCommentInputResponse(CommentInputResponse response);
        void onCommentInputResponseFailure();
    }
}

