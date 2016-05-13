package com.example.jy.gongdol;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String[][] db = {{"Subject 6", "1700", "2000", "Mon", "413"}, {"Subject 1", "1300", "1450", "Mon", "413"},{"Subject 3", "1700", "1750", "Wed", "413"},
            {"Subject 4", "1330", "1550", "Thu", "413"}, {"Subject 3", "1700", "1750", "Tue", "413"}, {"Subject 2", "1000", "1400", "Tue", "412"},
            {"Subject 5", "1215", "1745", "Fri", "412"}, {"Subject 7", "1115", "1345", "Wed", "413"}};
    ArrayList<TimeTable> tt = new ArrayList<TimeTable>(); // list for data from db
    TextView[] table;//textview array for subjects
    TextView[] time;
    int latest = 1700; //initialize latest time
    static int earliest = 800;
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
        setContentView(R.layout.activity_main);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);// 타이틀 안보이게한다.

        linearForTime = (LinearLayout) findViewById(R.id.time);
        layoutMon = (GridLayout) findViewById(R.id.monday);
        layoutTue = (GridLayout) findViewById(R.id.tuesday);
        layoutWed = (GridLayout) findViewById(R.id.wednesday);
        layoutThu = (GridLayout) findViewById(R.id.thursday);
        layoutFri = (GridLayout) findViewById(R.id.friday);

        initArrayList();
        setFirstRow();
    }
    //this overrides onWindowFocusChanged method for getting height from not-yet-laid-out object
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // the height will be set at this point
        hourHeight = (linearForTime.getMeasuredHeight())/time.length;
        hourHeight = time[0].getMeasuredHeight();
        makeTable();
        //Toast.makeText(getApplicationContext(), hourHeight + " " + linearForTime.getMeasuredHeight() + " " + time[0].getMeasuredHeight(), Toast.LENGTH_SHORT).show();
    }

    //Temporary class for inputting tables
    public void initArrayList() {
        for (int i = 0; i < db.length; i++) {
            TimeTable t = new TimeTable();
            t.setSubject(db[i][0]);
            t.setStart(Integer.parseInt(db[i][1]));
            t.setEnd(Integer.parseInt(db[i][2]));
            t.setDay(db[i][3]);
            t.setClassroom(db[i][4]);
            tt.add(t);

            checkLatestTime(t.getEnd());
        }
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
        if (endTime  > latest)
            latest = endTime + 100;
    }

    //creating first row
    public void setFirstRow() {
        time = new TextView[(latest - earliest)/100 + 1];
        l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, hourHeight, 1);

        for (int i = 0; i < time.length; i++) {
            time[i] = new TextView(this);
            time[i].setText((earliest/100 + i) + "");
            time[i].setTextColor(Color.WHITE);
            linearForTime.addView(time[i], l);
        }
    }

    //fill the timetable for calculating location and height of each subject
    public void makeTable() {
        LinearLayout.LayoutParams lForBlank;
        TextView blank; // blank between subjects
        table = new TextView[tt.size()];
        int[] start = {earliest, earliest,earliest,earliest, earliest};
        //hourHeight = time[0].getHeight()/((latest - earliest)/100 + 1);

        for (int i = 0; i < tt.size(); i++) {
            table[i] = new TextView(this);
            table[i].setText(tt.get(i).getSubject() + "\n" + tt.get(i).getClassroom());
            table[i].setBackgroundColor(Color.WHITE);
            table[i].setOnClickListener(this);

            int hour = ((tt.get(i).getEnd() - tt.get(i).getStart()))/ 100 * hourHeight;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
/*
    public int dip2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return  (dpValue * scale + 0.5f);
    }*/

}
