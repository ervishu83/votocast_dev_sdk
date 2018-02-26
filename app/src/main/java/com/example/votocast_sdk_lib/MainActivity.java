package com.example.votocast_sdk_lib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import libClass.votocastLib;

public class MainActivity extends AppCompatActivity {

    private Button btnVotocast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnVotocast = (Button) findViewById(R.id.btnVotocast);

        btnVotocast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new votocastLib(MainActivity.this);
            }
        });
    }
}
