package com.leedga.seagate.leedga;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.leedga.seagate.leedga.REF.LESSONS_KEY;


public class LessonsMenuFragment extends Fragment {


    public LessonsMenuFragment() {
        // Required empty public constructor
    }

    public static LessonsMenuFragment newInstance() {
        LessonsMenuFragment fragment = new LessonsMenuFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((LessonsActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View view = inflater.inflate(R.layout.fragment_lessons_menu, container, false);
        ((LessonsActivity) getActivity()).getSupportActionBar().setTitle("Lessons");
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.lessons_recyc);
        LessonsRecyclerAdapter adapter = new LessonsRecyclerAdapter(getContext(), new LessonsRecyclerAdapter.LessonsItemClick() {
            @Override
            public void onItemClick(int position) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_lessons, LessonShowFragment.newInstance(position, LESSONS_KEY));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        return view;
    }


}
