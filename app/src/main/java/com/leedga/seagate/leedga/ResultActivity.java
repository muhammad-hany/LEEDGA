package com.leedga.seagate.leedga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.google.gson.Gson;
import com.vungle.publisher.VunglePub;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.UUID;

import static com.leedga.seagate.leedga.HistoryActivity.TEST_ID_KEY;
import static com.leedga.seagate.leedga.TestActivity.UNFINISHED;

public class ResultActivity extends AppCompatActivity {

    public static final String TESTS_PREFS = "tests";
    public static final String CATEGORY_KEY = "category";
    private final com.vungle.publisher.VunglePub vunglePub = VunglePub.getInstance();
    Button showAll;
    ImageButton button;
    String date;
    ListView listView;
    String testId;
    private Test test;
    private TextView score, precentage;
    private ArrayList<String> categoryNamesForAnsweredQuestions;
    private RadarChart mChart;
    private TestListAdapter adapter;

    public static int calculatePrecentage(Test test) {
        double x = calculateScore(test);
        double y = test.getNumberOfAnsweredQuestions();
        double r = (x / y) * 100;
        return (int) r;
    }

    public static int calculateScore(Test test) {
        int scoreCount = 0;
        for (int i = 0; i < test.getUserResult().size(); i++) {
            if (test.getUserResult().get(i)) {
                scoreCount++;
            }
        }

        return scoreCount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Result");
        Bundle bundle = getIntent().getBundleExtra(TestCategoriesFragment.TEST_BUNDLE);
        if (bundle != null) {
            test = (Test) bundle.getSerializable(TestCategoriesFragment.TEST_BUNDLE);
        }
        if (getIntent().hasExtra("clear")) {
            clearUnfinishedTestInPrefs();
        }


        initViews();


        // listView= (ListView) findViewById(R.id.resultList);
        ArrayList<Integer> numberOfAnsweredQuestionPerCategory = new ArrayList<>();
        categoryNamesForAnsweredQuestions = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            int j = 0;
            String cat = REF.CATEGORY_NAMES[i];
            for (Question q : test.getAnsweredQuestions()) {
                if (q.getCategory().equals(cat)) {
                    j++;
                    categoryNamesForAnsweredQuestions.add(cat);
                }
            }
            if (j != 0) {
                numberOfAnsweredQuestionPerCategory.add(j);
            }
        }
        categoryNamesForAnsweredQuestions = new ArrayList<>(new LinkedHashSet<>(categoryNamesForAnsweredQuestions));
        ArrayList<Boolean> userResult = test.getUserResult();
        ArrayList<Question> questions = test.getAnsweredQuestions();
        /*adapter = new TestListAdapter(this, categoryNamesForAnsweredQuestions, numberOfAnsweredQuestionPerCategory, numberofCorrectQ,
                questions);
        listView.setAdapter(adapter);*/

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ResultRecyclerAdaptor adaptor = new ResultRecyclerAdaptor(this, categoryNamesForAnsweredQuestions, numberOfAnsweredQuestionPerCategory, userResult,
                questions, Integer.parseInt(test.getTestPercentage()), calculateScore(test), test.getNumberOfAnsweredQuestions(),
                new
                        ResultRecyclerAdaptor.OnMyItemClick() {
                            @Override
                            public void onItemClick(int position) {
                                Intent i = new Intent(ResultActivity.this, AnswersActivity.class);
                                i.putExtra(CATEGORY_KEY, categoryNamesForAnsweredQuestions.get(position));
                                i.putExtra(TestCategoriesFragment.TEST_BUNDLE, test);
                                i.putExtra(TEST_ID_KEY, testId);
                                startActivity(i);
                            }

                            @Override
                            public void onShowAllClick() {
                                Intent i = new Intent(ResultActivity.this, AnswersActivity.class);
                                i.putExtra(CATEGORY_KEY, "all");
                                i.putExtra(TestCategoriesFragment.TEST_BUNDLE, test);
                                i.putExtra(TEST_ID_KEY, testId);
                                startActivity(i);
                            }
                        }, date);
        recyclerView.setAdapter(adaptor);


        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i=new Intent(ResultActivity.this,AnswersActivity.class);
                i.putExtra(CATEGORY_KEY, categoryNamesForAnsweredQuestions.get(position));
                i.putExtra(TestCategoriesFragment.TEST_BUNDLE,tests);
                i.putExtra(TEST_ID_KEY, testId);
                startActivity(i);
            }
        });*/


       /* showAll = (Button) findViewById(R.id.showAll);
        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultActivity.this, AnswersActivity.class);
                i.putExtra(CATEGORY_KEY, "all");
                i.putExtra(TestCategoriesFragment.TEST_BUNDLE, test);
                i.putExtra(TEST_ID_KEY, testId);
                startActivity(i);
            }
        });*/
        // settingRadarChart();
        if (!MainActivity.mPremiumAcount) {
            vunglePub.playAd();
        }

    }

    /*private void setData() {
        ArrayList<RadarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            entries.add(new RadarEntry(adapter.calculateResultPerCategory()[i]));
        }
        RadarDataSet set1 = new RadarDataSet(entries, "TestResult");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);
        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        mChart.invalidate();
    }*/

    private void clearUnfinishedTestInPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences(REF.UNCOMPLETED_PREF, MODE_PRIVATE);
        if (sharedPreferences.contains(REF.UNCOMPLETED_TEST)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(REF.UNCOMPLETED_TEST);
            editor.apply();
        }


    }

    private void settingRadarChart() {
        mChart = (RadarChart) findViewById(R.id.radarChart);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setWebLineWidth(1f);
        mChart.setWebColor(Color.LTGRAY);
        mChart.setWebLineWidthInner(1f);
        mChart.setWebColorInner(Color.LTGRAY);
        mChart.setWebAlpha(100);

        //setData();

    }

    private void initViews() {
        String ratio = String.valueOf(calculateScore(test)) + "/" + test.getNumberOfAnsweredQuestions();
        java.text.DateFormat format = new SimpleDateFormat("MMM dd, yyyy h:mm a");
        date = format.format(new Date());
        if (!test.isSaved() || test.getTestId().equals(UNFINISHED)) {
            test.setSavingDate(new Date());
            test.setSaved(true);
            test.setRatio(ratio);
            test.setTestPercentage(String.valueOf(calculatePrecentage(test)));
            savingTestInMemory();
        } else {
            date = format.format(test.getSavingDate());
            testId = getIntent().getStringExtra(TEST_ID_KEY);
        }


    }

    private void savingTestInMemory() {
        deletingUnfinishedTest();
        SharedPreferences prefs = getSharedPreferences(TESTS_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        testId = UUID.randomUUID().toString();
        test.setTestId(testId);
        String json = gson.toJson(test);
        editor.putString(testId, json);
        editor.apply();


    }

    private void deletingUnfinishedTest() {
        SharedPreferences unfinished = getSharedPreferences(REF.UNCOMPLETED_PREF, MODE_PRIVATE);
        if (unfinished.contains(REF.UNCOMPLETED_TEST)) {
            SharedPreferences.Editor editor = unfinished.edit();
            editor.remove(REF.UNCOMPLETED_TEST);
            editor.apply();
        }

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
        }
            /*case R.id.goAgain:
                Intent intent = new Intent(this, TestSettingActivity.class);
                startActivity(intent);
                return true;*/

        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onBackPressed() {
        setResult(5);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vunglePub.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vunglePub.onResume();
    }
}
