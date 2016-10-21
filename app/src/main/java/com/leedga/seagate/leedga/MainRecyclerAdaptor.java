package com.leedga.seagate.leedga;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * Created by Muhammad Workstation on 07/10/2016.
 */

public class MainRecyclerAdaptor extends RecyclerView.Adapter<MainRecyclerAdaptor.ViewHolder> {


    static ArrayList<Test> tests;
    private int[] cardsIds;
    private Context context;
    private OnMyItemClick listener;

    public MainRecyclerAdaptor(Context context, OnMyItemClick listener, ArrayList<Test> tests) {
        cardsIds = new int[]{R.layout.main_card_chart, R.layout.main_card};

        this.context = context;
        MainRecyclerAdaptor.tests = tests;
    }

    @Override
    public MainRecyclerAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        v = LayoutInflater.from(parent.getContext()).inflate(cardsIds[viewType], parent, false);
        return getProperHandler(v, viewType);

    }

    private ViewHolder getProperHandler(View v, int position) {
        ViewHolder holder = null;
        switch (position) {
            case 0:
                holder = new MainChartHolder(v);
                break;
            default:
                holder = new MainMenuHolder(v);
                break;


        }
        return holder;
    }

    @Override
    public void onBindViewHolder(MainRecyclerAdaptor.ViewHolder viewHolder, int position) {
        MainMenuHolder holder1;
        switch (position) {
            case 0:
                MainChartHolder holder = (MainChartHolder) viewHolder;
                /*holder.setData();*/
                break;
            case 1:
                holder1 = (MainMenuHolder) viewHolder;
                holder1.textView.setText("Test");
                holder1.icon.setImageResource(R.drawable.ic_test);
                break;
            case 2:
                holder1 = (MainMenuHolder) viewHolder;
                holder1.textView.setText("Test History");
                holder1.icon.setImageResource(R.drawable.ic_history);
                break;
            case 3:
                holder1 = (MainMenuHolder) viewHolder;
                holder1.textView.setText("Lessons");
                holder1.icon.setImageResource(R.drawable.ic_studying);
                break;
            case 4:
                holder1 = (MainMenuHolder) viewHolder;
                holder1.textView.setText("General Setting");
                holder1.icon.setImageResource(R.drawable.ic_settings);
                break;
            case 5:
                holder1 = (MainMenuHolder) viewHolder;
                holder1.textView.setText("Key Terms and definitions");
                holder1.icon.setImageResource(R.drawable.ic_job_search);
                break;
            case 6:
                holder1 = (MainMenuHolder) viewHolder;
                holder1.textView.setText("Reference Materials");
                holder1.icon.setImageResource(R.drawable.ic_law);
                break;
            case 7:
                holder1 = (MainMenuHolder) viewHolder;
                holder1.textView.setText("Rate us");
                holder1.icon.setImageResource(R.drawable.ic_star);
                break;
            case 8:
                holder1 = (MainMenuHolder) viewHolder;
                holder1.textView.setText("3 days until Exam");
                holder1.icon.setImageResource(R.drawable.ic_calendar);
                break;


        }
    }

    private View getCard(int position) {


        return null;
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    @Override
    public int getItemViewType(int position) {

        switch (position) {
            case 0:
                return 0;
            default:
                return 1;
        }

    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }


    public interface OnMyItemClick {
        void onItemClick(int position);
    }

    public static class ChartFragment extends Fragment {
        BarChart barChart;


        public ChartFragment() {

        }

        @Override
        public void onResume() {
            super.onResume();
            if (barChart != null) {
                barChart.animateXY(2000, 2000);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View root = inflater.inflate(R.layout.fragment_main_chart, container, false);
            definingChart(root);
            setData(getContext(), tests);
            return root;
        }


        private void definingChart(View view) {
            barChart = (BarChart) view.findViewById(R.id.lineChart);
            barChart.setDrawBarShadow(false);
            barChart.setDrawValueAboveBar(true);
            barChart.setDescription("");
            barChart.setMaxVisibleValueCount(60);
            barChart.setPinchZoom(false);
            barChart.setDrawGridBackground(false);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
            xAxis.setDrawGridLines(false);
            xAxis.setEnabled(false);

            YAxis yAxis = barChart.getAxisLeft();
            yAxis.setAxisMinValue(0);
            yAxis.setDrawGridLines(false);
            yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            yAxis.setEnabled(false);

            barChart.getAxisRight().setEnabled(false);
        }

        public void setData(Context context, ArrayList<Test> tests) {

            ArrayList<BarEntry> values = new ArrayList<>();
            ArrayList<Integer> scorePerCategory = calculateResultPerCategory(tests);
            ArrayList<String> labels = new ArrayList<>();


            for (int i = 0; i < scorePerCategory.size(); i++) {
                values.add(new BarEntry(scorePerCategory.get(i), i));
                labels.add(String.valueOf(i + 1));

            }


            BarDataSet barDataSet;
            barDataSet = new BarDataSet(values, "Correct Questions");
            barDataSet.setLabel("");
            barDataSet.setBarSpacePercent(20f);
            TypedArray typedColors = context.getResources().obtainTypedArray(R.array.my_colors);
            int[] myColors = new int[typedColors.length()];
            for (int i = 0; i < typedColors.length(); i++) {
                myColors[i] = typedColors.getColor(i, 0);
            }
            barDataSet.setColors(myColors);
            barDataSet.setDrawValues(true);

            BarData data = new BarData(labels, barDataSet);

            data.setValueTextSize(10f);
            barChart.setData(data);
            barChart.getLegend().setEnabled(false);


        }

        public ArrayList<Integer> calculateResultPerCategory(ArrayList<Test> tests) {

            ArrayList<Integer> numberOfCorrectQuestionPerCategory = new ArrayList<>();
            for (Test test : tests) {
                ArrayList<Question> questions = test.getAnsweredQuestions();
                ArrayList<Boolean> userResult = test.getUserResult();
                for (int i = 0; i < DBHelper.CATEGORY_NAMES.length; i++) {
                    int j = 0;
                    for (Question question : questions) {
                        if (DBHelper.CATEGORY_NAMES[i].equals(question.getCategory())) {
                            if (userResult.get(j)) {
                                try {
                                    numberOfCorrectQuestionPerCategory.set(i, numberOfCorrectQuestionPerCategory.get(i) + 1);
                                } catch (IndexOutOfBoundsException e) {
                                    numberOfCorrectQuestionPerCategory.add(1);
                                }

                            } else {
                                try {
                                    numberOfCorrectQuestionPerCategory.set(i, numberOfCorrectQuestionPerCategory.get(i)
                                    );
                                } catch (IndexOutOfBoundsException e) {
                                    numberOfCorrectQuestionPerCategory.add(0);
                                }
                            }
                        }
                        j++;
                    }

                }
            }

            return numberOfCorrectQuestionPerCategory;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

    class MainChartHolder extends ViewHolder {
        ViewPager pager;

        public MainChartHolder(View view) {
            super(view);


            pager = (ViewPager) view.findViewById(R.id.mainPager);
            ChartPagerAdapter adapter = new ChartPagerAdapter(((MainActivity) context).getSupportFragmentManager());
            pager.setAdapter(adapter);
            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(pager);


        }


        public class ChartPagerAdapter extends FragmentPagerAdapter {

            public ChartPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                return new ChartFragment();
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                CharSequence sequence = new String();
                switch (position) {
                    case 0:
                        sequence = "Correct Questions";
                        break;
                    case 1:
                        sequence = "Wrong Questions";
                        break;
                }
                return sequence;
            }
        }


    }

    public class MainMenuHolder extends ViewHolder {
        ImageView icon;
        TextView textView;

        public MainMenuHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.image);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }


}
