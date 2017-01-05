package com.leedga.seagate.leedga;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.gson.Gson;
import com.vungle.publisher.VunglePub;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static com.leedga.seagate.leedga.REF.DEFAULT_TEST_KEY;
import static com.leedga.seagate.leedga.REF.DEFAULT_TEST_PREF_KEY;
import static com.leedga.seagate.leedga.ResultActivity.TESTS_PREFS;

public class TestActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    public static final String UNFINISHED = "UNFINISHED";
    public static final String UNFINISHED_TEST = "unfinished_test";
    private final com.vungle.publisher.VunglePub vunglePub = VunglePub.getInstance();
    public boolean isAppWentToBg = false;
    public boolean isWindowFocused = false;
    public boolean isMenuOpened = false;
    public boolean isBackPressed = false;
    TestViewPager pager;
    PagerAdapter pagerAdapter;
    private Test test;
    private AlertDialog dialog;
    private boolean isExplainOn = false;
    private String fragmentType;
    private LinearLayout next, back, explain;
    private RoundCornerProgressBar progress;
    private TextView progressText;
    private ProgressDialog questionLoadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        defineButtons();
        createQuestionLoadingDialog();

        //
        questionLoadDialog.show();
        fragmentType = getIntent().getStringExtra(REF.TEST_FRAGMENT_TYPE);
        pager = (TestViewPager) findViewById(R.id.pager);
        test = (Test) getIntent().getSerializableExtra(TestCategoriesFragment.TEST_BUNDLE);
        if (fragmentType.equals(REF.FULL_QUESTIONS)) {
            getSupportActionBar().setTitle("Test");
            pagerAdapter = new PagerAdapter(getSupportFragmentManager(), test, PagerAdapter.TEST);
            pager.setAdapter(pagerAdapter);
            pager.setOffscreenPageLimit(test.getNumberOfQuestions());
            pager.addOnPageChangeListener(this);
            progress.setProgress(((float) (1) / (float) test.getNumberOfQuestions()) * 100);
            progressText.setText(1 + "/" + test.getNumberOfQuestions());
            if (test.getTestId() != null) {

                //for unfinished Test
                int position = (test.getAnsweredQuestions() == null) ? 0 : test.getNumberOfAnsweredQuestions();
                pager.setCurrentItem(position);
                setEnableNext(false);
                showExplainBtn(false);
                progress.setProgress(((float) (position + 1) / (float) test.getNumberOfQuestions()) * 100);
                progressText.setText(position + 1 + "/" + test.getNumberOfQuestions());
            }
        } else {
            //question of the day
            getSupportActionBar().setTitle("Question of day");
            pager.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            DBHelper helper = new DBHelper(this, REF.DATABASE_NAME);
            ArrayList<Question> questions = new ArrayList<>();
            questions.add(helper.getRandomQuestion());
            Test singleQuestionTest = gettingDefaultTest();
            singleQuestionTest.setQuestions(questions);
            Fragment fragment = TestFragment.init(singleQuestionTest, 0);
            transaction.add(R.id.linear, fragment);
            transaction.commit();
        }
        questionLoadDialog.dismiss();


    }

    private void createQuestionLoadingDialog() {
        questionLoadDialog = new ProgressDialog(this);
        questionLoadDialog.setTitle("Please wait");
        questionLoadDialog.setMessage("Loading Questions");
    }

    private void defineButtons() {
        next = (LinearLayout) findViewById(R.id.nextt);
        next.setOnClickListener(this);
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        explain = (LinearLayout) findViewById(R.id.explain);
        explain.setOnClickListener(this);
        progress = (RoundCornerProgressBar) findViewById(R.id.progressBar2);
        progress.setMax(100);
        progressText = (TextView) findViewById(R.id.progressTextt);
        setEnableNext(false);
        showExplainBtn(false);
    }


    public Test gettingDefaultTest() {
        SharedPreferences defaultTestPref = getSharedPreferences(DEFAULT_TEST_PREF_KEY, MODE_PRIVATE);
        String decode = defaultTestPref.getString(DEFAULT_TEST_KEY, null);
        Gson gson = new Gson();
        return gson.fromJson(decode, Test.class);
    }

    private void buildAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to end the test without submitting the result ?");

        /*String positiveText, message, title = null;
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
                    savingTestInMemory(test, true);
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
        });*/

        dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "End", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
                SharedPreferences preferences = getSharedPreferences(REF.UNCOMPLETED_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(REF.UNCOMPLETED_TEST);
                editor.apply();
                // Todo delete unfinished test
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Submit", new DialogInterface.OnClickListener
                () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (pager.getCurrentItem() > 0) {
                    TestFragment fragment = (TestFragment) pagerAdapter.instantiateItem(pager, pager.getCurrentItem());
                    Test test = fragment.getTest();
                    savingTestInMemory(test, true);
                }
                dialog.dismiss();
                onBackPressed();
            }
        });

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }

    private void savingTestInMemory(Test test, boolean isItFinishedTest) {
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
            SharedPreferences prefs = getSharedPreferences(REF.UNCOMPLETED_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            test.setTestId(UNFINISHED);
            String json = gson.toJson(test);
            editor.putString(REF.UNCOMPLETED_TEST, json);
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

    @Override
    public void onClick(View v) {
        TestFragment fragment;
        if (!fragmentType.equals(REF.FULL_QUESTIONS)) {
            fragment = (TestFragment) getSupportFragmentManager().findFragmentById(R.id.linear);
        } else {
            fragment = (TestFragment) pagerAdapter.instantiateItem(pager, pager.getCurrentItem());
        }
        isExplainOn = fragment.isExplainOn;
        switch (v.getId()) {
            case R.id.nextt:
                fragment.next();
                break;
            case R.id.back:
                pager.setCurrentItem(pager.getCurrentItem() - 1);
                break;
            case R.id.explain:
                isExplainOn = !isExplainOn;
                setExplainText(isExplainOn);
                if (isExplainOn) {
                    fragment.displayExplanations();
                } else {
                    fragment.hideExplanations();
                }

        }
    }

    public void showExplainBtn(boolean state) {
        if (state) {
            explain.setVisibility(View.VISIBLE);
        } else {
            explain.setVisibility(View.GONE);
        }
    }

    public void setEnableNext(boolean state) {
        next.setClickable(state);
        if (state) {
            next.setBackgroundResource(R.drawable.select_style_color);
        } else {
            next.setBackgroundResource(R.drawable.disabled_next);
        }
    }

    public void setExplainText(boolean isExplainOn) {
        if (isExplainOn) {
            ((TextView) explain.findViewById(R.id.explainText)).setText("Hide Explanation");
        } else {
            ((TextView) explain.findViewById(R.id.explainText)).setText("Show Explanation");
        }
    }

    public void setVisibilityNext(int state) {
        next.setVisibility(state);
    }

    public void setNextText(String txt) {
        ((TextView) next.findViewById(R.id.txt)).setText(txt);
    }


    protected int getCurrentFragmentPosition() {
        return pager.getCurrentItem();
    }

    public void setCurrentItem(int i) {
        pager.setCurrentItem(i, true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && fragmentType.equals(REF.FULL_QUESTIONS)) {
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
    protected void onPause() {
        super.onPause();
        if (fragmentType.equals(REF.FULL_QUESTIONS)) {
            if (!isWindowFocused && !isBackPressed) {
                isAppWentToBg = true;
                /*Toast.makeText(this, "Background", Toast.LENGTH_LONG).show();*/
                //saving tests item
                Test unFinishedTest = ((TestFragment) pagerAdapter.instantiateItem(pager, pager
                        .getCurrentItem())).getTest();
                savingTestInMemory(unFinishedTest, false);
            }
        }

        vunglePub.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vunglePub.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();


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


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public void setNextSubmit(TestFragment fragment) {
        ((TextView) next.findViewById(R.id.txt)).setText("Submit");
        next.setClickable(true);
        next.setBackgroundResource(R.drawable.select_style_color);
        fragment.nextCounter = 1;

    }

    public void setNextNext(TestFragment fragment) {
        ((TextView) next.findViewById(R.id.txt)).setText("Next");
        next.setClickable(true);
        next.setBackgroundResource(R.drawable.select_style_color);
        fragment.nextCounter = 2;
    }

    public void setNextDisabled() {
        ((TextView) next.findViewById(R.id.txt)).setText("Submit");
        next.setClickable(false);
        next.setBackgroundResource(R.drawable.disabled_next);
    }

    @Override
    public void onPageSelected(int position) {
        TestFragment fragment = (TestFragment) pagerAdapter.instantiateItem(pager, pager.getCurrentItem());
        isExplainOn = fragment.isExplainOn;
        Test test = fragment.getTest();
        if (test == null) {
            test = this.test;
        }
        if (fragment.nextNext && fragment.nextSubmit) {
            setNextNext(fragment);
            if (fragment.question.getNote() != null && test.getAnswerShow() != TestTypeFragment.ANSWER_AFTER_ALL) {
                showExplainBtn(true);
            } else {
                showExplainBtn(false);
            }
        } else {
            if (fragment.nextSubmit) {
                setNextSubmit(fragment);
                showExplainBtn(false);
            } else {
                setNextDisabled();
                showExplainBtn(false);
            }
        }

        /*if (test.getNextState() != null) {
            try {
                if (test.getNextState().get(position) == null) {
                    setEnableNext(false);
                    setNextText("Submit");
                    fragment.setNextCounter(0);
                    showExplainBtn(false);
                } else {
                    if (test.getNextState().get(position)) {
                        setEnableNext(true);
                        setNextText("Next");
                        fragment.setNextCounter(1);
                        if (test.getQuestions().get(position).getNote() != null) showExplainBtn(true);
                    } else {
                        setEnableNext(false);
                        setNextText("Submit");
                        fragment.setNextCounter(0);
                        showExplainBtn(false);
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                setEnableNext(false);
                setNextText("Submit");
                fragment.setNextCounter(0);
                showExplainBtn(false);
            }
        } else {
            setEnableNext(false);
            setNextText("Submit");
            fragment.setNextCounter(0);
            showExplainBtn(false);
        }*/
        /*if (fragment.nextState){
            setEnableNext(true);
        }*/
        float progress = ((float) (position + 1) / (float) test.getNumberOfQuestions()) * 100;
        this.progress.setProgress((int) progress);
        progressText.setText(position + 1 + "/" + test.getNumberOfQuestions());

        setExplainText(isExplainOn);


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
