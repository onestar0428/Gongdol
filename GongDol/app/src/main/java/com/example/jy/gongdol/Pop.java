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
    ArrayList<String> subjects = new ArrayList<String>();
    ArrayList<String> details = new ArrayList<String>();
    String[] select = new String[10];
    String selected="";
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
                                      count_s = 0;
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
        Cursor c = db.getAllClassroom();
        int flag = 0;
        String s="";

        //if(c.getCount()==0)
        //db.init();//
        if (c.moveToFirst()) {
            do {
                for (int i = 0; i < subjects.size(); i++) {//check if this subject is already existed in array
                    if (c.getString(c.getColumnIndex("subject")).equals(subjects.get(i))) {
                        flag = 1;
                    }
                }
                if (flag == 0) {
                    subjects.add(c.getString(c.getColumnIndex("subject")));
                    //count_s++;
                }
                flag=0;
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
                count_s=0;
                details.clear();
                db.open();
                Cursor c = db.getAllClassroom();
                if (c.moveToFirst()) {
                    do {
                        if (c.getString(c.getColumnIndex("subject")).equals(subjects.get(position))) {
                            details.add(c.getString(c.getColumnIndex("time")) + "(" + c.getString(c.getColumnIndex("prof")) + ")");
                            select[count_s] = c.getString(c.getColumnIndex("subject")) + "&" + c.getString(c.getColumnIndex("prof")) + "&" + c.getString(c.getColumnIndex("time")) + "&"
                                    + c.getString(c.getColumnIndex("building")) + "&" + c.getString(c.getColumnIndex("classroom")) + "&"
                                    + c.getString(c.getColumnIndex("sid"));
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
                        selected = select[position];
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
