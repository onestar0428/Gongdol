package com.example.jy.gongdol;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class mySchedule extends AppCompatActivity {
    ClassroomDB db;
    ArrayList<TimeTable> tt = new ArrayList<TimeTable>(); // list for data from db
    String[][] classtime = new String[10][4];
    TextView[] table;//textview array for subjects
    TextView[] time;
    int latest = 1700; //initialize latest time
    int earliest = 800;
    int hourHeight;

    static String Monday = "Mon";
    static String Tuesday = "Tue";
    static String Wednesday = "Wed";
    static String Thursday = "Thu";
    static String Friday = "Fri";

    LinearLayout.LayoutParams l;
    LinearLayout linearForTime;
    GridLayout layoutMon, layoutTue, layoutWed, layoutThu, layoutFri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);// 타이틀 안보이게한다.

        db = new ClassroomDB(this);
        linearForTime = (LinearLayout) findViewById(R.id.time);
        layoutMon = (GridLayout) findViewById(R.id.monday);
        layoutTue = (GridLayout) findViewById(R.id.tuesday);
        layoutWed = (GridLayout) findViewById(R.id.wednesday);
        layoutThu = (GridLayout) findViewById(R.id.thursday);
        layoutFri = (GridLayout) findViewById(R.id.friday);

        initArrayList();
        setFirstRow();

        Button b = (Button) findViewById(R.id.stack);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mySchedule.this, Pop.class));
            }

        });
    }

    //this overrides onWindowFocusChanged method for getting height from not-yet-laid-out object
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // the height will be set at this point
        hourHeight = (linearForTime.getMeasuredHeight()) / time.length;
        hourHeight = time[0].getMeasuredHeight();
        makeTable();
        //Toast.makeText(getApplicationContext(), hourHeight + " " + linearForTime.getMeasuredHeight() + " " + time[0].getMeasuredHeight(), Toast.LENGTH_SHORT).show();
    }

    //Temporary class for inputting tables
    public void initArrayList() {
        String[][] s = {{"6440001", "자료구조및실습 (원어강의)", "김원", "목1 ,목2 ,목3 ,목4", "IT대학-601"},
                {"9392001", "이산수학 (원어강의) ", "최재혁", "월E ,금D", "IT대학-412"},
                {"9656002", "웹프로그래밍 (원어강의)", "정옥란", "화2 ,화3 ,목2 ,목3", "IT대학-413"},
                {"11672001", "컴퓨터프로그래밍 (원어강의)", "최아영", "화2 ,화3 ,목2 ,목3", "IT대학-412"}};
        int i=0;

        db.open();
        Cursor c = db.getAllClassrooms();
        if (c.moveToFirst()) {
            do {
                Log.w("TAG_check", c.getString(0));
            } while (c.moveToNext());
        }
        db.close();

        db.open();
        for(i=0; i<s.length; i++){
            long l = db.insertClassroom(s[i][0], s[i][1], s[i][2], s[i][3], s[i][4]);
            //classtime[i][] = s[i][3].split(",");
            //checkLatestTime(Integer.parseInt(s[i][3]));
        }
        db.close();

        db.open();
        //Cursor c = db.getAllClassrooms();
        if (c.moveToFirst()) {
            do {
                TimeTable t = new TimeTable();
                t.setSubject(c.getString(1));
                t.setStart(Integer.parseInt(c.getString(2)));
                t.setEnd(Integer.parseInt(c.getString(3)));
                t.setDay(c.getString(4));
                t.setClassroom(c.getString(5));
                tt.add(t);
            } while (c.moveToNext());
        }
        db.close();

        arraySort();
    }

    //sorting arraylist in order of starting time
    public void arraySort() {
        Comparator<TimeTable> comp = new Comparator<TimeTable>() {
            @Override
            public int compare(TimeTable t1, TimeTable t2) {
                return (t1.getStart()) > (t2.getStart()) ? 1 : -1;
            }

        };
        Collections.sort(tt, comp);

        //checking order of arraylist<timetable>
        /*
        String str = "";
        for(int i=0; i<tt.size(); i++) {
            str += tt.get(i).getSubject();
        }
        Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();
        */
    }

    //find max value for creating first row of timetable
    public void checkLatestTime(int endTime) {
        if (endTime > latest)
            latest = endTime + 100;
    }

    //creating first row
    public void setFirstRow() {
        time = new TextView[(latest - earliest) / 100 + 1];
        l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, hourHeight, 1);

        for (int i = 0; i < time.length-1; i++) {
            time[i] = new TextView(this);
            time[i].setText((earliest / 100 + i) + "");
            time[i].setTextColor(Color.WHITE);
            linearForTime.addView(time[i], l);
        }
    }

    //fill the timetable for calculating location and height of each subject
    public void makeTable() {
        LinearLayout.LayoutParams lForBlank;
        TextView blank; // blank between subjects
        table = new TextView[tt.size()];
        int[] start = {earliest, earliest, earliest, earliest, earliest};
        //hourHeight = time[0].getHeight()/((latest - earliest)/100 + 1);

        for (int i = 0; i < tt.size(); i++) {
            table[i] = new TextView(this);
            table[i].setText(tt.get(i).getSubject() + "\n" + tt.get(i).getClassroom());
            table[i].setBackgroundColor(Color.WHITE);
            //table[i].setOnClickListener(this);

            int hour = ((tt.get(i).getEnd() - tt.get(i).getStart())) / 100 * hourHeight;
            int minute = ((tt.get(i).getEnd() - tt.get(i).getStart())) % 100 * hourHeight / 100;
            l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, hour + minute);

            //calculating location and height of each subject
            switch (tt.get(i).getDay()) {
                case "Mon":
                    lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            ((tt.get(i).getStart() - start[0]) / 100 * hourHeight) + (tt.get(i).getStart() - start[0]) % 100 * hourHeight / 100);
                    blank = new TextView(this);
                    layoutMon.addView(blank, lForBlank);
                    layoutMon.addView(table[i], l);
                    start[0] = tt.get(i).getEnd();
                    break;
                case "Tue":
                    lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            ((tt.get(i).getStart() - start[1]) / 100 * hourHeight) + (tt.get(i).getStart() - start[1]) % 100 * hourHeight / 100);
                    blank = new TextView(this);
                    layoutTue.addView(blank, lForBlank);
                    layoutTue.addView(table[i], l);
                    start[1] = tt.get(i).getEnd();
                    break;
                case "Wed":
                    lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            ((tt.get(i).getStart() - start[2]) / 100 * hourHeight) + (tt.get(i).getStart() - start[2]) % 100 * hourHeight / 100);
                    blank = new TextView(this);
                    layoutWed.addView(blank, lForBlank);
                    layoutWed.addView(table[i], l);
                    start[2] = tt.get(i).getEnd();
                    break;
                case "Thu":
                    lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            ((tt.get(i).getStart() - start[3]) / 100) * hourHeight + (tt.get(i).getStart() - start[3]) % 100 * hourHeight / 100);
                    blank = new TextView(this);
                    layoutThu.addView(blank, lForBlank);
                    layoutThu.addView(table[i], l);
                    start[3] = tt.get(i).getEnd();
                    break;
                case "Fri":
                    lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            ((tt.get(i).getStart() - start[4]) / 100 * hourHeight) + (tt.get(i).getStart() - start[4]) % 100 * hourHeight / 100);
                    blank = new TextView(this);
                    layoutFri.addView(blank, lForBlank);
                    layoutFri.addView(table[i], l);
                    start[4] = tt.get(i).getEnd();
                    break;
            }
        }
    }

/*
    public int dip2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return  (dpValue * scale + 0.5f);
    }*/

}
