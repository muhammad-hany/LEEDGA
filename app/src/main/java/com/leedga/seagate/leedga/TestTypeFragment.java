package com.leedga.seagate.leedga;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;


public class TestTypeFragment extends Fragment {
    Button next;
    Switch trueFalse;
    Switch oneChoice;
    Switch multiChoice;
    View view;
    Test test;
    public static final String TEST_BUNDLE_NAME="test";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNewTest();
    }

    private void createNewTest() {
        test=new Test();
        DBHelper dbHelper=new DBHelper(getContext(), TestFragment.DATABASE_NAME);
        test.prepaireQuestions(dbHelper);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_test_type, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        trueFalse= (Switch) view.findViewById(R.id.true_false);
        oneChoice= (Switch) view.findViewById(R.id.one_choice);
        multiChoice= (Switch) view.findViewById(R.id.multi_choice);
        next= (Button) view.findViewById(R.id.next);
        FragmentManager fragmentManager=getFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        CompoundButton.OnCheckedChangeListener listener=new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateTest();
            }
        };

        trueFalse.setOnCheckedChangeListener(listener);
        oneChoice.setOnCheckedChangeListener(listener);
        multiChoice.setOnCheckedChangeListener(listener);



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTest();
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




    private void updateTest() {
        test.setTypeMultiChoice(multiChoice.isChecked());
        test.setTypeOneChoice(oneChoice.isChecked());
        test.setTypeTrueFalse(trueFalse.isChecked());
    }
}
