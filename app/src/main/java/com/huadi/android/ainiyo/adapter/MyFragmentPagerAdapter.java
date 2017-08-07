package com.huadi.android.ainiyo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.frag.MovementFragment;
import com.huadi.android.ainiyo.frag.ChooseFragment;
import com.huadi.android.ainiyo.frag.FindingFragment;
import com.huadi.android.ainiyo.frag.MeFragment;
import com.huadi.android.ainiyo.frag.ModeFragment;

/**
 * Created by geange on 8/4/17.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{

    private final int PAGER_COUNT = 5;
    private Fragment myFragment1 = null;
    private Fragment myFragment2 = null;
    private Fragment myFragment3 = null;
    private Fragment myFragment4 = null;
    private Fragment myFragment5 = null;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        myFragment1 = new FindingFragment();
        myFragment2 = new ChooseFragment();
        myFragment3 = new ModeFragment();
        myFragment4 = new MovementFragment();
        myFragment5 = new MeFragment();
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = myFragment1;
                break;
            case MainActivity.PAGE_TWO:
                fragment = myFragment2;
                break;
            case MainActivity.PAGE_THREE:
                fragment = myFragment3;
                break;
            case MainActivity.PAGE_FOUR:
                fragment = myFragment4;
                break;
            case MainActivity.PAGE_FIVE:
                fragment = myFragment5;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
