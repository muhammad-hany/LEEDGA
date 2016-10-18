package com.leedga.seagate.leedga;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends BaseActivity {
    static final String TEST_ID_KEY = "test_id";
    TextView exams, overAllRatio, overAllPercentage, examTxt;
    ArrayList<String> stringPref;
    List<Test> testPref;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        defineNavigationMenu();
        initViews();
        gettingTestFromPref();
        settingValues();

        HistoryListAdapter adapter = new HistoryListAdapter(this, testPref);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);


    }

    private void settingValues() {
        if (testPref.size() == 1 || testPref.size() == 0) {
            examTxt.setText("Exam");
        }
        exams.setText(String.valueOf(testPref.size()));
        int comulativePrec = 0;
        int comulativeRightQuestionsCount = 0;
        int comulativeQuestionCount = 0;
        for (Test test : testPref) {
            comulativeRightQuestionsCount = comulativeRightQuestionsCount + calculateScore(test);
            comulativeQuestionCount = comulativeQuestionCount + test.getNumberOfAnsweredQuestions();
        }
        comulativePrec = (int) ((((double) comulativeRightQuestionsCount /
                (double) comulativeQuestionCount)) * 100);
        overAllPercentage.setText(String.valueOf(comulativePrec) + "%");
        overAllRatio.setText(String.valueOf(comulativeRightQuestionsCount) + "/" + comulativeQuestionCount);
    }

    private void initViews() {
        exams = (TextView) findViewById(R.id.exams);
        examTxt = (TextView) findViewById(R.id.examText);
        overAllRatio = (TextView) findViewById(R.id.precentage);
        overAllPercentage = (TextView) findViewById(R.id.overallPrecntage);

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(TestCategoriesFragment.TEST_BUNDLE, testPref.get(position));
                Intent intent = new Intent(HistoryActivity.this, ResultActivity.class);
                intent.putExtra(TestCategoriesFragment.TEST_BUNDLE, bundle);
                intent.putExtra(TEST_ID_KEY, testPref.get(position).getTestId());
                startActivity(intent);
            }
        });
        stringPref = new ArrayList<>();
        testPref = new ArrayList<>();

    }


    public void gettingTestFromPref() {
        SharedPreferences prefs = getSharedPreferences(ResultActivity.TESTS_PREFS, MODE_PRIVATE);
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
            builder.setMessage("Are you sure  you want to delete Tests History?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences prefs = getSharedPreferences(ResultActivity.TESTS_PREFS,
                            MODE_PRIVATE);
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
                holder.donutProgress = (DonutProgress) convertView.findViewById(R.id.item_donut);
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
                    (test.getTestPercentage()))));*/
            holder.donutProgress.setProgress(Integer.parseInt(test.getTestPercentage()));
            holder.donutProgress.setInnerBackgroundColor((getColorByScore(Integer.parseInt(
                    (test.getTestPercentage())))));
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
            DonutProgress donutProgress;
        }
    }




}
