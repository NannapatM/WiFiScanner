package com.example.dell.wifiscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        TextView result = (TextView) findViewById(R.id.result);
            String getName = (String) bd.get("demo");
            result.setText(getName);

        //String demo = getIntent().getExtras();
        //String getdemo = (String)getdemo.get()
        //ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("mylist");

       // result.setText("result");
    }
}
