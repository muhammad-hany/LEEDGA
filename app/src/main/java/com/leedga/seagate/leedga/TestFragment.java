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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class TestFragment extends Fragment  {

    FragmentTypeListener testHandleCallback;
    View line4,line5, line6, line7;
    Button next,back;
    RelativeLayout r1,r2,r3,r4,r5,r6;
    TextView questionBodyS,text1,text2,text3,text4,text5,text6;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4,checkBox5,checkBox6;
    TextView answerText,progressText;
    ArrayList<String> userAnswers;
    Test test;
    Question question;
    int fragmentPosition=0;
    int uniCount =0;
    int position;
    public static final String DATABASE_NAME="leed.sqlite";
    public static final String TRUE_FALSE_KEY="truefalse";
    public static final String SINGLE_CHOICE_KEY="single";
    public static final String MULTI_CHOICE_KEY="multi";
    public static final String TEST_BUNDLE_KEY ="test";
    public static final String QUESTIONS_POSTITION_KEY="count";

    public String [] checkBoxRealName={"a","b","c","d","e","f"};
    ArrayList<CheckBox> checkBoxes;

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
        position=bundle.getInt(QUESTIONS_POSTITION_KEY);
        View view=inflater.inflate(R.layout.fragment_test, container, false);
        initViews(view);
        previewNextQuestion(uniCount,view);
        return view;
    }





    private void previewNextQuestion(int count,View v){
        next.setEnabled(false);
        progressText.setText((count+1)+"/"+test.getNumberOfQuestions());
        question=  test.getQuestions().get(count);



        settingUpClickListener();
        settingUpCheckListener(question.getType());
        if (question.getKey().equals(SINGLE_CHOICE_KEY)){
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.VISIBLE);
            r3.setVisibility(View.VISIBLE);
            r4.setVisibility(View.VISIBLE);
            r5.setVisibility(View.INVISIBLE);
            r6.setVisibility(View.INVISIBLE);
            line6.setVisibility(View.INVISIBLE);
            line7.setVisibility(View.INVISIBLE);
        }else if (question.getKey().equals(MULTI_CHOICE_KEY)){
            if (question.getAswersCount()==4){
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.VISIBLE);
                r3.setVisibility(View.VISIBLE);
                r4.setVisibility(View.VISIBLE);
                r5.setVisibility(View.INVISIBLE);
                r6.setVisibility(View.INVISIBLE);
                line6.setVisibility(View.INVISIBLE);
                line7.setVisibility(View.INVISIBLE);
            }else if (question.getAswersCount()==5){
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.VISIBLE);
                r3.setVisibility(View.VISIBLE);
                r4.setVisibility(View.VISIBLE);
                r5.setVisibility(View.VISIBLE);
                r6.setVisibility(View.INVISIBLE);
                line6.setVisibility(View.VISIBLE);
                line7.setVisibility(View.INVISIBLE);
            }else {
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.VISIBLE);
                r3.setVisibility(View.VISIBLE);
                r4.setVisibility(View.VISIBLE);
                r5.setVisibility(View.VISIBLE);
                r6.setVisibility(View.VISIBLE);
                line6.setVisibility(View.VISIBLE);
                line7.setVisibility(View.VISIBLE);
            }
        }else {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.VISIBLE);
            r3.setVisibility(View.INVISIBLE);
            r4.setVisibility(View.INVISIBLE);
            r5.setVisibility(View.INVISIBLE);
            r6.setVisibility(View.INVISIBLE);
            line6.setVisibility(View.INVISIBLE);
            line7.setVisibility(View.INVISIBLE);
        }

        if (question.getKey().equals(TRUE_FALSE_KEY)){
            text1.setText("True");
            text2.setText("False");
            line4.setVisibility(View.INVISIBLE);
            line5.setVisibility(View.INVISIBLE);
            line6.setVisibility(View.INVISIBLE);
            line7.setVisibility(View.INVISIBLE);
        }else {


            text1.setText(question.choice1);
            text2.setText(question.choice2);
            text3.setText(question.choice3);
            text4.setText(question.choice4);
            text5.setText(question.choice5);
            text6.setText(question.choice6);

        }
        answerText.setText(question.getNote());
        questionBodyS.setText(question.getQuestionBody());

    }

    private void settingUpClickListener() {
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.r1:
                        checkBox1.setChecked(checkBox1.isChecked() ? false : true);
                        break;
                    case R.id.r2:
                        checkBox2.setChecked(checkBox2.isChecked() ? false : true);
                        break;
                    case R.id.r3:
                        checkBox3.setChecked(checkBox3.isChecked() ? false : true);
                        break;
                    case R.id.r4:
                        checkBox4.setChecked(checkBox4.isChecked() ? false : true);
                        break;
                    case R.id.r5:
                        checkBox5.setChecked(checkBox5.isChecked() ? false : true);
                        break;
                    case R.id.r6:
                        checkBox6.setChecked(checkBox6.isChecked() ? false : true);
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

    private void settingUpCheckListener(final int minimumAnswer) {

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
                    /*next.setEnabled(true);*/
                    next.setEnabled(true);
                }else {
                    /*next.setEnabled(false);*/
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
        line4=v.findViewById(R.id.line4);
        line5=v.findViewById(R.id.line5);
        line6 =v.findViewById(R.id.line6);
        line7 =v.findViewById(R.id.line7);
        back= (Button) v.findViewById(R.id.back);
        userResult=new boolean[test.getNumberOfQuestions()];
        userAnswers=new ArrayList<>();
        progressText = (TextView) v.findViewById(R.id.progressText);
        answerText= (TextView) v.findViewById(R.id.answerText);
        answerText.setMovementMethod(new ScrollingMovementMethod());
        answerText.setVisibility(View.INVISIBLE);

        next= (Button)v.findViewById(R.id.nextt);
        next.setEnabled(false);
        questionBodyS = (TextView) v.findViewById(R.id.question_body);

        r1= (RelativeLayout) v.findViewById(R.id.r1);
        r2= (RelativeLayout) v.findViewById(R.id.r2);
        r3= (RelativeLayout) v.findViewById(R.id.r3);
        r4= (RelativeLayout) v.findViewById(R.id.r4);
        r5= (RelativeLayout) v.findViewById(R.id.r5);
        r6= (RelativeLayout) v.findViewById(R.id.r6);

        checkBox1 = (CheckBox) v.findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) v.findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) v.findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) v.findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox) v.findViewById(R.id.checkBox5);
        checkBox6 = (CheckBox) v.findViewById(R.id.checkBox6);

        text1= (TextView) v.findViewById(R.id.text1);
        text2= (TextView) v.findViewById(R.id.text2);
        text3= (TextView) v.findViewById(R.id.text3);
        text4= (TextView) v.findViewById(R.id.text4);
        text5= (TextView) v.findViewById(R.id.text5);
        text6= (TextView) v.findViewById(R.id.text6);

        checkBoxes = new ArrayList<>();
        checkBoxes.add(checkBox1);
        checkBoxes.add(checkBox2);
        checkBoxes.add(checkBox3);
        checkBoxes.add(checkBox4);
        checkBoxes.add(checkBox5);
        checkBoxes.add(checkBox6);





        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentPosition=((TestActivity)getActivity()).getCurrentFragmentPosition();
                String answer=getUserAnswer();
                /*userAnswers.add(getUserAnswer());*/
                /*userResult[fragmentPosition]=getResult(answer);*/
                test.setUserAnswerElement(answer,fragmentPosition);
                test.setUserResultElement(getResult(answer),fragmentPosition);

                ((TestActivity)getActivity()).setCurrentItem(fragmentPosition+1);
                 if (fragmentPosition+1>=test.getNumberOfQuestions()) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(TestCategoriesFragment.TEST_BUNDLE,test);
                    Intent i = new Intent(getContext(), ResultActivity.class);
                    i.putExtra(TestCategoriesFragment.TEST_BUNDLE,bundle);
                    startActivity(i);
                }

            }




        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentPosition=((TestActivity)getActivity()).getCurrentFragmentPosition();
                ((TestActivity)getActivity()).setCurrentItem(fragmentPosition-1);
            }
        });







    }



    private boolean getResult(String answer) {
        if (answer.equals(question.getAnswer())){
            return true;
        }else {
            return false;
        }
    }


    private String getUserAnswer() {
        if (!question.getKey().equals(TRUE_FALSE_KEY)) {
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
        }else {
            if (checkBox1.isChecked()){
                return "1";
            }else {
                return "0";
            }
        }
        return null;

    }



}
