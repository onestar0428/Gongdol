package com.example.jy.gongdol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.util.Log;
/**
 * Created by YY on 2016-05-30.
 */
/**
 * Created by YY on 2016-06-04.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class ClassroomDB {
    static final String KEY_ROWID = "_id";
    static final String KEY_SID = "sid";
    static final String KEY_SNAME = "subject";
    static final String KEY_PNAME = "prof";
    static final String KEY_TIME = "time";
    static final String KEY_BUILDING = "building";
    static final String KEY_CLASS = "classroom";



    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLE = "Classroom";
    static final int DATABASE_VERSION = 4;
    static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
                    + "sid text not null unique , subject text not null, prof text not null, time text not null, building text not null, classroom text not null );";
    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;
    public ClassroomDB(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);

    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {

            String drop = "drop table if exists "+DATABASE_TABLE;
            try {

                db.execSQL(drop);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {

                db.execSQL(DATABASE_CREATE);

             /*   db.execSQL("insert into "+DATABASE_TABLE+"(sid, subject, prof, time, building, classroom) values ('06480001', '데이터마이닝 (원어강의)', 정옥란, '금A ,금B','IT대학','412' );");
                db.execSQL("insert into "+DATABASE_TABLE+"(sid, subject, prof, time, building, classroom) values ('10176001', '컴퓨터그래픽스 (원어강의)', '최진우', '화4 ,화5 ,화6 ,화7','IT대학', '304');");
                db.execSQL("insert into "+DATABASE_TABLE+"(sid, subject, prof, time, building, classroom) values ('10176002','컴퓨터그래픽스 (원어강의)',\t'최진우','목4 ,목5 ,목6 ,목7','IT대학','304');");
                db.execSQL("insert into "+DATABASE_TABLE+"(sid, subject, prof, time, building, classroom) values ('10177001','소프트웨어공학 (원어강의)','최아영','금A ,금B','IT대학','304');");
                db.execSQL("insert into "+DATABASE_TABLE+"(sid, subject, prof, time, building, classroom) values ('10178001','모바일프로그래밍 (원어강의)','최재혁','월3 ,월4 ,수2 ,수3','IT대학','412');");
                db.execSQL("insert into "+DATABASE_TABLE+"(sid, subject, prof, time, building, classroom) values ('10178002','모바일프로그래밍 (원어강의)','최재혁','월5 ,월6 ,수5 ,수6','IT대학','412');");
                */
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS Classroom");
            onCreate(db);
        }
    }
    // ---opens the database---
    public ClassroomDB open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    // ---closes the database---
    public void close() {
        DBHelper.close();
    }

    // ---insert a contact into the database---
    public long insertContact(String sid, String sub,String prof, String time, String bd, String cl) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SID, sid);
        initialValues.put(KEY_SNAME, sub);
        initialValues.put(KEY_PNAME, prof);
        initialValues.put(KEY_TIME, time);
        initialValues.put(KEY_BUILDING, bd);
        initialValues.put(KEY_CLASS, cl);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    // ---deletes a particular contact---
    public boolean deleteContact(long rowId) {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    // ---retrieves all the Classroom---
    public Cursor getAllClassroom() {
        return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_SID, KEY_SNAME, KEY_PNAME, KEY_TIME, KEY_BUILDING,
                KEY_CLASS }, null, null, null, null, null);
    }


    // ---retrieves all the Classroom---
    public Cursor getPartClasses(String cl) {
        return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_SID, KEY_SNAME, KEY_PNAME, KEY_TIME, KEY_BUILDING,
                KEY_CLASS }, KEY_CLASS+ "=" +cl, null, null, null, null);
    }
    // ---retrieves a particular contact---
    public Cursor getContact(long rowId) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_SID, KEY_SNAME, KEY_PNAME, KEY_TIME, KEY_BUILDING,
                        KEY_CLASS }, KEY_ROWID + "=" + rowId,
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    // ---updates a contact---
    public boolean updateContact(long rowId, String sid, String sub,String prof, String time, String bd, String cl ) {
        ContentValues args = new ContentValues();
        args.put(KEY_SID, sid);
        args.put(KEY_SNAME, sub);
        args.put(KEY_PNAME, prof);
        args.put(KEY_TIME, time);
        args.put(KEY_BUILDING, bd);
        args.put(KEY_CLASS, cl);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}// class DBAdapter
