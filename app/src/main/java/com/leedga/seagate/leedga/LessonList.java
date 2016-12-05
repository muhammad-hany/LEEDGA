package com.leedga.seagate.leedga;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class LessonList extends Fragment {


    public LessonList() {
        // Required empty public constructor
    }


    public static LessonList newInstance() {
        return new LessonList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.lessonList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout
                .simple_list_item_1, android.R.id.text1, generateNames(10));
        listView.setAdapter(adapter);

        return view;
    }

    private String[] generateNames(int rounds) {
        String[] names = new String[rounds];
        for (int i = 0; i < rounds; i++) {
            names[i] = "Lesson " + String.valueOf(i + 1);
        }
        return names;
    }


}
