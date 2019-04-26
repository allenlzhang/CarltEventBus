package com.carlt.carlteventbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.carlt.carlteventbus.eventbus.EventBus;
import com.carlt.carlteventbus.eventbus.Subscribe;
import com.carlt.carlteventbus.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getInstance().register(this);
        startActivity(new Intent(this, Main2Activity.class));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(MesageEvent event) {
        Log.e("MainActivity====>", event.toString() + "---" + Thread.currentThread().getName());
    }
}
