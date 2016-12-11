package com.leedga.seagate.leedga;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static com.leedga.seagate.leedga.REF.DATABASE_NAME;
import static com.leedga.seagate.leedga.REF.MULTI_CHOICE_KEY;
import static com.leedga.seagate.leedga.REF.QUESTIONS_POSTITION_KEY;
import static com.leedga.seagate.leedga.REF.SINGLE_CHOICE_KEY;
import static com.leedga.seagate.leedga.REF.TEST_BUNDLE_KEY;
import static com.leedga.seagate.leedga.REF.TRUE_FALSE_KEY;
import static com.leedga.seagate.leedga.ResultActivity.calculatePrecentage;
import static com.leedga.seagate.leedga.ResultActivity.calculateScore;


public class TestFragment extends Fragment {

    public String[] checkBoxRealName = {"a", "b", "c", "d", "e", "f"};
    public boolean nextState = false, nextSubmit = false, nextNext = false;
    listCallback listener;
    View line4, line5, line6, line3;
    Button explain;
    ConstraintLayout r1, r2, r3, r4, r5, r6;
    TextView questionBodyS, text1, text2, text3, text4, text5, text6, note1;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6;
    TextView answerText;
    ArrayList<String> userAnswers;
    Test test;
    RadioButton radio1, radio2, radio3, radio4;
    Question question;
    ImageView image1, image2, image3, image4, image5, image6;
    int fragmentPosition = 0;
    int uniCount = 0;
    int position;
    int nextCounter = 0;
    ArrayList<CheckBox> checkBoxes;
    CardView answerCard;
    TextView typeText, categoryText;
    CompoundButton lastCheckedBox;
    boolean isItAnswerShow = false;
    boolean isExplainOn = false;
    private CardView mainCard;

    public TestFragment() {
        // Required empty public constructor
    }


