package com.leedga.seagate.leedga;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Muhammad Workstation on 21/09/2016.
 */
public class TestListAdapter extends ArrayAdapter<String>  {


    String [] categoryNames;
    int [] numberofQuestionsPerCategory;
    boolean [] userResult;
    int [] numberOfCorrectQuestionPerCategory;
    ArrayList<Question> questions;
    int position;
    private int[] correctPerCat;


    public TestListAdapter(Context context, String [] categoryNames, int [] numberofQuestionsPerCategory, boolean[] userResult, ArrayList<Question> questions) {
        super(context, R.layout.test_row,categoryNames);

        this.categoryNames=categoryNames;
        this.numberofQuestionsPerCategory=numberofQuestionsPerCategory;
        this.userResult=userResult;
        this.questions=questions;
        numberOfCorrectQuestionPerCategory=new int[categoryNames.length];

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        if (convertView==null){
            LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView=inflater.inflate(R.layout.result_row,null);

            holder=new ViewHolder();
            holder.categoryName= (TextView) convertView.findViewById(R.id.categoryName);
            holder.result= (TextView) convertView.findViewById(R.id.correct);
            holder.colorResult= (LinearLayout) convertView.findViewById(R.id.colorResult);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        this.position=position;

        holder.categoryName.setText(categoryNames[position]);
         correctPerCat=calculateResultPerCategory();
        holder.result.setText(String.valueOf(correctPerCat[position])+"/"+String.valueOf(numberofQuestionsPerCategory[position])+" Correct Answers");
        holder.colorResult.setBackgroundColor(getColorForCategory());
        return convertView;
    }

    private int getColorForCategory() {
        double scoreDouble=correctPerCat[position];
        double noOfQuestionPerCat=numberofQuestionsPerCategory[position];
        double precDoubl=(scoreDouble/noOfQuestionPerCat)*100;
        int score=(int) precDoubl;

        if (score>=0 && score<20 ){
            return Color.rgb(237,27,36);
        }else if (score>=20 && score<40){
            return Color.rgb(243,112,32);
        }else if (score>=40 && score<55){
            return Color.rgb(252,185,19 );
        }else if (score>=55 && score<70){
            return Color.rgb(254,242,0);
        }else if (score>=70 && score<80){
            return Color.rgb(92,215,49);
        }else if (score>=80 && score<90){
            return Color.rgb(80,184,73);
        }else if (score>=90 && score<=100){
            return Color.rgb(0,166,82);
        }
        return 0;
    }

    int [] calculateResultPerCategory(){
        numberOfCorrectQuestionPerCategory=new int[categoryNames.length];
        for (int i=0;i<categoryNames.length;i++){
            int j=0;
            for (Question question:questions){
                if (categoryNames[i].equals(question.getCategory())){
                    if (userResult[j]){
                        numberOfCorrectQuestionPerCategory[i]++;
                    }
                }
                j++;
            }

        }

        return numberOfCorrectQuestionPerCategory;
    }




    private class ViewHolder{
        TextView categoryName;
        TextView result;
        LinearLayout colorResult;

    }
}
