package com.leedga.seagate.leedga;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Workstation on 14/09/2016.
 */
public class Question implements Serializable {

    String questionBody,choice1,choice2,choice3,choice4,choice5,choice6,answer,note,category,key;
    int id,type;



    public Question(String questionBody, String choice1, String choice2, String choice3, String choice4, String choice5, String choice6, String answer , String note,String category, int type , int id,String key){
        this.questionBody=questionBody;
        this.choice1=choice1;
        this.choice2=choice2;
        this.choice3=choice3;
        this.choice4=choice4;
        this.choice5=choice5;
        this.choice6=choice6;
        this.type=type;
        this.answer=answer;
        this.note=note;
        this.id=id;
        this.category=category;
        this.key=key;

    }
    public Question(){

    }

    public String getKey() {
        return key;
    }

    public List<String> getChoices(){
        List<String> choices=new ArrayList<>();
        choices.add(choice1);
        choices.add(choice2);
        choices.add(choice3);
        choices.add(choice4);
        if (choice5!=null) choices.add(choice5);
        if (choice6!=null) choices.add(choice6);
        return choices;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getChoice5() {
        return choice5;
    }

    public void setChoice5(String choice5) {
        this.choice5 = choice5;
    }

    public String getChoice6() {
        return choice6;
    }

    public void setChoice6(String choice6) {
        this.choice6 = choice6;
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

    public int getAswersCount(){
        if (choice5==null){
            return 4;
        }else if (choice6!=null){
            return 6;
        }else {
            return 5;
        }
    }


}
