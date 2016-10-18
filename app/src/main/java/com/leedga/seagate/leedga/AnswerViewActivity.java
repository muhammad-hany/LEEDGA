package com.leedga.seagate.leedga;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class AnswerViewActivity extends BaseActivity {

    public static boolean AnswerShowFlag = true;
    private Test test;
    private String questionBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_view);
        defineNavigationMenu();
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
