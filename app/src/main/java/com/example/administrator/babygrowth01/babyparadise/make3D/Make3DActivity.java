package com.example.administrator.babygrowth01.babyparadise.make3D;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.administrator.babygrowth01.R;


public class Make3DActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make3_d);

        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .add(R.id.model3d_place, new Main_3d())
                .commit();
    }
}
