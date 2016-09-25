package com.leedga.seagate.leedga;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Collections;


public class TestCategoriesFragment extends Fragment  {

    RelativeLayout next;
    Test test;
    Switch s1,s2,s3,s4,s5,s6,s7,s8,s9;
    static final String TEST_BUNDLE="test2";
    public static int LEED_PROCESS=0;
    public static int INTEGRATIVE_STRATEGIES=1;
    public static int LOCATION_AND_TRANSPORTATION=2;
    public static int SUSTAINABLE_SITES=3;
    public static int PROJECT_SURROUNDINGS=4;
    public static int WATER_EFFICIENCY=5;
    public static int ENERGY_AND_ATMOSPHERE=6;
    public static int MATERIAL_AND_RESOURCES=7;
    public static int INDOOR_ENVIRO_QUALITY=8;

    boolean[] chapters=new boolean[9];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_test_categories, container, false);
        Bundle bundle=getArguments();
        test= (Test) bundle.getSerializable(TestTypeFragment.TEST_BUNDLE_NAME);
        initViews(view);
        updateSwitchesStatusTest();
        return view;
    }

    public static Fragment init(){
        return new TestCategoriesFragment();
    }

    private void updateSwitchesStatusTest() {
        Switch [] switches={s1,s2,s3,s4,s5,s6,s7,s8,s9};
        for (int i=0 ; i<9;i++){
            if (switches.length>0) {
                chapters[i] = switches[i].isChecked();
            }

        }
        test.setChapters(chapters);
    }

    private void initViews(View view) {
        CompoundButton.OnCheckedChangeListener listener=new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSwitchesStatusTest();
            }
        };
        definingSwitches(view ,listener);
        next= (RelativeLayout) view.findViewById(R.id.nextt);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Question> questions=new DBHelper(getContext(),TestFragment.DATABASE_NAME).getAll(test.getChapters(),test.getcountPerCategory(),test.getQuestionTypes());
                Collections.shuffle(questions);
                test.setQuestions(questions);
                Intent i=new Intent(getContext(),TestActivity.class);
                i.putExtra(TEST_BUNDLE,test);
                startActivity(i);
            }
        });
    }



    private void definingSwitches(View view, CompoundButton.OnCheckedChangeListener listener) {
        s1= (Switch) view.findViewById(R.id.switch1);
        s2= (Switch) view.findViewById(R.id.switch2);
        s3= (Switch) view.findViewById(R.id.switch3);
        s4= (Switch) view.findViewById(R.id.switch4);
        s5= (Switch) view.findViewById(R.id.switch5);
        s6= (Switch) view.findViewById(R.id.switch6);
        s7= (Switch) view.findViewById(R.id.switch7);
        s8= (Switch) view.findViewById(R.id.switch8);
        s9= (Switch) view.findViewById(R.id.switch9);
        s1.setOnCheckedChangeListener(listener);
        s2.setOnCheckedChangeListener(listener);
        s3.setOnCheckedChangeListener(listener);
        s4.setOnCheckedChangeListener(listener);
        s5.setOnCheckedChangeListener(listener);
        s6.setOnCheckedChangeListener(listener);
        s7.setOnCheckedChangeListener(listener);
        s8.setOnCheckedChangeListener(listener);
        s9.setOnCheckedChangeListener(listener);

        updateSwitchesStatusTest();

    }


    public void setTest(Test test){
        this.test=test;
    }




}
