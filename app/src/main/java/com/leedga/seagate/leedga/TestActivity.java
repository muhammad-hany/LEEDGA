package com.leedga.seagate.leedga;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.Menu;

import com.google.gson.Gson;

import java.util.Date;
import java.util.UUID;

import static com.leedga.seagate.leedga.ResultActivity.TESTS_PREFS;

public class TestActivity extends BaseActivity {

    public static final String UNFINISHED = "UNFINISHED";
    public static final String UNFINISHED_TEST = "unfinished_test";
    public boolean isAppWentToBg = false;
    public boolean isWindowFocused = false;
    public boolean isMenuOpened = false;
    public boolean isBackPressed = false;
    TestViewPager pager;
    PagerAdapter pagerAdapter;
    private Test test;
    private AlertDialog dialog;
    private boolean isItRandom = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        defineNavigationMenu();




        pager= (TestViewPager) findViewById(R.id.pager);
        test= (Test) getIntent().getSerializableExtra(TestCategoriesFragment.TEST_BUNDLE);

        pagerAdapter=new PagerAdapter(getSupportFragmentManager(),test,PagerAdapter.TEST);
        assert pager != null;
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(3);
        if (test.getTestId() != null) {
            pager.setCurrentItem(test.getNumberOfAnsweredQuestions());
        }



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
                    savingTestInMemmory(test, true);
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

    private void savingTestInMemmory(Test test, boolean isItFinishedTest) {
        if (isItFinishedTest) {
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
        } else {
            //saving tests for retrieval when launching testActivity next time because user exit
            // from home key
            SharedPreferences prefs = getSharedPreferences(UNFINISHED_TEST, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            test.setTestId(UNFINISHED);
            String json = gson.toJson(test);
            editor.putString(UNFINISHED, json);
            editor.apply();
        }
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            buildAlertDialog();
            clearUnfinishedTest();
            dialog.show();
            isBackPressed = true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void clearUnfinishedTest() {
        SharedPreferences pref = getSharedPreferences(UNFINISHED_TEST, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear().apply();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);


        if (isBackPressed && !hasFocus) {
            isWindowFocused = true;

        } else if (isMenuOpened && !hasFocus) {
            isMenuOpened = false;
            isWindowFocused = true;
        } else {
            isWindowFocused = hasFocus;
        }
    }

    @Override
    protected void onStart() {
        if (isAppWentToBg) {
            isAppWentToBg = false;
            /*Toast.makeText(this, "Foreground", Toast.LENGTH_LONG).show();*/

        }

        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isWindowFocused && !isBackPressed) {
            isAppWentToBg = true;
            /*Toast.makeText(this, "Background", Toast.LENGTH_LONG).show();*/
            //saving tests item
            Test unFinishedTest = ((TestFragment) pagerAdapter.instantiateItem(pager, pager
                    .getCurrentItem())).getTest();
            savingTestInMemmory(unFinishedTest, false);
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        isMenuOpened = true;
        return super.onMenuOpened(featureId, menu);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5) {
            finish();
        }
    }
}
