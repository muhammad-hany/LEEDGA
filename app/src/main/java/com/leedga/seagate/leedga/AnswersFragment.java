package com.leedga.seagate.leedga;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.leedga.seagate.leedga.AnswersActivity.QUESTION_BODY;

/**
 * Created by Muhammad Workstation on 02/10/2016.
 */

public class AnswersFragment extends Fragment implements listCallback {


    public static final String RADIO_DATASET_CHANGED = "com.yourapp.app.RADIO_DATASET_CHANGED";
    private static final String ARG_SECTION_NUMBER = "section_number";
    public AnswerListAdapter adapter;
    List<String> questionsBodyList;
    private int fragmentNumber;
    private Test test;
    private String categoryName;
    /*public Test testId;*/


    public AnswersFragment() {
    }


    public static Fragment newInstance(int sectionNumber, Test test, String categoryName) {
        Log.i("FRAGMENT", "Answer init called");
        AnswersFragment fragment = new AnswersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(TestCategoriesFragment.TEST_BUNDLE, test);
        args.putString(ResultActivity.CATEGORY_KEY, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        testId=getArguments().getString(TestCategoriesFragment.TEST_BUNDLE);
        test=fetchTest(testId);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("FRAGMENT", "Answer oncreateView called");
        final View rootView = inflater.inflate(R.layout.fragment_answers, container, false);
        fragmentNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        test = (Test) getArguments().getSerializable(TestCategoriesFragment.TEST_BUNDLE);
        /*test=fetchTest(testId);*/
        categoryName = getArguments().getString(ResultActivity.CATEGORY_KEY);

        ListView listView = (ListView) rootView.findViewById(R.id.answerList);
        questionsBodyList = fetchForQuestionBodyList();

        if (questionsBodyList.isEmpty()) {
            TextView textView = (TextView) rootView.findViewById(R.id.noItems);
            textView.setText("no items to display");
            textView.setVisibility(View.VISIBLE);
        }

        adapter = new AnswerListAdapter(getContext(), fragmentNumber, questionsBodyList, test.getUserResult());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), AnswerViewActivity.class);
                intent.putExtra(TestCategoriesFragment.TEST_BUNDLE, test);
                intent.putExtra(QUESTION_BODY, questionsBodyList.get(position));
                startActivity(intent);
            }
        });


        return rootView;
    }

    private List<String> fetchForQuestionBodyList() {
        List<String> questionsBodyList = new ArrayList<>();
        //TODO check that logic again !!!!!
        if (fragmentNumber == 0) {
            if (categoryName.equals("all")) {
                for (Question question : test.getAnsweredQuestions()) {
                    questionsBodyList.add(question.getQuestionBody());
                }
            } else {
                for (Question question : test.getAnsweredQuestions()) {
                    if (question.getCategory().equals(categoryName)) {
                        questionsBodyList.add(question.getQuestionBody());
                    }
                }
            }
        } else if (fragmentNumber == 1) {

            if (categoryName.equals("all")) {
                for (int i = 0; i < test.getAnsweredQuestions().size(); i++) {
                    if (test.getUserResult().get(i)) {
                        questionsBodyList.add(test.getAnsweredQuestions().get(i).getQuestionBody());

                    }
                }
            } else {
                for (int i = 0; i < test.getAnsweredQuestions().size(); i++) {
                    if (test.getUserResult().get(i) && test.getAnsweredQuestions().get(i).getCategory().equals(categoryName)) {
                        questionsBodyList.add(test.getAnsweredQuestions().get(i).getQuestionBody());

                    }
                }

            }
        } else if (fragmentNumber == 2) {
            if (categoryName.equals("all")) {
                for (int i = 0; i < test.getAnsweredQuestions().size(); i++) {
                    if (!test.getUserResult().get(i)) {
                        questionsBodyList.add(test.getAnsweredQuestions().get(i).getQuestionBody());
                    }
                }
            } else {
                for (int i = 0; i < test.getAnsweredQuestions().size(); i++) {
                    if (!test.getUserResult().get(i) && test.getAnsweredQuestions().get(i).getCategory().equals(categoryName)) {
                        questionsBodyList.add(test.getAnsweredQuestions().get(i).getQuestionBody());
                    }
                }
            }
        } else {
            if (categoryName.equals("all")) {
                for (int i = 0; i < test.getAnsweredQuestions().size(); i++) {
                    if (test.getAnsweredQuestions().get(i).isFlagged()) {
                        questionsBodyList.add(test.getAnsweredQuestions().get(i).getQuestionBody());
                    }
                }
            } else {
                for (int i = 0; i < test.getAnsweredQuestions().size(); i++) {
                    if (test.getAnsweredQuestions().get(i).isFlagged() && test.getAnsweredQuestions().get(i).getCategory().equals(categoryName)) {
                        questionsBodyList.add(test.getAnsweredQuestions().get(i).getQuestionBody());
                    }
                }
            }
        }
        return questionsBodyList;
    }

    @Override
    public void onDataChange() {
        questionsBodyList = fetchForQuestionBodyList();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void setTest(Test test) {
        this.test = test;
    }

   /* @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback= (listCallback) activity;
    }*/

    class AnswerListAdapter extends ArrayAdapter<String> {
        List<String> questions;
        ArrayList<Boolean> results;
        int ftagmentNum;

        public AnswerListAdapter(Context context, int fragmentNum, List<String> questions,
                                 ArrayList<Boolean> results) {
            super(context, R.layout.answer_row, questions);
            this.questions = questions;
            this.results = results;
            this.ftagmentNum = fragmentNum;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            Log.i("FRAGMENT", "AnswerListAdapter getView called");
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.answer_row, null);
                viewHolder = new ViewHolder();
                viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
                viewHolder.flagImage = (ImageView) convertView.findViewById(R.id.flag);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.textView.setText(questions.get(position));
            int resultPosition = 0;
            for (Question question : test.getAnsweredQuestions()) {
                if (question.getQuestionBody().equals(questions.get(position))) {
                    resultPosition = test.getAnsweredQuestions().indexOf(question);
                }

            }
            if (ftagmentNum == 0 || ftagmentNum == 3) {
                if (results.get(resultPosition)) {
                    viewHolder.image.setImageResource(R.drawable.ic_correct);
                } else {
                    viewHolder.image.setImageResource(R.drawable.ic_incorrect);
                }

            } else if (ftagmentNum == 1) {
                viewHolder.image.setImageResource(R.drawable.ic_correct);
            } else if (ftagmentNum == 2) {
                viewHolder.image.setImageResource(R.drawable.ic_incorrect);
            }

            if (test.getAnsweredQuestions().get(resultPosition).isFlagged()) {
                viewHolder.flagImage.setVisibility(View.VISIBLE);
            } else {
                viewHolder.flagImage.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }


    }

    class ViewHolder {
        ImageView image;
        TextView textView;
        ImageView flagImage;
    }


}
