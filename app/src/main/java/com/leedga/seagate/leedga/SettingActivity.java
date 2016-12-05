package com.leedga.seagate.leedga;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Map;

import static com.leedga.seagate.leedga.MainActivity.mPremiumAcount;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private LinearLayout examDate, clearHistory, clearFlags;
    private Switch questionOfDayAlert;
    private TextView dateText;
    private DatePickerDialog datePickerDialog;
    private SharedPreferences generalSetting;
    private Calendar examDateCalendar;
    private AlarmManager alarmManager;
    private ServiceConnection mServiceConnection;
    private IInAppBillingService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("General Setting");


        generalSetting = getSharedPreferences(REF.GENERAL_SETTING_PREF, MODE_PRIVATE);
        String json = generalSetting.getString(REF.SCHEDULE_EXAM_DATE_PREF, null);
        Gson gson = new Gson();
        examDateCalendar = gson.fromJson(json, Calendar.class);

        setUpBillingSystem();
        defineViews();
        showingSettings();

        //defining alarm manger


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_general_setting, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                Log.i("sdf", "wqer");
                return true;
            case R.id.reset_settings:
                Log.i("sdf", "wqer");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showingSettings() {
        showScheduleExamDate();
        showSwitches();


    }

    private void showSwitches() {
        questionOfDayAlert.setChecked(generalSetting.getBoolean(REF.DAY_QUESTION_PREF, false));
    }

    private void showScheduleExamDate() {
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
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingActivity.this, REF.PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
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

        LinearLayout layout = (LinearLayout) findViewById(R.id.l5);
        View view = findViewById(R.id.lastLine);
        int visibility = mPremiumAcount ? View.GONE : View.VISIBLE;
        layout.setVisibility(visibility);
        view.setVisibility(visibility);

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Upgrade Details");
        alertBuilder.setMessage("Unlock additional XX questions\nUnlock the question of the day\nNo ads");
        alertBuilder.setPositiveButton("Upgrade", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                upgradeAccount();
            }
        });
        final AlertDialog alertDialog = alertBuilder.create();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        defineDatePickerDialog();

    }

    private void upgradeAccount() {
        Bundle buyIntentBundle = null;
        try {
            buyIntentBundle = mService.getBuyIntent(3, getPackageName(), REF.SKU_PREMIUM, "inapp", "");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (buyIntentBundle.getInt("RESPONSE_CODE") == 0) {
            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
            try {
                startIntentSenderForResult(pendingIntent.getIntentSender(), REF.RC_UPGRADE_PURCHASE, new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
    }


    private void setUpBillingSystem() {


        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mService = IInAppBillingService.Stub.asInterface(iBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mService = null;
            }
        };

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConnection, BIND_AUTO_CREATE);


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
        builder.setMessage("Are you sure you want to clear Questions Flags?");
        builder.setPositiveButton("Clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBHelper helper = new DBHelper(SettingActivity.this, REF.DATABASE_NAME);
                helper.deleteAllFlags();
                clearFlagFromHistory();
                clearFlagsFromDefualtTest();
                Toast.makeText(SettingActivity.this, "Questions flags has been cleared", Toast
                        .LENGTH_LONG).show();

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

    private void clearFlagsFromDefualtTest() {
        SharedPreferences sharedPreferences = getSharedPreferences(REF.DEFAULT_TEST_PREF_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences.edit();
        String decode = sharedPreferences.getString(REF.DEFAULT_TEST_KEY, null);
        Gson gson = new Gson();
        Test sTest = gson.fromJson(decode, Test.class);
        sTest.setOnlyFlagged(false);
        sTest.setNumberOfQuestions(10);
        decode = gson.toJson(sTest);
        editor1.putString(REF.DEFAULT_TEST_KEY, decode);
        editor1.apply();
    }


    public void clearFlagFromHistory() {
        SharedPreferences prefs = getSharedPreferences(ResultActivity.TESTS_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Map<String, ?> tests = prefs.getAll();
        Gson gson = new Gson();
        for (Map.Entry<String, ?> test : tests.entrySet()) {
            String entry = test.getValue().toString();
            Test testt = gson.fromJson(entry, Test.class);
            for (Question question : testt.getAnsweredQuestions()) {
                if (question.isFlagged()) {
                    question.setFlagged(false);
                }
            }
            for (Question question : testt.getQuestions()) {
                if (question.isFlagged()) {
                    question.setFlagged(false);
                }
            }
            entry = gson.toJson(testt);
            editor.putString(testt.getTestId(), entry);
            editor.apply();
        }


    }

    private void resetTestHistory() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure  you want to clear Tests History?");
        builder.setPositiveButton("clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences prefs = getSharedPreferences(ResultActivity.TESTS_PREFS, MODE_PRIVATE);
                prefs.edit().clear().apply();
                dialog.dismiss();
                Toast.makeText(SettingActivity.this, "Test history has been cleared", Toast
                        .LENGTH_LONG).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConnection);
        }
    }
}
