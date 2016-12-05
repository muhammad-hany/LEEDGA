package com.leedga.seagate.leedga;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AnswerViewActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean AnswerShowFlag = true;
    private Test test;
    private String questionBody;
    private LinearLayout explain;
    private TestFragment testFragment;
    private boolean isExplainOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Question Answer");
        test= (Test) getIntent().getSerializableExtra(TestCategoriesFragment.TEST_BUNDLE);
        questionBody=getIntent().getStringExtra(AnswersActivity.QUESTION_BODY);

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.container, TestFragment.init(test, fetchPosition()), REF.TEST_FRAGMENT_TAG);
        transaction.commit();

        explain = (LinearLayout) findViewById(R.id.explain);
        explain.setOnClickListener(this);
        if (test.getQuestions().get(fetchPosition()).getNote() != null) {
            explain.setVisibility(View.VISIBLE);
        } else {
            explain.setVisibility(View.GONE);
        }
    }

    public void setExplainText(boolean isExplainOn) {
        if (isExplainOn) {
            ((TextView) explain.findViewById(R.id.explainText)).setText("Hide Explanation");
        } else {
            ((TextView) explain.findViewById(R.id.explainText)).setText("Show Explanation");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int  fetchPosition() {
        int i=0;
        for (Question question : test.getAnsweredQuestions()) {
            if (question.getQuestionBody().equals(questionBody)) return i;
            i++;
        }
        return -1;

    }


    @Override
    public void onClick(View v) {
        testFragment = (TestFragment) getSupportFragmentManager().findFragmentByTag(REF.TEST_FRAGMENT_TAG);
        switch (v.getId()) {
            case R.id.explain:
                isExplainOn = !isExplainOn;
                setExplainText(isExplainOn);
                if (isExplainOn) {
                    testFragment.displayExplanations();


                } else {
                    testFragment.hideExplanations();
                }

        }
    }
}
