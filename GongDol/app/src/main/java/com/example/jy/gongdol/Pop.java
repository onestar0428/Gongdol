package com.example.jy.gongdol;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jy on 2016-05-21.
 */
public class Pop extends AppCompatActivity {
    ClassroomDB db;
    Spinner spin1;
    Spinner spin2;
    String[] subjects = new String[30];
    String[] details = new String[30];
    String selected = "";
    int count_s = 0;
    ArrayAdapter<String> list1;
    ArrayAdapter<String> list2;
    Button ok;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        ok = (Button) findViewById(R.id.ok_button);
        cancel = (Button) findViewById(R.id.ok_cancel);

        ok.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      count_s=0;
                                      Intent myLocalIntent = getIntent();
                                      Bundle myBundle = myLocalIntent.getExtras();
                                      myBundle.putString("selected", selected);
                                      myLocalIntent.putExtras(myBundle);
                                      setResult(Activity.RESULT_OK, myLocalIntent);
                                      finish();
                                  }
                              }
        );
        cancel.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          finish();
                                      }
                                  }
        );

        db = new ClassroomDB(this);
        getWindow().setLayout((int) (width * .80), (int) (height * .4));
        initArray();
        makeSpinner();
    }

    public void initArray() {
        db.open();
        Cursor c = db.getAllClassrooms();
        if(c.getCount()==0)
            db.init();//
        if (c.moveToFirst()) {
            do {
                subjects[count_s] = c.getString(1);
                count_s++;
            } while (c.moveToNext());
        }
        db.close();
    }

    public void makeSpinner() {
        spin1 = (Spinner) findViewById(R.id.subject_spinner);
        spin2 = (Spinner) findViewById(R.id.detail_spinner);

        list1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, subjects);
        //list1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin1.setAdapter(list1);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                selected = subjects[position];
                db.open();
                Cursor c = db.getAllClassrooms();
                if (c.moveToFirst()) {
                    count_s = 0;
                    do {
                        if (c.getString(1).equals(subjects[position])) {
                            details[count_s] = "&" + c.getString(2) + "&" + c.getString(3) + "&" + c.getString(4);
                            count_s++;
                        }
                    } while (c.moveToNext());
                }
                db.close();

                list2 = new ArrayAdapter<String>(Pop.this, android.R.layout.simple_spinner_dropdown_item, details);
                //list2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spin2.setAdapter(list2);
                spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                        selected += details[position];
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

}
