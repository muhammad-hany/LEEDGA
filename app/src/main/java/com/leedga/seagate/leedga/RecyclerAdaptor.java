package com.leedga.seagate.leedga;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;

/**
 * Created by Muhammad Workstation on 07/10/2016.
 */

public class RecyclerAdaptor extends RecyclerView.Adapter<RecyclerAdaptor.ViewHolder> {

    ArrayList<String> categoryNames;
    ArrayList<Integer> numberofQuestionsPerCategory;
    ArrayList<Boolean> userResult;
    int[] numberOfCorrectQuestionPerCategory;
    ArrayList<Question> questions;
    private int position;
    private int[] correctPerCat;

    public RecyclerAdaptor(ArrayList<String> categoryNames, ArrayList<Integer> numberofQuestionsPerCategory, ArrayList<Boolean> userResult, ArrayList<Question> questions) {
        this.categoryNames = categoryNames;
        this.numberofQuestionsPerCategory = numberofQuestionsPerCategory;
        this.userResult = userResult;
        this.questions = questions;
        numberOfCorrectQuestionPerCategory = new int[categoryNames.size()];
    }

    @Override
    public RecyclerAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_card, parent, false);
            return new ProgressBarViewHolder(v);
        } else if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_card, parent, false);
            return new ChartViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_card, parent, false);
            return new ResultViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerAdaptor.ViewHolder viewHolder, int position) {
        if (viewHolder.getItemViewType() == 0 || viewHolder.getItemViewType() == 1) {

        } else {
            ResultViewHolder holder = (ResultViewHolder) viewHolder;
            this.position = position;
            holder.categoryName.setText(categoryNames.get(position));
            correctPerCat = calculateResultPerCategory();
            holder.result.setText(String.valueOf(correctPerCat[position]) + "/" + String.valueOf(numberofQuestionsPerCategory.get(position)) + " Correct Answers");
            holder.colorResult.setBackgroundColor(getColorForCategory());
            double x = ((double) correctPerCat[position] / (double) numberofQuestionsPerCategory.get(position)) * 100;
            holder.precPerCateg.setText(String.valueOf((int) x) + "%");

        }
    }

    @Override
    public int getItemCount() {
        return categoryNames.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private int getColorForCategory() {
        double scoreDouble = correctPerCat[position];
        double noOfQuestionPerCat = numberofQuestionsPerCategory.get(position);
        double precDoubl = (scoreDouble / noOfQuestionPerCat) * 100;
        int score = (int) precDoubl;

        if (score >= 0 && score < 20) {
            return Color.rgb(237, 27, 36);
        } else if (score >= 20 && score < 40) {
            return Color.rgb(243, 112, 32);
        } else if (score >= 40 && score < 55) {
            return Color.rgb(252, 185, 19);
        } else if (score >= 55 && score < 70) {
            return Color.rgb(254, 242, 0);
        } else if (score >= 70 && score < 80) {
            return Color.rgb(92, 215, 49);
        } else if (score >= 80 && score < 90) {
            return Color.rgb(80, 184, 73);
        } else if (score >= 90 && score <= 100) {
            return Color.rgb(0, 166, 82);
        }
        return 0;
    }

    int[] calculateResultPerCategory() {
        numberOfCorrectQuestionPerCategory = new int[categoryNames.size()];
        for (int i = 0; i < categoryNames.size(); i++) {
            int j = 0;
            for (Question question : questions) {
                if (categoryNames.get(i).equals(question.getCategory())) {
                    if (userResult.get(j)) {
                        numberOfCorrectQuestionPerCategory[i]++;
                    }
                }
                j++;
            }

        }

        return numberOfCorrectQuestionPerCategory;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

    class ChartViewHolder extends ViewHolder {

        RadarChart mChart;

        public ChartViewHolder(View view) {
            super(view);
            this.mChart = (RadarChart) view.findViewById(R.id.radarChart);
            settingRadarChart();
        }


        private void settingRadarChart() {
            mChart.setBackgroundColor(Color.WHITE);
            mChart.setWebLineWidth(1f);
            mChart.setWebColor(Color.LTGRAY);
            mChart.setWebLineWidthInner(1f);
            mChart.setWebColorInner(Color.LTGRAY);
            mChart.setDescription("");
            mChart.setWebAlpha(100);

            setData();

        }

        private void setData() {
            ArrayList<RadarEntry> entries = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                entries.add(new RadarEntry(/*adapter.calculateResultPerCategory()[i]*/20 * i));
            }
            RadarDataSet set1 = new RadarDataSet(entries, "TestResult");
            set1.setColor(Color.rgb(103, 110, 129));
            set1.setFillColor(Color.rgb(103, 110, 129));
            set1.setDrawFilled(true);
            set1.setFillAlpha(180);
            set1.setLineWidth(2f);
            set1.setDrawHighlightCircleEnabled(true);
            set1.setDrawHighlightIndicators(false);

            ArrayList<IRadarDataSet> sets = new ArrayList<>();
            sets.add(set1);
            RadarData data = new RadarData(sets);
            data.setValueTextSize(8f);
            data.setDrawValues(true);
            data.setValueTextColor(Color.RED);
            mChart.setData(data);
            mChart.getYAxis().setEnabled(false);
            mChart.invalidate();
        }
    }

    class ResultViewHolder extends ViewHolder {
        TextView categoryName;
        TextView result;
        RelativeLayout colorResult;
        TextView precPerCateg;

        public ResultViewHolder(View convertView) {
            super(convertView);
            categoryName = (TextView) convertView.findViewById(R.id.categoryName);
            result = (TextView) convertView.findViewById(R.id.correct);
            colorResult = (RelativeLayout) convertView.findViewById(R.id.colorResult);
            precPerCateg = (TextView) convertView.findViewById(R.id.prec);
        }
    }

    public class ProgressBarViewHolder extends ViewHolder {
        ArcProgress arcProgress;
        TextView value;

        public ProgressBarViewHolder(View itemView) {
            super(itemView);

            arcProgress = (ArcProgress) itemView.findViewById(R.id.arc_progress);
            value = (TextView) itemView.findViewById(R.id.value);
            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/adcc.ttf");
            value.setTypeface(typeface);


        }
    }
}
