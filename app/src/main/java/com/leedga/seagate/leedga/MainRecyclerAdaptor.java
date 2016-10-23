package com.leedga.seagate.leedga;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

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
                holder1.icon.setImageResource(R.drawable.ic_terms);
                break;
            case 6:
                holder1 = (MainMenuHolder) viewHolder;
                holder1.textView.setText("Reference Materials");
                holder1.icon.setImageResource(R.drawable.ic_law);
                break;
            case 7:
                holder1 = (MainMenuHolder) viewHolder;
                holder1.textView.setText("Rate us");
                holder1.icon.setImageResource(R.drawable.ic_rating);
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
        return 9;
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



    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

    class MainChartHolder extends ViewHolder {
        TextView prec, bigNum1, bigNum2, smallText1, smallText2, mainText;
        DonutProgress donutProgress;
        public MainChartHolder(View view) {
            super(view);

            prec = (TextView) view.findViewById(R.id.prec);
            bigNum1 = (TextView) view.findViewById(R.id.bigNum1);
            bigNum2 = (TextView) view.findViewById(R.id.bigNum2);
            smallText1 = (TextView) view.findViewById(R.id.smallText1);
            smallText2 = (TextView) view.findViewById(R.id.smallText2);
            mainText = (TextView) view.findViewById(R.id.mainText);

            Typeface big = Typeface.createFromAsset(context.getAssets(), "fonts/big.otf");
            Typeface small = Typeface.createFromAsset(context.getAssets(), "fonts/slim.otf");


            smallText1.setTypeface(small);
            smallText2.setTypeface(small);

            bigNum1.setTypeface(big);
            bigNum2.setTypeface(big);
            mainText.setTypeface(big);

            donutProgress = (DonutProgress) view.findViewById(R.id.donutProgress);
        }





    }

    public class MainMenuHolder extends ViewHolder {
        ImageView icon;
        TextView textView;

        public MainMenuHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }


}
