package com.leedga.seagate.leedga;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class TestActivity extends AppCompatActivity{

    TestViewPager pager;
    PagerAdapter pagerAdapter;
    private Test test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        pager= (TestViewPager) findViewById(R.id.pager);
        test= (Test) getIntent().getSerializableExtra(TestCategoriesFragment.TEST_BUNDLE);

        pagerAdapter=new PagerAdapter(getSupportFragmentManager(),test,PagerAdapter.TEST);
        assert pager != null;
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(3);




    }


    protected int getCurrentFragmentPosition(){
        return pager.getCurrentItem();
    }

    public void setCurrentItem(int i){
        pager.setCurrentItem(i,true);
    }





}
