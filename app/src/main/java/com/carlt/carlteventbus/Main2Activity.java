package com.carlt.carlteventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.carlt.carlteventbus.eventbus.EventBus;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.tvSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getInstance().post(new MesageEvent("123", "hehe"));
                Log.e("Main2Activity====>", Thread.currentThread().getName());
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        EventBus.getInstance().post(new MesageEvent("123", "hehe"));
//                        Log.e("Main2Activity====>", Thread.currentThread().getName());
//                    }
//                }).start();

            }
        });
    }
}
