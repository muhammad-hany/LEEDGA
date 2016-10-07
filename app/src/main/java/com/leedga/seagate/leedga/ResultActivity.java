package com.leedga.seagate.leedga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.UUID;

import static com.leedga.seagate.leedga.HistoryActivity.TEST_ID_KEY;

public class ResultActivity extends AppCompatActivity {

    public static final String TESTS_PREFS = "tests";
    public static final String CATEGORY_KEY = "category";
    Button showAll;
    ImageButton button;
    String date;
    ListView listView;
    String testId;
    private Test test;
    private TextView score, precentage;
    private TextView dateTextView;
    private ArrayList<String> categoryNamesForAnsweredQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar layout = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle=getIntent().getBundleExtra(TestCategoriesFragment.TEST_BUNDLE);
        if (bundle!=null) {
            test = (Test) bundle.getSerializable(TestCategoriesFragment.TEST_BUNDLE);
        }


        initViews();


        listView= (ListView) findViewById(R.id.resultList);
        ArrayList<Integer> numberOfAnsweredQuestionPerCategory = new ArrayList<>();
        categoryNamesForAnsweredQuestions = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            int j = 0;
            for (Question q : test.getAnsweredQuestions()) {
                if (q.getCategory().equals(DBHelper.CATEGORY_NAMES[i])) {
                    j++;
                    try {
                        categoryNamesForAnsweredQuestions.set(i, DBHelper.CATEGORY_NAMES[i]);
                    } catch (Exception e) {
                        categoryNamesForAnsweredQuestions.add(DBHelper.CATEGORY_NAMES[i]);
                    }
                }
            }
            if (j != 0) {
                numberOfAnsweredQuestionPerCategory.add(j);
            }
        }
        ArrayList<Boolean> userResult = test.getUserResult();
        ArrayList<Question> questions = test.getAnsweredQuestions();
        TestListAdapter adapter = new TestListAdapter(this, categoryNamesForAnsweredQuestions, numberOfAnsweredQuestionPerCategory, userResult,
                questions);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i=new Intent(ResultActivity.this,AnswersActivity.class);
                i.putExtra(CATEGORY_KEY, categoryNamesForAnsweredQuestions.get(position));
                i.putExtra(TestCategoriesFragment.TEST_BUNDLE,test);
                i.putExtra(TEST_ID_KEY, testId);
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
                i.putExtra(TEST_ID_KEY, testId);
                startActivity(i);
            }
        });


    }

    private void initViews() {
        score = (TextView) findViewById(R.id.exams);
        precentage = (TextView) findViewById(R.id.overallPrecntage);
        String ratio = String.valueOf(calculateScore()) + "/" + test.getNumberOfAnsweredQuestions();
        score.setText(ratio);
        precentage.setText(String.valueOf(calculatePrecentage()) + "%");
        java.text.DateFormat format = new SimpleDateFormat("MMM dd, yyyy h:mm a");
        date = format.format(new Date());
        dateTextView = (TextView) findViewById(R.id.date);
        if (!test.isSaved()) {
            test.setSavingDate(new Date());
            test.setSaved(true);
            test.setRatio(ratio);
            test.setTestPercentage(String.valueOf(calculatePrecentage()));
            savingTestInMemmory();
            dateTextView.setText(date);
        } else {
            dateTextView.setText(format.format(test.getSavingDate()));
            testId = getIntent().getStringExtra(TEST_ID_KEY);
        }


    }

    private void savingTestInMemmory() {
        SharedPreferences prefs = getSharedPreferences(TESTS_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        Gson gson=new Gson();
        testId = UUID.randomUUID().toString();
        test.setTestId(testId);
        String json=gson.toJson(test);
        editor.putString(testId, json);
        editor.apply();

    }

    private int calculatePrecentage() {
        double x=calculateScore();
        double y = test.getNumberOfAnsweredQuestions();
        double r=(x/y)*100;
        return (int) r;
    }

    private int calculateScore() {
        int scoreCount=0;
        for (int i = 0; i < test.getUserResult().size(); i++) {
            if (test.getUserResult().get(i)) {
                scoreCount++;
            }
        }

        return scoreCount;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.goAgain:
                Intent intent = new Intent(this, TestSettingActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
