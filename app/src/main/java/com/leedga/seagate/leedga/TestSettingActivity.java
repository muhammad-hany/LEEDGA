package com.leedga.seagate.leedga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import static com.leedga.seagate.leedga.MainActivity.DEFAULT_TEST_KEY;
import static com.leedga.seagate.leedga.MainActivity.DEFAULT_TEST_PREF_KEY;
import static com.leedga.seagate.leedga.TestTypeFragment.ANSWER_AFTER_ALL;
import static com.leedga.seagate.leedga.TestTypeFragment.ANSWER_AFTER_EVERY;
import static com.leedga.seagate.leedga.TestTypeFragment.ANSWER_WHEN_WRONG;
import static com.leedga.seagate.leedga.TestTypeFragment.MULTI_CHOICE;
import static com.leedga.seagate.leedga.TestTypeFragment.SINGLE_CHOICE;
import static com.leedga.seagate.leedga.TestTypeFragment.TRUE_FALSE;

public class TestSettingActivity extends BaseActivity implements FragmentListener {

    private RadioGroup radioGroup;
    private Switch trueFalse;
    private Switch oneChoice;
    private Switch multiChoice;
    private SeekBar seekBar;
    private int[] myValues = {10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100};
    private Switch s1, s2, s3, s4, s5, s6, s7, s8, s9;
    private Test test;
    private SharedPreferences defaultTestPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_type);
        defineNavigationMenu();
        gettingDefaultTest();
        definingViews();
        displayValues();


        /*FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        TestTypeFragment test1Fragment=new TestTypeFragment();
        transaction.add(R.id.test_relative_layout,test1Fragment);
        transaction.commit();*/


    }


    private void displayValues() {
        displayAnswerSettingValues();
        displayQuestionTypeValues();
        displayQuestionsNumber();
        displayQuestionsDomains();

    }

    private void displayQuestionsDomains() {
        boolean[] chapters = test.getChapters();
        Switch[] switches = {s1, s2, s3, s4, s5, s6, s7, s8, s9};
        for (int i = 0; i < 9; i++) {
            if (chapters[i]) {
                switches[i].setChecked(true);
            } else {
                switches[i].setChecked(false);
            }

        }

    }

    private void displayQuestionsNumber() {
        int numberOfQuestion = test.getNumberOfQuestions();
        int i = 0;
        for (int num : myValues) {
            if (num == numberOfQuestion) {
                seekBar.setProgress(i);
            }
            i++;
        }
    }

    private void displayQuestionTypeValues() {
        boolean[] questionTypes = test.getQuestionTypes();

        int n = 0;
        for (boolean i : questionTypes) {
            if (n == TRUE_FALSE) {
                if (i) {
                    trueFalse.setChecked(true);
                } else {
                    trueFalse.setChecked(false);
                }

            } else if (n == SINGLE_CHOICE) {
                if (i) {
                    oneChoice.setChecked(true);
                } else {
                    oneChoice.setChecked(false);
                }
            } else if (n == MULTI_CHOICE) {
                if (i) {
                    multiChoice.setChecked(true);
                } else {
                    multiChoice.setChecked(false);
                }
            }
            n++;
        }
    }

    private void displayAnswerSettingValues() {
        RadioButton radioButton = null;
        switch (test.getAnswerShow()) {
            case ANSWER_AFTER_EVERY:
                radioButton = (RadioButton) findViewById(R.id.radio1);
                break;
            case ANSWER_WHEN_WRONG:
                radioButton = (RadioButton) findViewById(R.id.radio2);
                break;
            case ANSWER_AFTER_ALL:
                radioButton = (RadioButton) findViewById(R.id.radio3);
                break;
        }
        radioButton.setChecked(true);
    }

    private void definingQuestionCategories() {
        s1 = (Switch) findViewById(R.id.switch1);
        s2 = (Switch) findViewById(R.id.switch2);
        s3 = (Switch) findViewById(R.id.switch3);
        s4 = (Switch) findViewById(R.id.switch4);
        s5 = (Switch) findViewById(R.id.switch5);
        s6 = (Switch) findViewById(R.id.switch6);
        s7 = (Switch) findViewById(R.id.switch7);
        s8 = (Switch) findViewById(R.id.switch8);
        s9 = (Switch) findViewById(R.id.switch9);
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSwitchesStatusTest();
            }
        };
        s1.setOnCheckedChangeListener(listener);
        s2.setOnCheckedChangeListener(listener);
        s3.setOnCheckedChangeListener(listener);
        s4.setOnCheckedChangeListener(listener);
        s5.setOnCheckedChangeListener(listener);
        s6.setOnCheckedChangeListener(listener);
        s7.setOnCheckedChangeListener(listener);
        s8.setOnCheckedChangeListener(listener);
        s9.setOnCheckedChangeListener(listener);
    }

    private void updateSwitchesStatusTest() {
        Switch[] switches = {s1, s2, s3, s4, s5, s6, s7, s8, s9};
        boolean[] chapters = new boolean[9];
        for (int i = 0; i < 9; i++) {
            chapters[i] = switches[i].isChecked();
        }
        test.setChapters(chapters);
    }

    private void definingNumberOfQuestions() {
        final TextView seekBarTxt = (TextView) findViewById(R.id.seekBarTxt);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(0);
        seekBar.setMax(18);
        seekBarTxt.setText(String.valueOf(myValues[seekBar.getProgress()]) + " Questions");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarTxt.setText(String.valueOf(myValues[progress]) + " Questions");
                test.setNumberOfQuestions(myValues[progress]);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void definingQuestionType() {
        trueFalse = (Switch) findViewById(R.id.true_false);
        oneChoice = (Switch) findViewById(R.id.one_choice);
        multiChoice = (Switch) findViewById(R.id.multi_choice);
        final boolean[] questionTypes = test.getQuestionTypes();
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.true_false:
                        questionTypes[TRUE_FALSE] = isChecked;
                        break;
                    case R.id.one_choice:
                        questionTypes[SINGLE_CHOICE] = isChecked;
                        break;
                    case R.id.multi_choice:
                        questionTypes[MULTI_CHOICE] = isChecked;
                        break;
                }
                test.setQuestionTypes(questionTypes);
            }
        };
        trueFalse.setOnCheckedChangeListener(listener);
        oneChoice.setOnCheckedChangeListener(listener);
        multiChoice.setOnCheckedChangeListener(listener);
    }

    private void definingAnswerSetting() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio1:
                        test.setAnswerShow(ANSWER_AFTER_EVERY);
                        break;
                    case R.id.radio2:
                        test.setAnswerShow(ANSWER_WHEN_WRONG);
                        break;
                    case R.id.radio3:
                        test.setAnswerShow(ANSWER_AFTER_ALL);
                        break;
                }
            }
        });
    }

    private void definingViews() {
        definingAnswerSetting();
        definingQuestionType();
        definingNumberOfQuestions();
        definingQuestionCategories();
    }


    @Override
    public void testToActivity(Test test) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 5) {
            setResult(5);
            finish();
        }
    }


    public void gettingDefaultTest() {
        defaultTestPref = getSharedPreferences(DEFAULT_TEST_PREF_KEY, MODE_PRIVATE);
        String decode = defaultTestPref.getString(DEFAULT_TEST_KEY, null);
        Gson gson = new Gson();
        test = gson.fromJson(decode, Test.class);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Gson gson = new Gson();
        String encode = gson.toJson(test);
        SharedPreferences.Editor editor = defaultTestPref.edit();
        editor.putString(DEFAULT_TEST_KEY, encode);
        editor.apply();

    }
}
