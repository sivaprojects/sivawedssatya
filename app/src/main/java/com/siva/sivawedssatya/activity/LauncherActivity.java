package com.siva.sivawedssatya.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.siva.sivawedssatya.R;


/**
 * Created by User on 07-04-2016.
 */
public class LauncherActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_view);
        try {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Intent mainIntent = new Intent(LauncherActivity.this, MainActivity.class);
                    LauncherActivity.this.startActivity(mainIntent);
                    LauncherActivity.this.finish();
                }
            }, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
