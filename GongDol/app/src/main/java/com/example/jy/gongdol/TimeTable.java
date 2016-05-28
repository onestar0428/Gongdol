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
    int start=800;
    int end=800;

    int classTime_num[][] = {{900, 950}, {1000, 1050}, {1100, 1150}, {1200, 1250}, {1300, 1350}, {1400, 1450}, {1500, 1550},
            {1600, 1650}, {1700, 1750}, {1800, 1850}, {1900, 1950}, {2000, 2150}, {2100, 2150}, {2200, 2250}};//[교시][시작, 끝]
    int classTime_alpha[][] = {{900, 1045}, {1100, 1215}, {1230, 1345}, {1400, 1515}, {1530, 1645}};//[교시][시작,끝]

    TimeTable() {
        courseID = 0;
        subject = "Subject";
        time = " ";
        prof = "";
        classroom = " ";

    }

    //parsing time and classroom information
    public void splitTime(String t) {
        String temp[] = t.split(" ,");
        int max_n = 0, min_n = 10000;
        char max_a = 'F', min_a = 'A';
        int flag = 0;//0 for numbers and 1 for alphabets

        for (int i = 0; i < temp.length; i++) {
            String s = temp[i].substring(1);//put time

            if (s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D") || s.equals("E")) {
                char c = s.charAt(0);
                if (c > max_a)//string 비교 다시
                    max_a = c;
                if (c < min_n)
                    min_a = c;
                flag = 1;
            } else {

                if (Integer.parseInt(s) > max_n)
                    max_n = Integer.parseInt(s);
                if (Integer.parseInt(s) < min_n)
                    min_n = Integer.parseInt(s);
            }

        }
        if (flag == 1)
            transformTimeForAlpahbet(max_a, min_a);
        else
            transformTimeForNumber(max_n, min_n);
    }

    public void splitClassroom(String c) {

    }

    //transform format of data from database for using it in drawing timetable
    public void transformTimeForNumber(int startT, int endT) {
        int s=classTime_num[startT][0], e=classTime_num[endT][1];

        setStart(s);
        setEnd(e);
    }

    public void transformTimeForAlpahbet(char startT, char endT) {
        int s = 0, e = 0;
        //교시마다의 시간을 받아와서 시작시간 종료시간 설정
        switch (startT) {
            case 'A':
                s = classTime_alpha[0][0];
                break;
            case 'B':
                s = classTime_alpha[1][0];
                break;
            case 'C':
                s = classTime_alpha[2][0];
                break;
            case 'D':
                s = classTime_alpha[3][0];
                break;
            case 'E':
                s = classTime_alpha[4][0];
                break;
        }
        switch (endT) {
            case 'A':
                s = classTime_alpha[0][1];
                break;
            case 'B':
                s = classTime_alpha[1][1];
                break;
            case 'C':
                s = classTime_alpha[2][1];
                break;
            case 'D':
                s = classTime_alpha[3][1];
                break;
            case 'E':
                s = classTime_alpha[4][1];
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
        time = t;
        splitTime(t);
    }

    public void setClassroom(String c) {
        classroom = c;
    }

    public void setDay(String d){
        day = d;
    }
    //=========================================
    public void setStart(int s) {
        s = (s/100*100) + (s%100 * 100 / 60);
        start = s;
    }

    public void setEnd(int e) {
        e = (e/100*100) + (e%100 * 100 / 60);
        end = e;
    }

    //getter
    public String getSubject() {
        return subject;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getDay() {
        return day;
    }

    public String getClassroom() {
        return classroom;
    }
}