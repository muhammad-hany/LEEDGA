package com.leedga.seagate.leedga;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;


public class TestCategoriesFragment extends Fragment {

    Button next;
    Test test;
    Switch s1,s2,s3,s4,s5,s6,s7,s8,s9;
    static final String TEST_BUNDLE="test2";

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
                Log.i("ggg",String.valueOf(buttonView.getId())+"was clicked");
            }
        };
        definingSwitches(view ,listener);
        next= (Button) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                TestFragment singleChoiceFragment=new TestFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable(TEST_BUNDLE,test);
                singleChoiceFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.test_relative_layout,singleChoiceFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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






}
