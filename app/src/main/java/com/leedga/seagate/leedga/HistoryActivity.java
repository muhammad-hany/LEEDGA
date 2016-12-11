package com.leedga.seagate.leedga;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.leedga.seagate.leedga.AnswersActivity.QUESTION_BODY;

public class HistoryActivity extends AppCompatActivity {
    static final String TEST_ID_KEY = "test_id";
    TextView examTxt;
    ArrayList<String> stringPref;
    List<Test> testPref;
    ArrayList<String> testDates;
    RecyclerView recyclerView;
    HistoryRecyclerAdaptor recyclerAdaptor;
    ArrayList<Integer> numberofAnsweredQList;
    private int comulativePrec;
    private String overAllPrec;
    private boolean isDayQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isDayQuestion = getIntent().getBooleanExtra(REF.DAY_QUESTION_PREF, false);
        String txt = isDayQuestion ? "Question of the Day" : "Tests History";
        getSupportActionBar().setTitle(txt);
        initViews();
        gettingTestFromPref();
        settingValues();

        HistoryListAdapter adapter = new HistoryListAdapter(this, testPref);
        adapter.notifyDataSetChanged();


        recyclerView = (RecyclerView) findViewById(R.id.historyList);
        recyclerAdaptor = new HistoryRecyclerAdaptor(this, comulativePrec, new HistoryRecyclerAdaptor.OnMyItemClick() {
            @Override
            public void onItemClick(int position) {
                if (isDayQuestion) {
                    Intent intent = new Intent(HistoryActivity.this, AnswerViewActivity.class);
                    intent.putExtra(TestCategoriesFragment.TEST_BUNDLE, testPref.get(position));
                    intent.putExtra(QUESTION_BODY, testPref.get(position).getQuestions().get(0)
                            .getQuestionBody());
                    startActivity(intent);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(TestCategoriesFragment.TEST_BUNDLE, testPref.get(position));
                    Intent intent = new Intent(HistoryActivity.this, ResultActivity.class);
                    intent.putExtra(TestCategoriesFragment.TEST_BUNDLE, bundle);
                    intent.putExtra(TEST_ID_KEY, testPref.get(position).getTestId());
                    startActivity(intent);
                }

            }

            @Override
            public void onShowAllClick() {

            }
        }, overAllPrec, testPref, isDayQuestion);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdaptor);



    }

    private void settingValues() {
        /*if (testPref.size() == 1 || testPref.size() == 0) {
            examTxt.setText("Exam");
        }*/
        comulativePrec = 0;
        int comulativeRightQuestionsCount = 0;
        int comulativeQuestionCount = 0;
        for (Test test : testPref) {
            comulativeRightQuestionsCount = comulativeRightQuestionsCount + calculateScore(test);
            comulativeQuestionCount = comulativeQuestionCount + test.getNumberOfAnsweredQuestions();
        }
        comulativePrec = (int) ((((double) comulativeRightQuestionsCount /
                (double) comulativeQuestionCount)) * 100);
        overAllPrec = String.valueOf(comulativeRightQuestionsCount) + "/" + comulativeQuestionCount;
    }

    private void initViews() {

        /*examTxt = (TextView) findViewById(R.id.examText);*/
        /*listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/
        stringPref = new ArrayList<>();
        testPref = new ArrayList<>();
        testDates = new ArrayList<>();
        numberofAnsweredQList = new ArrayList<>();

    }


    public void gettingTestFromPref() {
        SharedPreferences prefs;
        if (isDayQuestion) {
            // question of day history tests
            prefs = getSharedPreferences(REF.DAY_QUESTION_HISTORY_PREF, MODE_PRIVATE);
        } else {
            // history test
            prefs = getSharedPreferences(ResultActivity.TESTS_PREFS, MODE_PRIVATE);
        }

        Map<String, ?> tests = prefs.getAll();
        for (Map.Entry<String, ?> test : tests.entrySet()) {
            stringPref.add(test.getValue().toString());
        }
        Gson gson = new Gson();
        if (stringPref.size() != 0) {
            for (String entry : stringPref) {
                Test test = gson.fromJson(entry, Test.class);
                testPref.add(test);
            }
        }

        Collections.sort(testPref, new Comparator<Test>() {
            @Override
            public int compare(Test lhs, Test rhs) {
                return rhs.getSavingDate().compareTo(lhs.getSavingDate());
            }
        });
        for (Test test : testPref) {
            String date = new SimpleDateFormat(REF.DETAILED_DATE_FORMAT, Locale.US).format(test.getSavingDate());
            testDates.add(date);
            numberofAnsweredQList.add(test.getNumberOfAnsweredQuestions());
        }
    }

    private int calculateScore(Test test) {
        int scoreCount = 0;
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
        getMenuInflater().inflate(R.menu.menu_history, menu);
        if (testPref.isEmpty()) {
            menu.findItem(R.id.resetHistory).setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.resetHistory) {
            AlertDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure  you want to clear Tests History?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences prefs = getSharedPreferences(ResultActivity.TESTS_PREFS, MODE_PRIVATE);
                    prefs.edit().clear().apply();
                    gettingTestFromPref();
                    settingValues();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialog = builder.create();
            dialog.show();

            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class HistoryListAdapter extends ArrayAdapter<Test> {
        List<Test> tests;
        DateFormat format;

        public HistoryListAdapter(Context context, List<Test> tests) {
            super(context, R.layout.result_row, tests);
            this.tests = tests;
            format = new SimpleDateFormat("MMM dd, yyyy h:mm a");
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.result_row, null);
                holder = new ViewHolder();
                holder.date = (TextView) convertView.findViewById(R.id.categoryName);
                holder.ratio = (TextView) convertView.findViewById(R.id.correct);
                holder.colorResult = (RelativeLayout) convertView.findViewById(R.id.colorResult);
                holder.insidePrec = (TextView) convertView.findViewById(R.id.prec);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Test test = tests.get(position);
            holder.date.setText(format.format(test.getSavingDate()));
            holder.ratio.setText(test.getRatio() + " Correct Answer");
            holder.insidePrec.setText(test.getTestPercentage() + "%");
            /*GradientDrawable drawable= (GradientDrawable) holder.colorResult.getBackground();
             drawable.setColor(getColorByScore(Integer.parseInt(
                    (tests.getTestPercentage()))));*/

            return convertView;


        }

        private int getColorByScore(int score) {

            if (score >= 0 && score < 20) {
                return Color.rgb(237, 27, 36);
            } else if (score >= 20 && score < 40) {
                return Color.rgb(243, 112, 32);
            } else if (score >= 40 && score < 55) {
                return Color.rgb(252, 185, 19);
            } else if (score >= 55 && score < 70) {
                return Color.rgb(254, 242, 0);
            } else if (score >= 70 && score < 80) {
                return Color.rgb(92, 215, 49);
            } else if (score >= 80 && score < 90) {
                return Color.rgb(80, 184, 73);
            } else if (score >= 90 && score <= 100) {
                return Color.rgb(0, 166, 82);
            }
            return 0;
        }

        private class ViewHolder {
            TextView date;
            TextView ratio, insidePrec;
            RelativeLayout colorResult;
        }
    }


}
