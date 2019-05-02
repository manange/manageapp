package com.example.manageapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class SplashActivity extends AppCompatActivity {

    private int waktu_loading=2000;

    //4000=4 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.splash_activity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah ke home activity
                Intent main=new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(main);
                finish();

            }
        },waktu_loading);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.splash_activity);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        final Handler hnldr=new Handler();
//        hnldr.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(getApplicationContext(),MainActivity.class));
//
//                finish();
//            }
//        },3000L);
//    }
}

