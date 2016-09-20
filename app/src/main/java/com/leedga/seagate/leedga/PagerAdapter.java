package com.leedga.seagate.leedga;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Muhammad Workstation on 20/09/2016.
 */


public class PagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Question> questions;
    Test test;

    public PagerAdapter(FragmentManager fm , Test test) {
        super(fm);
        this.test=test;

    }

    @Override
    public Fragment getItem(int position) {
        return TestFragment.init(test,position);
    }

    @Override
    public int getCount() {
        return test.getNumberOfQuestions();
    }

}
