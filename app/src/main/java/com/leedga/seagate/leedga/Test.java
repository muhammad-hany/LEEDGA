package com.leedga.seagate.leedga;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Muhammad Workstation on 14/09/2016.
 */
public class Test implements Serializable {
    boolean []  chapters,questionTypes;

    int [] numberPerCategory;

    ArrayList<Question> questions;
    int numberOfQuestions;

    public Test(ArrayList<Question> questions,boolean [] chapters,int [] numberPerCategory , boolean [] questionTypes,int numberOfQuestions){
        this.chapters=chapters;
        this.questions=questions;
        this.questionTypes=questionTypes;
        this.numberOfQuestions=numberOfQuestions;
        this.numberPerCategory=numberPerCategory;

    }

    public boolean[] getQuestionTypes() {
        return questionTypes;
    }

    public void setQuestionTypes(boolean[] questionTypes) {
        this.questionTypes = questionTypes;
    }



    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> multiQuestions) {
        this.questions = multiQuestions;
    }

    public Test(){

    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public boolean[] getChapters() {
        return chapters;
    }

    public void setChapters(boolean[] chapters) {
        this.chapters = chapters;
    }


    public void prepareQuestions(DBHelper helper){

    }

    public int [] getcountPerCategory(){
        numberPerCategory =new int[9];
        int unitCount=0;
        int j=0;
        for (int i=0; i<getNumberOfQuestions();i++){
            if (j>8){
                j=0;
            }
            if (chapters[j]) {
                numberPerCategory[j] = numberPerCategory[j] + 1;
                j++;
            }else {
                numberPerCategory[j] =0;
                j++;
                i--;
            }
        }

        return numberPerCategory;

    }












}
