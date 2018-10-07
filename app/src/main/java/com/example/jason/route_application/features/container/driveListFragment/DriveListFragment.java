package com.example.jason.route_application.features.container.driveListFragment;

import com.example.jason.route_application.R;
import com.example.jason.route_application.data.pojos.Event;
import com.example.jason.route_application.data.pojos.RouteInfoHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jason on 3/22/2018.,
 */

public class DriveListFragment extends Fragment implements MvpDriveList.View{

    @BindView(R.id.route_list)
    RecyclerView recyclerView;

    private final String debugTag = "debugTag";

    private MvpDriveList.Presenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        RouteInfoHolder routeInfoHolder = getArguments().getParcelable("routeInfoHolder");
        presenter = new DriveListPresenter(this, routeInfoHolder.getDriveList());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drive_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.showDriveList();
    }

    @Override
    public void setupAdapter(DriveListAdapter adapter) {
        adapter.addContext(this.getContext());
        adapter.addTouchHelper(recyclerView);
        recyclerView.setAdapter(adapter);
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