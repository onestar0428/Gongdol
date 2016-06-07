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

/**
 * Created by jy on 2016-06-05
 */
public class Pop2 extends AppCompatActivity {
    Button delete;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow2);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        delete = (Button) findViewById(R.id.delete);
        cancel = (Button) findViewById(R.id.cancel);

        delete.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      Intent myLocalIntent = getIntent();
                                      Bundle myBundle = myLocalIntent.getExtras();
                                      int id = myBundle.getInt("table");
                                      myBundle.putInt("table2", id);
                                      Log.w("id_pop2", id+"");
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

        getWindow().setLayout((int) (width * .80), (int) (height * .4));
    }
}
