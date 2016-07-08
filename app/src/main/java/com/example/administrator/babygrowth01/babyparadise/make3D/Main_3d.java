package com.example.administrator.babygrowth01.babyparadise.make3D;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.babygrowth01.Common.ToolClass.CircleTransform;
import com.example.administrator.babygrowth01.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class Main_3d extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.model3d_main, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        initWidget();
        viewOnClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_tutorial:{

                //Toast.makeText(activity,"dala",Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.model3d_place, new Tutorial_3d())
                        .commit();

            }
        }
    }

    public void viewOnClick(){
        take_video.setOnClickListener(Main_3d.this);
        gallery_pic.setOnClickListener(Main_3d.this);
        iv_tutorial.setOnClickListener(Main_3d.this);
    }


    public void initWidget(){

        activity=getActivity();
        make01= (ImageView) view.findViewById(R.id.make01);
        make02= (ImageView) view.findViewById(R.id.make02);
        make03= (ImageView) view.findViewById(R.id.make03);

        take_video= (ImageView) view.findViewById(R.id.take_video);
        gallery_pic= (ImageView) view.findViewById(R.id.gallery_pic);
        iv_tutorial= (ImageView) view.findViewById(R.id.iv_tutorial);

        Picasso.with(activity).load(R.drawable.make01).transform(new CircleTransform()).into(make01);
        Picasso.with(activity).load(R.drawable.make02).transform(new CircleTransform()).into(make02);
        Picasso.with(activity).load(R.drawable.make03).transform(new CircleTransform()).into(make03);
    }

    private View view;
    private Activity activity;
    private ImageView make01,make02,make03,take_video,gallery_pic,iv_tutorial;


}
