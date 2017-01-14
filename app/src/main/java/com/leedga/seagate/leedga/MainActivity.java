package com.leedga.seagate.leedga;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.vending.billing.IInAppBillingService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.vungle.publisher.VunglePub;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import util.IabBroadcastReceiver;

import static com.leedga.seagate.leedga.REF.DEFAULT_TEST_KEY;
import static com.leedga.seagate.leedga.REF.DEFAULT_TEST_PREF_KEY;
import static com.leedga.seagate.leedga.REF.FIREBASE_COMMIT_NUMBER_PREF_KEY;
import static com.leedga.seagate.leedga.REF.PREMIUM_USER_KEY;
import static com.leedga.seagate.leedga.TestActivity.UNFINISHED_TEST;
import static com.leedga.seagate.leedga.TestCategoriesFragment.TEST_BUNDLE;

public class MainActivity extends AppCompatActivity implements MainRecyclerAdaptor.OnMyItemClick, IabBroadcastReceiver.IabBroadcastListener {


    public static boolean mPremiumAcount;
    private final com.vungle.publisher.VunglePub vunglePub = VunglePub.getInstance();
    AlertDialog.Builder builder;
    private Test test;
    private SharedPreferences unFinishedTestPref;
    private RecyclerView recyclerView;
    private SharedPreferences defaultTestPref;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private MainRecyclerAdaptor adaptor;
    private BroadcastReceiver mBroadcastReceiver;
    private DatabaseReference mDatabase;
    private ContentValues contentValues;
    private SharedPreferences generalSettingPreferences;
    private IInAppBillingService mService;
    private ServiceConnection mServiceConnection;
    private Menu mainMenu;
    private CardView upgradeDetailsCard;
    private Button upgradeDetailsButton;
    private AlertDialog alertDialog;
    private ProgressDialog updateCheckDialog;
    private AlertDialog updateStatusDialog;
    private ProgressDialog questionLoadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("LEED Green Associate V4");
        generalSettingPreferences = getSharedPreferences(REF.GENERAL_SETTING_PREF, MODE_PRIVATE);
        setDefaultPrefrences();
        setDefaultTestPreferences();
        definingViews();

        vunglePub.init(this, REF.VUNGLE_APP_ID);
        checkForQuestionUpdate();


        setUpBillingSystem();



