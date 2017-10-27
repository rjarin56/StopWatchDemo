package com.reniejarin.demo1;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import android.os.Handler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    List<String> TimeList;
    String[] ListTimes = new String[] {};
    ListView listView;
    int seconds, mins, mSec;
    Handler handler;
    long mSecTime, startTime, timeBuff, updateTime = 0L;
    Button start, pause, reset, lap;
    TextView timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer = (TextView)findViewById(R.id.timer);
        start = (Button)findViewById(R.id.start);
        pause = (Button)findViewById(R.id.pause);
        reset = (Button)findViewById(R.id.reset);
        lap = (Button)findViewById(R.id.lap);
        listView = (ListView)findViewById(R.id.listView);

        TimeList = new ArrayList<>(Arrays.asList(ListTimes));

        adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, TimeList);

        handler = new Handler();

        listView.setAdapter(adapter);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SystemClock.uptimeMillis is the method that will increment the startTime by millisecond
                startTime = SystemClock.uptimeMillis();

                //postDelayed will have the runnable to be added to the message queue by the specified
                //value which in this case would be 0
                handler.postDelayed(runnable, 0);

                //This will make the reset button inactive while the timer is going
                reset.setEnabled(false);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //this adds to the time buffer from milli second time
                timeBuff += mSecTime;

                //removeCallbacks will remove the runnable that are in the message queue
                handler.removeCallbacks(runnable);

                //this will enable the reset button
                reset.setEnabled(true);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reset the timer and value to 0
                mSec = 0;
                mins = 0;
                seconds = 0;
                updateTime = 0L;
                timeBuff = 0L;
                startTime = 0L;
                mSecTime = 0L;

                timer.setText("00:00:00");

                //this clears the list view
                TimeList.clear();

                //notifyDataSetChanged method notifies the adapter that the data
                // has been changed and it needs to do a refresh
                adapter.notifyDataSetChanged();
            }
        });

        lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Every time the lap button is clicked, the TimeList list view
                //will add the string in the timer text view
                TimeList.add(timer.getText().toString());

                adapter.notifyDataSetChanged();
            }
        });
    }

    // Runnable() is a method that will have the class active while not subclassing Thread.
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mSecTime = SystemClock.uptimeMillis() - startTime;

            updateTime = timeBuff + mSecTime;

            seconds = (int) (updateTime / 1000);

            mins = seconds / 60;

            seconds = seconds % 60;

            mSec = (int)(updateTime % 1000);

            timer.setText("" + mins + ":"
                + String.format("%02d", seconds) + ":"
                + String.format("%03d", mSec));

            handler.postDelayed(this, 0);
        }
    };
}
