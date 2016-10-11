package com.leedga.seagate.leedga;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import static com.leedga.seagate.leedga.TestCategoriesFragment.TEST_BUNDLE;

public class MainActivity extends AppCompatActivity  {

    AlertDialog.Builder builder;
    private Test test;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button testButton= (Button) findViewById(R.id.test_btn);
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


    }

    private void checkThereIsUnfinishedTest() {
        preferences = getSharedPreferences(TestActivity.UNFINISHED_TEST, Context.MODE_PRIVATE);
        buildDialog();

        if (preferences.contains(TestActivity.UNFINISHED)) {
            String jsonTest = preferences.getString(TestActivity.UNFINISHED, null);
            Gson gson = new Gson();
            test = gson.fromJson(jsonTest, Test.class);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            Intent i = new Intent(MainActivity.this, TestSettingActivity.class);
            startActivity(i);
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
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MainActivity.this, TestSettingActivity.class);
                startActivity(i);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(TestActivity.UNFINISHED);
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
}
