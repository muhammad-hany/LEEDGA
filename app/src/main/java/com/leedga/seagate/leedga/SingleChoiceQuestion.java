package com.leedga.seagate.leedga;

import java.io.Serializable;

/**
 * Created by Muhammad Workstation on 14/09/2016.
 */
public class SingleChoiceQuestion implements Serializable {

    String questionBody,choice1,choice2,choice3,choice4,answer,note;
    int id;
    public SingleChoiceQuestion(String questionBody, String choice1, String choice2, String choice3, String
            choice4, String answer , String note , int id){
        this.questionBody=questionBody;
        this.choice1=choice1;
        this.choice2=choice2;
        this.choice3=choice3;
        this.choice4=choice4;
        this.answer=answer;
        this.note=note;
        this.id=id;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody;
    }

    public String getChoice1() {
        return choice1;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public void setChoice3(String choice3) {
        this.choice3 = choice3;
    }

    public String getChoice4() {
        return choice4;
    }

    public void setChoice4(String choice4) {
        this.choice4 = choice4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SingleChoiceQuestion(){}
}
