package com.leedga.seagate.leedga;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Calendar;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private LinearLayout examDate, clearHistory, clearFlags;
    private Switch questionOfDayAlert;
    private TextView dateText;
    private DatePickerDialog datePickerDialog;
    private SharedPreferences generalSetting;
    private Calendar examDateCalendar;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        generalSetting = getSharedPreferences(REF.GENERAL_SETTING_PREF, MODE_PRIVATE);
        String json = generalSetting.getString(REF.SCHEDULE_EXAM_DATE_PREF, null);
        Gson gson = new Gson();
        examDateCalendar = gson.fromJson(json, Calendar.class);


        defineViews();
        showingSettings();

        //defining alarm manger


    }

    private void showingSettings() {
        String examDate;
        if (generalSetting.contains(REF.SCHEDULE_EXAM_DATE_PREF)) {
            examDate = examDateCalendar.get(Calendar.DAY_OF_MONTH) + "/" + examDateCalendar.get(Calendar.MONTH) + "/" + examDateCalendar.get(Calendar.YEAR);

        } else {
            examDate = "not set yet";
        }
        dateText.setText(examDate);


    }

    private void startNotificationService() {
        Intent intent = new Intent(SettingActivity.this, NotificationService.class);
        Gson gson = new Gson();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingActivity.this, REF.PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private void defineViews() {
        examDate = (LinearLayout) findViewById(R.id.l1);
        examDate.setOnClickListener(this);
        clearHistory = (LinearLayout) findViewById(R.id.l3);
        clearHistory.setOnClickListener(this);
        clearFlags = (LinearLayout) findViewById(R.id.l4);
        clearFlags.setOnClickListener(this);

        dateText = (TextView) findViewById(R.id.date);
        questionOfDayAlert = (Switch) findViewById(R.id.s1);
        questionOfDayAlert.setOnCheckedChangeListener(this);
        defineDatePickerDialog();

    }


    private void defineDatePickerDialog() {
        Calendar calendar;
        if (examDateCalendar != null) {
            calendar = examDateCalendar;
        } else {
            calendar = Calendar.getInstance();
        }
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar savedDate = Calendar.getInstance();
                savedDate.set(Calendar.YEAR, year);
                savedDate.set(Calendar.MONTH, monthOfYear);
                savedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Gson gson = new Gson();
                String s = gson.toJson(savedDate);
                SharedPreferences.Editor editor = generalSetting.edit();
                editor.putString(REF.SCHEDULE_EXAM_DATE_PREF, s);
                editor.apply();
                dateText.setText(dayOfMonth + "/" + monthOfYear + "/" + year);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.l1:
                datePickerDialog.show();
                break;
            case R.id.l3:
                resetTestHistory();
                break;
            case R.id.l4:
                resetQuestionFlags();
        }
    }

    private void resetQuestionFlags() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete Questions Flags?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBHelper helper = new DBHelper(SettingActivity.this, REF.DATABASE_NAME);
                helper.deleteAllFlags();
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
    }

    private void resetTestHistory() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure  you want to delete Tests History?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences prefs = getSharedPreferences(ResultActivity.TESTS_PREFS, MODE_PRIVATE);
                prefs.edit().clear().apply();
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
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = generalSetting.edit();
        switch (buttonView.getId()) {
            case R.id.s1:
                if (!isChecked) {
                    editor.putBoolean(REF.DAY_QUESTION_PREF, false);
                    editor.apply();
                    cancelNotificationService();
                } else {
                    editor.putBoolean(REF.DAY_QUESTION_PREF, true);
                    editor.apply();
                    startNotificationService();
                }

        }
    }

    private void cancelNotificationService() {
        Intent intent = new Intent(SettingActivity.this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingActivity.this, REF.PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
