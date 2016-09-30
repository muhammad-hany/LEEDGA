package com.leedga.seagate.leedga;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AnswersActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Test test;
    public static final String QUESTION_BODY="question_body";
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        test= (Test) getIntent().getSerializableExtra(TestCategoriesFragment.TEST_BUNDLE);
        categoryName=getIntent().getStringExtra(ResultActivity.CATEGORY_KEY);


        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private int fragmentNumber;
        private Test test;
        private String categoryName;

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber,Test test,String categoryName) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putSerializable(TestCategoriesFragment.TEST_BUNDLE,test);
            args.putString(ResultActivity.CATEGORY_KEY,categoryName);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_answers, container, false);
            fragmentNumber=getArguments().getInt(ARG_SECTION_NUMBER);
            test= (Test) getArguments().getSerializable(TestCategoriesFragment.TEST_BUNDLE);
            categoryName=getArguments().getString(ResultActivity.CATEGORY_KEY);

            ListView listView= (ListView) rootView.findViewById(R.id.answerList);
            final List<String> questionsBodyList=new ArrayList<>();

            if (fragmentNumber==0){
                if (categoryName.equals("all")){
                    for (Question question:test.getQuestions()){
                            questionsBodyList.add(question.getQuestionBody());
                    }
                }else {
                    for (Question question:test.getQuestions()){
                        if (question.getCategory().equals(categoryName)){
                            questionsBodyList.add(question.getQuestionBody());
                        }
                    }
                }
            }else if (fragmentNumber==1){

                if (categoryName.equals("all")) {
                    for (int i = 0; i < test.getQuestions().size(); i++) {
                        if (test.getUserResult()[i]) {
                            questionsBodyList.add(test.getQuestions().get(i).getQuestionBody());

                        }
                    }
                }else {
                    if (categoryName.equals("all")){
                        for (int i = 0; i < test.getQuestions().size(); i++) {
                            if (test.getUserResult()[i]) {
                                questionsBodyList.add(test.getQuestions().get(i).getQuestionBody());

                            }
                        }
                    }else {
                        for (int i = 0; i < test.getQuestions().size(); i++) {
                            if (test.getUserResult()[i] && test.getQuestions().get(i).getCategory().equals(categoryName)) {
                                questionsBodyList.add(test.getQuestions().get(i).getQuestionBody());

                            }
                        }
                    }
                }
            }else if (fragmentNumber==2){
                if (categoryName.equals("all")){
                    for (int i=0;i<test.getQuestions().size();i++){
                        if (!test.getUserResult()[i]){
                            questionsBodyList.add(test.getQuestions().get(i).getQuestionBody());
                        }
                    }
                }else {
                    for (int i=0;i<test.getQuestions().size();i++){
                        if (!test.getUserResult()[i]&&test.getQuestions().get(i).getCategory().equals(categoryName)){
                            questionsBodyList.add(test.getQuestions().get(i).getQuestionBody());
                        }
                    }
                }
            }

            if (questionsBodyList.isEmpty()){
                TextView textView= (TextView) rootView.findViewById(R.id.noItems);
                textView.setText("no items to display");
                textView.setVisibility(View.VISIBLE);
            }
            AnswerListAdapter adapter=new AnswerListAdapter(getContext(),fragmentNumber,questionsBodyList,test.getUserResult());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //create new Activity For it
                    Intent intent=new Intent(getContext(),AnswerViewActivity.class);
                    intent.putExtra(TestCategoriesFragment.TEST_BUNDLE,test);
                    intent.putExtra(QUESTION_BODY,questionsBodyList.get(position));
                    startActivity(intent);
                }
            });



            return rootView;
        }

        class AnswerListAdapter extends ArrayAdapter<String>{
            List<String> questions;
            boolean [] results;
            int ftagmentNum;
            public AnswerListAdapter(Context context,int fragmentNum, List <String> questions, boolean
                    [] results) {
                super(context, R.layout.answer_row,questions);
                this.questions=questions;
                this.results=results;
                this.ftagmentNum=fragmentNum;
            }

            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder=null;
                if (convertView==null){
                    LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    convertView=inflater.inflate(R.layout.answer_row,null);
                    viewHolder=new ViewHolder();
                    viewHolder.image= (ImageView) convertView.findViewById(R.id.imageView);
                    viewHolder.textView= (TextView) convertView.findViewById(R.id.textView);
                    convertView.setTag(viewHolder);

                }else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                viewHolder.textView.setText(questions.get(position));
                int resultPosition=0;
                for (Question question:test.getQuestions()){
                    if (question.getQuestionBody().equals(questions.get(position))){
                        resultPosition=test.getQuestions().indexOf(question);
                    }

                }
                    if (ftagmentNum==0) {
                        if (results[resultPosition]) {
                            viewHolder.image.setImageResource(R.drawable.ic_correct);
                        } else {
                            viewHolder.image.setImageResource(R.drawable.ic_incorrect);
                        }
                    }else if (ftagmentNum==1){
                        viewHolder.image.setImageResource(R.drawable.ic_correct);
                    }else if (ftagmentNum==2){
                        viewHolder.image.setImageResource(R.drawable.ic_incorrect);
                    }

                return convertView;
            }


        }
        class ViewHolder{
            ImageView image;
            TextView textView;
        }





    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position,test,categoryName);
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
