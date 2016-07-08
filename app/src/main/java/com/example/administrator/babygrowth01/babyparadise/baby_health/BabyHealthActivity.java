package com.example.administrator.babygrowth01.babyparadise.baby_health;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.administrator.babygrowth01.R;

public class BabyHealthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_health);
        init();
    }

    public void init(){
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .add(R.id.health_holder, new HealthFragment())
                .commit();
    }

}