        ArrayList<Test> tests = getLastTests();
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        adaptor = new MainRecyclerAdaptor(this, this, tests);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (position) {
                    case 0:
                        return 3;
                    default:
                        return 1;
                }

            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        /*adaptor.setHasStableIds(true);*/
        recyclerView.setAdapter(adaptor);
        createQuestionLoadingDialog();


    }

    private void checkForQuestionUpdate() {
        long twoWeeksInterval = AlarmManager.INTERVAL_DAY * 14;
        SharedPreferences.Editor editor = generalSettingPreferences.edit();
        if (!generalSettingPreferences.contains(REF.LAST_CHECK_FOR_UPDATE_KEY)) {
            testFireBase();
            editor.putLong(REF.LAST_CHECK_FOR_UPDATE_KEY, System.currentTimeMillis());
            editor.apply();
        } else {
            long lastUpdate = generalSettingPreferences.getLong(REF.LAST_CHECK_FOR_UPDATE_KEY, 0);
            if (System.currentTimeMillis() > lastUpdate + twoWeeksInterval) {
                //need check For update
                testFireBase();
                editor.putLong(REF.LAST_CHECK_FOR_UPDATE_KEY, System.currentTimeMillis());
                editor.apply();
            }
        }

    }

    private void definingViews() {
        upgradeDetailsCard = (CardView) findViewById(R.id.upgradeDetailsCard);
        int visability = generalSettingPreferences.getBoolean(PREMIUM_USER_KEY, false) ? View.GONE :
                View.VISIBLE;
        upgradeDetailsCard.setVisibility(visability);
        upgradeDetailsButton = (Button) findViewById(R.id.upgradeDeatils);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Upgrade Details");
        alertBuilder.setMessage("Unlock additional 500 questions\nUnlock the question of the day\nNo ads");
        alertBuilder.setPositiveButton("Upgrade", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                upgradeAccount();
            }
        });
        alertDialog = alertBuilder.create();
        upgradeDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        updateCheckDialog = new ProgressDialog(this);
        updateCheckDialog.setTitle("Please wait");
        updateCheckDialog.setIndeterminate(true);
        updateCheckDialog.setCancelable(false);
        updateCheckDialog.setMessage("Checking for question updates");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Question update");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        updateStatusDialog = builder.create();


    }

    private void setUpBillingSystem() {


        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mService = IInAppBillingService.Stub.asInterface(iBinder);
                //checking is this premium account ?
                try {
                    Bundle ownedItems = mService.getPurchases(3, getPackageName(), "inapp", null);
                    int response = ownedItems.getInt("RESPONSE_CODE");

                    if (response == 0) {
                        int visibility;
                        ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                        for (String itemId : ownedSkus) {
                            mPremiumAcount = itemId.equals(REF.SKU_PREMIUM);
                        }

                        visibility = mPremiumAcount ? View.GONE : View.VISIBLE;
                        SharedPreferences.Editor editor = generalSettingPreferences.edit();
                        editor.putBoolean(PREMIUM_USER_KEY, /*mPremiumAcount*/true);
                        editor.apply();
                        updateDefaultTest(/*mPremiumAcount*/true);
                        upgradeDetailsCard.setVisibility(visibility);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
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

    private void updateDefaultTest(boolean mPremiumAcount) {
        defaultTestPref = getSharedPreferences(DEFAULT_TEST_PREF_KEY, MODE_PRIVATE);
        Gson gson = new Gson();
        Test test = gson.fromJson(defaultTestPref.getString(DEFAULT_TEST_KEY, null), Test.class);
        if (mPremiumAcount) {
            test.setQuestionTypes(new boolean[]{true, true, true});
        } else {
            test.setQuestionTypes(new boolean[]{false, true, false});
        }
        SharedPreferences.Editor editor = defaultTestPref.edit();
        String json = gson.toJson(test);
        editor.putString(DEFAULT_TEST_KEY, json);
        editor.apply();
    }

    /*private void changeUpgradeMenuItemState(boolean mPremiumAcount) {
        mainMenu.findItem(R.id.upgrarde).setVisible(mPremiumAcount);
    }*/

    private void testFireBase() {
        updateCheckDialog.show();
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DBHelper dbHelper = new DBHelper(this, REF.DATABASE_NAME);
        Question q = dbHelper.getRandomQuestion();
        final int versionCode = packageInfo.versionCode;
        final int lastCommitNumber = generalSettingPreferences.getInt(FIREBASE_COMMIT_NUMBER_PREF_KEY, 0);
        mDatabase.child("versions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("v" + versionCode)) {
                    contentValues = new ContentValues();

                    for (DataSnapshot questionSnap : dataSnapshot.getChildren()) {
                        updateDatabase(questionSnap, lastCommitNumber, dbHelper);
                    }
                } else {
                    updateCheckDialog.dismiss();
                    updateStatusDialog.setMessage("No updates found");
                    updateStatusDialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                updateCheckDialog.dismiss();
            }
        });

    }

    private void updateDatabase(DataSnapshot questionSnap, int lastCommitNumber, DBHelper dbHelper) {
        contentValues.clear();
        Integer i = questionSnap.child(REF.COMMIT_NUMBER_KEY).getValue(Integer.class);
        if (i != null) {
            if (i > lastCommitNumber) {
                contentValues.put(REF.QUESTION_KEY, questionSnap.child(REF.QUESTION_KEY).getValue(String.class));
                contentValues.put(REF.FIRST_CHOICE, questionSnap.child(REF.FIRST_CHOICE).getValue(String.class));
                contentValues.put(REF.SECOND_CHOICE, questionSnap.child(REF.SECOND_CHOICE).getValue(String.class));
                contentValues.put(REF.THIRD_CHOICE, questionSnap.child(REF.THIRD_CHOICE).getValue(String.class));
                contentValues.put(REF.FOURTH_CHOICE, questionSnap.child(REF.FOURTH_CHOICE).getValue(String.class));
                if (questionSnap.hasChild(REF.FIFTH_CHOICE)) {
                    contentValues.put(REF.FIFTH_CHOICE, questionSnap.child(REF.FIFTH_CHOICE).getValue(String.class));
                }
                if (questionSnap.hasChild(REF.SIXITH_CHOICE)) {
                    contentValues.put(REF.SIXITH_CHOICE, questionSnap.child(REF.SIXITH_CHOICE).getValue(String.class));
                }
                contentValues.put(REF.ANSWER, questionSnap.child(REF.ANSWER).getValue(String.class));
                contentValues.put(REF.NOTES_ON_ANSWER, questionSnap.child(REF.NOTES_ON_ANSWER).getValue(String
                        .class));
                contentValues.put(REF.CATEGORY, questionSnap.child(REF.CATEGORY).getValue(String
                        .class));
                contentValues.put(REF.KEY, questionSnap.child(REF.KEY).getValue(String
                        .class));
                contentValues.put(REF.FLAGGED, 0);
                contentValues.put(REF.TYPE, questionSnap.child(REF.TYPE).getValue(Integer
                        .class));
                if (questionSnap.child("action").getValue(String.class).equals("add")) {

                    dbHelper.addQuestion(contentValues);
                } else if (questionSnap.child("action").getValue(String.class).equals
                        ("edit")) {
                    if (questionSnap.hasChild(REF.ID)) {
                        int l = questionSnap.child(REF.ID).getValue(Integer.class);
                        dbHelper.editQuestion(contentValues, l);
                    }
                }

                SharedPreferences.Editor editor = generalSettingPreferences.edit();
                editor.putInt(REF.FIREBASE_COMMIT_NUMBER_PREF_KEY, i);
                editor.apply();
                updateCheckDialog.dismiss();
                updateStatusDialog.setMessage("Question database has been updated");
                updateStatusDialog.show();

            } else {
                updateCheckDialog.dismiss();
                updateStatusDialog.setMessage("No updates found");
                updateStatusDialog.show();
            }
        } else {
            updateCheckDialog.dismiss();
        }
    }



    private void setDefaultPrefrences() {
        SharedPreferences prefs = getSharedPreferences(REF.GENERAL_SETTING_PREF, MODE_PRIVATE);
        if (!prefs.contains(REF.DAY_QUESTION_PREF)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(REF.DAY_QUESTION_PREF, true);
            editor.putBoolean(REF.KNOWLEDGE_DOMAIN, true);
            editor.putInt(REF.FIREBASE_COMMIT_NUMBER_PREF_KEY, 0);
            editor.putLong(REF.FIRST_LOG_IN_KEY, System.currentTimeMillis());
            editor.putBoolean(PREMIUM_USER_KEY, false);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            editor.apply();
            Intent intent = new Intent(this, NotificationService.class);
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
            Test test = createDefaultTest();
            Gson gson = new Gson();
            String json = gson.toJson(test);
            editor.putString(DEFAULT_TEST_KEY, json);
            editor.apply();
        }
    }

    private Test createDefaultTest() {
        Test test = new Test();
        if (generalSettingPreferences.getBoolean(PREMIUM_USER_KEY, false)) {
            test.setQuestionTypes(new boolean[]{true, true, true});
        } else {
            test.setQuestionTypes(new boolean[]{false, true, false});
        }
        test.setNumberOfQuestions(10);
        test.setOnlyFlagged(false);
        test.setAnswerShow(TestTypeFragment.ANSWER_AFTER_ALL);
        test.setChapters(new boolean[]{true, true, true, true, true, true, true, true, true});
        return test;
    }

    protected void checkThereIsUnfinishedTest() {
        /*questionLoadDialog.show();*/
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
        ArrayList<Question> questions;
        if (test.isOnlyFlagged()) {
            questions = new DBHelper(this, REF.DATABASE_NAME).getFlaggedQuestions(test.getNumberOfQuestions());
        } else {
            if (generalSettingPreferences.getBoolean(PREMIUM_USER_KEY, false)) {
                questions = new DBHelper(this, REF.DATABASE_NAME).getAll(test.getChapters(), test.getcountPerCategory(), test.getQuestionTypes());
            } else {
                questions = new DBHelper(this, REF.DATABASE_NAME).getFree(test.getChapters(), test.getcountPerCategory(), test.getQuestionTypes());
            }
        }
        Collections.shuffle(questions);
        test.setQuestions(questions);
        i.putExtra(REF.TEST_FRAGMENT_TYPE, REF.FULL_QUESTIONS);
        i.putExtra(TEST_BUNDLE, test);
        startActivityForResult(i, 1);
    }

    private void createQuestionLoadingDialog() {
        questionLoadDialog = new ProgressDialog(this);
        questionLoadDialog.setTitle("Please wait");
        questionLoadDialog.setMessage("Loading Questions");
    }

    private void buildDialog() {
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to continue Your last Test ?");
        builder.setPositiveButton("Resume", new DialogInterface.OnClickListener() {
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
        mainMenu = menu;

        /*menu.getItem(R.id.upgrarde).setVisible(!mPremiumAcount);*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.rating:
                openAppPage();
                return true;

            /*case R.id.upgrarde:
                //make the purchase
                upgradeAccount();
                return true;*/

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openAppPage() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
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



        /*ArrayList<String> skuList = new ArrayList<>();
        skuList.add(REF.SKU_PREMIUM);
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
        Bundle skuDetails = null;
        try {
            skuDetails = mService.getSkuDetails(3, getPackageName(), "inapp", querySkus);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (skuDetails.getInt("RESPONSE_CODE") == 0) {
            ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
            for (String response : responseList) {
                try {
                    JSONObject object = new JSONObject(response);
                    String sku = object.getString("productId");
                    if (sku.equals(REF.SKU_PREMIUM))
                        mPremiumAcount = object.getString("price");
                    try {


                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case 5:
                SharedPreferences preferences = getSharedPreferences(UNFINISHED_TEST, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear().apply();
                break;
            case REF.RC_UPGRADE_PURCHASE:
                int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
                String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
                String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Upgrade account");

                String msg;
                if (resultCode == RESULT_OK) {
                    try {
                        JSONObject jsonObject = new JSONObject(purchaseData);
                        String sku = jsonObject.getString("productId");
                        msg = "You upgraded your account successfully";
                        upgradeDetailsCard.setVisibility(View.GONE);

                        SharedPreferences.Editor editor2 = generalSettingPreferences.edit();
                        editor2.putBoolean(PREMIUM_USER_KEY, true);
                        editor2.apply();

                        mPremiumAcount = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        msg = "Failed to upgrade your account";
                    }
                    builder.setMessage(msg);
                    builder.create().show();
                }
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
                i.putExtra(REF.DAY_QUESTION_PREF, false);
                startActivity(i);
                break;
            case 4:
                //question of the day
                // determine last time make question of question of day
                boolean isItPremiumUser = generalSettingPreferences.getBoolean(PREMIUM_USER_KEY, false);
                if (isItPremiumUser) {
                    makeQuestionOfDayAction();
                } else {
                    long trialTime = AlarmManager.INTERVAL_DAY * 5;
                    long firstLogIn = generalSettingPreferences.getLong(REF.FIRST_LOG_IN_KEY, 0);
                    if (System.currentTimeMillis() > firstLogIn + trialTime) {
                        //exceed trial time
                        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                        alert.setTitle("Caution");
                        alert.setMessage("If you want to proceed to Question of the day you should upgrade to premium account");
                        alert.setPositiveButton("Upgrade", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                upgradeAccount();
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alert.create().show();
                    } else {
                        // still in trial time
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                        alertBuilder.setTitle("Caution");
                        alertBuilder.setMessage("Question of will be available only for 5 days in the free version");
                        alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                makeQuestionOfDayAction();
                            }
                        });

                        alertBuilder.setNegativeButton("Upgrade", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                upgradeAccount();
                            }
                        });

                        alertBuilder.create().show();

                    }


                }


                break;
            case 5:
                //lessons
                Intent intent = new Intent(MainActivity.this, LessonsActivity.class);
                intent.putExtra(REF.LESSON_ACTIVITY_KEY, REF.LESSONS);
                startActivity(intent);
                break;
            case 6:
                //general setting
                Intent itent2 = new Intent(this, SettingActivity.class);
                startActivity(itent2);
                break;
            case 7:
                // key terms and definitions
                i = new Intent(this, LessonsActivity.class);
                i.putExtra(REF.LESSON_ACTIVITY_KEY, REF.KEY_TERMS);
                startActivity(i);

                /*android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main, LessonShowFragment.newInstance(0, TERMS_KEY));
                transaction.addToBackStack(null);
                transaction.commit();*/
                break;
            case 8:
                // references

                i = new Intent(this, LessonsActivity.class);
                i.putExtra(REF.LESSON_ACTIVITY_KEY, REF.REFERENCE);
                startActivity(i);

                /*android.support.v4.app.FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                transaction2.replace(R.id.main, LessonShowFragment.newInstance(0, REFERENCES_KEY));
                transaction2.addToBackStack(null);
                transaction2.commit();*/
                break;
            case 9:

        }
    }

    private void makeQuestionOfDayAction() {
        SharedPreferences preferences = getSharedPreferences(REF.GENERAL_SETTING_PREF, MODE_PRIVATE);

        long lastTime = preferences.getLong(REF.DAY_QUESTION_DATE_PREF_KEY, 0);
        float diff = (float) (System.currentTimeMillis() - lastTime) / (float) AlarmManager.INTERVAL_DAY;
        Intent i;
        if (diff > 1) {
            // make the question of day
            i = new Intent(this, TestActivity.class);
            i.putExtra(REF.TEST_FRAGMENT_TYPE, REF.SINGLE_QUESTION);
            startActivity(i);
        } else {
            // show history of question of day
            i = new Intent(MainActivity.this, HistoryActivity.class);
            i.putExtra(REF.DAY_QUESTION_PREF, true);
            startActivity(i);

        }
    }

    @Override
    public void onDetailsClick() {
        Intent i = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(i);

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

    @Override
    protected void onResume() {
        super.onResume();
        adaptor.setTests(getLastTests());
        adaptor.notifyDataSetChanged();
        vunglePub.onResume();
        if (questionLoadDialog != null) {
            questionLoadDialog.dismiss();
        }



    }

    @Override
    protected void onPause() {
        super.onPause();
        vunglePub.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mService != null) {
            unbindService(mServiceConnection);
        }
    }


    @Override
    public void receivedBroadcast() {

    }
}
