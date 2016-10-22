package com.leedga.seagate.leedga;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import static com.leedga.seagate.leedga.TestActivity.UNFINISHED;
import static com.leedga.seagate.leedga.TestActivity.UNFINISHED_TEST;
import static com.leedga.seagate.leedga.TestCategoriesFragment.TEST_BUNDLE;

public class MainActivity extends BaseActivity implements MainRecyclerAdaptor.OnMyItemClick {

    public static final String DEFAULT_TEST_KEY = "default_test";
    public static final String DEFAULT_TEST_PREF_KEY = "default_test_pref";
    AlertDialog.Builder builder;
    private Test test;
    private SharedPreferences preferences;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defineNavigationMenu();
        setDefaultTestPreferences();
        /*getSupportActionBar().setDisplayShowTitleEnabled(false);*/
        Button testButton = (Button) findViewById(R.id.test_btn);
        assert testButton != null;
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkThereIsUnfinishedTest();

            }
        });

        Button testHistory= (Button) findViewById(R.id.testHistory);
        testHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,HistoryActivity.class);
                startActivity(i);
            }
        });
        ArrayList<Test> tests = getLastTests();
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        MainRecyclerAdaptor adaptor = new MainRecyclerAdaptor(this, this, tests);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adaptor);


    }

    public Question getRandomQuestion() {
        DBHelper helper = new DBHelper(this, TestFragment.DATABASE_NAME);
        return helper.getRandomQuestion();
    }

    private void setDefaultTestPreferences() {
        SharedPreferences preferences = getSharedPreferences(DEFAULT_TEST_PREF_KEY, MODE_PRIVATE);
        if (!preferences.contains(DEFAULT_TEST_KEY)) {
            SharedPreferences.Editor editor = preferences.edit();
            Test test = createNewTest();
            Gson gson = new Gson();
            String json = gson.toJson(test);
            editor.putString(DEFAULT_TEST_KEY, json);
            editor.apply();
        }
    }

    private Test createNewTest() {
        Test test = new Test();
        test.setQuestionTypes(new boolean[]{true, true, true});
        test.setNumberOfQuestions(10);
        test.setAnswerShow(TestTypeFragment.ANSWER_AFTER_ALL);
        test.setChapters(new boolean[]{true, true, true, true, true, true, true, true, true});
        return test;
    }

    protected void checkThereIsUnfinishedTest() {
        preferences = getSharedPreferences(TestActivity.UNFINISHED_TEST, Context.MODE_PRIVATE);
        buildDialog();

        if (preferences.contains(UNFINISHED)) {
            String jsonTest = preferences.getString(UNFINISHED, null);
            Gson gson = new Gson();
            test = gson.fromJson(jsonTest, Test.class);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            Intent i = new Intent(MainActivity.this, TestSettingActivity.class);
            startActivityForResult(i, 1);
        }
    }

    private void buildDialog() {
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to continue Your last Test ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MainActivity.this, TestActivity.class);
                i.putExtra(TEST_BUNDLE, test);
                startActivity(i);
            }
        });
        builder.setNegativeButton("Start New one", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MainActivity.this, TestSettingActivity.class);
                startActivity(i);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(UNFINISHED);
                editor.apply();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 5) {
            SharedPreferences preferences = getSharedPreferences(UNFINISHED_TEST, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear().apply();
        }
    }

    @Override
    public void onItemClick(int position) {

    }

    public ArrayList<Test> getLastTests() {
        SharedPreferences prefs = getSharedPreferences(ResultActivity.TESTS_PREFS, MODE_PRIVATE);
        ArrayList<String> stringPref = new ArrayList<>();
        ArrayList<Test> testPref = new ArrayList<>();

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
        return testPref;
    }


}
