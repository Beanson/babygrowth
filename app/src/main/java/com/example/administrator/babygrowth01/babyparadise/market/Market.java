package com.example.administrator.babygrowth01.babyparadise.market;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.babygrowth01.R;

public class Market extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
    }

    /* add data to widget */
    public void initData(){
        market_each= (RecyclerView) findViewById(R.id.market_each);
    }

    /* widget that load into code */
    public void initWidget(){

    }

    private RecyclerView market_each;

}
