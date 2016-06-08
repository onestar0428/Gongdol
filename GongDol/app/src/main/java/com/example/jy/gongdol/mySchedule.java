package com.example.jy.gongdol;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class mySchedule extends AppCompatActivity {
    ClassroomDB db;

    ArrayList<TimeTable> mtt = new ArrayList<TimeTable>(); // list for data from data
    TextView[] time;
    Typeface tf1;
    int latest = 1700; //initialize latest time
    int[] start = {earliest, earliest, earliest, earliest, earliest};
    static int earliest = 800;
    int hourHeight, timeWidth, timeHeight;

    LinearLayout.LayoutParams l;
    LinearLayout linearForTime, forTimeWidth;
    GridLayout layoutMon, layoutTue, layoutWed, layoutThu, layoutFri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule);

        //setGlobalFont(linearForTime);//
        //setGlobalFont(forTimeWidth);//

        //requestWindowFeature(Window.FEATURE_NO_TITLE);// 타이틀 안보이게한다.
        linearForTime = (LinearLayout) findViewById(R.id.time);
        forTimeWidth = (LinearLayout) findViewById((R.id.forTimeWidth));
        layoutMon = (GridLayout) findViewById(R.id.monday);
        layoutTue = (GridLayout) findViewById(R.id.tuesday);
        layoutWed = (GridLayout) findViewById(R.id.wednesday);
        layoutThu = (GridLayout) findViewById(R.id.thursday);
        layoutFri = (GridLayout) findViewById(R.id.friday);

        setFirstRow();

        db = new ClassroomDB(this);
        db.open();
        Cursor c = db.getAllClassroom();
        if (c.getCount() == 0) {
            long id = db.insertContact("06480001", "데이터마이닝 (원어강의)", "정옥란", "금A ,금B", "IT대학", "412");
            id = db.insertContact("10176001", "컴퓨터그래픽스 (원어강의)", "최진우", "화4 ,화5 ,화6 ,화7", "IT대학", "304");
            id = db.insertContact("10176002", "컴퓨터그래픽스 (원어강의)", "최진우", "목4 ,목5 ,목6 ,목7", "IT대학", "304");
            id = db.insertContact("10177001", "소프트웨어공학 (원어강의)", "최아영", "금A ,금B", "IT대학", "304");
            id = db.insertContact("10178001", "모바일프로그래밍 (원어강의)", "최재혁", "월3 ,월4 ,수2 ,수3", "IT대학", "412");
            id = db.insertContact("10178002", "모바일프로그래밍 (원어강의)", "최재혁", "월5 ,월6 ,수5 ,수6", "IT대학", "412");
        }
        db.close();

        setFirstRow();

        Button b1 = (Button) findViewById(R.id.compare);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mySchedule.this, CompareWithMySchedule.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle myData = new Bundle();
                intent.putExtras(myData);
                startActivityForResult(intent, 103);

            }
        });

        Button b2 = (Button) findViewById(R.id.stack);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntentA1A2 = new Intent(mySchedule.this, Pop.class);
                Bundle myData = new Bundle();
                myIntentA1A2.putExtras(myData);
                startActivityForResult(myIntentA1A2, 101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //try {
        //handling activity result for making tables
        if ((requestCode == 101) && (resultCode == Activity.RESULT_OK)) {
            // Bundle myResults = data.getExtras();
            String result = data.getExtras().getString("selected");
            initArrayList(result);
        }
        //handling activity result for deleting table
        if ((requestCode == 102) && (resultCode == Activity.RESULT_OK)) {
            Bundle myResults = data.getExtras();
            int id = myResults.getInt("table2");
            Iterator<TimeTable> iterator = mtt.iterator();

            while (iterator.hasNext()) {//for array mtt
                TimeTable tt = iterator.next();
                //Toast.makeText(getApplicationContext(), tt.getSubject() + " " + s + " " + tt.getDay(), Toast.LENGTH_SHORT).show();
                if (tt.getCourseID() == id) {//intent result로 받은 값이 그려진 테이블의 과목과 일치하면 지운다
                    switch (tt.getDay()) {
                        case "월":
                            layoutMon.removeView(tt.getTextView());
                            start[0] = earliest;
                            break;
                        case "화":
                            layoutTue.removeView(tt.getTextView());
                            //deleteTable(tt);
                            start[1] = earliest;
                            break;
                        case "수":
                            layoutWed.removeView(tt.getTextView());
                            //deleteTable(tt);
                            start[2] = earliest;
                            break;
                        case "목":
                            layoutThu.removeView(tt.getTextView());
                            //deleteTable(tt);
                            start[3] = earliest;
                            break;
                        case "금":
                            layoutFri.removeView(tt.getTextView());
                            //deleteTable(tt);
                            start[4] = earliest;
                            break;
                    }
                    iterator.remove();
                }
            }
            makeTable();
        }

        if ((requestCode == 103) && (resultCode == Activity.RESULT_OK)) {

        }
// } catch (Exception e) {
//    Log.e("d", e+"");
//  }
    }//onActivityResult

    //setting font
    public void setGlobalFont(ViewGroup root) {
        tf1 = Typeface.createFromAsset(getAssets(), "font1.ttf");
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof TextView)
                ((TextView) child).setTypeface(tf1);
            else if (child instanceof ViewGroup)
                setGlobalFont((ViewGroup) child);
        }
    }

    //this overrides onWindowFocusChanged method for getting height from not-yet-laid-out object
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // the height will be set at this point
        hourHeight = (linearForTime.getMeasuredHeight()) / time.length;
        hourHeight = time[0].getMeasuredHeight();
        timeWidth = forTimeWidth.getMeasuredWidth() / 5;
        timeHeight = forTimeWidth.getMeasuredHeight();
        //Log.w("width", timeWidth + "");

        //Toast.makeText(getApplicationContext(), hourHeight + " " + linearForTime.getMeasuredHeight() + " " + time[0].getMeasuredHeight(), Toast.LENGTH_SHORT).show();
    }

    //Temporary class for inputting tables
    public void initArrayList(String s) {
        String arr[] = s.split("&");
        TimeTable t = new TimeTable();
        t.setSubject(arr[0]);
        t.setProf(arr[1]);
        t.setTime(arr[2]);
        t.setBuilding(arr[3]);
        t.setRoom(arr[4]);
        t.setClassroom(arr[3] + "-" + arr[4]);
        t.setCourseId(Integer.parseInt(arr[5]));
        checkingDate(arr[2], t);
        mtt.add(t);

        checkLatestTime();
        //display arraylist values

        makeTable();
    }

    public void checkingDate(String arr, TimeTable cur) {
        String d[] = arr.split(" ,");//월1/화2/수3으로 분리
        String date = d[0];//월1
        String day = d[0].substring(0, 1);//월

        cur.setDay(day);
        cur.setTime(arr);

        //월1, 화2, 수3 형식일 때, 요일 별로 string 쪼개서 새 객체 생성한 뒤 저장
        for (int i = 0; i < d.length; i++) {
            if (!d[i].substring(0, 1).equals(day)) {//월1, 화2처럼 다를떄
                cur.setTime(date);//original 객체에 월1 저장

                day = d[i].substring(0, 1);
                TimeTable new_t = new TimeTable();
                new_t.setCourseId(cur.getCourseID());
                new_t.setSubject(cur.getSubject());
                new_t.setProf(cur.getProf());
                new_t.setClassroom(cur.getClassroom());

                new_t.setDay(day);
                new_t.setTime(arr.substring(arr.indexOf(day), arr.length()));
                date = arr.substring(arr.indexOf(day), arr.length());
                //checkingDate(date.split(" ,"), new_t);
                mtt.add(new_t);

                cur = new_t;
            } else if (i != 0)
                date += " ," + d[i];
        }
    }

    //sorting arraylist in order of starting time
    public void arraySort(ArrayList<TimeTable> mtt) {
        Comparator<TimeTable> comp = new Comparator<TimeTable>() {
            @Override
            public int compare(TimeTable t1, TimeTable t2) {
                return (t1.getStart()) >= (t2.getStart()) ? 1 : -1;
            }
        };
        Collections.sort(mtt, comp);
    }

    //find max value for creating first row of timetable
    public void checkLatestTime() {
        for (TimeTable e : mtt) {
            if (e.getEnd() > latest) {
                latest = e.getEnd() + 100;
                setFirstRow();
            }
        }
    }

    //creating first row
    public void setFirstRow() {
        linearForTime.removeAllViews();
        time = new TextView[(latest - earliest) / 100 + 1];
        l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, hourHeight, 1);

        TextView temp = new TextView(this);
        temp.setText("");
        LinearLayout.LayoutParams l_blank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, timeHeight, 1);
        linearForTime.addView(temp, l_blank);

        for (int i = 0; i < time.length; i++) {
            time[i] = new TextView(this);
            time[i].setText((earliest / 100 + i) + "");
            time[i].setTextColor(Color.WHITE);
            linearForTime.addView(time[i], l);
        }
    }

    public void initTable() {
        Iterator<TimeTable> iterator = mtt.iterator();

        layoutMon.removeAllViews();
        layoutTue.removeAllViews();
        layoutWed.removeAllViews();
        layoutThu.removeAllViews();
        layoutFri.removeAllViews();

        //remove all textViews

        while (iterator.hasNext()) {
            TimeTable t = iterator.next();
            t.setDraw(0);
        }
        start[0] = earliest;
        start[1] = earliest;
        start[2] = earliest;
        start[3] = earliest;
        start[4] = earliest;
    }

    //fill the timetable for calculating location and height of each subject
    public void makeTable() {
        //hourHeight = time[0].getHeight()/((latest - earliest)/100 + 1);
        LinearLayout.LayoutParams lForBlank;
        TextView blank; // blank between subjects
        TextView tempTable;
        Iterator<TimeTable> iterator = mtt.iterator();

        initTable();
        if (mtt.size() > 1)
            arraySort(mtt);

        while (iterator.hasNext()) {
            TimeTable t = iterator.next();
            if (t.getDraw() == 0) {
                tempTable = new TextView(this);
                tempTable.setText(t.getSubject() + "\n" + t.getClassroom());
                tempTable.setBackgroundColor(Color.WHITE);
                tempTable.setId(t.getCourseID());
                tempTable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mIntent = new Intent(mySchedule.this, Pop2.class);
                        Bundle b = new Bundle();
                        b.putInt("table", v.getId());
                        mIntent.putExtras(b);
                        startActivityForResult(mIntent, 102);
                    }
                });

                int hour = ((t.getEnd() - t.getStart())) / 100 * hourHeight;
                int minute = ((t.getEnd() - t.getStart())) % 100 * hourHeight / 100;
                l = new LinearLayout.LayoutParams(timeWidth, hour + minute);

                //calculating location and height of each subject
                switch (t.getDay()) {
                    case "월":
                        if (t.getStart() > start[0]) {
                            lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    ((t.getStart() - start[0]) / 100 * hourHeight) + (t.getStart() - start[0]) % 100 * hourHeight / 100);
                            blank = new TextView(this);
                            layoutMon.addView(blank, lForBlank);
                            layoutMon.addView(tempTable, l);
                            start[0] = t.getEnd();
                            t.setDraw(1);
                            t.setTextView(tempTable);
                        } else {
                            Toast.makeText(getApplicationContext(), "Duplicate time!", Toast.LENGTH_SHORT).show();
                            iterator.remove();
                        }
                        break;
                    case "화":
                        if (t.getStart() > start[1]) {
                            lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    ((t.getStart() - start[1]) / 100 * hourHeight) + (t.getStart() - start[1]) % 100 * hourHeight / 100);
                            blank = new TextView(this);
                            layoutTue.addView(blank, lForBlank);
                            layoutTue.addView(tempTable, l);
                            start[1] = t.getEnd();
                            t.setDraw(1);
                            t.setTextView(tempTable);
                        } else {
                            Toast.makeText(getApplicationContext(), "Duplicate time!", Toast.LENGTH_SHORT).show();
                            iterator.remove();
                        }
                        break;
                    case "수":
                        if (t.getStart() > start[2]) {
                            lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    ((t.getStart() - start[2]) / 100 * hourHeight) + (t.getStart() - start[2]) % 100 * hourHeight / 100);
                            blank = new TextView(this);
                            layoutWed.addView(blank, lForBlank);
                            layoutWed.addView(tempTable, l);
                            start[2] = t.getEnd();
                            t.setDraw(1);
                            t.setTextView(tempTable);
                        } else {
                            Toast.makeText(getApplicationContext(), "Duplicate time!", Toast.LENGTH_SHORT).show();
                            iterator.remove();
                        }
                        break;
                    case "목":
                        if (t.getStart() > start[3]) {
                            lForBlank = new LinearLayout.LayoutParams(timeWidth,
                                    ((t.getStart() - start[3]) / 100) * hourHeight + (t.getStart() - start[3]) % 100 * hourHeight / 100);
                            blank = new TextView(this);
                            layoutThu.addView(blank, lForBlank);
                            layoutThu.addView(tempTable, l);
                            start[3] = t.getEnd();
                            t.setDraw(1);
                            t.setTextView(tempTable);
                        } else {
                            Toast.makeText(getApplicationContext(), "Duplicate time!", Toast.LENGTH_SHORT).show();
                            iterator.remove();
                        }
                        break;
                    case "금":
                        if (t.getStart() > start[4]) {
                            lForBlank = new LinearLayout.LayoutParams(timeWidth,
                                    ((t.getStart() - start[4]) / 100 * hourHeight) + (t.getStart() - start[4]) % 100 * hourHeight / 100);
                            blank = new TextView(this);
                            layoutFri.addView(blank, lForBlank);
                            layoutFri.addView(tempTable, l);
                            start[4] = t.getEnd();
                            t.setDraw(1);
                            t.setTextView(tempTable);
                        } else {
                            Toast.makeText(getApplicationContext(), "Duplicate time!", Toast.LENGTH_SHORT).show();
                            iterator.remove();
                        }
                        break;
                }
            }
        }
    }

    public void compareSchedule() {

    }
}
