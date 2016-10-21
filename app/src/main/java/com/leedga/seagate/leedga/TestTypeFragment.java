package com.leedga.seagate.leedga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class TestTypeFragment extends Fragment {
    public static final int TRUE_FALSE = 0;
    public static final int SINGLE_CHOICE = 1;
    public static final int MULTI_CHOICE = 2;
    public static final String TEST_BUNDLE_NAME = "tests";
    public static final int ANSWER_AFTER_ALL = 0;
    public static final int ANSWER_AFTER_EVERY = 1;
    public static final int ANSWER_WHEN_WRONG = 2;
    public boolean[] questionTypes;
    Button next;
    Switch trueFalse;
    Switch oneChoice;
    Switch multiChoice;
    View view;
    Test test;
    SeekBar seekBar;
    RadioGroup radioGroup;
    private int [] myValues={10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100};
    private FragmentListener testHandleCallback;

    public static Fragment init() {
        return new TestTypeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNewTest();
    }

    private void createNewTest() {
        test=new Test();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_test_type, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        questionTypes=new boolean[3];
        definingSeekBar(view);
        trueFalse= (Switch) view.findViewById(R.id.true_false);
        oneChoice= (Switch) view.findViewById(R.id.one_choice);
        multiChoice= (Switch) view.findViewById(R.id.multi_choice);
        questionTypes[TRUE_FALSE]=trueFalse.isChecked();
        questionTypes[SINGLE_CHOICE]=oneChoice.isChecked();
        questionTypes[MULTI_CHOICE]=multiChoice.isChecked();
        next= (Button) view.findViewById(R.id.next);
        FragmentManager fragmentManager=getFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        CompoundButton.OnCheckedChangeListener listener=new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                switch (buttonView.getId()){
                    case R.id.true_false:
                        questionTypes[TRUE_FALSE]=isChecked;
                        break;
                    case R.id.one_choice :
                        questionTypes[SINGLE_CHOICE]=isChecked;
                        break;
                    case R.id.multi_choice :
                        questionTypes[MULTI_CHOICE]=isChecked;
                        break;
                }

                updateTest();
                if (isAllTypesFalse()) {
                    next.setEnabled(false);
                    Toast.makeText(getContext(), "You must chosse one of Question type at least", Toast.LENGTH_LONG).show();
                } else {
                    next.setEnabled(true);
                }
            }
        };

        trueFalse.setOnCheckedChangeListener(listener);
        oneChoice.setOnCheckedChangeListener(listener);
        multiChoice.setOnCheckedChangeListener(listener);



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTest();
                testHandleCallback.testToActivity(test);
                Bundle bundle=new Bundle();
                bundle.putSerializable(TEST_BUNDLE_NAME,test);
                TestCategoriesFragment testCategoriesFragment=new TestCategoriesFragment();
                testCategoriesFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.test_relative_layout,testCategoriesFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


    }



    private void definingSeekBar(View view) {
        final TextView seekBarTxt= (TextView) view.findViewById(R.id.seekBarTxt);
        seekBar= (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setProgress(0);
        seekBar.setMax(18);
        seekBarTxt.setText(String.valueOf(myValues[seekBar.getProgress()])+" Questions");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarTxt.setText(String.valueOf(myValues[progress])+" Questions");
                updateTest();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }



    private void updateTest() {
        test.setQuestionTypes(questionTypes);
        test.setNumberOfQuestions(myValues[seekBar.getProgress()]);
        int answerShow = 0;
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radio1:
                answerShow = ANSWER_AFTER_EVERY;
                break;
            case R.id.radio2:
                answerShow = ANSWER_WHEN_WRONG;
                break;
            case R.id.radio3:
                answerShow = ANSWER_AFTER_ALL;
                break;
        }
        test.setAnswerShow(answerShow);
        Log.i("hhg","ok");
    }

    private boolean isAllTypesFalse() {
        for (boolean state : questionTypes) {
            if (state) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        testHandleCallback = (FragmentListener) activity;
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
