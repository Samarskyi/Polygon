package com.global.training.polygon;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.global.training.polygon.utils.Api;


public class TestActivtity extends Activity  implements Api.OfficeTimeCallback{

    Button button;

    long from=1414792800000L;
    long till=1417381200000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activtity);
        button = (Button) findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.timeWork(from, till, 1498, TestActivtity.this);
            }
        });
    }


    @Override
    public void getTimeList(String list) {
        Log.d(TestActivtity.class.getSimpleName(), "Return list from callback : " +list);
    }
}
