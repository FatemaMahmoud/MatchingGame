package com.example.user.matchingarcticanimals;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {


    private DBAdapter myDb;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        startService(new Intent(this, SoundService.class));

        lv = (ListView) findViewById(R.id.scores);
        final ImageView backgroundOne = (ImageView) findViewById(R.id.background_one);
        final ImageView backgroundTwo = (ImageView) findViewById(R.id.background_two);
        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);

        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX - width);
            }
        });
        animator.start();

        myDb = new DBAdapter(this);
        myDb.open();
        Bundle extras = getIntent().getExtras();
        displayRecords(extras.getString("name"), extras.getInt("score"));

    }

    public void displayRecords(String name, int score) {

        myDb.insertRow(name, score);
        Cursor cursor = myDb.getAllRows();
        displayRecordSet(cursor);

    }

    private void displayRecordSet(Cursor cursor) {

        // populate the message from the cursor
        ArrayList<String> al = new ArrayList<>();
        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                String message = "";
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String name = cursor.getString(DBAdapter.COL_NAME);
                int score = cursor.getInt(DBAdapter.COL_SCORE);

                // Append data to the message:
                message = "Name: " + name
                        +"         Score=" + score
                        +"\n";

                // [TO_DO_B6]
                // Create arraylist(s)? and use it(them) in the list view
                al.add(message);
            } while(cursor.moveToNext());
        }

        // Close the cursor to avoid a resource leak.
        cursor.close();


        // [TO_DO_B7]
        // Update the list view
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, al);
        lv.setAdapter(adapter);
        // [TO_DO_B8]
        // Display a Toast message
    }
    protected void onDestroy() {
        //stop service and stop music
        stopService(new Intent(this, SoundService.class));
        super.onDestroy();
    }
}

