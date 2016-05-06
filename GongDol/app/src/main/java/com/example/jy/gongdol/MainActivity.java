package com.example.jy.gongdol;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    String[][] db = {{"Subject 1", "13", "15", "Mon", "413"}, {"Subject 2", "10", "14", "Tue", "412"},
            {"Subject 3", "17", "20", "Wed", "413"}, {"Subject 4", "13", "15", "Thu", "413"},
            {"Subject 5", "12", "17", "Fri", "412"},{"Subject 6", "17", "18", "Mon", "413"}};
    ArrayList<TimeTable> tt = new ArrayList<TimeTable>(); // list for data from db
    TextView[] table;//textview array for subjects
    int latest = 18; //initialize latest time
    int hourHeight = 45;

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
        makeTable();
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
    public void arraySort(){
        Comparator<TimeTable> comp = new Comparator<TimeTable>() {
            @Override
            public int compare(TimeTable t1, TimeTable t2) {
                return (t1.getStart()) > (t2.getStart()) ? 1:-1;
            }

        };
        Collections.sort(tt, comp);
    }

    //find max value for creating first row of timetable
    public void checkLatestTime(int endTime) {
        if (endTime > latest)
            latest = endTime;
    }

    //creating first row
    public void setFirstRow() {
        TextView[] time = new TextView[latest - 7];
        l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

        for (int i = 0; i < latest - 7; i++) {
            time[i] = new TextView(this);
            time[i].setText(8 + i + "");
            time[i].setTextColor(Color.WHITE);
            linearForTime.addView(time[i], l);
        }
        //=====this code is for getting height from undecided view component, but not invalid yet
        final TextView ttv = time[0];
        ViewTreeObserver vto = ttv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                LayerDrawable ld = (LayerDrawable) ttv.getBackground();
                ViewTreeObserver obs = ttv.getViewTreeObserver();
                hourHeight = ttv.getHeight();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }
        });
        ///================================
    }

    //fill the timetable for calculating location and height of each subject
    public void makeTable() {
        LinearLayout.LayoutParams lForBlank;
        TextView blank; // blank between subjects
        table = new TextView[tt.size()];
        int[] start = {8, 8, 8, 8, 8};


        for (int i = 0; i < tt.size(); i++) {
            table[i] = new TextView(this);
            table[i].setText(tt.get(i).getSubject());
            table[i].setBackgroundColor(Color.WHITE);

            int term = (tt.get(i).getEnd() - tt.get(i).getStart()) * hourHeight;
            l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, term);

            //calculating location and height of each subject
            switch (tt.get(i).getDay()) {

                case "Mon":
                    lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (tt.get(i).getStart() - start[0]) * hourHeight);
                    blank = new TextView(this);
                    layoutMon.addView(blank, lForBlank);
                    layoutMon.addView(table[i], l);
                    start[0] = tt.get(i).getEnd();
                    break;
                case "Tue":
                    lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (tt.get(i).getStart() - start[1]) * hourHeight);
                    blank = new TextView(this);
                    layoutTue.addView(blank, lForBlank);
                    layoutTue.addView(table[i], l);
                    start[1] = tt.get(i).getEnd();
                    break;
                case "Wed":
                    lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (tt.get(i).getStart() - start[2]) * hourHeight);
                    blank = new TextView(this);
                    layoutWed.addView(blank, lForBlank);
                    layoutWed.addView(table[i], l);
                    start[2] = tt.get(i).getEnd();
                    break;
                case "Thu":
                    lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (tt.get(i).getStart() - start[3]) * hourHeight);
                    blank = new TextView(this);
                    layoutThu.addView(blank, lForBlank);
                    layoutThu.addView(table[i], l);
                    start[3] = tt.get(i).getEnd();
                    break;
                case "Fri":
                    lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (tt.get(i).getStart() - start[4]) * hourHeight);
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
        return (int) (dpValue * scale + 0.5f);
    }*/

}
