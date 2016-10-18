package com.leedga.seagate.leedga;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class TestSettingActivity extends BaseActivity implements FragmentListener {

    PagerAdapter adapter;
    TestViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_type);
        defineNavigationMenu();


        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        TestTypeFragment test1Fragment=new TestTypeFragment();
        transaction.add(R.id.test_relative_layout,test1Fragment);
        transaction.commit();


        pager= (TestViewPager) findViewById(R.id.pager);
        adapter=new PagerAdapter(getSupportFragmentManager(),null,PagerAdapter.TYPE);
    }


    @Override
    public void testToActivity(Test test) {
        TestCategoriesFragment fragment= (TestCategoriesFragment) adapter.getItem(1);
        fragment.setTest(test);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 5) {
            setResult(5);
            finish();
        }
    }




}
