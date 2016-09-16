package com.leedga.seagate.leedga;

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

/**
 * Created by Muhammad Workstation on 12/09/2016.
 */

public class DBHelper extends SQLiteOpenHelper {
    public int GetCursor;
    private Context myContext;
    public String DB_PATH = "data/data/com.leedga.seagate.leedga/databases/";
    public static String DB_NAME;// your database name
    private SQLiteDatabase db;


    public static final String SINGLE_CHOICE_LEED_TABLE ="leed_process_14";
    public static final String ID="id";
    public static final String QUESTION_KEY ="question";
    public static final String ANSWER="answer";
    public static final String FIRST_CHOICE="a";
    public static final String SECOND_CHOICE="b";
    public static final String THIRD_CHOICE="c";
    public static final String FOURTH_CHOICE="d";
    public static final String NOTES_ON_ANSWER="notes";


    public static final String MULTI_CHOICE_LEED_TABLE="leed_process_multi";
    public static final String FIFTH_CHOICE="e";
    public static final String SIXITH_CHOICE="f";
    public static final String MULT_CHOICE_TABLE_TYPE="type";


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

        return checkDB != null ? true : false;
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

    protected ArrayList<SingleChoiceQuestion> getSingleChoice(){
        if (db !=null){
            /*Random random=new Random();
            int max=getRowsCount(SINGLE_CHOICE_LEED_TABLE);
            int min=1;
            int i=random.nextInt(max-min+1)+min;
            Cursor cursor=db.query(SINGLE_CHOICE_LEED_TABLE,null,ID+" = '"+i+"'",null,null,null,
                    null);*/
            ArrayList<SingleChoiceQuestion> singleChoiceQuestions=new ArrayList<>();
            Cursor cursor=db.query(SINGLE_CHOICE_LEED_TABLE,null,null,null,null,null,"random()");
            SingleChoiceQuestion q = null;
            while (cursor.moveToNext()) {
                String question=cursor.getString(cursor.getColumnIndex(QUESTION_KEY));
                String ch1=cursor.getString(cursor.getColumnIndex(FIRST_CHOICE));
                String ch2=cursor.getString(cursor.getColumnIndex(SECOND_CHOICE));
                String ch3=cursor.getString(cursor.getColumnIndex(THIRD_CHOICE));
                String ch4=cursor.getString(cursor.getColumnIndex(FOURTH_CHOICE));
                String answer=cursor.getString(cursor.getColumnIndex(ANSWER));
                String note=cursor.getString(cursor.getColumnIndex(NOTES_ON_ANSWER));
                int id=cursor.getInt(cursor.getColumnIndex(ID));
                q = new SingleChoiceQuestion(question,ch1,ch2,ch3,ch4,answer,note,id);
                singleChoiceQuestions.add(q);
            }
            return singleChoiceQuestions;
        }else {
            return null;
        }
    }

    protected ArrayList<MultiChoiceQuestion> getMultiChoice(){
        if (db !=null){
            ArrayList<MultiChoiceQuestion>multiChoiceQuestions=new ArrayList<>();
            Cursor cursor=db.query(MULTI_CHOICE_LEED_TABLE,null,null,null,null,null,
                    "random()");
            MultiChoiceQuestion q = null;
            while (cursor.moveToNext()) {
                String question=cursor.getString(cursor.getColumnIndex(QUESTION_KEY));
                String ch1=cursor.getString(cursor.getColumnIndex(FIRST_CHOICE));
                String ch2=cursor.getString(cursor.getColumnIndex(SECOND_CHOICE));
                String ch3=cursor.getString(cursor.getColumnIndex(THIRD_CHOICE));
                String ch4=cursor.getString(cursor.getColumnIndex(FOURTH_CHOICE));
                String ch5=cursor.getString(cursor.getColumnIndex(FIFTH_CHOICE));
                String ch6=cursor.getString(cursor.getColumnIndex(SIXITH_CHOICE));
                String answer=cursor.getString(cursor.getColumnIndex(ANSWER));
                String note=cursor.getString(cursor.getColumnIndex(NOTES_ON_ANSWER));

                int id=cursor.getInt(cursor.getColumnIndex(ID));
                int type=cursor.getInt(cursor.getColumnIndex(MULT_CHOICE_TABLE_TYPE));
                q = new MultiChoiceQuestion(question,ch1,ch2,ch3,ch4,ch5,ch6,note,answer,type,id);
                multiChoiceQuestions.add(q);
            }
            return multiChoiceQuestions;
        }else {
            return null;
        }
    }



    private int getRowsCount(String tableName) {
        String countQuery="SELECT * FROM "+tableName;
        Cursor cursor=db.rawQuery(countQuery,null);
        int count =cursor.getCount();
        return count;
    }


}
