package com.leedga.seagate.leedga;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class TestActivity extends AppCompatActivity {

    TestViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pager= (TestViewPager) findViewById(R.id.pager);
        Test test= (Test) getIntent().getSerializableExtra(TestCategoriesFragment.TEST_BUNDLE);
        PagerAdapter pagerAdapter=new PagerAdapter(getSupportFragmentManager(),test);
        assert pager != null;
        pager.setAdapter(pagerAdapter);



        /*FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        TestTypeFragment test1Fragment=new TestTypeFragment();
        transaction.add(R.id.test_relative_layout,test1Fragment,"TEST1");
        transaction.commit();*/



    }


    public void setCurrentItem(int i){
        pager.setCurrentItem(i,true);
    }



}
