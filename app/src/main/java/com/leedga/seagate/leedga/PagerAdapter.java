package com.leedga.seagate.leedga;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Muhammad Workstation on 20/09/2016.
 */


public class PagerAdapter extends FragmentStatePagerAdapter {

    private Test test;
    public static final int TYPE=0;
    public static final int TEST=1;
    private int fragmentType;

    TestTypeFragment testTypeFragment;
    TestCategoriesFragment testCategoriesFragment;

    public PagerAdapter(FragmentManager fm , Test test,int fragmentType) {
        super(fm);
        this.test=test;
        this.fragmentType=fragmentType;
        testCategoriesFragment=new TestCategoriesFragment();
        testTypeFragment=new TestTypeFragment();

    }

    @Override
    public Fragment getItem(int position) {

        if (fragmentType==TEST) {
            return TestFragment.init(test, position);
        }else {
            switch (position){
                case 0: return testTypeFragment;
                case 1: return testCategoriesFragment;
            }

            return null;
        }
    }

    @Override
    public int getCount() {
        if (fragmentType==TEST) {
            return test.getNumberOfQuestions();
        }else {
            return 2;
        }
    }

}
