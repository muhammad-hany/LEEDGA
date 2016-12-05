package com.leedga.seagate.leedga;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

import static com.leedga.seagate.leedga.REF.ANSWER;
import static com.leedga.seagate.leedga.REF.CATEGORY_NAMES;
import static com.leedga.seagate.leedga.REF.DB_PATH;
import static com.leedga.seagate.leedga.REF.FIFTH_CHOICE;
import static com.leedga.seagate.leedga.REF.FIRST_CHOICE;
import static com.leedga.seagate.leedga.REF.FLAGGED;
import static com.leedga.seagate.leedga.REF.FOURTH_CHOICE;
import static com.leedga.seagate.leedga.REF.ID;
import static com.leedga.seagate.leedga.REF.NOTES_ON_ANSWER;
import static com.leedga.seagate.leedga.REF.QUESTION_KEY;
import static com.leedga.seagate.leedga.REF.SECOND_CHOICE;
import static com.leedga.seagate.leedga.REF.SIXITH_CHOICE;
import static com.leedga.seagate.leedga.REF.TABLE_NAME;
import static com.leedga.seagate.leedga.REF.THIRD_CHOICE;
import static com.leedga.seagate.leedga.REF.TYPE;
import static com.leedga.seagate.leedga.REF.typesNames;

/**
 * Created by Muhammad Workstation on 12/09/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static String DB_NAME;// your database name
    public int GetCursor;
    private Context myContext;
    private SQLiteDatabase db;



    public DBHelper(Context context, String db_name) {
        super(context, db_name, null, 2);
        if (db != null && db.isOpen())
            close();

        this.myContext = context;
        DB_NAME = db_name;

        try {
            createDataBase();
            openDataBase();
        } catch (IOException e) {
            // System.out.println("Exception in creation of database : "+
            // e.getMessage());
            e.printStackTrace();
        }

    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {

        } else {
            this.getReadableDatabase();

            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private void copyDatabase() throws IOException {
        InputStream input = myContext.getAssets().open(DB_NAME);
        String outputFileName = DB_PATH + DB_NAME;
        OutputStream output = new FileOutputStream(outputFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        // Close the streams
        output.flush();
        output.close();
        input.close();
        // System.out.println(DB_NAME + "Database Copied !");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDataBase() throws SQLException {
        // Open the database
        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        Log.i("SQLITE","sdsd");

    }

    public boolean isOpen() {
        if (db != null)
            return db.isOpen();
        return false;
    }

    @Override
    public synchronized void close() {
        if (db != null)
            db.close();
        super.close();
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            // System.out.println("My Pathe is:- " + myPath);
            // System.out.println("Open");
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            // System.out.println("checkDB value:" + checkDB);
            // System.out.println("My Pathe is:- " + myPath);
        } catch (Exception e) {
            // database does't exist yet.
        }

        if (checkDB != null) {
            // System.out.println("Closed");
            checkDB.close();
            // System.out.println("My db is:- " + checkDB.isOpen());
        }

        return checkDB != null;
    }



    public Cursor execCursorQuery(String sql) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
            GetCursor = cursor.getCount();
            Log.i("SQLITE", sql);
        } catch (Exception e) {
            Log.i("SQLITE", e.getMessage());
        }
        return cursor;
    }

    public void execNonQuery(String sql) {
        try {
            db.execSQL(sql);
            // Log.d("SQL", sql);
        } catch (Exception e) {
            // Log.e("Err", e.getMessage());
        } finally {
            // closeDb();
        }
    }



    protected ArrayList<Question> getAll(boolean[] chaptersTrueFalse,int [] numberPerCategory ,boolean [] questionsTypes){

        ArrayList<Question> questions=new ArrayList<>();

        for (int i=0;i<9;i++){
            if (chaptersTrueFalse[i]) {
                StringBuilder query=new StringBuilder();
                if (questionsTypes[TestTypeFragment.SINGLE_CHOICE] &&
                        questionsTypes[TestTypeFragment.MULTI_CHOICE] &&
                        questionsTypes[TestTypeFragment.TRUE_FALSE]){

                }
                for (int j=0; j<typesNames.length;j++){
                    if (questionsTypes[j]){
                        query.append("'"+typesNames[j]+"'");
                        query.append(" , ");

                    }
                }
                query.delete(query.length()-3,query.length()-1);
                String qw="( "+query.toString()+")";


                Cursor cursor = db.query(TABLE_NAME, null, REF.CATEGORY + " = '" + CATEGORY_NAMES[i] + "' AND " + REF.KEY + " IN " + qw, null, null, null, "random()");
                Question q=null;
                int count=0;
                while (cursor.moveToNext()){
                    count++;
                    if (count>numberPerCategory[i]){
                        cursor.close();
                        break;
                    }else {
                        String question = cursor.getString(cursor.getColumnIndex(QUESTION_KEY));
                        String ch1 = cursor.getString(cursor.getColumnIndex(FIRST_CHOICE));
                        String ch2 = cursor.getString(cursor.getColumnIndex(SECOND_CHOICE));
                        String ch3 = cursor.getString(cursor.getColumnIndex(THIRD_CHOICE));
                        String ch4 = cursor.getString(cursor.getColumnIndex(FOURTH_CHOICE));
                        String ch5 = cursor.getString(cursor.getColumnIndex(FIFTH_CHOICE));
                        String ch6 = cursor.getString(cursor.getColumnIndex(SIXITH_CHOICE));
                        String answer = cursor.getString(cursor.getColumnIndex(ANSWER));
                        String note = cursor.getString(cursor.getColumnIndex(NOTES_ON_ANSWER));
                        String categoty = cursor.getString(cursor.getColumnIndex(REF.CATEGORY));
                        String key = cursor.getString(cursor.getColumnIndex(REF.KEY));

                        int flagString = cursor.getInt(cursor.getColumnIndex(FLAGGED));
                        int id = cursor.getInt(cursor.getColumnIndex(ID));
                        int type = cursor.getInt(cursor.getColumnIndex(TYPE));
                        boolean flag;
                        flag = flagString != 0;
                        q = new Question(question, ch1, ch2, ch3, ch4, ch5, ch6, answer, note,
                                categoty, type, id, key, flag);
                        questions.add(q);
                    }
                }
                cursor.close();
            }
        }


        return questions;
    }


    public void updateFlag(int id, boolean flag) {
        ContentValues contentValues = new ContentValues();
        if (flag) {
            contentValues.put(FLAGGED, 1);
        } else {
            contentValues.put(FLAGGED, 0);
        }
        db.update(TABLE_NAME, contentValues, ID + " = '" + id + "'", null);
    }

    public void editQuestion(ContentValues contentValues, int id) {
        db.update(TABLE_NAME, contentValues, ID + " = '" + id + "'", null);
    }


    public void addQuestion(ContentValues contentValues) {
        db.insert(TABLE_NAME, null, contentValues);
    }

    public void deleteAllFlags() {
        Cursor cursor = db.query(TABLE_NAME, null, FLAGGED + " = 1", null, null, null, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put(FLAGGED, 0);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(ID));
            db.update(TABLE_NAME, contentValues, ID + " = '" + id + "'", null);
        }
        cursor.close();
    }

    public int getFlaggedCount() {
        Cursor cursor = db.query(TABLE_NAME, null, FLAGGED + " = 1", null, null, null, null);
        cursor.moveToFirst();
        int i = cursor.getCount();
        cursor.close();
        return i;
    }

    public ArrayList<Question> getFlaggedQuestions(int questionsCount) {
        Cursor cursor = db.query(TABLE_NAME, null, FLAGGED + " = 1", null, null, null, null);
        ArrayList<Question> questions = new ArrayList<>();
        int count = 0;
        while (cursor.moveToNext()) {
            if (count > questionsCount) {
                cursor.close();
                break;
            }
            String question = cursor.getString(cursor.getColumnIndex(QUESTION_KEY));
            String ch1 = cursor.getString(cursor.getColumnIndex(FIRST_CHOICE));
            String ch2 = cursor.getString(cursor.getColumnIndex(SECOND_CHOICE));
            String ch3 = cursor.getString(cursor.getColumnIndex(THIRD_CHOICE));
            String ch4 = cursor.getString(cursor.getColumnIndex(FOURTH_CHOICE));
            String ch5 = cursor.getString(cursor.getColumnIndex(FIFTH_CHOICE));
            String ch6 = cursor.getString(cursor.getColumnIndex(SIXITH_CHOICE));
            String answer = cursor.getString(cursor.getColumnIndex(ANSWER));
            String note = cursor.getString(cursor.getColumnIndex(NOTES_ON_ANSWER));
            String categoty = cursor.getString(cursor.getColumnIndex(REF.CATEGORY));
            String key = cursor.getString(cursor.getColumnIndex(REF.KEY));

            int flagString = cursor.getInt(cursor.getColumnIndex(FLAGGED));
            int id = cursor.getInt(cursor.getColumnIndex(ID));
            int type = cursor.getInt(cursor.getColumnIndex(TYPE));
            boolean flag;
            flag = flagString != 0;
            Question q = new Question(question, ch1, ch2, ch3, ch4, ch5, ch6, answer, note,
                    categoty, type, id, key, flag);
            questions.add(q);
            count++;
        }
        cursor.close();
        return questions;
    }



    private int getRowsCount(String tableName) {
        String countQuery="SELECT * FROM "+tableName;
        Cursor cursor=db.rawQuery(countQuery,null);
        int count =cursor.getCount();
        cursor.close();
        return count;
    }

    public Question getRandomQuestion() {
        int i = 0;
        Question q = null;
        Random random = new Random();
        int randomId = random.nextInt(getRowsCount(TABLE_NAME));
        Cursor cursor = db.query(TABLE_NAME, null, ID + " = " + randomId, null, null, null,
                null);
        while (cursor.moveToNext()) {

            String question = cursor.getString(cursor.getColumnIndex(QUESTION_KEY));
            String ch1 = cursor.getString(cursor.getColumnIndex(FIRST_CHOICE));
            String ch2 = cursor.getString(cursor.getColumnIndex(SECOND_CHOICE));
            String ch3 = cursor.getString(cursor.getColumnIndex(THIRD_CHOICE));
            String ch4 = cursor.getString(cursor.getColumnIndex(FOURTH_CHOICE));
            String ch5 = cursor.getString(cursor.getColumnIndex(FIFTH_CHOICE));
            String ch6 = cursor.getString(cursor.getColumnIndex(SIXITH_CHOICE));
            String answer = cursor.getString(cursor.getColumnIndex(ANSWER));
            String note = cursor.getString(cursor.getColumnIndex(NOTES_ON_ANSWER));
            String categoty = cursor.getString(cursor.getColumnIndex(REF.CATEGORY));
            String key = cursor.getString(cursor.getColumnIndex(REF.KEY));

            int flagString = cursor.getInt(cursor.getColumnIndex(FLAGGED));
            int id = cursor.getInt(cursor.getColumnIndex(ID));
            int type = cursor.getInt(cursor.getColumnIndex(TYPE));
            boolean flag;
            flag = flagString != 0;
            q = new Question(question, ch1, ch2, ch3, ch4, ch5, ch6, answer, note,
                    categoty, type, id, key, flag);
            i++;
        }

        cursor.close();
        return q;
    }


}
