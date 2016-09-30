package com.leedga.seagate.leedga;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Muhammad Workstation on 14/09/2016.
 */
public class Test implements Serializable {
    boolean []  chapters,questionTypes;

    int [] numberPerCategory;

    ArrayList<Question> questions;
    ArrayList<String> userAnswers;
    boolean [] userResult;
    int numberOfQuestions;
    Date savingDate;
    boolean saved;

    public Test(ArrayList<Question> questions, boolean [] chapters, int [] numberPerCategory , boolean [] questionTypes, int numberOfQuestions, ArrayList<String> userAnswers, boolean [] userResult, Date savingDate,boolean saved){
        this.chapters=chapters;
        this.questions=questions;
        this.questionTypes=questionTypes;
        this.numberOfQuestions=numberOfQuestions;
        this.numberPerCategory=numberPerCategory;
        this.userAnswers=userAnswers;
        this.userResult=userResult;
        this.savingDate=savingDate;
        this.saved=saved;

    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public Date getSavingDate() {
        return savingDate;
    }

    public void setSavingDate(Date savingDate) {
        this.savingDate = savingDate;
    }

    public boolean[] getUserResult() {
        return userResult;
    }

    public void setUserResult(boolean[] userResult) {
        this.userResult = userResult;
    }

    public void setUserResultElement(boolean result , int index){
        if (userResult==null){
            userResult=new boolean[numberOfQuestions];
        }
        this.userResult[index]=result;
    }

    public boolean[] getQuestionTypes() {
        return questionTypes;
    }

    public void setQuestionTypes(boolean[] questionTypes) {
        this.questionTypes = questionTypes;
    }


    public ArrayList<String> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(ArrayList<String> userAnswers) {
        this.userAnswers = userAnswers;
    }

    public void setUserAnswerElement(String answer,int index){
        if (this.userAnswers==null){
            userAnswers=new ArrayList<>(numberOfQuestions);
        }
        this.userAnswers.add(index, answer);

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
