package com.leedga.seagate.leedga;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DayQuestionFragment extends Fragment {


    public DayQuestionFragment() {
        // Required empty public constructor
    }

    public static Fragment init(Question question) {
        DayQuestionFragment fragment = new DayQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(REF.SINGLE_QUESTION, question);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day_question, container, false);
    }

}
