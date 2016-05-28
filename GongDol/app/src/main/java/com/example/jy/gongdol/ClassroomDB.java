package com.example.jy.gongdol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.util.Log;

/**
 * Created by jy on 2016-05-06.
 */
public class ClassroomDB {

    static final String DATABASE_NAME = "ClassroomDB";
    static final String DATABASE_TABLE = "ClassroomInfo";
    static final String TAG = "ClassroomDB";

    static final String KEY_ROWID = "_id";//학수번호
    static final String subject="Subject";//과목명
    static final String prof="Prof";
    static final String time = "Time";//강의시간
    static final String classroom = "Classroom";//강의실 호수
    static final String DATABASE_CREATE = "CREATE TABLE ClassroomInfo (_id INTEGER, Subject VARCHAR(20), Prof VARCHAR(10), Time VARCHAR(20), Classroom VARCHAR(20));";
    static final int DATABASE_VERSION = 1;

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    String[][] data = {{"6440001", "자료구조및실습 (원어강의)", "김원", "목1 ,목2 ,목3 ,목4", "IT대학-601"},
    {"9392001", "이산수학 (원어강의) ", "최재혁", "월A ,금D", "IT대학-412"},
    {"9656002", "웹프로그래밍 (원어강의)", "정옥란", "화2 ,화3 ,목2 ,목3", "IT대학-413"},
    {"11672001", "컴퓨터프로그래밍 (원어강의)", "최아영", "수2 ,수3 ,금2 ,금3", "IT대학-412"}};

    public ClassroomDB(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            //db.execSQL("DROP VIEW IF EXISTS "+viewEmps);
            onCreate(db);
        }
    }

    public void init(){
        for(int i=0; i<data.length; i++){
            this.insertClassroom(data[i][0], data[i][1], data[i][2], data[i][3], data[i][4]);
        }
    }

    public ClassroomDB open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // ---closes the database---
    public void close() {
        DBHelper.close();
    }

    // ---insert a Classroom into the database---
    public long insertClassroom(String n, String sub, String p, String t, String c) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, n);
        initialValues.put(subject, sub);
        initialValues.put(prof, p);
        initialValues.put(time, t);;
        initialValues.put(classroom, c);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    // ---deletes a particular Classroom---
    public boolean deleteClassroom(String rowId) {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    // ---retrieves all the Classrooms---
    public Cursor getAllClassrooms() {
        return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, subject, prof, time, classroom }, null, null, null, null, KEY_ROWID);
    }

    // ---retrieves a particular Classroom---
    public Cursor getClassroom(String rowId) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROWID, subject, prof, time, classroom }, KEY_ROWID + "=" + rowId,
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // ---updates a Classroom---
    public boolean updateClassroom(String n, String sub, String p, String t, String c) {
        ContentValues args = new ContentValues();
        args.put(KEY_ROWID, n);
        args.put(subject, sub);
        args.put(prof, p);
        args.put(time, t);
        args.put(classroom, c);

        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + n, null) > 0;
    }
}

