package com.example.jason.route_application.features.container;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 3/22/2018.
 */

public class ContainerSectionPagerAdapter extends FragmentPagerAdapter{

    private List<Fragment>fragmentList = new ArrayList<>();

    ContainerSectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
