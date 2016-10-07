package com.leedga.seagate.leedga;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.google.gson.Gson;

import java.util.Date;
import java.util.UUID;

import static com.leedga.seagate.leedga.ResultActivity.TESTS_PREFS;

public class TestActivity extends AppCompatActivity{

    TestViewPager pager;
    PagerAdapter pagerAdapter;
    private Test test;
    private AlertDialog dialog;

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

    private void buildAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String positiveText, message, title = null;
        if (pager.getCurrentItem() == 0) {
            message = "Are you sure ?";
            positiveText = "Yes";
        } else {
            if (pager.getCurrentItem() == 1) {
                title = "You only answered one question";
            } else {
                title = "You only answered " + pager.getCurrentItem() + "questions";
            }
            message = "Do you want to save this exam result in History ?";
            positiveText = "Save";
        }
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (pager.getCurrentItem() != 0) {
                    TestFragment fragment = (TestFragment) pagerAdapter.instantiateItem(pager, pager.getCurrentItem());
                    Test test = fragment.getTest();
                    savingTestInMemmory(test);
                }
                dialog.dismiss();
                onBackPressed();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        dialog = builder.create();
    }

    private void savingTestInMemmory(Test test) {
        String ratio = String.valueOf(calculateScore()) + "/" + test.getNumberOfAnsweredQuestions();
        test.setSavingDate(new Date());
        test.setSaved(true);
        test.setRatio(ratio);
        test.setTestPercentage(String.valueOf(calculatePrecentage()));
        SharedPreferences prefs = getSharedPreferences(TESTS_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String testId = UUID.randomUUID().toString();
        test.setTestId(testId);
        String json = gson.toJson(test);
        editor.putString(testId, json);
        editor.apply();
    }

    private int calculatePrecentage() {
        double x = calculateScore();
        double y = test.getNumberOfAnsweredQuestions();
        double r = (x / y) * 100;
        return (int) r;
    }

    private int calculateScore() {
        int scoreCount = 0;
        for (int i = 0; i < test.getUserResult().size(); i++) {
            if (test.getUserResult().get(i)) {
                scoreCount++;
            }
        }

        return scoreCount;
    }


    protected int getCurrentFragmentPosition(){
        return pager.getCurrentItem();
    }

    public void setCurrentItem(int i){
        pager.setCurrentItem(i,true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        buildAlertDialog();
        dialog.show();

        return super.onKeyDown(keyCode, event);
    }
}
