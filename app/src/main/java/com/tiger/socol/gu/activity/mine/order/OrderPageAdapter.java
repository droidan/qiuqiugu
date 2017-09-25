package com.tiger.socol.gu.activity.mine.order;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class OrderPageAdapter extends FragmentPagerAdapter {

    private OrderTypeFragment zhiFragment;
    private OrderTypeFragment faFragment;
    private OrderTypeFragment shouFragment;
    private OrderTypeFragment pingFragment;
    private OrderTypeFragment tuiFragment;

    public OrderPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (zhiFragment == null) {
                    zhiFragment = OrderTypeFragment.newInstance(0);
                }
                return zhiFragment;

            case 1:
                if (faFragment == null) {
                    faFragment = OrderTypeFragment.newInstance(1);
                }
                return faFragment;

            case 2:
                if (shouFragment == null) {
                    shouFragment = OrderTypeFragment.newInstance(2);
                }
                return shouFragment;

            case 3:
                if (pingFragment == null) {
                    pingFragment = OrderTypeFragment.newInstance(3);
                }
                return pingFragment;

            case 4:
                if (tuiFragment == null) {
                    tuiFragment = OrderTypeFragment.newInstance(4);
                }
                return tuiFragment;

        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

}
