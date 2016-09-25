package com.leedga.seagate.leedga;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class TestSettingActivity extends AppCompatActivity implements FragmentTypeListener {

    PagerAdapter adapter;
    TestViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_type);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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




}
