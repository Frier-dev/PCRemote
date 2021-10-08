package com.friertech.pcremote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Button next = (Button)findViewById(R.id.ContinueBtn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        TextView textView4 = (TextView)findViewById(R.id.textView4);
        new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                textView4.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                textView4.setText("Redirecting..");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }.start();

    }
}