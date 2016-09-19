package com.leedga.seagate.leedga;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {


    private Test test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle=getIntent().getBundleExtra(TestCategoriesFragment.TEST_BUNDLE);
        test= (Test) bundle.getSerializable(TestCategoriesFragment.TEST_BUNDLE);

        TextView score= (TextView) findViewById(R.id.score);
        TextView precentage= (TextView) findViewById(R.id.precentage);
        assert score != null;
        score.setText(String.valueOf(calculateScore())+"/"+test.getNumberOfQuestions());
        assert precentage != null;
        precentage.setText(String.valueOf(calculatePrecentage())+"%");
        java.text.DateFormat format=new SimpleDateFormat("MMM dd, yyyy h:mm a");
        String date=format.format(new Date());
        TextView dateTextView= (TextView) findViewById(R.id.date);
        assert dateTextView != null;
        dateTextView.setText(date);




    }

    private int calculatePrecentage() {
        double x=calculateScore();
        double y=test.getNumberOfQuestions();
        double r=(x/y)*100;
        return (int) r;
    }

    private int calculateScore() {
        int scoreCount=0;
        for (int i=0;i<test.getUserResult().length;i++){
            if (test.getUserResult()[i]){
                scoreCount++;
            }
        }

        return scoreCount;
    }

}
