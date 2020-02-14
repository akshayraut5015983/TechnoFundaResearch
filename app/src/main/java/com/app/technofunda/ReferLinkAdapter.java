package com.app.technofunda;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.technofunda.fragment.Commodity;
import com.app.technofunda.fragment.Futures;
import com.app.technofunda.fragment.Equity;
import com.app.technofunda.fragment.Optional;

public class ReferLinkAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ReferLinkAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Equity tab1 = new Equity();
                return tab1;
            case 1:
                Futures tab2 = new Futures();
                return tab2;
            case 2:
                Commodity tab3 = new Commodity();
                return tab3;
            case 3:
                Optional tab4 = new Optional();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}