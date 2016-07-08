package com.example.administrator.babygrowth01.familydish;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.babygrowth01.R;

/**
 * Created by Administrator on 2016/3/27.
 */
public class BabysitterPlanFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DishStatic.dishMark=2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.family_babysitter_plan,container, false);
    }

    public void init(){
        activity=getActivity();
        imageView= (ImageView) getActivity().findViewById(R.id.iv_babysitter_plan);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity,DishListActivity.class);
                startActivity(intent);
            }
        });
    }

    private ImageView imageView;
    private Activity activity;
}

