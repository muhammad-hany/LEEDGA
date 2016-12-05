package com.leedga.seagate.leedga;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.RadarChart;

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
    int score, totalNumberQuestions;
    private int position;
    private int[] correctPerCat;
    private int progressPrec;
    private OnMyItemClick listener;
    private Context context;
    private String testDate;

    public ResultRecyclerAdaptor(Context context, ArrayList<String> categoryNames, ArrayList<Integer> numberofQuestionsPerCategory, ArrayList<Boolean> userResult, ArrayList<Question> questions, int progressPrec, int score, int totalNumberQuestions, OnMyItemClick listener, String testDate) {
        this.categoryNames = categoryNames;
        this.numberofQuestionsPerCategory = numberofQuestionsPerCategory;
        this.userResult = userResult;
        this.questions = questions;
        numberOfCorrectQuestionPerCategory = new int[categoryNames.size()];
        this.progressPrec = progressPrec;
        this.score = score;
        this.totalNumberQuestions = totalNumberQuestions;
        this.listener = listener;
        this.context = context;
        this.testDate = testDate;

    }

    @Override
    public ResultRecyclerAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_card, parent, false);
            return new ProgressBarViewHolder(v);
        } /*else if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_card, parent, false);
            return new ChartViewHolder(v);
        }*/ else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_card, parent, false);
            return new ResultViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(ResultRecyclerAdaptor.ViewHolder viewHolder, int position) {
        if (viewHolder.getItemViewType() == 0) {
            ProgressBarViewHolder progHolder = (ProgressBarViewHolder) viewHolder;
            progHolder.arcProgress.setProgress(progressPrec);
            progHolder.allQ.setText(String.valueOf(totalNumberQuestions));
            progHolder.corrctQ.setText(String.valueOf(score));
            /*progHolder.arcProgress.setBottomText(ratio);*/


        } else {
            ResultViewHolder holder = (ResultViewHolder) viewHolder;
            this.position = position - 1;
            holder.bind(this.position, listener);
            holder.categoryName.setText(categoryNames.get(this.position));
            correctPerCat = calculateResultPerCategory();
            int x = correctPerCat[this.position];
            int y = numberofQuestionsPerCategory.get(this.position);
            holder.result.setText(String.valueOf(x) + "/" + String.valueOf(y) + " Correct Answers");


            double v = ((double) correctPerCat[this.position] / (double) numberofQuestionsPerCategory.get(this.position)) * 100;
            /*holder.progress.setProgress((int) x);
            holder.progress.setInnerBackgroundColor(getColorForScore(gettingScore()));*/
            holder.precPerCateg.setText(String.valueOf((int) v) + "%");
            holder.colorResult.setBackgroundColor(getColorForScore(gettingScore()));

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
        return numberofQuestionsPerCategory.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private int getColorForScore(int score) {

        if (score >= 0 && score < 20) {
            return Color.argb(140, 237, 27, 36);
        } else if (score >= 20 && score < 40) {
            return Color.argb(140, 243, 112, 32);
        } else if (score >= 40 && score < 55) {
            return Color.argb(140, 252, 185, 19);
        } else if (score >= 55 && score < 70) {
            return Color.argb(140, 254, 242, 0);
        } else if (score >= 70 && score < 80) {
            return Color.argb(140, 92, 215, 49);
        } else if (score >= 80 && score < 90) {
            return Color.argb(140, 80, 184, 73);
        } else if (score >= 90 && score <= 100) {
            return Color.argb(140, 0, 166, 82);
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

        void onShowAllClick();
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

            //  setData();

        }

        /*private void setData() {
            ArrayList<RadarEntry> entries = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                entries.add(new RadarEntry(*//*adapter.calculateResultPerCategory()[i]*//*20 * i));
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
        }*/
    }

    class ResultViewHolder extends ViewHolder {
        TextView categoryName;
        TextView result;
        RelativeLayout colorResult;
        TextView precPerCateg;
        View container;
        DonutProgress progress;
        CardView cardView;

        public ResultViewHolder(View convertView) {
            super(convertView);
            categoryName = (TextView) convertView.findViewById(R.id.categoryName);
            result = (TextView) convertView.findViewById(R.id.correct);
            colorResult = (RelativeLayout) convertView.findViewById(R.id.colorResult);
            precPerCateg = (TextView) convertView.findViewById(R.id.prec);
            /*progress = (DonutProgress) convertView.findViewById(R.id.item_donut);*/
            cardView = (CardView) convertView.findViewById(R.id.result_card);
            container = convertView;

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                cardView.setRadius(0);
            }

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
        DonutProgress arcProgress;
        TextView value, corrctQ, allQ;
        ProgressAnimator animator;
        LinearLayout showAll;

        public ProgressBarViewHolder(View itemView) {
            super(itemView);

            arcProgress = (DonutProgress) itemView.findViewById(R.id.progressba);
            value = (TextView) itemView.findViewById(R.id.mainText);
            Typeface big = Typeface.createFromAsset(context.getAssets(), "fonts/big.otf");
            Typeface small = Typeface.createFromAsset(context.getAssets(), "fonts/arvo.ttf");
            value.setTypeface(big);

            corrctQ = (TextView) itemView.findViewById(R.id.bigNum1);
            corrctQ.setTypeface(big);
            allQ = (TextView) itemView.findViewById(R.id.bigNum2);
            allQ.setTypeface(big);
            TextView details = (TextView) itemView.findViewById(R.id.details);
            details.setTypeface(small);
            ((TextView) itemView.findViewById(R.id.smallText1)).setTypeface(small);
            ((TextView) itemView.findViewById(R.id.smallText2)).setTypeface(small);
            animator = new ProgressAnimator(arcProgress, value, progressPrec);
            animator.setDuration(1000);
            animator.setInterpolator(new LinearInterpolator());
            arcProgress.setAnimation(animator);
            value.setAnimation(animator);
            showAll = (LinearLayout) itemView.findViewById(R.id.showAll);
            showAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onShowAllClick();
                }
            });
            TextView dateText = (TextView) itemView.findViewById(R.id.date);
            dateText.setText(testDate);
            dateText.setTypeface(small);


        }
    }

    public class ProgressAnimator extends Animation {
        DonutProgress progress;
        TextView textView;
        float to;

        public ProgressAnimator(DonutProgress progress, TextView textView, float to) {
            this.progress = progress;
            this.textView = textView;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = interpolatedTime * to;
            progress.setProgress((int) value);
            textView.setText(String.valueOf((int) value));

        }
    }


}
