package com.leedga.seagate.leedga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import static com.leedga.seagate.leedga.REF.DEFAULT_TEST_KEY;
import static com.leedga.seagate.leedga.REF.DEFAULT_TEST_PREF_KEY;
import static com.leedga.seagate.leedga.TestTypeFragment.ANSWER_AFTER_ALL;
import static com.leedga.seagate.leedga.TestTypeFragment.ANSWER_AFTER_EVERY;
import static com.leedga.seagate.leedga.TestTypeFragment.ANSWER_WHEN_WRONG;
import static com.leedga.seagate.leedga.TestTypeFragment.MULTI_CHOICE;
import static com.leedga.seagate.leedga.TestTypeFragment.SINGLE_CHOICE;
import static com.leedga.seagate.leedga.TestTypeFragment.TRUE_FALSE;

public class TestSettingActivity extends AppCompatActivity implements FragmentListener {

    private RadioGroup radioGroup;
    private Switch trueFalse;
    private Switch oneChoice;
    private Switch multiChoice;
    private SeekBar seekBar;
    private int[] myValues = {10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100};
    private Switch s1, s2, s3, s4, s5, s6, s7, s8, s9;
    private Test test;
    private SharedPreferences defaultTestPref;
    private int activeTypeNumber, activeChapters;
    private Switch knowledgeDomain, flaggedOnly;
    private SharedPreferences generalSetting;
    private DBHelper helper;
    private boolean premiumUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_type);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Test Settings");
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
        displayQuestionsSetting();
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

    private void displayQuestionsSetting() {
        int numberOfQuestion = test.getNumberOfQuestions();
        int i = 0;
        if (!test.isOnlyFlagged()) {
            for (int num : myValues) {
                if (num == numberOfQuestion) {
                    seekBar.setProgress(i);
                }
                i++;
            }
        } else {
            seekBar.setProgress(numberOfQuestion - 5);
        }

        knowledgeDomain.setChecked(generalSetting.getBoolean(REF.KNOWLEDGE_DOMAIN, false));
        flaggedOnly.setChecked(test.isOnlyFlagged());
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
        boolean[] chapters = test.getChapters();
        activeChapters = 0;
        for (boolean isChecked : chapters) {
            activeChapters = isChecked ? activeChapters + 1 : activeChapters - 1;
        }
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSwitchesStatusTest();
                activeChapters = isChecked ? activeChapters + 1 : activeChapters - 1;
                if (activeChapters == 1) {
                    disableLastSwitch();
                } else {
                    enableLastSwitch();
                }
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

    private void enableLastSwitch() {
        Switch[] switches = {s1, s2, s3, s4, s5, s6, s7, s8, s9};
        for (Switch s : switches) {
            if (!s.isEnabled()) {
                s.setEnabled(true);
            }
        }
    }

    private void disableLastSwitch() {
        Switch[] switches = {s1, s2, s3, s4, s5, s6, s7, s8, s9};
        for (Switch s : switches) {
            if (s.isChecked()) {
                s.setEnabled(false);
                Toast.makeText(this, "You must choose at least one domain ", Toast
                        .LENGTH_LONG).show();
            }
        }
    }


    private void updateSwitchesStatusTest() {
        Switch[] switches = {s1, s2, s3, s4, s5, s6, s7, s8, s9};
        boolean[] chapters = new boolean[9];
        for (int i = 0; i < 9; i++) {
            chapters[i] = switches[i].isChecked();
        }
        test.setChapters(chapters);
    }

    private void definingQuestionsSetting() {
        final TextView seekBarTxt = (TextView) findViewById(R.id.seekBarTxt);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(0);

        if (test.isOnlyFlagged() && helper.getFlaggedCount() >= 5) {
            // flagged question only
            int flaggedCount = helper.getFlaggedCount();
            seekBar.setMax(flaggedCount - 5);
            seekBarTxt.setText(String.valueOf(seekBar.getProgress() + 5) + " Flagged Questions");

        } else {
            //no flagged question only
            seekBar.setMax(18);
            seekBarTxt.setText(String.valueOf(myValues[seekBar.getProgress()]) + " Questions");
        }


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                if (test.isOnlyFlagged() && helper.getFlaggedCount() >= 5) {
                    seekBarTxt.setText(String.valueOf(progress + 5) + " Flagged Questions");
                    test.setNumberOfQuestions(progress + 5);
                } else {
                    if (!premiumUser) {
                        if (myValues[progress] > 30) {
                            if (progress == 5) {
                                Toast.makeText(TestSettingActivity.this, "You need to upgrade to use " +
                                        "more questions in the test", Toast.LENGTH_SHORT).show();
                            }
                            progress = 4;
                            seekBar.setProgress(4);

                        }
                    }
                    seekBarTxt.setText(String.valueOf(myValues[progress]) + " Questions");
                    test.setNumberOfQuestions(myValues[progress]);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        knowledgeDomain = (Switch) findViewById(R.id.knowledge);
        knowledgeDomain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = generalSetting.edit();
                editor.putBoolean(REF.KNOWLEDGE_DOMAIN, b);
                editor.apply();
            }
        });

        flaggedOnly = (Switch) findViewById(R.id.flagQuestion);
        flaggedOnly.setChecked(test.isOnlyFlagged());
        View v = findViewById(R.id.dd);
        if (helper.getFlaggedCount() < 5) {
            flaggedOnly.setEnabled(false);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(TestSettingActivity.this, "There is no enough flagged questions to perform test", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            v.setVisibility(View.GONE);
        }
        flaggedOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                test.setOnlyFlagged(b);
                if (b) {
                    seekBar.setMax(helper.getFlaggedCount() - 5);
                    seekBarTxt.setText(String.valueOf(seekBar.getProgress() + 5) + " Flagged Questions");
                    changeDomainState(false);
                } else {
                    seekBar.setMax(18);
                    seekBarTxt.setText(String.valueOf(myValues[seekBar.getProgress()]) + " " +
                            "Questions");
                    changeDomainState(true);
                }
            }
        });
    }


    private void changeDomainState(boolean state) {
        Switch[] switches = {s1, s2, s3, s4, s5, s6, s7, s8, s9};
        for (Switch s : switches) {
            s.setEnabled(state);
        }
    }


    private void definingQuestionType() {
        activeTypeNumber = 0;
        trueFalse = (Switch) findViewById(R.id.true_false);
        oneChoice = (Switch) findViewById(R.id.one_choice);
        multiChoice = (Switch) findViewById(R.id.multi_choice);
        final boolean[] questionTypes = test.getQuestionTypes();
        for (boolean state : questionTypes) {
            activeTypeNumber = state ? activeTypeNumber + 1 : activeTypeNumber - 1;
        }
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
                activeTypeNumber = isChecked ? activeTypeNumber + 1 : activeTypeNumber - 1;
                if (premiumUser) {
                    if (activeTypeNumber == 1) {
                        findWhichActiveToDisable();
                    } else {
                        enableAll();
                    }
                }
            }
        };
        trueFalse.setOnCheckedChangeListener(listener);
        oneChoice.setOnCheckedChangeListener(listener);
        multiChoice.setOnCheckedChangeListener(listener);
        View layout = findViewById(R.id.moc2);
        View layout1 = findViewById(R.id.moc3);
        if (premiumUser) {
            trueFalse.setEnabled(true);
            oneChoice.setEnabled(true);
            multiChoice.setEnabled(true);
            layout.setVisibility(View.GONE);
            layout1.setVisibility(View.GONE);
        } else {
            trueFalse.setEnabled(false);
            oneChoice.setEnabled(false);
            multiChoice.setEnabled(false);
            layout.setVisibility(View.VISIBLE);
            layout1.setVisibility(View.VISIBLE);

            View.OnClickListener mockListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(TestSettingActivity.this, "You need to upgrde to premium " +
                            "account to unlock all question types", Toast.LENGTH_LONG).show();
                }
            };

            layout.setOnClickListener(mockListener);


            layout1.setOnClickListener(mockListener);


        }
    }

    private void enableAll() {
        if (!trueFalse.isEnabled()) {
            trueFalse.setEnabled(true);
        } else if (!oneChoice.isEnabled()) {
            oneChoice.setEnabled(true);
        } else if (!multiChoice.isEnabled()) {
            multiChoice.setEnabled(true);
        }
    }

    private void findWhichActiveToDisable() {
        if (trueFalse.isChecked()) {
            trueFalse.setEnabled(false);
        } else if (oneChoice.isChecked()) {
            oneChoice.setEnabled(false);
        } else if (multiChoice.isChecked()) {
            multiChoice.setEnabled(false);
        }

        Toast.makeText(this, "You must choose at least one type", Toast.LENGTH_LONG).show();
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
        definingQuestionsSetting();
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
        generalSetting = getSharedPreferences(REF.GENERAL_SETTING_PREF, MODE_PRIVATE);
        premiumUser = generalSetting.getBoolean(REF.PREMIUM_USER_KEY, false);

        helper = new DBHelper(this, REF.DATABASE_NAME);
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
