package com.example.jy.gongdol;

import android.util.Log;

/**
 * Created by jy on 2016-05-03.
 */
public class TimeTable {
    int courseID;
    String subject;
    String prof;
    String time;
    String classroom;

    String day="";
    String room="";
    String building="";
    int start=800;
    int end=800;
    int draw_flag=0;

    int classTime_num[][] = {{900, 950}, {1000, 1050}, {1100, 1150}, {1200, 1250}, {1300, 1350}, {1400, 1450}, {1500, 1550},
            {1600, 1650}, {1700, 1750}, {1800, 1850}, {1900, 1950}, {2000, 2150}, {2100, 2150}, {2200, 2250}};//[교시][시작, 끝]
    int classTime_alpha[][] = {{900, 1045}, {1100, 1215}, {1230, 1345}, {1400, 1515}, {1530, 1645}};//[교시][시작,끝]

    TimeTable() {
        courseID = 0;
        subject = "Subject";
        time = " ";
        prof = "";
        classroom = " ";
        draw_flag=0;
    }

    //parsing time and classroom information
    public void splitTime(String t) {
        String temp[] = t.split(" ,");
        int max_n = 0, min_n = 100000;
        String max_a = "A", min_a = "F";
        int flag = 0;//0 for numbers and 1 for alphabets

        for (int i = 0; i < temp.length; i++) {
            String s = temp[i].substring(1);//put time

            if (s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D") || s.equals("E")) {
                String c = s;
                if (c.compareTo(max_a) > 0)
                    max_a = c;
                if (c.compareTo(min_a) < 0)
                    min_a = c;
                Log.w("c",c + " " +  max_a + " " + min_a);
                flag = 1;
            } else {
                if (Integer.parseInt(s) > max_n)
                    max_n = Integer.parseInt(s);
                if (Integer.parseInt(s) < min_n)
                    min_n = Integer.parseInt(s);
            }

        } Log.w("splitTime", min_a + " " + max_a);

        if (flag == 1)
            transformTimeForAlphabet(min_a, max_a);
        else
            transformTimeForNumber(min_n-1, max_n-1);
    }

    public void splitClassroom(String c) {
        String arr[] = c.split("-");
        setBuilding(arr[0]);
        setRoom(arr[1]);
    }

    //transform format of data from database for using it in drawing timetable
    public void transformTimeForNumber(int startT, int endT) {
        int s=classTime_num[startT][0], e=classTime_num[endT][1];

        setStart(s);
        setEnd(e);
    }

    public void transformTimeForAlphabet(String startT, String endT) {
        int s = 0, e = 0;
        //교시마다의 시간을 받아와서 시작시간 종료시간 설정
        switch (startT) {
            case "A":
                s = classTime_alpha[0][0];
                break;
            case "B":
                s = classTime_alpha[1][0];
                break;
            case "C":
                s = classTime_alpha[2][0];
                break;
            case "D":
                s = classTime_alpha[3][0];
                break;
            case "E":
                s = classTime_alpha[4][0];
                break;
        }
        switch (endT) {
            case "A":
                e = classTime_alpha[0][1];
                break;
            case "B":
                e = classTime_alpha[1][1];
                break;
            case "C":
                e = classTime_alpha[2][1];
                break;
            case "D":
                e = classTime_alpha[3][1];
                break;
            case "E":
                e = classTime_alpha[4][1];
                break;
        }
        setStart(s);
        setEnd(e);
    }

    //setter
    public void setCourseId(int id) {
        courseID = id;
    }

    public void setSubject(String s) {
        subject = s;
    }

    public void setProf(String p) {
        prof = p;
    }

    public void setTime(String t) {
        Log.w("setTime", t);
        time = t;
        splitTime(t);
    }

    public void setClassroom(String c) {
        classroom = c;
        splitClassroom(c);
    }

    public void setDay(String d){
        day = d;
    }
    public void setBuilding(String b){
        building = b;
    }
    public void setRoom(String r){
        room = r;
    }
    public void setStart(int s) {
        start = (s/100*100) + (s%100 * 100 / 60);
    }
    public void setEnd(int e) {
        end = (e/100*100) + (e%100 * 100 / 60);
    }
    public void setDraw(int d){draw_flag = d;}
    //getter

    public int getCourseID(){return courseID;}
    public String getSubject() {
        return subject;
    }

    public String getProf(){return prof;}

    public String getClassroom() {
        return classroom;
    }

    public String getDay() {
        return day;
    }

    public String getTime(){ return time;}

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getDraw(){return draw_flag;}
}