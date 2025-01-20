package com.hikonaoki.dodge_colors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class result_scr extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_scr);

        Intent intent = getIntent();
        String message = intent.getStringExtra("time");

        TextView textView = (TextView) findViewById(R.id.view_message);
        textView.setText(message);
    }
    public void jump_start(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}