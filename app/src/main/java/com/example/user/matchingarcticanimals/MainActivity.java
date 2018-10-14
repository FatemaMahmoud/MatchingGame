package com.example.user.matchingarcticanimals;

import android.content.Intent;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


class CountUpTimer extends TimerTask {

    private TextView tv;
    private int timer;

    CountUpTimer(TextView tv){
        this.tv = tv;
        timer = 0;
    }

    @Override
    public void run() {
        tv.setText(timer + " sec");
        timer++;
    }

    public int getTimer(){
        return timer;
    }
}


public class MainActivity extends AppCompatActivity{

    private static final String a = R.drawable.a + "";
    private static final String b = R.drawable.b + "";
    private static final String c = R.drawable.c + "";
    private static final String d = R.drawable.d + "";
    private int firstClick = -1;
    private ImageView firstImg;
    private  TextView firstTxt;
    private TextView timerTv;
    private CountUpTimer cut;
    private TextToSpeech tts;
    private boolean clickable;
    private int cnt;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                name = null;
            } else {
                name = extras.getString("name");
            }
        } else {
            name = (String) savedInstanceState.getSerializable("name");
        }
        if (name != null) {
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText(tv.getText() + ", " + name);
        }


        clickable = true;
        cnt = 0;
        //timer
        timerTv = (TextView) findViewById(R.id.timer);
        cut = new CountUpTimer(timerTv);
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(cut, 0, 1000);
        //img places
        placeImagesRnd();
        //text to speech
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.UK);
            }
        });
    }

    private void speak(String p){
        if (p.equals(a)){
            tts.speak("seal", TextToSpeech.QUEUE_FLUSH, null);
        } else if (p.equals(b)){
            tts.speak("penguin", TextToSpeech.QUEUE_FLUSH, null);
        } else if (p.equals(c)){
            tts.speak("fox", TextToSpeech.QUEUE_FLUSH, null);
        } else {
            tts.speak("orcas", TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void onClickFn(final ImageView iv, int num,  ArrayList<String> nums, final TextView tv){
        Handler handler = new Handler();
        Runnable runable = new Runnable() {
            @Override
            public void run() {
                // set thr image to image1
                iv.setImageResource(R.drawable.footprints);
                firstImg.setImageResource(R.drawable.footprints);
                tv.setText("");
                firstTxt.setText("");
                clickable = true;
            }
        };
        speak(nums.get(num));
        if (firstClick == -1 && clickable){
            firstClick = num;
            firstImg = iv;
            firstTxt = tv;
            iv.setImageResource(Integer.parseInt(nums.get(num)));
            tv.setText(updateTxt(nums.get(num)));
        } else if (nums.get(firstClick).equals(nums.get(num)) && !iv.equals(firstImg)){
            //win situation
            Toast.makeText(getApplication(), "Matched", Toast.LENGTH_SHORT).show();
            firstClick = -1;
            iv.setImageResource(Integer.parseInt(nums.get(num)));
            tv.setText(updateTxt(nums.get(num)));
            iv.setClickable(false);
            firstImg.setClickable(false);
            if (++cnt == 4 ){
                Intent i = new Intent(this, ScoreActivity.class);
                i.putExtra("name", name);
                i.putExtra("score", 2000/cut.getTimer());
                startActivity(i);
                finish();
            }
        } else if (!nums.get(firstClick).equals(nums.get(num))){
            //not match
            clickable = false;
            iv.setImageResource(Integer.parseInt(nums.get(num)));
            tv.setText(updateTxt(nums.get(num)));
            handler.removeCallbacks(runable);
            handler.postDelayed(runable, 1000);
            firstClick = -1;
        }
    }

    private String updateTxt(String s){
        if (s.equals(a)){
            return "Seal";
        } else if (s.equals(b)){
            return "Penguin";
        } else if (s.equals(c)){
            return "Fox";
        } else {
            return "Orcas";
        }
    }

    private void placeImagesRnd(){
        final ImageView iv0 = (ImageView) findViewById(R.id.i0);
        final ImageView iv1 = (ImageView) findViewById(R.id.i1);
        final ImageView iv2 = (ImageView) findViewById(R.id.i2);
        final ImageView iv3 = (ImageView) findViewById(R.id.i3);
        final ImageView iv4 = (ImageView) findViewById(R.id.i4);
        final ImageView iv5 = (ImageView) findViewById(R.id.i5);
        final ImageView iv6 = (ImageView) findViewById(R.id.i6);
        final ImageView iv7 = (ImageView) findViewById(R.id.i7);

        final TextView tv0 = (TextView) findViewById(R.id.t0);
        final TextView tv1 = (TextView) findViewById(R.id.t1);
        final TextView tv2 = (TextView) findViewById(R.id.t2);
        final TextView tv3 = (TextView) findViewById(R.id.t3);
        final TextView tv4 = (TextView) findViewById(R.id.t4);
        final TextView tv5 = (TextView) findViewById(R.id.t5);
        final TextView tv6 = (TextView) findViewById(R.id.t6);
        final TextView tv7 = (TextView) findViewById(R.id.t7);

        final ArrayList<String> nums = new ArrayList<>(Arrays.asList(a, a, b, b, c, c, d, d));
        java.util.Collections.shuffle(nums);

        iv0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickable)
                    onClickFn(iv0, 0, nums, tv0);
            }
        });

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickable)
                    onClickFn(iv1, 1, nums, tv1);
            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickable)
                    onClickFn(iv2, 2, nums, tv2);
            }
        });

        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickable)
                    onClickFn(iv3, 3, nums, tv3);
            }
        });

        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickable)
                    onClickFn(iv4, 4, nums, tv4);
            }
        });

        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickable)
                    onClickFn(iv5, 5, nums, tv5);
            }
        });

        iv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickable)
                    onClickFn(iv6, 6, nums, tv6);
            }
        });

        iv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickable)
                    onClickFn(iv7, 7, nums, tv7);
            }
        });

    }


}
