package com.leedga.seagate.leedga;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Muhammad Workstation on 07/10/2016.
 */

public class MainRecyclerAdaptor extends RecyclerView.Adapter<MainRecyclerAdaptor.ViewHolder> {


    ArrayList<Test> tests;
    private int[] cardsIds;
    private Context context;
    private OnMyItemClick listener;
    private int[] imagesIds;

    public MainRecyclerAdaptor(Context context, OnMyItemClick listener, ArrayList<Test> tests) {
        cardsIds = new int[]{R.layout.main_card_chart, R.layout.main_card};

        this.context = context;
        this.tests = tests;
        this.listener = listener;
        gettingDrawables();

    }

    public void setTests(ArrayList<Test> tests) {
        this.tests = tests;
    }

    private void gettingDrawables() {
        imagesIds = new int[]{R.drawable.test, R.drawable.testsetting_, R.drawable.testhistory_, R.drawable.dayquestion_, R.drawable.studying_, R.drawable.settings_, R.drawable.terms_, R.drawable.law_, R.drawable.calendar_};
    }

    @Override
    public MainRecyclerAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        /*if (viewType==0){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_chart, parent, false);
        }else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card, parent, false);
        }*/
        v = LayoutInflater.from(parent.getContext()).inflate(cardsIds[viewType], parent, false);
        return getProperHandler(v, viewType);

    }

    private ViewHolder getProperHandler(View v, int viewType) {
        ViewHolder holder = null;
        switch (viewType) {
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
        if (position != 0) {
            /*((MainMenuHolder)viewHolder).icon.setImageResource(imagesIds[position-1]);*/
            Picasso.with(context).load(imagesIds[position - 1]).into(((MainMenuHolder) viewHolder).icon);
            ((MainMenuHolder) viewHolder).bind(position, listener);
        }
        switch (position) {
            case 0:
                MainChartHolder holder = (MainChartHolder) viewHolder;
                holder.donutProgress.setProgress(getMainPercentage());
                holder.donutProgress.animate();
                holder.bigNum2.setText(String.valueOf(tests.size()));
                holder.mainText.setText(String.valueOf(getMainPercentage()));
                holder.bigNum1.setText(String.valueOf(getTotalCorrectQuestions() + "/" + String.valueOf(getTotalAnsweredQuestions())));
                break;
            case 1:
                ((MainMenuHolder) viewHolder).textView.setText("Test");
                break;
            case 2:
                ((MainMenuHolder) viewHolder).textView.setText("Test Setting");
                break;
            case 3:
                ((MainMenuHolder) viewHolder).textView.setText("Test History");
                break;
            case 4:
                ((MainMenuHolder) viewHolder).textView.setText("Question of The Day");
                break;
            case 5:
                ((MainMenuHolder) viewHolder).textView.setText("Lessons");
                break;
            case 6:
                ((MainMenuHolder) viewHolder).textView.setText("General Setting");
                break;
            case 7:
                ((MainMenuHolder) viewHolder).textView.setText("Key Terms and definitions");
                break;
            case 8:
                ((MainMenuHolder) viewHolder).textView.setText("Reference Materials");
                break;
            case 9:

                ((MainMenuHolder) viewHolder).textView.setText(getTimeDifference());
                break;


        }
    }

    private String getTimeDifference() {
        SharedPreferences pref = context.getSharedPreferences(REF.GENERAL_SETTING_PREF, Context
                .MODE_PRIVATE);
        Gson gson = new Gson();
        String date = pref.getString(REF.SCHEDULE_EXAM_DATE_PREF, null);
        if (date == null) {
            return "not set yet";
        }
        Calendar calendar = gson.fromJson(date, Calendar.class);
        float diff = (float) (calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / (float) AlarmManager.INTERVAL_DAY;

        return String.valueOf((int) diff) + " days\nuntil exam";
    }

    private int getMainPercentage() {
        int comulativeScore = 0;
        for (Test test : tests) {
            comulativeScore = comulativeScore + Integer.parseInt(test.getTestPercentage());
        }
        return (int) ((float) comulativeScore / (float) tests.size());
    }

    private int getTotalCorrectQuestions() {
        int scoreCount = 0;
        for (Test test : tests) {
            for (int i = 0; i < test.getUserResult().size(); i++) {
                if (test.getUserResult().get(i)) {
                    scoreCount++;
                }
            }
        }
        return scoreCount;
    }

    private int getTotalAnsweredQuestions() {
        int counter = 0;
        for (Test test : tests) {
            counter = counter + test.getNumberOfAnsweredQuestions();
        }
        return counter;
    }


    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {

        /*switch (position) {
            case 0:
                return 0;
            default:
                return 1;
        }*/
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
        /*return position;*/

    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }


    public interface OnMyItemClick {
        void onItemClick(int position);

        void onDetailsClick();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

    class MainChartHolder extends ViewHolder {
        TextView prec, bigNum1, bigNum2, smallText1, smallText2, mainText, details;
        DonutProgress donutProgress;
        public MainChartHolder(View view) {
            super(view);

            prec = (TextView) view.findViewById(R.id.prec);
            bigNum1 = (TextView) view.findViewById(R.id.bigNum1);
            bigNum2 = (TextView) view.findViewById(R.id.bigNum2);
            smallText1 = (TextView) view.findViewById(R.id.smallText1);
            smallText2 = (TextView) view.findViewById(R.id.smallText2);
            mainText = (TextView) view.findViewById(R.id.mainText);
            details = (TextView) view.findViewById(R.id.details);

            Typeface big = Typeface.createFromAsset(context.getAssets(), "fonts/big.otf");
            Typeface small = Typeface.createFromAsset(context.getAssets(), "fonts/arvo.ttf");


            smallText1.setTypeface(small);
            smallText2.setTypeface(small);
            details.setTypeface(small);

            bigNum1.setTypeface(big);
            bigNum2.setTypeface(big);
            mainText.setTypeface(big);

            donutProgress = (DonutProgress) view.findViewById(R.id.donutProgress);

            smallText2.setText("Exams");
            smallText1.setText("Successful\nQuestions");
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.detailsC);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDetailsClick();
                }
            });
        }





    }

    public class MainMenuHolder extends ViewHolder {
        ImageView icon;
        TextView textView;
        CardView cardView;

        public MainMenuHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView15);
            cardView = (CardView) itemView.findViewById(R.id.card);
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                cardView.setRadius(0);
            }
        }


        public void bind(final int position, final OnMyItemClick onMyItemClick) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMyItemClick.onItemClick(position);
                }
            });
        }
    }


}
