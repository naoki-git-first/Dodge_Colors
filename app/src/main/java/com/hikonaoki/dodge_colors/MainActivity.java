package com.hikonaoki.dodge_colors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void lv0(View view){
        Intent intent = new Intent(this, game_scr.class);
        Integer i = Integer.valueOf(0);
        String lv_msg = i.toString();
        intent.putExtra("lv", lv_msg);
        startActivity(intent);
    }
    public void lv1(View view){
        Intent intent = new Intent(this, game_scr.class);
        Integer i = Integer.valueOf(1);
        String lv_msg = i.toString();
        intent.putExtra("lv", lv_msg);
        startActivity(intent);
    }
    public void lv2(View view){
        Intent intent = new Intent(this, game_scr.class);
        Integer i = Integer.valueOf(2);
        String lv_msg = i.toString();
        intent.putExtra("lv", lv_msg);
        startActivity(intent);
    }
}