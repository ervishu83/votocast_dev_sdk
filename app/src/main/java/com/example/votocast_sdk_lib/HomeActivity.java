package com.example.votocast_sdk_lib;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.votocast.votocast.SplashActivity;

public class HomeActivity extends AppCompatActivity {

    private Button btnVotocast,btnO2life;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnVotocast = (Button) findViewById(R.id.btnVotocast);
        btnO2life = (Button) findViewById(R.id.btnO2life);

        btnVotocast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent m1 = new Intent(HomeActivity.this, SplashActivity.class);
//                m1.putExtra("authToken","Basic cDJ6eGYzOjJkaW80aTBm");
//                startActivity(m1);
                startActivity(new Intent(HomeActivity.this, SplashActivity.class));
            }
        });

        btnO2life.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent m1 = new Intent(HomeActivity.this, SplashActivity.class);
                m1.putExtra("authToken","Basic bHk4cnF1OnpiMGxsazAy");
                startActivity(m1);
            }
        });
    }
}
