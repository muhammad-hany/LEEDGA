package com.leedga.seagate.leedga;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

public class AnswersActivity extends AppCompatActivity {

    public static final String QUESTION_BODY="question_body";
    protected SectionsPagerAdapter mSectionsPagerAdapter;
    String categoryName;
    String testId;
    private ViewPager mViewPager;
    private Test test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        categoryName=getIntent().getStringExtra(ResultActivity.CATEGORY_KEY);


        mViewPager = (ViewPager) findViewById(R.id.container);


    }

    protected SectionsPagerAdapter getPagerAdapter() {
        return mSectionsPagerAdapter;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_answers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



   /* @Override
    public void onDataChange() {
        PlaceholderFragment fragment= (PlaceholderFragment) mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());
        fragment.fetchForQuestionBodyList();
        fragment.adapter.notifyDataSetChanged();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        gettingDataForAdapters();

    }

    private void gettingDataForAdapters() {
        int position = mViewPager.getCurrentItem();
        testId = getIntent().getStringExtra(HistoryActivity.TEST_ID_KEY);
        test = fetchTest(testId);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), test);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setCurrentItem(position);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public Test fetchTest(String testId) {
        SharedPreferences preferences = getSharedPreferences(ResultActivity.TESTS_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String jason = preferences.getString(testId, null);
        Test test1 = gson.fromJson(jason, Test.class);
        return test1;
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        Test test;

        public SectionsPagerAdapter(FragmentManager fm, Test test) {
            super(fm);
            this.test = test;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Log.i("FRAGMENT", "activity get item called");
            return AnswersFragment.newInstance(position, test, categoryName);

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "All";
                case 1:
                    return "Correct";
                case 2:
                    return "Incorrect";
                case 3:
                    return "Flagged";
            }
            return null;
        }
    }


}
