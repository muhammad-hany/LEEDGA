package com.leedga.seagate.leedga;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import static com.leedga.seagate.leedga.REF.REFERENCES_KEY;
import static com.leedga.seagate.leedga.REF.TERMS_KEY;
import static com.leedga.seagate.leedga.TestActivity.UNFINISHED_TEST;
import static com.leedga.seagate.leedga.TestCategoriesFragment.TEST_BUNDLE;

public class MainActivity extends BaseActivity implements MainRecyclerAdaptor.OnMyItemClick {

    public static final String DEFAULT_TEST_KEY = "default_test";
    public static final String DEFAULT_TEST_PREF_KEY = "default_test_pref";
    AlertDialog.Builder builder;
    private Test test;
    private SharedPreferences unFinishedTestPref;
    private RecyclerView recyclerView;
    private SharedPreferences defaultTestPref;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defineNavigationMenu();
        setDefaultTestPreferences();
        setDefaultPrefrences();


        /*getSupportActionBar().setDisplayShowTitleEnabled(false);*/

        ArrayList<Test> tests = getLastTests();
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        MainRecyclerAdaptor adaptor = new MainRecyclerAdaptor(this, this, tests);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (position) {
                    case 0:
                        return 2;
                    default:
                        return 1;
                }

            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        /*adaptor.setHasStableIds(true);*/
        recyclerView.setAdapter(adaptor);


    }


    private void setDefaultPrefrences() {
        SharedPreferences prefs = getSharedPreferences(REF.GENERAL_SETTING_PREF, MODE_PRIVATE);
        if (!prefs.contains(REF.DAY_QUESTION_PREF)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(REF.DAY_QUESTION_PREF, true);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 22);
            calendar.set(Calendar.MINUTE, 9);
            editor.apply();
            Intent intent = new Intent(this, NotificationService.class);
            intent.putExtra(REF.TRIGGER_MILLS_KEY, calendar.getTimeInMillis());
            pendingIntent = PendingIntent.getBroadcast(this, REF.PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


        }
    }

    public Question getRandomQuestion() {
        DBHelper helper = new DBHelper(this, REF.DATABASE_NAME);
        return helper.getRandomQuestion();
    }

    private void setDefaultTestPreferences() {
        defaultTestPref = getSharedPreferences(DEFAULT_TEST_PREF_KEY, MODE_PRIVATE);
        if (!defaultTestPref.contains(DEFAULT_TEST_KEY)) {
            SharedPreferences.Editor editor = defaultTestPref.edit();
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
        SharedPreferences preferences = getSharedPreferences(REF.UNCOMPLETED_PREF, Context.MODE_PRIVATE);
        buildDialog();

        if (preferences.contains(REF.UNCOMPLETED_TEST)) {
            String jsonTest = preferences.getString(REF.UNCOMPLETED_TEST, null);
            Gson gson = new Gson();
            test = gson.fromJson(jsonTest, Test.class);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            startNewTest();

        }
    }

    private void startNewTest() {
        SharedPreferences preferences = getSharedPreferences(REF.UNCOMPLETED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        Intent i = new Intent(MainActivity.this, TestActivity.class);
        //todo change that after adding test setting
        String decode = defaultTestPref.getString(DEFAULT_TEST_KEY, null);
        Gson gson = new Gson();
        Test test = gson.fromJson(decode, Test.class);
        ArrayList<Question> questions = new DBHelper(this, REF.DATABASE_NAME).getAll(test.getChapters(), test.getcountPerCategory(), test.getQuestionTypes());
        Collections.shuffle(questions);
        test.setQuestions(questions);
        i.putExtra(REF.TEST_FRAGMENT_TYPE, REF.FULL_QUESTIONS);
        i.putExtra(TEST_BUNDLE, test);
        startActivityForResult(i, 1);
    }

    private void buildDialog() {
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to continue Your last Test ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MainActivity.this, TestActivity.class);
                i.putExtra(REF.TEST_FRAGMENT_TYPE, REF.FULL_QUESTIONS);
                i.putExtra(TEST_BUNDLE, test);
                startActivity(i);
            }
        });
        builder.setNegativeButton("Start New one", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences preferences = getSharedPreferences(REF.UNCOMPLETED_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(REF.UNCOMPLETED_TEST);
                editor.apply();
                startNewTest();
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
        Intent i;
        switch (position) {
            case 1:
                checkThereIsUnfinishedTest();
                break;
            case 2:
                i = new Intent(MainActivity.this, TestSettingActivity.class);
                startActivity(i);
                break;
            case 3:
                i = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(i);
                break;
            case 4:
                //question of the day
                i = new Intent(this, TestActivity.class);
                i.putExtra(REF.TEST_FRAGMENT_TYPE, REF.SINGLE_QUESTION);
                startActivity(i);
                break;
            case 5:
                //lessons
                Intent intent = new Intent(MainActivity.this, LessonsActivity.class);
                startActivity(intent);
                break;
            case 6:
                //general setting
                Intent itent2 = new Intent(this, SettingActivity.class);
                startActivity(itent2);
                break;
            case 7:
                // key terms and definitions

                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main, LessonShowFragment.newInstance(0, TERMS_KEY));
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 8:
                // references
                android.support.v4.app.FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                transaction2.replace(R.id.main, LessonShowFragment.newInstance(0, REFERENCES_KEY));
                transaction2.addToBackStack(null);
                transaction2.commit();
                break;
            case 9:

        }
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
