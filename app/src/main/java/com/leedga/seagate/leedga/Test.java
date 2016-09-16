package com.leedga.seagate.leedga;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Muhammad Workstation on 14/09/2016.
 */
public class Test implements Serializable {
    boolean typeTrueFalse,typeOneChoice,typeMultiChoice;
    boolean []  chapters;

    ArrayList<SingleChoiceQuestion> singleQuestions;
    ArrayList<MultiChoiceQuestion> multiQuestions;
    public Test(ArrayList <SingleChoiceQuestion> singleQuestions ,ArrayList<MultiChoiceQuestion> multiQuestions,boolean typeTrueFalse , boolean typeOneChoice , boolean typeMultiChoice,
                boolean [] chapters){
        this.chapters=chapters;
        this.typeTrueFalse=typeTrueFalse;
        this.typeOneChoice=typeOneChoice;
        this.typeMultiChoice=typeMultiChoice;
    }





    public ArrayList<SingleChoiceQuestion> getSingleQuestions() {
        return singleQuestions;
    }

    public void setSingleQuestions(ArrayList<SingleChoiceQuestion> singleQuestions) {
        this.singleQuestions = singleQuestions;
    }

    public ArrayList<MultiChoiceQuestion> getMultiQuestions() {
        return multiQuestions;
    }

    public void setMultiQuestions(ArrayList<MultiChoiceQuestion> multiQuestions) {
        this.multiQuestions = multiQuestions;
    }

    public Test(){

    }

    public boolean[] getChapters() {
        return chapters;
    }

    public void setChapters(boolean[] chapters) {
        this.chapters = chapters;
    }

    public boolean isTypeTrueFalse() {
        return typeTrueFalse;
    }

    public void setTypeTrueFalse(boolean typeTrueFalse) {
        this.typeTrueFalse = typeTrueFalse;
    }

    public boolean isTypeOneChoice() {
        return typeOneChoice;
    }

    public void setTypeOneChoice(boolean typeOneChoice) {
        this.typeOneChoice = typeOneChoice;
    }

    public boolean isTypeMultiChoice() {
        return typeMultiChoice;
    }

    public void setTypeMultiChoice(boolean typeMultiChoice) {
        this.typeMultiChoice = typeMultiChoice;
    }

    public void prepaireQuestions (DBHelper helper){
        multiQuestions=helper.getMultiChoice();
        singleQuestions=helper.getSingleChoice();
    }



}
