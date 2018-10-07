package com.example.jason.route_application.features.shared;

import com.example.jason.route_application.data.pojos.Event;

public interface MvpBasePresenter {

    void publishEvent(Event event);
}
