package com.leedga.seagate.leedga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {

    Button showAll;
    ImageButton button;
    private Test test;
    String date;
    ListView listView;
    ConstraintLayout layout;
    public static final String CATEGORY_KEY="category";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        layout = (ConstraintLayout) findViewById(R.id.toolbar);

        Bundle bundle=getIntent().getBundleExtra(TestCategoriesFragment.TEST_BUNDLE);
        if (bundle!=null) {
            test = (Test) bundle.getSerializable(TestCategoriesFragment.TEST_BUNDLE);
        }

        TextView score= (TextView) findViewById(R.id.score);
        TextView precentage= (TextView) findViewById(R.id.precentage);
        assert score != null;
        score.setText(String.valueOf(calculateScore())+"/"+test.getNumberOfQuestions());
        assert precentage != null;
        precentage.setText(String.valueOf(calculatePrecentage())+"%");
        java.text.DateFormat format=new SimpleDateFormat("MMM dd, yyyy h:mm a");
        date=format.format(new Date());
        TextView dateTextView= (TextView) findViewById(R.id.date);
        assert dateTextView != null;
        dateTextView.setText(date);
        test.setSavingDate(new Date());
        test.setSaved(true);

        savingTestInMemmory();

        listView= (ListView) findViewById(R.id.resultList);
        int [] numberofQuestionsPerCategory=test.getcountPerCategory();
        boolean [] userResult=test.getUserResult();
        ArrayList<Question> questions=test.getQuestions();
        TestListAdapter adapter =new TestListAdapter(this,DBHelper.CATEGORY_NAMES,numberofQuestionsPerCategory,userResult,questions);
        listView.setAdapter(adapter);

        button= (ImageButton) findViewById(R.id.upButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i=new Intent(ResultActivity.this,AnswersActivity.class);
                i.putExtra(CATEGORY_KEY,DBHelper.CATEGORY_NAMES[position]);
                i.putExtra(TestCategoriesFragment.TEST_BUNDLE,test);
                startActivity(i);
            }
        });

        showAll= (Button) findViewById(R.id.showAll);
        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ResultActivity.this,AnswersActivity.class);
                i.putExtra(CATEGORY_KEY,"all");
                i.putExtra(TestCategoriesFragment.TEST_BUNDLE,test);
                startActivity(i);
            }
        });


    }

    private void savingTestInMemmory() {
        SharedPreferences prefs=getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        Gson gson=new Gson();
        String json=gson.toJson(test);
        editor.putString(date,json);

    }

    private int calculatePrecentage() {
        double x=calculateScore();
        double y=test.getNumberOfQuestions();
        double r=(x/y)*100;
        return (int) r;
    }

    private int calculateScore() {
        int scoreCount=0;
        for (int i=0;i<test.getUserResult().length;i++){
            if (test.getUserResult()[i]){
                scoreCount++;
            }
        }

        return scoreCount;
    }

}
