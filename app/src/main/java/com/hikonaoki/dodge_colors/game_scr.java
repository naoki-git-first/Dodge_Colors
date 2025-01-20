package com.hikonaoki.dodge_colors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.MotionEvent;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class game_scr extends AppCompatActivity {

    Thread th;
    SurfaceHolder holder;
    BallSurfaceView bsv;
    Timer timer;
    LocalTime time;
    int p_cnt=0;    //period(100ms)経った回数を代入（速度変化に使用）
    int lv=0;       //難易度を受け取る変数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_scr);
        Intent intent = getIntent();
        int lv_msg = Integer.parseInt(intent.getStringExtra("lv"));
        lv = lv_msg;
        SurfaceView sv = (SurfaceView) findViewById(R.id.surfaceview);
        bsv = new BallSurfaceView();
        holder = sv.getHolder();
        holder.addCallback(bsv);
        sv.setOnTouchListener(bsv);
        timecnt();
    }
    @Override
    public void onStart(){
        super.onStart();
        th = new Thread(bsv);
        th.start();
    }
    @Override
    public void onStop(){
        super.onStop();
        th = null;
    }
    /*時間の計測*/
    public void timecnt(){
        if (timer != null) {
            return;
        }
        TextView textViewJudge = (TextView)findViewById(R.id.txtTime);
        final Handler handler = new Handler();
        time = LocalTime.of(0, 0);
        int period = 100;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time = time.plusNanos((long) (period * Math.pow(10, 6)));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String fmt = time.format(DateTimeFormatter.ofPattern("mm:ss.S"));
                        textViewJudge.setText(fmt);
                        p_cnt++;
                    }
                });
            }
        }, 0, period);
    }
    public void jump(){
        Intent intent = new Intent(this, result_scr.class);
        TextView time_txt = (TextView)findViewById(R.id.txtTime);
        String time_msg = time_txt.getText().toString();
        intent.putExtra("time", time_msg);
        startActivity(intent);
    }
    class BallSurfaceView implements View.OnTouchListener, SurfaceHolder.Callback, Runnable {
        int screen_width, screen_height;
        int num_of_fl = 2+lv;  //タイルの色の数
        int fl_id = 0;  //左のタイルから0→num_of_fl
        Ball ba = new Ball();
        Floor[] fl = new Floor[num_of_fl];
        ArrayList<String> list;

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
            screen_width = width;
            screen_height = height;
            list = new ArrayList<>();   //色をリストに追加
            list.add("255,0,0");
            list.add("0,0,255");
            if(lv==1){
                list.add("0,255,50");
            }else if(lv==2){
                list.add("0,255,50");
                list.add("220,220,0");
            }
            Collections.shuffle(list);  //色をシャッフル（初期配置）
            ba = new Ball();
            for(fl_id=0;fl_id<num_of_fl;fl_id++){
                fl[fl_id] = new Floor();
            }
        }
        public void surfaceCreated(SurfaceHolder holder){}
        public void surfaceDestroyed(SurfaceHolder holder){}
        class Floor{
            int top=0, bottom=screen_height/8, left=fl_id*(screen_width/num_of_fl), right=(fl_id+1)*(screen_width/num_of_fl), dy=25;
            int red, green, blue;
            Floor(){
                String[] color = list.get(fl_id).split(",");
                red=Integer.parseInt(color[0]);
                green=Integer.parseInt(color[1]);
                blue=Integer.parseInt(color[2]);
            }
            void move(){
                if(p_cnt%100==0) dy += 3;   //タイルの速度を10秒ごとに+3
                top = top + dy;
                bottom = bottom + dy;
                if(top > screen_height){    //タイルが画面から消えたとき
                    top=0;
                    bottom=screen_height/8; //タイルの高さ
                    if(fl_id==0){
                        Collections.shuffle(list);  //一番左のタイルを描画する際に色をシャッフル
                        String[] color = list.get(fl_id).split(",");
                        red=Integer.parseInt(color[0]);
                        green=Integer.parseInt(color[1]);
                        blue=Integer.parseInt(color[2]);
                    }else{
                        String[] color = list.get(fl_id).split(",");
                        red=Integer.parseInt(color[0]);
                        green=Integer.parseInt(color[1]);
                        blue=Integer.parseInt(color[2]);
                    }
                }
            }
            void draw(Canvas ca){
                Paint paint = new Paint();
                paint.setColor(Color.rgb(red,green,blue));
                ca.drawRect(left, top, right, bottom, paint);
            }
        }
        public boolean onTouch(View v, MotionEvent event){
            switch(event.getAction()){
                case MotionEvent.ACTION_MOVE:
                    int x = (int)event.getX();
                    int y = (int)event.getY();
                    if(y<screen_height/2) y=screen_height/2;    //ボールの縦の可動域（上限）
                    if(y>screen_height) y=screen_height;        //ボールの縦の可動域（下限）
                    ba.x = x;
                    ba.y = y;
                    break;
            }
            return true;
        }
        class Ball{
            int x=5+screen_width/2, y=screen_height-100, r=40;
            int red, green, blue;
            Random random = new Random();
            int rval = random.nextInt(num_of_fl);
            void draw(Canvas ca, int fl_top){
                if(fl_top == 0) rval = random.nextInt(num_of_fl);   //タイルがトップに戻った際に色を変更
                String[] color = list.get(rval).split(",");
                red=Integer.parseInt(color[0]);
                green=Integer.parseInt(color[1]);
                blue=Integer.parseInt(color[2]);
                Paint paint = new Paint();
                paint.setColor(Color.rgb(red,green,blue));
                ca.drawCircle(x, y, r, paint);
            }
        }
        public void run(){
            while(th != null){
                Canvas canvas = holder.lockCanvas();
                if(canvas != null){
                    canvas.drawColor(Color.BLACK);
                    for(fl_id=0;fl_id<num_of_fl;fl_id++) {  //タイルを動かす
                        fl[fl_id].move();
                        fl[fl_id].draw(canvas);
                    }
                    ba.draw(canvas, fl[0].top); //色変更のためfl[0].topを引数に取る
                    if(fl[0].bottom>=ba.y-ba.r && ba.y+ba.r>=fl[0].top){    //タイルとボールの接触判定
                        for(int i=0;i<num_of_fl;i++) {
                            if((ba.red == fl[i].red && ba.green == fl[i].green && ba.blue == fl[i].blue) != true) {
                                if (ba.x > fl[i].left && ba.x < fl[i].right) {
                                    th = null;
                                    if (null != timer) {
                                        timer.cancel();
                                        timer = null;
                                    }
                                   jump();
                                }
                            }
                        }
                    }
                    holder.unlockCanvasAndPost(canvas);
                }
                try{
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}