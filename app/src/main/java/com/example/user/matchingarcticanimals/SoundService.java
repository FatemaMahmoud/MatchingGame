package com.example.user.matchingarcticanimals;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class SoundService extends Service {

    MediaPlayer player;
    private int id;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {

        player = MediaPlayer.create(getApplicationContext(), R.raw.background); //select music file
        player.setLooping(true); //set looping

    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        player.start();
        return Service.START_NOT_STICKY;
    }

    public void onDestroy() {
        player.stop();
        player.release();
        stopSelf();
        super.onDestroy();
    }
}
