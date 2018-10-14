package com.example.user.matchingarcticanimals;


import android.widget.TextView;

import java.util.TimerTask;

public class CountUpTimer extends TimerTask {

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