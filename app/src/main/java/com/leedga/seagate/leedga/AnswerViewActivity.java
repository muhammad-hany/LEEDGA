package com.leedga.seagate.leedga;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class AnswerViewActivity extends AppCompatActivity {

    public static boolean AnswerShowFlag = true;
    private Test test;
    private String questionBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        test= (Test) getIntent().getSerializableExtra(TestCategoriesFragment.TEST_BUNDLE);
        questionBody=getIntent().getStringExtra(AnswersActivity.QUESTION_BODY);

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.relative,TestFragment.init(test,fetchPosition()));
        transaction.commit();
    }

    private int  fetchPosition() {
        int i=0;
        for (Question question : test.getAnsweredQuestions()) {
            if (question.getQuestionBody().equals(questionBody)) return i;
            i++;
        }
        return -1;

    }


}
