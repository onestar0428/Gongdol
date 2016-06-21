package com.example.jy.gongdol;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by jy on 2016-06-08.
 */
public class CompareWithMySchedule extends AppCompatActivity {
    ClassroomDB db;

    ArrayList<TimeTable> tt = new ArrayList<TimeTable>(); // list for data from data
    ArrayList<String> building = new ArrayList<String>();
    ArrayList<String> room = new ArrayList<String>();
    TextView[] time;
    Spinner spin1;
    Spinner spin2;
    ArrayAdapter<String> list1;
    ArrayAdapter<String> list2;
    Button ok;
    String[] select = new String[2];

    int latest = 1700, click_count=0; //initialize latest time
    static int earliest = 800;
    int hourHeight, timeWidth, timeHeight, window_flag=0;
    int[] start = {earliest, earliest, earliest, earliest, earliest};


    LinearLayout.LayoutParams l;
    LinearLayout linearForTime, forTimeWidth;
    GridLayout layoutMon, layoutTue, layoutWed, layoutThu, layoutFri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comparemy);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);// 타이틀 안보이게한다.

        linearForTime = (LinearLayout) findViewById(R.id.time);
        forTimeWidth = (LinearLayout) findViewById((R.id.forTimeWidth));
        layoutMon = (GridLayout) findViewById(R.id.monday);
        layoutTue = (GridLayout) findViewById(R.id.tuesday);
        layoutWed = (GridLayout) findViewById(R.id.wednesday);
        layoutThu = (GridLayout) findViewById(R.id.thursday);
        layoutFri = (GridLayout) findViewById(R.id.friday);
        ok = (Button) findViewById(R.id.makeOK);

        setFirstRow();

        ok.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      String result = select[0] + "-" + select[1];
                                      initArrayList(result, tt);
                                      if(click_count%2==0) {
                                          setFirstRow();
                                          makeTable();
                                      }
                                      else {
                                          initTable();
                                      }
                                      click_count++;
                                  }
                              }
        );

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

        int flag = 0;
        db = new ClassroomDB(this);
        db.open();
        c = db.getAllClassroom();
        if (c.moveToFirst()) {
            do {
                for (int i = 0; i < building.size(); i++) {//check if this subject is already existed in array
                    if (c.getString(c.getColumnIndex("building")).equals(building.get(i))) {
                        flag = 1;
                    }
                }
                if (flag == 0) {
                    building.add(c.getString(c.getColumnIndex("building")));
                }
                flag = 0;
            } while (c.moveToNext());
        }
        db.close();

        makeSpinner();
    }

    //this overrides onWindowFocusChanged method for getting height from not-yet-laid-out object
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(window_flag==1) {
            // the height will be set at this point
            hourHeight = (linearForTime.getMeasuredHeight()) / time.length;
            hourHeight = time[0].getMeasuredHeight();
            timeWidth = forTimeWidth.getMeasuredWidth() / 5;
            timeHeight = forTimeWidth.getMeasuredHeight();
        }
        //Toast.makeText(getApplicationContext(), hourHeight + " " + linearForTime.getMeasuredHeight() + " " + time[0].getMeasuredHeight(), Toast.LENGTH_SHORT).show();
    }

    public void makeSpinner() {
        spin1 = (Spinner) findViewById(R.id.building_spinner);
        spin2 = (Spinner) findViewById(R.id.room_spinner);

        list1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, building);
        //list1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin1.setAdapter(list1);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                room.clear();
                select[0] = building.get(position);

                db.open();
                Cursor c = db.getAllClassroom();
                if (c.moveToFirst()) {
                    do {
                        int flag = 0;
                        for (int i = 0; i < room.size(); i++) {//check if this subject is already existed in array
                            if (c.getString(c.getColumnIndex("classroom")).equals(room.get(i))) {
                                flag = 1;
                            }
                        }
                        if (flag == 0) {
                            room.add(c.getString(c.getColumnIndex("classroom")));
                        }
                    } while (c.moveToNext());
                }
                db.close();

                list2 = new ArrayAdapter<String>(CompareWithMySchedule.this, android.R.layout.simple_spinner_dropdown_item, room);
                spin2.setAdapter(list2);
                spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                        select[1] = room.get(position);
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
                list2.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void initTable() {
        Iterator<TimeTable> iterator = tt.iterator();

        //remove all textViews
        layoutMon.removeAllViews();
        layoutTue.removeAllViews();
        layoutWed.removeAllViews();
        layoutThu.removeAllViews();
        layoutFri.removeAllViews();

        start[0] = earliest;
        start[1] = earliest;
        start[2] = earliest;
        start[3] = earliest;
        start[4] = earliest;
    }

    //Temporary class for inputting tables
    public ArrayList<TimeTable> initArrayList(String info, ArrayList<TimeTable> tt) {
        tt.clear();
        db.open();
        Cursor c = db.getAllClassroom();
        if (c.moveToFirst()) {
            do {
                if ((c.getString(c.getColumnIndex("building")) + "-" + c.getString(c.getColumnIndex("classroom"))).equals(info)) {
                    TimeTable t = new TimeTable();
                    t.setCourseId(Integer.parseInt(c.getString(c.getColumnIndex("sid"))));
                    t.setSubject(c.getString(c.getColumnIndex("subject")));
                    t.setProf(c.getString(c.getColumnIndex("prof")));
                    t.setTime(c.getString(c.getColumnIndex("time")));
                    t.setClassroom(c.getString((c.getColumnIndex("building"))) + "-" + c.getString((c.getColumnIndex("classroom"))));
                    checkingDate(t, c);
                    tt.add(t);
                }
            } while (c.moveToNext());
        }
        db.close();

        checkLatestTime();
        //display arraylist values
        /*
        for (TimeTable e : tt) {
            Log.w("TT", e.getSubject() + " " + e.getStart() + " " + e.getEnd() + " " + e.getDay() + " ");
        }*/
        arraySort();
        return tt;
    }

    public void checkingDate(TimeTable cur, Cursor cur_c) {
        String timeString = cur_c.getString(cur_c.getColumnIndex("time"));
        String arr[] = timeString.split(" ,");
        String date = arr[0].substring(0, 1);

        cur.setDay(date);

        //월1, 화2, 수3 형식일 때, 요일 별로 string 쪼개서 새 객체 생성한 뒤 저장
        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].substring(0, 1).equals(date)) {//월1, 화2처럼 다를떄
                cur.setTime(timeString.substring(0, timeString.indexOf(arr[i].substring(0, 1))));//original 객체에 월1 저장
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
        for (TimeTable e : tt) {
            if (e.getEnd() > latest)
                latest = e.getEnd() + 100;
        }
    }

    //creating first row
    public void setFirstRow() {
        window_flag=1;
        linearForTime.removeAllViews();;
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

    //fill the timetable for calculating location and height of each subject
    public void makeTable() {
        LinearLayout.LayoutParams lForBlank;
        TextView blank; // blank between subjects
        TextView tempTable;
        Iterator<TimeTable> iterator = tt.iterator();

        initTable();
        if (tt.size() > 1)
            arraySort();

        while (iterator.hasNext()) {
            TimeTable t = iterator.next();
            if (t.getDraw() == 0) {
                tempTable = new TextView(this);
                //tempTable.setText(t.getSubject() + "\n" + t.getClassroom());
                tempTable.setBackgroundColor(Color.argb(100, 173, 209, 245));
                tempTable.setId(t.getCourseID());

                int hour = ((t.getEnd() - t.getStart())) / 100 * hourHeight;
                int minute = ((t.getEnd() - t.getStart())) % 100 * hourHeight / 100;
                l = new LinearLayout.LayoutParams(timeWidth, hour + minute);

                //calculating location and height of each subject
                switch (t.getDay()) {
                    case "월":
                        lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ((t.getStart() - start[0]) / 100 * hourHeight) + (t.getStart() - start[0]) % 100 * hourHeight / 100);
                        blank = new TextView(this);
                        layoutMon.addView(blank, lForBlank);
                        layoutMon.addView(tempTable, l);
                        start[0] = t.getEnd();
                        t.setDraw(1);
                        t.setTextView(tempTable);
                        break;
                    case "화":
                        lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ((t.getStart() - start[1]) / 100 * hourHeight) + (t.getStart() - start[1]) % 100 * hourHeight / 100);
                        blank = new TextView(this);
                        layoutTue.addView(blank, lForBlank);
                        layoutTue.addView(tempTable, l);
                        start[1] = t.getEnd();
                        break;
                    case "수":
                        lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ((t.getStart() - start[2]) / 100 * hourHeight) + (t.getStart() - start[2]) % 100 * hourHeight / 100);
                        blank = new TextView(this);
                        layoutWed.addView(blank, lForBlank);
                        layoutWed.addView(tempTable, l);
                        start[2] = t.getEnd();
                        break;
                    case "목":
                        lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ((t.getStart() - start[3]) / 100) * hourHeight + (t.getStart() - start[3]) % 100 * hourHeight / 100);
                        blank = new TextView(this);
                        layoutThu.addView(blank, lForBlank);
                        layoutThu.addView(tempTable, l);
                        start[3] = t.getEnd();
                        break;
                    case "금":
                        lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ((t.getStart() - start[4]) / 100 * hourHeight) + (t.getStart() - start[4]) % 100 * hourHeight / 100);
                        blank = new TextView(this);
                        layoutFri.addView(blank, lForBlank);
                        layoutFri.addView(tempTable, l);
                        start[4] = t.getEnd();
                        break;
                }
            }
        }
    }
}