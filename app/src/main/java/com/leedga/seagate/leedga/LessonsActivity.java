package com.leedga.seagate.leedga;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import static com.leedga.seagate.leedga.REF.REFERENCES_KEY;
import static com.leedga.seagate.leedga.REF.TERMS_KEY;

public class LessonsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        String fragmentType = getIntent().getStringExtra(com.leedga.seagate.leedga.REF.LESSON_ACTIVITY_KEY);
        if (fragmentType.equals(REF.LESSONS)) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content_lessons, new LessonsMenuFragment());
            transaction.commit();
        } else if (fragmentType.equals(REF.KEY_TERMS)) {
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_lessons, LessonShowFragment.newInstance(0, TERMS_KEY));
            transaction.commit();
        } else if (fragmentType.equals(REF.REFERENCE)) {
            android.support.v4.app.FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
            transaction2.replace(R.id.content_lessons, LessonShowFragment.newInstance(0,
                    REFERENCES_KEY));
            transaction2.commit();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
