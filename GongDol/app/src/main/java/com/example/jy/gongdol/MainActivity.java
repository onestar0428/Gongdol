package com.example.jy.gongdol;

import android.app.ActionBar;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    ClassroomDB db;

    ArrayList<TimeTable> tt = new ArrayList<TimeTable>(); // list for data from data
    TextView[] table;//textview array for subjects
    TextView[] time;
    int latest = 0; //initialize latest time
    static int earliest = 800;
    int hourHeight;

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

        db = new ClassroomDB(this);
        initArrayList();
        setFirstRow();
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
        db.open();
        db.init();
        Cursor c = db.getAllClassrooms();
        if (c.moveToFirst()) {
            do {
                TimeTable t = new TimeTable();
                t.setCourseId(c.getInt(0));
                t.setSubject(c.getString(1));
                t.setProf(c.getString(2));
                t.setTime(c.getString(3));
                t.setClassroom(c.getString(4));
                checkingDate(t, c);
                tt.add(t);

            } while (c.moveToNext());
        }

        checkLatestTime();
        //display arraylist values
        for(TimeTable e: tt){
            Log.w("TT", e.getSubject() + " " + e.getStart() + " " + e.getEnd() + " " + e.getDay() + " ");
        }
        db.close();
        arraySort();
    }

    public void checkingDate(TimeTable cur, Cursor cur_c) {
        String timeString = cur_c.getString(3);
        String arr[] = timeString.split(" ,");
        String date = arr[0].substring(0, 1);

        cur.setDay(date);

        //월1, 화2, 수3 형식일 때, 요일 별로 string 쪼개서 새 객체 생성한 뒤 저장
        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].substring(0,1).equals(date)) {//월1, 화2처럼 다를떄
                cur.setTime(timeString.substring(0, timeString.indexOf(arr[i].substring(0,1))));//original 객체에 월1 저장
                timeString = timeString.substring(timeString.indexOf(arr[i].substring(0, 1)), timeString.length());//회2, 수3으로 저장
                date = arr[i].substring(0, 1);//새로운 요일 값으로 설정->화

                TimeTable new_t = new TimeTable();
                new_t.setCourseId(cur.getCourseID());
                new_t.setSubject(cur.getSubject());
                new_t.setProf(cur.getProf());
                new_t.setClassroom(cur.getClassroom());

                new_t.setDay(date);
                new_t.setTime(timeString);
                tt.add(new_t);

                cur = new_t;
            }
        }
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
    public void checkLatestTime() {

        for(TimeTable e: tt) {
            if (e.getEnd() > latest)
                latest = e.getEnd() + 100;
        }
        Log.w("TAG", latest+"");
    }

    //creating first row
    public void setFirstRow() {
        time = new TextView[(latest - earliest) / 100 + 1];
        l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, hourHeight, 1);

        for (int i = 0; i < time.length; i++) {
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
            table[i].setOnClickListener(this);

            int hour = ((tt.get(i).getEnd() - tt.get(i).getStart())) / 100 * hourHeight;
            int minute = ((tt.get(i).getEnd() - tt.get(i).getStart())) % 100 * hourHeight / 100;
            l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, hour + minute);

            //calculating location and height of each subject
            switch (tt.get(i).getDay()) {
                case "월":
                    lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            ((tt.get(i).getStart() - start[0]) / 100 * hourHeight) + (tt.get(i).getStart() - start[0]) % 100 * hourHeight / 100);
                    blank = new TextView(this);
                    layoutMon.addView(blank, lForBlank);
                    layoutMon.addView(table[i], l);
                    start[0] = tt.get(i).getEnd();
                    break;
                case "화":
                    lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            ((tt.get(i).getStart() - start[1]) / 100 * hourHeight) + (tt.get(i).getStart() - start[1]) % 100 * hourHeight / 100);
                    blank = new TextView(this);
                    layoutTue.addView(blank, lForBlank);
                    layoutTue.addView(table[i], l);
                    start[1] = tt.get(i).getEnd();
                    break;
                case "수":
                    lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            ((tt.get(i).getStart() - start[2]) / 100 * hourHeight) + (tt.get(i).getStart() - start[2]) % 100 * hourHeight / 100);
                    blank = new TextView(this);
                    layoutWed.addView(blank, lForBlank);
                    layoutWed.addView(table[i], l);
                    start[2] = tt.get(i).getEnd();
                    break;
                case "목":
                    lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            ((tt.get(i).getStart() - start[3]) / 100) * hourHeight + (tt.get(i).getStart() - start[3]) % 100 * hourHeight / 100);
                    blank = new TextView(this);
                    layoutThu.addView(blank, lForBlank);
                    layoutThu.addView(table[i], l);
                    start[3] = tt.get(i).getEnd();
                    break;
                case "금":
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
