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
import android.widget.TextView;

import java.util.ArrayList;


public class TestFragment extends Fragment {

    Button next;
    TextView questionBodyS;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4,checkBox5,checkBox6;
    TextView answerText,progressText;
    ArrayList<String> userAnswers;
    Test test;
    Question question;
    int uniCount =0;
    public static final String DATABASE_NAME="leed.sqlite";
    public static final String TRUE_FALSE_KEY="truefalse";
    public static final String SINGLE_CHOICE_KEY="single";
    public static final String MULTI_CHOICE_KEY="multi";
    public static final String TEST_BUNDLE_KEY ="test";
    public static final String QUESTIONS_POSTITION_KEY="count";

    public String [] checkBoxRealName={"a","b","c","d","e","f"};

    CompoundButton lastCheckedBox;
    private boolean[] userResult;

    public TestFragment() {
        // Required empty public constructor
    }

    public static Fragment init(Test test,int position){
        TestFragment testFragment=new TestFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable(TEST_BUNDLE_KEY,test);
        bundle.putInt(QUESTIONS_POSTITION_KEY,position);
        testFragment.setArguments(bundle);
        return testFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle=getArguments();
        test= (Test) bundle.getSerializable(TEST_BUNDLE_KEY);
        uniCount=bundle.getInt(QUESTIONS_POSTITION_KEY);
        View view=inflater.inflate(R.layout.fragment_test, container, false);
        initViews(view);
        previewNextQuestion(uniCount);
        return view;
    }





    private void previewNextQuestion(int count){
        next.setEnabled(false);
        progressText.setText((count+1)+"/"+test.getNumberOfQuestions());
        question=  test.getQuestions().get(count);
        settingUpListener(question.getType());
        if (question.getKey().equals(SINGLE_CHOICE_KEY)){
            checkBox1.setVisibility(View.VISIBLE);
            checkBox2.setVisibility(View.VISIBLE);
            checkBox3.setVisibility(View.VISIBLE);
            checkBox4.setVisibility(View.VISIBLE);
            checkBox5.setVisibility(View.INVISIBLE);
            checkBox6.setVisibility(View.INVISIBLE);
        }else if (question.getKey().equals(MULTI_CHOICE_KEY)){
            if (question.getAswersCount()==4){
                checkBox1.setVisibility(View.VISIBLE);
                checkBox2.setVisibility(View.VISIBLE);
                checkBox3.setVisibility(View.VISIBLE);
                checkBox4.setVisibility(View.VISIBLE);
                checkBox5.setVisibility(View.INVISIBLE);
                checkBox6.setVisibility(View.INVISIBLE);
            }else if (question.getAswersCount()==5){
                checkBox1.setVisibility(View.VISIBLE);
                checkBox2.setVisibility(View.VISIBLE);
                checkBox3.setVisibility(View.VISIBLE);
                checkBox4.setVisibility(View.VISIBLE);
                checkBox5.setVisibility(View.VISIBLE);
                checkBox6.setVisibility(View.INVISIBLE);
            }else {
                checkBox1.setVisibility(View.VISIBLE);
                checkBox2.setVisibility(View.VISIBLE);
                checkBox3.setVisibility(View.VISIBLE);
                checkBox4.setVisibility(View.VISIBLE);
                checkBox5.setVisibility(View.VISIBLE);
                checkBox6.setVisibility(View.VISIBLE);
            }
        }else {
            checkBox1.setVisibility(View.VISIBLE);
            checkBox2.setVisibility(View.VISIBLE);
            checkBox3.setVisibility(View.INVISIBLE);
            checkBox4.setVisibility(View.INVISIBLE);
            checkBox5.setVisibility(View.INVISIBLE);
            checkBox6.setVisibility(View.INVISIBLE);
        }

        if (question.getKey().equals(TRUE_FALSE_KEY)){
            checkBox1.setText("True");
            checkBox2.setText("False");
        }else {
            checkBox1.setText(question.getChoice1());
            checkBox2.setText(question.getChoice2());
            checkBox3.setText(question.getChoice3());
            checkBox4.setText(question.getChoice4());
            checkBox5.setText(question.getChoice5());
            checkBox6.setText(question.getChoice6());
        }
        answerText.setText(question.getNote());
        questionBodyS.setText(question.getQuestionBody());
        this.uniCount++;
    }

    private void settingUpListener(final int minimumAnswer) {

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
                        lastCheckedBox.setChecked(false);
                    }
                    counter++;
                    lastCheckedBox = buttonView;


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
        userResult=new boolean[test.getNumberOfQuestions()];
        userAnswers=new ArrayList<>();
        progressText = (TextView) v.findViewById(R.id.progressText);
        answerText= (TextView) v.findViewById(R.id.answerText);
        answerText.setMovementMethod(new ScrollingMovementMethod());
        answerText.setVisibility(View.INVISIBLE);
        next= (Button) v.findViewById(R.id.next);
        next.setEnabled(false);
        questionBodyS = (TextView) v.findViewById(R.id.question_body);

        checkBox1 = (CheckBox) v.findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) v.findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) v.findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) v.findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox) v.findViewById(R.id.checkBox5);
        checkBox6 = (CheckBox) v.findViewById(R.id.checkBox6);







        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAnswers.add(getUserAnswer());
                userResult[uniCount-1]=getResult();
                getResult();

                if ( uniCount-1  < test.getQuestions().size()) {
                   // checkBoxesClearCheck();
                  //  previewNextQuestion(uniCount);
                    ((TestActivity)getActivity()).setCurrentItem(uniCount);

                } else {
                    test.setUserAnswers(userAnswers);
                    test.setUserResult(userResult);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(TestCategoriesFragment.TEST_BUNDLE,test);
                    Intent i = new Intent(getContext(), ResultActivity.class);
                    i.putExtra(TestCategoriesFragment.TEST_BUNDLE,bundle);
                    startActivity(i);
                }

            }




        });





    }

    private boolean getResult() {
        if (getUserAnswer().equals(question.getAnswer())){
            return true;
        }else {
            return false;
        }
    }

    private String getUserAnswer() {
        StringBuilder answer=new StringBuilder();
        CheckBox [] checkBoxes={checkBox1,checkBox2,checkBox3,checkBox4,checkBox5,
                checkBox6};
        for (int i=0;i<checkBoxes.length;i++){
            if (checkBoxes[i].isChecked()){
                answer.append(checkBoxRealName[i]+",");
            }
        }
        answer.deleteCharAt(answer.length()-1);
        String answerS=answer.toString();
        return answerS;





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
