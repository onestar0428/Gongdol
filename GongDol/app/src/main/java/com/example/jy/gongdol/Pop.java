package com.example.jy.gongdol;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jy on 2016-05-21.
 */
public class Pop extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ClassroomDB db;
    Spinner spin1;
    Spinner spin2;
    String[] subjects;
    String[] details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.10), (int) (height*.6));
        initArray();
        makespinner();
    }

    public void initArray(){
        db = new ClassroomDB(this);
        db.open();
        Cursor c = db.getAllClassrooms();
        if (c.moveToFirst()) {
            int i=0;
            do {
                subjects[i] = new String();
                subjects[i] = c.getString(0);

            } while (c.moveToNext());
        }

        if (c.moveToFirst()) {
            int i=0;
            do {
               details[i] = new String();
                String tmp = c.getString(1) +  c.getString(2) + c.getString(3) + c.getString(4);
                details[i] = tmp;
            } while (c.moveToNext());
        }
        db.close();
    }
    public void makespinner(){
        spin1 = (Spinner)findViewById(R.id.subject_spinner);
        spin2 = (Spinner)findViewById(R.id.detail_spinner);
        spin1.setOnItemSelectedListener(this);
        spin2.setOnItemSelectedListener(this);

        ArrayAdapter<String> list1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, subjects);
        list1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(list1);
        ArrayAdapter<String> list2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, details);
        list2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(list2);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
        //Toast.makeText(this, subjects[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg){
        //t.setText("");
    }

}
