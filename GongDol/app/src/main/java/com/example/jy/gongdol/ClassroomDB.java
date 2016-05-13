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

    static final String dbName = "Classroom";
    static final String TAG = "ClassroomDB";

    static final String classroomName = "ClassroomName";
    static final String courseID = "CourseID";
    static final String day = "Day";
    static final String courseName = "Course";
    static final String startTime = "0800";
    static final String endTime = "0800";
    static final String DATABASE_CREATE = "CREATE TABLE " + dbName + "(" + classroomName + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + courseName + " TEXT, " + courseID + " Integer, " + day
            + "INTEGER NOT NULL" + startTime + "INTEGER NOT NULL" + endTime + "INTEGER NOT NULL+ deptTable" + ");";
    static final int DBversion = 1;

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public ClassroomDB(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    public class Classroom{
       // private
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, dbName, null, DBversion);
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

            db.execSQL("DROP TABLE IF EXISTS " + dbName);

            //db.execSQL("DROP VIEW IF EXISTS "+viewEmps);
            onCreate(db);
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

}

