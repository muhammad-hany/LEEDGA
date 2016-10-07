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
    ArrayList<Question> answeredQuestions;
    ArrayList<Question> questions;
    ArrayList<String> userAnswers;
    ArrayList<Boolean> userResult;
    int numberOfQuestions, answerShow;
    Date savingDate;
    boolean saved;
    String testPercentage, ratio, testId;

    public Test(ArrayList<Question> questions, boolean[] chapters, int[] numberPerCategory, boolean[] questionTypes, int numberOfQuestions, ArrayList<String> userAnswers, ArrayList<Boolean> userResult, Date savingDate, boolean saved, String testPercentage, String ratio, String testId, int answerShow) {
        this.chapters=chapters;
        this.questions=questions;
        this.questionTypes=questionTypes;
        this.numberOfQuestions=numberOfQuestions;
        this.numberPerCategory=numberPerCategory;
        this.userAnswers=userAnswers;
        this.userResult=userResult;
        this.savingDate=savingDate;
        this.saved=saved;
        this.testPercentage = testPercentage;
        this.ratio = ratio;
        this.testId = testId;
        this.answerShow = answerShow;


    }

    public Test() {

    }

    public int getNumberOfAnsweredQuestions() {
        return answeredQuestions.size();
    }

    public int getAnswerShow() {
        return answerShow;
    }

    public void setAnswerShow(int answerShow) {
        this.answerShow = answerShow;
    }

    public ArrayList<Question> getAnsweredQuestions() {
        return answeredQuestions;
    }

    public void addToAnsweredQuestions(Question question, int position) {
        if (answeredQuestions == null) {
            answeredQuestions = new ArrayList<>();
        }
        try {
            answeredQuestions.set(position, question);
        } catch (Exception e) {
            answeredQuestions.add(question);
        }

    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getTestPercentage() {
        return testPercentage;
    }

    public void setTestPercentage(String testPercentage) {
        this.testPercentage = testPercentage;
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

    public ArrayList<Boolean> getUserResult() {
        return userResult;
    }

    public void setUserResult(ArrayList<Boolean> userResult) {
        this.userResult = userResult;
    }

    public void setUserResultElement(boolean result , int index){
        if (userResult==null){
            userResult = new ArrayList<>();
        }
        try {
            this.userResult.set(index, result);
        } catch (Exception e) {
            this.userResult.add(result);
        }

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