    public static Fragment init(Test test, int position) {
        TestFragment testFragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TEST_BUNDLE_KEY, test);
        bundle.putInt(QUESTIONS_POSTITION_KEY, position);
        testFragment.setArguments(bundle);
        return testFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        test = (Test) bundle.getSerializable(TEST_BUNDLE_KEY);
        isItAnswerShow = test.isSaved();
        uniCount = bundle.getInt(QUESTIONS_POSTITION_KEY);
        position = bundle.getInt(QUESTIONS_POSTITION_KEY);
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        CardView cardView = (CardView) view.findViewById(R.id.card);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            cardView.setRadius(0);
        }
        initViews(view);
        previewNextQuestion(uniCount, view);
        return view;
    }


    private void previewNextQuestion(int count, View v) {

        question = test.getQuestions().get(count);


        if (isItAnswerShow) {
            /*progressBar.setVisibility(View.GONE);*/
            /*next.setVisibility(View.INVISIBLE);*/
            /*back.setVisibility(View.INVISIBLE);*/
            disableAllViewClicks();
            image1.setVisibility(View.VISIBLE);
            image2.setVisibility(View.VISIBLE);
            image3.setVisibility(View.VISIBLE);
            image4.setVisibility(View.VISIBLE);


        } else if (test.getQuestions().size() == 1) {
            //question of the day
            ((TestActivity) getActivity()).setVisibilityNext(View.VISIBLE);
            settingUpClickListener();
            settingUpCheckListener(question.getType());

        } else {
            /*next.setEnabled(false);*/


            /*progressText.setText((count + 1) + "/" + test.getNumberOfQuestions());*/
            /*float progress=((float)(count)/(float)test.getNumberOfQuestions())*100;
            progressBar.setProgress((int)progress);*/
            settingUpClickListener();
            settingUpCheckListener(question.getType());
            /*if (test.getNextState()!=null){
                try {
                    if (test.getNextState().get(uniCount)==null){
                        disableNext();
                    }else {
                        enableNext();
                    }
                }catch (IndexOutOfBoundsException e){
                    disableNext();
                }
            }else {
                disableNext();
            }*/
            disableNext();
            ((TestActivity) getActivity()).showExplainBtn(false);

            if (test.getTestId() != null) {
                // displaying the unfinished tests
                displayUnfinishedTest();
            }


        }


        if (question.getKey().equals(SINGLE_CHOICE_KEY)) {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.VISIBLE);
            r3.setVisibility(View.VISIBLE);
            r4.setVisibility(View.VISIBLE);
            r5.setVisibility(View.GONE);
            r6.setVisibility(View.GONE);
            line6.setVisibility(View.INVISIBLE);
            line5.setVisibility(View.INVISIBLE);
            radio1.setVisibility(View.VISIBLE);
            radio2.setVisibility(View.VISIBLE);
            radio3.setVisibility(View.VISIBLE);
            radio4.setVisibility(View.VISIBLE);
            checkBox1.setVisibility(View.INVISIBLE);
            checkBox2.setVisibility(View.INVISIBLE);
            checkBox3.setVisibility(View.INVISIBLE);
            checkBox4.setVisibility(View.INVISIBLE);
            checkBox5.setVisibility(View.INVISIBLE);
            checkBox6.setVisibility(View.INVISIBLE);

            if (isItAnswerShow) {
                String userAnswer = test.getUserAnswers().get(uniCount);
                boolean result = test.getUserResult().get(uniCount);
                String answer = test.getAnsweredQuestions().get(uniCount).getAnswer();
                switch (answer) {
                    case "a":
                        image1.setImageResource(R.drawable.ic_correct);
                        r1.setBackgroundResource(R.color.correct);
                        break;
                    case "b":
                        image2.setImageResource(R.drawable.ic_correct);
                        r2.setBackgroundResource(R.color.correct);
                        break;
                    case "c":
                        image3.setImageResource(R.drawable.ic_correct);
                        r3.setBackgroundResource(R.color.correct);
                        break;
                    case "d":
                        image4.setImageResource(R.drawable.ic_correct);
                        r4.setBackgroundResource(R.color.correct);
                        break;
                }
                int source = result ? R.drawable.ic_correct : R.drawable.ic_incorrect;
                int colorRes = result ? R.color.correct : R.color.incorrect;
                switch (userAnswer) {
                    case "a":
                        radio1.setChecked(true);
                        image1.setImageResource(source);
                        r1.setBackgroundResource(colorRes);
                        break;
                    case "b":
                        radio2.setChecked(true);
                        image2.setImageResource(source);
                        r2.setBackgroundResource(colorRes);
                        break;
                    case "c":
                        radio3.setChecked(true);
                        image3.setImageResource(source);
                        r3.setBackgroundResource(colorRes);
                        break;
                    case "d":
                        radio4.setChecked(true);
                        image4.setImageResource(source);
                        r4.setBackgroundResource(colorRes);
                        break;
                }

            }
        } else if (question.getKey().equals(MULTI_CHOICE_KEY)) {
            if (question.getAswersCount() == 4) {
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.VISIBLE);
                r3.setVisibility(View.VISIBLE);
                r4.setVisibility(View.VISIBLE);
                r5.setVisibility(View.GONE);
                r6.setVisibility(View.GONE);
                line6.setVisibility(View.GONE);
                line5.setVisibility(View.GONE);

                radio1.setVisibility(View.INVISIBLE);
                radio2.setVisibility(View.INVISIBLE);
                radio3.setVisibility(View.INVISIBLE);
                radio4.setVisibility(View.INVISIBLE);

                checkBox1.setVisibility(View.VISIBLE);
                checkBox2.setVisibility(View.VISIBLE);
                checkBox3.setVisibility(View.VISIBLE);
                checkBox4.setVisibility(View.VISIBLE);
                checkBox5.setVisibility(View.VISIBLE);
                checkBox6.setVisibility(View.VISIBLE);


            } else if (question.getAswersCount() == 5) {
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.VISIBLE);
                r3.setVisibility(View.VISIBLE);
                r4.setVisibility(View.VISIBLE);
                r5.setVisibility(View.VISIBLE);
                r6.setVisibility(View.GONE);
                line6.setVisibility(View.GONE);

                radio1.setVisibility(View.INVISIBLE);
                radio2.setVisibility(View.INVISIBLE);
                radio3.setVisibility(View.INVISIBLE);
                radio4.setVisibility(View.INVISIBLE);

                checkBox1.setVisibility(View.VISIBLE);
                checkBox2.setVisibility(View.VISIBLE);
                checkBox3.setVisibility(View.VISIBLE);
                checkBox4.setVisibility(View.VISIBLE);
                checkBox5.setVisibility(View.VISIBLE);
                checkBox6.setVisibility(View.VISIBLE);
            } else {
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.VISIBLE);
                r3.setVisibility(View.VISIBLE);
                r4.setVisibility(View.VISIBLE);
                r5.setVisibility(View.VISIBLE);
                r6.setVisibility(View.VISIBLE);
                line6.setVisibility(View.VISIBLE);

                radio1.setVisibility(View.INVISIBLE);
                radio2.setVisibility(View.INVISIBLE);
                radio3.setVisibility(View.INVISIBLE);
                radio4.setVisibility(View.INVISIBLE);

                checkBox1.setVisibility(View.VISIBLE);
                checkBox2.setVisibility(View.VISIBLE);
                checkBox3.setVisibility(View.VISIBLE);
                checkBox4.setVisibility(View.VISIBLE);
                checkBox5.setVisibility(View.VISIBLE);
                checkBox6.setVisibility(View.VISIBLE);

            }
            if (isItAnswerShow) {

                image1.setVisibility(View.VISIBLE);
                image2.setVisibility(View.VISIBLE);
                image3.setVisibility(View.VISIBLE);
                image4.setVisibility(View.VISIBLE);
                image5.setVisibility(View.VISIBLE);
                image6.setVisibility(View.VISIBLE);

                String userAnswer = test.getUserAnswers().get(uniCount);
                String userLetter;
                for (int i = 0; i < userAnswer.length(); i++) {
                    userLetter = Character.toString(userAnswer.charAt(i));
                    if (!userLetter.equals(",")) {
                        switch (userLetter) {
                            case "a":
                                checkBox1.setChecked(true);
                                image1.setImageResource(R.drawable.ic_incorrect);
                                r1.setBackgroundResource(R.color.incorrect);
                                break;
                            case "b":
                                checkBox2.setChecked(true);
                                image2.setImageResource(R.drawable.ic_incorrect);
                                r2.setBackgroundResource(R.color.incorrect);
                                break;
                            case "c":
                                checkBox3.setChecked(true);
                                image3.setImageResource(R.drawable.ic_incorrect);
                                r3.setBackgroundResource(R.color.incorrect);
                                break;
                            case "d":
                                checkBox4.setChecked(true);
                                image4.setImageResource(R.drawable.ic_incorrect);
                                r4.setBackgroundResource(R.color.incorrect);
                                break;
                            case "e":
                                checkBox5.setChecked(true);
                                image5.setImageResource(R.drawable.ic_incorrect);
                                r5.setBackgroundResource(R.color.incorrect);
                                break;
                            case "f":
                                checkBox6.setChecked(true);
                                image6.setImageResource(R.drawable.ic_incorrect);
                                r6.setBackgroundResource(R.color.incorrect);
                                break;
                        }
                    }
                }
                String answer = test.getAnsweredQuestions().get(uniCount).getAnswer();
                String letter;

                for (int i = 0; i < answer.length(); i++) {
                    letter = Character.toString(answer.charAt(i));
                    if (!letter.equals(",")) {
                        switch (letter) {
                            case "a":
                                image1.setImageResource(R.drawable.ic_correct);
                                r1.setBackgroundResource(R.color.correct);
                                break;
                            case "b":
                                image2.setImageResource(R.drawable.ic_correct);
                                r2.setBackgroundResource(R.color.correct);
                                break;
                            case "c":
                                image3.setImageResource(R.drawable.ic_correct);
                                r3.setBackgroundResource(R.color.correct);
                                break;
                            case "d":
                                image4.setImageResource(R.drawable.ic_correct);
                                r4.setBackgroundResource(R.color.correct);
                                break;
                            case "e":
                                image5.setImageResource(R.drawable.ic_correct);
                                r5.setBackgroundResource(R.color.correct);
                                break;
                            case "f":
                                image6.setImageResource(R.drawable.ic_correct);
                                r6.setBackgroundResource(R.color.correct);
                                break;
                        }
                    }
                }
            }

        } else {

            // true or false question
            radio1.setVisibility(View.VISIBLE);
            radio2.setVisibility(View.VISIBLE);
            radio3.setVisibility(View.INVISIBLE);
            radio4.setVisibility(View.INVISIBLE);

            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.VISIBLE);
            r3.setVisibility(View.GONE);
            r4.setVisibility(View.GONE);
            r5.setVisibility(View.GONE);
            r6.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
            line5.setVisibility(View.GONE);
            line6.setVisibility(View.GONE);
            checkBox1.setVisibility(View.INVISIBLE);
            checkBox2.setVisibility(View.INVISIBLE);
            checkBox3.setVisibility(View.INVISIBLE);
            checkBox4.setVisibility(View.INVISIBLE);
            checkBox5.setVisibility(View.INVISIBLE);
            checkBox6.setVisibility(View.INVISIBLE);
            if (isItAnswerShow) {
                String answer = test.getUserAnswers().get(uniCount);
                boolean result = test.getUserResult().get(uniCount);


                switch (answer) {
                    case "1":
                        radio1.setChecked(true);
                        if (result) {
                            image1.setImageResource(R.drawable.ic_correct);
                            /*image2.setImageResource(R.drawable.ic_incorrect);*/

                            r1.setBackgroundResource(R.color.correct);
                            /*r2.setBackgroundResource(R.color.incorrect);*/
                        } else {
                            image1.setImageResource(R.drawable.ic_incorrect);
                            /*image2.setImageResource(R.drawable.ic_correct);*/

                            r1.setBackgroundResource(R.color.incorrect);
                            /*r2.setBackgroundResource(R.color.correct);*/
                        }
                        break;
                    case "0":
                        radio2.setChecked(true);
                        if (result) {
                            /*image1.setImageResource(R.drawable.ic_incorrect);*/
                            image2.setImageResource(R.drawable.ic_correct);

                            /*r1.setBackgroundResource(R.color.incorrect);*/
                            r2.setBackgroundResource(R.color.correct);
                        } else {
                            /*image1.setImageResource(R.drawable.ic_correct);*/
                            image2.setImageResource(R.drawable.ic_incorrect);

                            /*r1.setBackgroundResource(R.color.correct);*/
                            r2.setBackgroundResource(R.color.incorrect);
                        }
                        break;
                }
            }
        }

        //displaying the text
        if (question.getKey().equals(TRUE_FALSE_KEY)) {
            text1.setText("True");
            text2.setText("False");
        } else {


            text1.setText(question.choice1);
            text2.setText(question.choice2);
            text3.setText(question.choice3);
            text4.setText(question.choice4);
            text5.setText(question.choice5);
            text6.setText(question.choice6);

        }
        String note = null;
        if (question.getNote() != null) {
            note = question.getNote();
        }
        answerText.setText(note);
        questionBodyS.setText(question.getQuestionBody());
        showDomainOrNot();

        typeText.setText("Choose " + question.getType());

    }

    private void showDomainOrNot() {
        SharedPreferences pref = getActivity().getSharedPreferences(REF.GENERAL_SETTING_PREF, Context.MODE_PRIVATE);
        if (pref.getBoolean(REF.KNOWLEDGE_DOMAIN, false)) {
            categoryText.setText(question.getCategory());
        } else {
            categoryText.setVisibility(View.GONE);
        }

    }


    private void displayUnfinishedTest() {
        if (test.getNumberOfAnsweredQuestions() > uniCount) {
            disableAllViewClicks();
            nextNext = true;
            /*next.setVisibility(View.VISIBLE);*/
            ((TestActivity) getActivity()).setVisibilityNext(View.VISIBLE);
            /*back.setVisibility(View.VISIBLE);*/
            /*next.setEnabled(true);*/
            enableNext();
            /*back.setEnabled(true);*/
            showingDetailedAnswer();
            image1.setVisibility(View.INVISIBLE);
            image2.setVisibility(View.INVISIBLE);
            image3.setVisibility(View.INVISIBLE);
            image4.setVisibility(View.INVISIBLE);
            image5.setVisibility(View.INVISIBLE);
            image6.setVisibility(View.INVISIBLE);
            if (test.getAnswerShow() == TestTypeFragment.ANSWER_AFTER_ALL) {
                answerText.setVisibility(View.GONE);
                answerCard.setVisibility(View.GONE);
            } else if (test.getAnswerShow() == TestTypeFragment.ANSWER_WHEN_WRONG) {
                if (!test.getUserResult().get(uniCount)) {
                    /*answerText.setVisibility(View.VISIBLE);
                    if (question.getNote()!=null) {
                        *//*answerCard.setVisibility(View.VISIBLE);*//*
                        displayExplanations();
                    }else {
                        answerCard.setVisibility(View.GONE);
                    }*/
                    image1.setVisibility(View.VISIBLE);
                    image2.setVisibility(View.VISIBLE);
                    image3.setVisibility(View.VISIBLE);
                    image4.setVisibility(View.VISIBLE);
                    image5.setVisibility(View.VISIBLE);
                    image6.setVisibility(View.VISIBLE);
                }
            } else {
                answerText.setVisibility(View.VISIBLE);
                /*if (question.getNote()!=null) {
                    *//*answerCard.setVisibility(View.VISIBLE);*//*
                    displayExplanations();
                }else {
                    answerCard.setVisibility(View.GONE);
                }*/
                image1.setVisibility(View.VISIBLE);
                image2.setVisibility(View.VISIBLE);
                image3.setVisibility(View.VISIBLE);
                image4.setVisibility(View.VISIBLE);
                image5.setVisibility(View.VISIBLE);
                image6.setVisibility(View.VISIBLE);
            }
            if (question.getKey().equals(SINGLE_CHOICE_KEY)) {
                String userAnswer = test.getUserAnswers().get(uniCount);
                switch (userAnswer) {
                    case "a":
                        radio1.setChecked(true);
                        break;
                    case "b":
                        radio2.setChecked(true);
                        break;
                    case "c":
                        radio3.setChecked(true);
                        break;
                    case "d":
                        radio4.setChecked(true);
                        break;
                }
            } else if (question.getKey().equals(MULTI_CHOICE_KEY)) {
                String userAnswer = test.getUserAnswers().get(uniCount);
                String userLetter;
                for (int i = 0; i < userAnswer.length(); i++) {
                    userLetter = Character.toString(userAnswer.charAt(i));
                    if (!userLetter.equals(",")) {
                        switch (userLetter) {
                            case "a":
                                checkBox1.setChecked(true);
                                break;
                            case "b":
                                checkBox2.setChecked(true);
                                break;
                            case "c":
                                checkBox3.setChecked(true);
                                break;
                            case "d":
                                checkBox4.setChecked(true);
                                break;
                            case "e":
                                checkBox5.setChecked(true);
                                break;
                            case "f":
                                checkBox6.setChecked(true);
                                break;
                        }
                    }
                }
            } else {
                String answer = test.getUserAnswers().get(uniCount);
                switch (answer) {
                    case "1":
                        radio1.setChecked(true);
                        break;
                    case "0":
                        radio2.setChecked(true);
                        break;
                }
            }
        }
    }

    private void disableAllViewClicks() {
        View[] array = new View[]{r1, r2, r3, r4, r5, r6, checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, radio1, radio2, radio3, radio4};
        for (View view : array) {
            view.setClickable(false);
        }
    }


    private void settingUpClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.r1:
                        if (question.getKey().equals(MULTI_CHOICE_KEY)) {
                            checkBox1.setChecked(!checkBox1.isChecked());
                        } else {
                            radio1.setChecked(!radio1.isChecked());
                        }
                        break;
                    case R.id.r2:
                        if (question.getKey().equals(MULTI_CHOICE_KEY)) {
                            checkBox2.setChecked(!checkBox2.isChecked());
                        } else {
                            radio2.setChecked(!radio2.isChecked());
                        }
                        break;
                    case R.id.r3:
                        if (question.getKey().equals(MULTI_CHOICE_KEY)) {
                            checkBox3.setChecked(!checkBox3.isChecked());
                        } else {
                            radio3.setChecked(!radio3.isChecked());
                        }
                        break;
                    case R.id.r4:
                        if (question.getKey().equals(MULTI_CHOICE_KEY)) {
                            checkBox4.setChecked(!checkBox4.isChecked());
                        } else {
                            radio4.setChecked(!radio4.isChecked());
                        }
                        break;
                    case R.id.r5:
                        checkBox5.setChecked(!checkBox5.isChecked());
                        break;
                    case R.id.r6:
                        checkBox6.setChecked(!checkBox6.isChecked());
                        break;

                }
            }
        };

        r1.setOnClickListener(listener);
        r2.setOnClickListener(listener);
        r3.setOnClickListener(listener);
        r4.setOnClickListener(listener);
        r5.setOnClickListener(listener);
        r6.setOnClickListener(listener);


    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void settingUpCheckListener(final int minimumAnswer) {

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            int counter = 0;

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    if (counter == 0) {
                        counter = 0;
                    } else {
                        counter--;
                    }
                } else {

                    if (counter == minimumAnswer) {
                        lastCheckedBox.setChecked(false);
                    }
                    counter++;
                    lastCheckedBox = buttonView;


                }

                if (counter == minimumAnswer) {
                    /*next.setEnabled(true);*/
                    enableNext();
                    nextSubmit = true;
                    nextState = true;
                } else {
                    /*next.setEnabled(false);*/
                    disableNext();
                    nextSubmit = false;
                    nextState = false;
                }


            }
        };

        checkBox1.setOnCheckedChangeListener(listener);
        checkBox2.setOnCheckedChangeListener(listener);
        checkBox3.setOnCheckedChangeListener(listener);
        checkBox4.setOnCheckedChangeListener(listener);
        checkBox5.setOnCheckedChangeListener(listener);
        checkBox6.setOnCheckedChangeListener(listener);

        radio1.setOnCheckedChangeListener(listener);
        radio2.setOnCheckedChangeListener(listener);
        radio3.setOnCheckedChangeListener(listener);
        radio4.setOnCheckedChangeListener(listener);


    }


    private void initViews(View v) {
        image1 = (ImageView) v.findViewById(R.id.image1);
        image2 = (ImageView) v.findViewById(R.id.image2);
        image3 = (ImageView) v.findViewById(R.id.image3);
        image4 = (ImageView) v.findViewById(R.id.image4);
        image5 = (ImageView) v.findViewById(R.id.image5);
        image6 = (ImageView) v.findViewById(R.id.image6);
        radio1 = (RadioButton) v.findViewById(R.id.radio1);
        radio2 = (RadioButton) v.findViewById(R.id.radio2);
        radio3 = (RadioButton) v.findViewById(R.id.radio3);
        radio4 = (RadioButton) v.findViewById(R.id.radio4);
        line4 = v.findViewById(R.id.line4);
        line5 = v.findViewById(R.id.line5);
        line6 = v.findViewById(R.id.line6);
        line3 = v.findViewById(R.id.line3);
        /*back = (Button) v.findViewById(back);*/
        userAnswers = new ArrayList<>();
        answerText = (TextView) v.findViewById(R.id.answerText);/*
        answerText.setMovementMethod(new ScrollingMovementMethod());*/

        mainCard = (CardView) v.findViewById(R.id.card);
        answerCard = (CardView) v.findViewById(R.id.answerCard);
        /*next = (Button) v.findViewById(R.id.nextt);*/
        /*next.setEnabled(false);*/
        questionBodyS = (TextView) v.findViewById(R.id.question_body);
        r1 = (ConstraintLayout) v.findViewById(R.id.r1);
        r2 = (ConstraintLayout) v.findViewById(R.id.r2);
        r3 = (ConstraintLayout) v.findViewById(R.id.r3);
        r4 = (ConstraintLayout) v.findViewById(R.id.r4);
        r5 = (ConstraintLayout) v.findViewById(R.id.r5);
        r6 = (ConstraintLayout) v.findViewById(R.id.r6);


        checkBox1 = (CheckBox) v.findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) v.findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) v.findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) v.findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox) v.findViewById(R.id.checkBox5);
        checkBox6 = (CheckBox) v.findViewById(R.id.checkBox6);

        text1 = (TextView) v.findViewById(R.id.text1);
        text2 = (TextView) v.findViewById(R.id.text2);
        text3 = (TextView) v.findViewById(R.id.text3);
        text4 = (TextView) v.findViewById(R.id.text4);
        text5 = (TextView) v.findViewById(R.id.text5);
        text6 = (TextView) v.findViewById(R.id.text6);

        checkBoxes = new ArrayList<>();
        checkBoxes.add(checkBox1);
        checkBoxes.add(checkBox2);
        checkBoxes.add(checkBox3);
        checkBoxes.add(checkBox4);
        checkBoxes.add(checkBox5);
        checkBoxes.add(checkBox6);

        typeText = (TextView) v.findViewById(R.id.textType);
        categoryText = (TextView) v.findViewById(R.id.textCat);


    }

    private void disableNext() {
        ((TestActivity) getActivity()).setNextText("Submit");
        ((TestActivity) getActivity()).setEnableNext(false);
    }

    private void enableNext() {
        ((TestActivity) getActivity()).setEnableNext(true);
    }

    private void showAnswerOfDayQuestion() {
        answerText.setText(question.getNote());
        answerText.setVisibility(View.VISIBLE);
        /*if (question.getNote()!=null) {
            displayExplanations();
        }else {
            answerCard.setVisibility(View.GONE);
        }*/

        if (question.getKey().equals(SINGLE_CHOICE_KEY)) {
            showSingleAnswersToUser(getUserAnswer(), getResult(getUserAnswer()), question.getAnswer());
        } else if (question.getKey().equals(MULTI_CHOICE_KEY)) {
            showMultiAnswersToUser(getUserAnswer(), question.getAnswer());
        } else {
            showTrueFalseAnswerToUser(getResult(getUserAnswer()), getUserAnswer());
        }
    }

    public void displayExplanations() {
        if (question.getNote() != null) {
            isExplainOn = true;
            mainCard.setVisibility(View.GONE);
            answerCard.setVisibility(View.VISIBLE);
        }
    }

    public void hideExplanations() {
        isExplainOn = false;
        mainCard.setVisibility(View.VISIBLE);
        answerCard.setVisibility(View.INVISIBLE);
    }


    public void next() {
        if (test.getQuestions().size() == 1) {
            // question of the day
            ((TestActivity) getActivity()).setVisibilityNext(View.GONE);
            showingDetailedAnswer();
            disableAllViewClicks();
            //save answer date for question of the day
            saveDayQuestion();

        } else {
            if (test.getAnswerShow() == TestTypeFragment.ANSWER_AFTER_ALL) {
                updateTest();
            } else if (test.getAnswerShow() == TestTypeFragment.ANSWER_AFTER_EVERY) {
                disableAllViewClicks();
                if (test.getTestId() != null && test.getNumberOfAnsweredQuestions() > uniCount) {
                    nextCounter++;
                }
                nextCounter++;
                //mdoify next counter for unfinished tests
                if (nextCounter > 1) {
                    updateTest();
                } else if (nextCounter == 1) {
                    showingDetailedAnswer();

                }
            } else {
                disableAllViewClicks();
                String userAnswer = getUserAnswer();
                boolean result = getResult(userAnswer);
                if (test.getTestId() != null && test.getNumberOfAnsweredQuestions() > uniCount) {
                    nextCounter++;
                }
                if (!result) {
                    nextCounter++;
                    if (nextCounter > 1) {
                        //moving to next tests
                        updateTest();
                    } else if (nextCounter == 1) {
                        showingDetailedAnswer();
                    }
                } else {
                    updateTest();
                }
            }
        }
    }

    private void saveDayQuestion() {
        test.setSavingDate(new Date());
        test.setSaved(true);
        test.setTestId(UUID.randomUUID().toString());
        String answer = getUserAnswer();
        test.setUserAnswerElement(answer, fragmentPosition);
        test.setUserResultElement(getResult(answer), fragmentPosition);
        test.addToAnsweredQuestions(question, uniCount);
        String ratio = String.valueOf(calculateScore(test)) + "/" + test.getNumberOfAnsweredQuestions();
        test.setRatio(ratio);
        test.setTestPercentage(String.valueOf(calculatePrecentage(test)));
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(REF.DAY_QUESTION_HISTORY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(test.getTestId(), REF.getGsonString(test));
        editor.apply();

        sharedPreferences = getContext().getSharedPreferences(REF.GENERAL_SETTING_PREF, Context
                .MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putLong(REF.DAY_QUESTION_DATE_PREF_KEY, System.currentTimeMillis());
        editor.apply();

    }

    private void showingDetailedAnswer() {
        /*next.setText("Next");*/
        ((TestActivity) getActivity()).setNextText("Next");
        nextNext = true;
        answerText.setText(question.getNote());
        answerText.setVisibility(View.VISIBLE);
        if (question.getNote() != null) {
            ((TestActivity) getActivity()).showExplainBtn(true);
        }


        if (question.getKey().equals(SINGLE_CHOICE_KEY)) {
            if (test.getTestId() != null && test.getNumberOfAnsweredQuestions() > uniCount) {
                showSingleAnswersToUser(test.getUserAnswers().get(uniCount), test.getUserResult().get(uniCount), question.getAnswer());
            } else {
                showSingleAnswersToUser(getUserAnswer(), getResult(getUserAnswer()), question.getAnswer());
            }
        } else if (question.getKey().equals(MULTI_CHOICE_KEY)) {
            if (test.getTestId() != null && test.getNumberOfAnsweredQuestions() > uniCount) {
                showMultiAnswersToUser(test.getUserAnswers().get(uniCount), question.getAnswer());
            } else {
                showMultiAnswersToUser(getUserAnswer(), question.getAnswer());
            }
        } else {
            if (test.getTestId() != null && test.getNumberOfAnsweredQuestions() > uniCount) {
                showTrueFalseAnswerToUser(test.getUserResult().get(uniCount), test.getUserAnswers
                        ().get(uniCount));
            } else {
                showTrueFalseAnswerToUser(getResult(getUserAnswer()), getUserAnswer());
            }
        }
    }


    private void updateTest() {
        fragmentPosition = ((TestActivity) getActivity()).getCurrentFragmentPosition();
        String answer = getUserAnswer();
        test.setUserAnswerElement(answer, fragmentPosition);
        test.setUserResultElement(getResult(answer), fragmentPosition);
        test.addToAnsweredQuestions(question, uniCount);
        ((TestActivity) getActivity()).setCurrentItem(fragmentPosition + 1);
        test.addToNextSate(true, uniCount);
        nextNext = true;
        if (fragmentPosition + 1 >= test.getNumberOfQuestions()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(TestCategoriesFragment.TEST_BUNDLE, test);
            Intent i = new Intent(getContext(), ResultActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(TestCategoriesFragment.TEST_BUNDLE, bundle);
            i.putExtra("clear", true);
            startActivityForResult(i, 2);
        }
    }

    private void showTrueFalseAnswerToUser(boolean result, String answer) {
        switch (answer) {
            case "1":
                if (result) {
                    image1.setImageResource(R.drawable.ic_correct);
            /*image2.setImageResource(R.drawable.ic_incorrect);*/

                    r1.setBackgroundResource(R.color.correct);
            /*r2.setBackgroundResource(R.color.incorrect);*/
                } else {
            /*image1.setImageResource(R.drawable.ic_incorrect);*/
                    image1.setImageResource(R.drawable.ic_incorrect);

            /*r1.setBackgroundResource(R.color.incorrect);*/
                    r1.setBackgroundResource(R.color.incorrect);
                }
                break;
            case "0":
                if (result) {
                    image2.setImageResource(R.drawable.ic_correct);
            /*image2.setImageResource(R.drawable.ic_incorrect);*/

                    r2.setBackgroundResource(R.color.correct);
            /*r2.setBackgroundResource(R.color.incorrect);*/
                } else {
            /*image1.setImageResource(R.drawable.ic_incorrect);*/
                    image2.setImageResource(R.drawable.ic_incorrect);

            /*r1.setBackgroundResource(R.color.incorrect);*/
                    r2.setBackgroundResource(R.color.incorrect);
                }
        }


    }

    private void showSingleAnswersToUser(String userAnswer, boolean result, String actualAnswer) {

        image1.setVisibility(View.VISIBLE);
        image2.setVisibility(View.VISIBLE);
        image3.setVisibility(View.VISIBLE);
        image4.setVisibility(View.VISIBLE);
        switch (actualAnswer) {
            case "a":

                image1.setImageResource(R.drawable.ic_correct);
                r1.setBackgroundResource(R.color.correct);
                break;
            case "b":
                image2.setImageResource(R.drawable.ic_correct);
                r2.setBackgroundResource(R.color.correct);
                break;
            case "c":
                image3.setImageResource(R.drawable.ic_correct);
                r3.setBackgroundResource(R.color.correct);
                break;
            case "d":
                image4.setImageResource(R.drawable.ic_correct);
                r4.setBackgroundResource(R.color.correct);
                break;
        }
        int id = result ? R.drawable.ic_correct : R.drawable.ic_incorrect;
        int color = result ? R.color.correct : R.color.incorrect;
        switch (userAnswer) {
            case "a":
                image1.setImageResource(id);
                r1.setBackgroundResource(color);
                break;
            case "b":
                image2.setImageResource(id);
                r2.setBackgroundResource(color);
                break;
            case "c":
                image3.setImageResource(id);
                r3.setBackgroundResource(color);
                break;
            case "d":
                image4.setImageResource(id);
                r4.setBackgroundResource(color);
                break;
        }

    }

    private void showMultiAnswersToUser(String userAnswer, String actualAnswer) {
        image1.setVisibility(View.VISIBLE);
        image2.setVisibility(View.VISIBLE);
        image3.setVisibility(View.VISIBLE);
        image4.setVisibility(View.VISIBLE);
        image5.setVisibility(View.VISIBLE);
        image6.setVisibility(View.VISIBLE);
        boolean result = getResult(userAnswer);
        String userLetter;
        for (int i = 0; i < userAnswer.length(); i++) {
            userLetter = Character.toString(userAnswer.charAt(i));
            if (!userLetter.equals(",")) {
                switch (userLetter) {
                    case "a":
                        r1.setBackgroundResource(R.color.incorrect);
                        image1.setImageResource(R.drawable.ic_incorrect);
                        break;
                    case "b":
                        image2.setImageResource(R.drawable.ic_incorrect);
                        r2.setBackgroundResource(R.color.incorrect);
                        break;
                    case "c":
                        image3.setImageResource(R.drawable.ic_incorrect);
                        r3.setBackgroundResource(R.color.incorrect);
                        break;
                    case "d":
                        image4.setImageResource(R.drawable.ic_incorrect);
                        r4.setBackgroundResource(R.color.incorrect);
                        break;
                    case "e":
                        image5.setImageResource(R.drawable.ic_incorrect);
                        r5.setBackgroundResource(R.color.incorrect);
                        break;
                    case "f":
                        image6.setImageResource(R.drawable.ic_incorrect);
                        r6.setBackgroundResource(R.color.incorrect);
                        break;
                }
            }
        }

        String letter;
        for (int i = 0; i < actualAnswer.length(); i++) {
            letter = Character.toString(actualAnswer.charAt(i));
            if (!letter.equals(",")) {
                switch (letter) {
                    case "a":
                        image1.setImageResource(R.drawable.ic_correct);
                        r1.setBackgroundResource(R.color.correct);
                        break;
                    case "b":
                        image2.setImageResource(R.drawable.ic_correct);
                        r2.setBackgroundResource(R.color.correct);
                        break;
                    case "c":
                        image3.setImageResource(R.drawable.ic_correct);
                        r3.setBackgroundResource(R.color.correct);
                        break;
                    case "d":
                        image4.setImageResource(R.drawable.ic_correct);
                        r4.setBackgroundResource(R.color.correct);
                        break;
                    case "e":
                        image5.setImageResource(R.drawable.ic_correct);
                        r5.setBackgroundResource(R.color.correct);
                        break;
                    case "f":
                        image6.setImageResource(R.drawable.ic_correct);
                        r6.setBackgroundResource(R.color.correct);
                        break;
                }
            }
        }


    }


    private boolean getResult(String answer) {
        return answer.equals(question.getAnswer());
    }


    private String getUserAnswer() {
        if (question.getKey().equals(MULTI_CHOICE_KEY)) {
            StringBuilder answer = new StringBuilder();
            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isChecked()) {
                    answer.append(checkBoxRealName[i] + ",");
                }
            }

            if (answer.length() != 0) {
                answer.deleteCharAt(answer.length() - 1);
                String answerS = answer.toString();
                return answerS;
            }
        } else if (question.getKey().equals(SINGLE_CHOICE_KEY)) {
            if (radio1.isChecked()) {
                return "a";
            } else if (radio2.isChecked()) {
                return "b";
            } else if (radio3.isChecked()) {
                return "c";
            } else {
                return "d";
            }
        } else {
            if (radio1.isChecked()) {
                return "1";
            } else {
                return "0";
            }
        }
        return null;

    }

    public Test getTest() {
        return test;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_test, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.flagged);
        if (question.isFlagged()) {
            item.setIcon(R.drawable.ic_flaggold);
        } else {
            item.setIcon(R.drawable.ic_flaglight);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.flagged) {
            DBHelper helper = new DBHelper(getContext(), DATABASE_NAME);
            if (question.isFlagged()) {
                item.setIcon(R.drawable.ic_flaglight);
                question.setFlagged(false);
                helper.updateFlag(question.getId(), false);

            } else {
                item.setIcon(R.drawable.ic_flaggold);
                question.setFlagged(true);
                helper.updateFlag(question.getId(), true);
            }
            if (isItAnswerShow) {
                Question answerShowQuestion = test.getAnsweredQuestions().get(uniCount);
                answerShowQuestion.setFlagged(!answerShowQuestion.isFlagged());
                SharedPreferences preferences = getContext().getSharedPreferences(ResultActivity.TESTS_PREFS, Context.MODE_PRIVATE);
                preferences.edit().putString(test.getTestId(), new Gson().toJson(test)).apply();

            }
        } else if (item.getItemId() == R.id.myReport) {
            Intent intent = new Intent(getContext(), ReportActivity.class);
            intent.putExtra(REF.QUESTION_KEY, question);
            startActivity(intent);
        }


        return true;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 5) {
            getActivity().setResult(5);
            getActivity().finish();
        }
    }
}
