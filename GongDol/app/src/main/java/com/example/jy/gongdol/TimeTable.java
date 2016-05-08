package com.example.jy.gongdol;

/**
 * Created by jy on 2016-05-03.
 */
public class TimeTable {
    String subject;
    int start;
    int end;
    String day;
    String classroom;

    TimeTable() {
        subject = "Subject";
        start = 8;
        end = 8;
        day = "Mon";
        classroom = " ";
    }

    public void setSubject(String s) {
        subject = s;
    }

    public void setStart(int s) {
        s = (s/100*100) + (s%100 * 100 / 60);
        start = s;
    }

    public void setEnd(int e) {
        e = (e/100*100) + (e%100 * 100 / 60);
        end = e;
    }

    public void setDay(String d) {
        day = d;
    }

    public void setClassroom(String c) {
        classroom = c;
    }

    public String getSubject() {
        return subject;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getDay(){
        return day;
    }

    public String getClassroom() {
        return classroom;
    }
}