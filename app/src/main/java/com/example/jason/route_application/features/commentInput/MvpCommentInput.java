package com.example.jason.route_application.features.commentInput;

import com.example.jason.route_application.data.database.DatabaseCallback;
import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.Session;

/**
 * Created by Jason on 2/10/2018.
 */

public interface MvpCommentInput {

    interface View{

        void updateTextViews(String employeeId, String date);

        void onAddCommentBtnClick();

        void closeActivity();

        void onStartNetworkOperation();

        void onFinishNetworkOperation();

        void showToast(String message);
    }

    interface Presenter{

        void setUpInfo(Session session, Address address);

        void onAddCommentBtnClick(String comment);
    }

    interface Interactor{
        void addCommentToAddress(
                Address address,
                String username,
                String comment,
                String date,
                DatabaseCallback.CommentInputCallBack callback);
    }
}
