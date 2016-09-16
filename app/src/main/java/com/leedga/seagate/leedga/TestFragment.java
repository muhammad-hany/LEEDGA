package com.leedga.seagate.leedga;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class TestFragment extends Fragment {

    Button next;
    TextView questionBodyS;
    RadioButton firstChoiceS, secondChoiceS, thirdChoiceS, fourthChoiceS;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4,checkBox5,checkBox6;
    TextView answerText;
    Test test;
    int singleCount =0;
    int multiCount=0;
    public static final String DATABASE_NAME="leed.sqlite";
    LinearLayout linearLayout;
    RadioGroup radioGroup;
    CompoundButton lastcheckedBox;
    boolean firstClickperQuestion=true;

    public TestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle=getArguments();
        test= (Test) bundle.getSerializable(TestCategoriesFragment.TEST_BUNDLE);
        View view=inflater.inflate(R.layout.fragment_test, container, false);
        initViews(view);
        previewNextQuestion(singleCount,multiCount);
        return view;
    }

    public void previewNextQuestion(int singlecount,int multiCount){
        if (singlecount<test.getSingleQuestions().size()){
            previewSingleNextQuestion(singlecount);
        }else {
            previewMultiNextQuestion(multiCount);
        }


    }

    private void previewSingleNextQuestion(int i) {
        linearLayout.setVisibility(View.INVISIBLE);
        radioGroup.setVisibility(View.VISIBLE);
        next.setEnabled(false);
        radioGroup.clearCheck();
        SingleChoiceQuestion question= test.getSingleQuestions().get(i);
        answerText.setText(question.getNote());
        questionBodyS.setText(question.getQuestionBody());
        firstChoiceS.setText(question.getChoice1());
        secondChoiceS.setText(question.getChoice2());
        thirdChoiceS.setText(question.getChoice3());
        fourthChoiceS.setText(question.getChoice4());
        singleCount++;
    }

    private void previewMultiNextQuestion (int count){
        linearLayout.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.INVISIBLE);
        next.setEnabled(false);

        MultiChoiceQuestion question=  test.getMultiQuestions().get(count);
        updateCheckBoxForAnswer(question.getType());
        if (question.getAswersCount()==4){
            checkBox5.setVisibility(View.INVISIBLE);
            checkBox6.setVisibility(View.INVISIBLE);
        }else if (question.getAswersCount()==5){
            checkBox5.setVisibility(View.VISIBLE);
            checkBox6.setVisibility(View.INVISIBLE);
        }else {
            checkBox5.setVisibility(View.VISIBLE);
            checkBox6.setVisibility(View.VISIBLE);
        }
        answerText.setText(question.getNote());
        questionBodyS.setText(question.getQuestionBody());
        checkBox1.setText(question.getChoice1());
        checkBox2.setText(question.getChoice2());
        checkBox3.setText(question.getChoice3());
        checkBox4.setText(question.getChoice4());
        checkBox5.setText(question.getChoice5());
        checkBox6.setText(question.getChoice6());
        this.multiCount++;
    }

    private void updateCheckBoxForAnswer(final int minimumAnswer) {
        CompoundButton.OnCheckedChangeListener listener=new CompoundButton.OnCheckedChangeListener() {
            int counter=0;
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    if (counter==0){
                        counter=0;
                    }else {
                        counter--;
                    }
                }else {

                    if (counter == minimumAnswer ) {
                        lastcheckedBox.setChecked(false);
                    }
                    counter++;
                    lastcheckedBox = buttonView;


                }

                if (counter==minimumAnswer){
                    next.setEnabled(true);
                }else {
                    next.setEnabled(false);
                }
            }
        };

        checkBox1.setOnCheckedChangeListener(listener);
        checkBox2.setOnCheckedChangeListener(listener);
        checkBox3.setOnCheckedChangeListener(listener);
        checkBox4.setOnCheckedChangeListener(listener);
        checkBox5.setOnCheckedChangeListener(listener);
        checkBox6.setOnCheckedChangeListener(listener);
    }

    private void initViews(View v) {
        answerText= (TextView) v.findViewById(R.id.answerText);
        answerText.setMovementMethod(new ScrollingMovementMethod());
        answerText.setVisibility(View.INVISIBLE);
        next= (Button) v.findViewById(R.id.next);
        next.setEnabled(false);
        questionBodyS = (TextView) v.findViewById(R.id.question_body);
        firstChoiceS = (RadioButton) v.findViewById(R.id.choice1);
        secondChoiceS = (RadioButton) v.findViewById(R.id.choice2);
        thirdChoiceS = (RadioButton) v.findViewById(R.id.choice3);
        fourthChoiceS = (RadioButton) v.findViewById(R.id.choice4);

        checkBox1 = (CheckBox) v.findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) v.findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) v.findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) v.findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox) v.findViewById(R.id.checkBox5);
        checkBox6 = (CheckBox) v.findViewById(R.id.checkBox6);




        linearLayout= (LinearLayout) v.findViewById(R.id.check_box_group);
        radioGroup= (RadioGroup) v.findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==firstChoiceS.getId()||checkedId==secondChoiceS.getId()||checkedId==thirdChoiceS.getId()||checkedId==fourthChoiceS.getId())
                next.setEnabled(true);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (singleCount < test.getSingleQuestions().size() || multiCount < test.getMultiQuestions().size()) {
                    clearRadioCheck();
                    checkBoxesClearCheck();
                    previewNextQuestion(singleCount, multiCount);

                } else {
                    Intent i = new Intent(getContext(), ResultActivity.class);
                    startActivity(i);
                }

            }


        });





    }

    private void clearRadioCheck() {
        radioGroup.clearAnimation();
        radioGroup.clearCheck();
    }


    private void checkBoxesClearCheck() {
        checkBox1.clearAnimation();
        checkBox2.clearAnimation();
        checkBox3.clearAnimation();
        checkBox4.clearAnimation();
        checkBox5.clearAnimation();
        checkBox6.clearAnimation();

        checkBox1.setChecked(false);
        checkBox2.setChecked(false);
        checkBox3.setChecked(false);
        checkBox4.setChecked(false);
        checkBox5.setChecked(false);
        checkBox6.setChecked(false);
    }

}
