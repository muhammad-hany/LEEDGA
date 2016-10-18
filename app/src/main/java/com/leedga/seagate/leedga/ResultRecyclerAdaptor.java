package com.leedga.seagate.leedga;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;

/**
 * Created by Muhammad Workstation on 07/10/2016.
 */

public class ResultRecyclerAdaptor extends RecyclerView.Adapter<ResultRecyclerAdaptor.ViewHolder> {

    ArrayList<String> categoryNames;
    ArrayList<Integer> numberofQuestionsPerCategory;
    ArrayList<Boolean> userResult;
    int[] numberOfCorrectQuestionPerCategory;
    ArrayList<Question> questions;
    String ratio;
    private int position;
    private int[] correctPerCat;
    private int progressPrec;
    private OnMyItemClick listener;

    public ResultRecyclerAdaptor(ArrayList<String> categoryNames, ArrayList<Integer> numberofQuestionsPerCategory, ArrayList<Boolean> userResult, ArrayList<Question> questions, int progressPrec, String ratio, OnMyItemClick listener) {
        this.categoryNames = categoryNames;
        this.numberofQuestionsPerCategory = numberofQuestionsPerCategory;
        this.userResult = userResult;
        this.questions = questions;
        numberOfCorrectQuestionPerCategory = new int[categoryNames.size()];
        this.progressPrec = progressPrec;
        this.ratio = ratio;
        this.listener = listener;
    }

    @Override
    public ResultRecyclerAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(ResultRecyclerAdaptor.ViewHolder viewHolder, int position) {
        if (viewHolder.getItemViewType() == 0) {
            ProgressBarViewHolder progHolder = (ProgressBarViewHolder) viewHolder;
            progHolder.arcProgress.setProgress(progressPrec);
            progHolder.arcProgress.setBottomText(ratio);


        } else if (viewHolder.getItemViewType() == 1) {

        } else {
            ResultViewHolder holder = (ResultViewHolder) viewHolder;
            this.position = position - 2;
            holder.bind(this.position, listener);
            holder.categoryName.setText(categoryNames.get(this.position));
            correctPerCat = calculateResultPerCategory();
            holder.result.setText(String.valueOf(correctPerCat[this.position]) + "/" + String.valueOf(numberofQuestionsPerCategory.get(this.position)) + " Correct Answers");


            double x = ((double) correctPerCat[this.position] / (double) numberofQuestionsPerCategory.get(this.position)) * 100;
            holder.progress.setProgress((int) x);
            holder.progress.setInnerBackgroundColor(getColorForScore(gettingScore()));
            holder.precPerCateg.setText(String.valueOf((int) x) + "%");


        }
    }

    private int gettingScore() {
        double scoreDouble = correctPerCat[position];
        double noOfQuestionPerCat = numberofQuestionsPerCategory.get(position);
        double precDoubl = (scoreDouble / noOfQuestionPerCat) * 100;
        return (int) precDoubl;
    }

    @Override
    public int getItemCount() {
        return categoryNames.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private int getColorForScore(int score) {

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

    public interface onItemClickListener {
        void onItemClick(int position);
    }


    public interface OnMyItemClick {
        void onItemClick(int position);
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
        View container;
        DonutProgress progress;

        public ResultViewHolder(View convertView) {
            super(convertView);
            categoryName = (TextView) convertView.findViewById(R.id.categoryName);
            result = (TextView) convertView.findViewById(R.id.correct);
            colorResult = (RelativeLayout) convertView.findViewById(R.id.colorResult);
            precPerCateg = (TextView) convertView.findViewById(R.id.prec);
            progress = (DonutProgress) convertView.findViewById(R.id.item_donut);
            container = convertView;

        }

        public void bind(final int position, final OnMyItemClick listener) {
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }
    }

    public class ProgressBarViewHolder extends ViewHolder {
        ArcProgress arcProgress;
        TextView value;
        ProgressAnimator animator;

        public ProgressBarViewHolder(View itemView) {
            super(itemView);

            arcProgress = (ArcProgress) itemView.findViewById(R.id.arc_progress);
            value = (TextView) itemView.findViewById(R.id.value);
            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/adcc.ttf");
            value.setTypeface(typeface);
            animator = new ProgressAnimator(arcProgress, value, progressPrec);
            animator.setDuration(1000);
            animator.setInterpolator(new LinearInterpolator());
            arcProgress.setAnimation(animator);
            value.setAnimation(animator);


        }
    }

    public class ProgressAnimator extends Animation {
        ArcProgress progress;
        TextView textView;
        float to;

        public ProgressAnimator(ArcProgress progress, TextView textView, float to) {
            this.progress = progress;
            this.textView = textView;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = interpolatedTime * to;
            progress.setProgress((int) value);
            progress.setFinishedStrokeColor(getColorForScore((int) value));
            textView.setTextColor(getColorForScore((int) value));
            textView.setText(String.valueOf((int) value));

        }
    }


}
