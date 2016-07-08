package com.example.administrator.babygrowth01.familydish;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.Common.MediaPlayer.MediaPlayActivity;

/**
 * Created by Administrator on 2016/3/27.
 */
public class DishDetailFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyResource.setFragment_show_3("DishDetailFragment");
        return inflater.inflate(R.layout.family_dish_detail,container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        getSetDishDetailData();
    }


    public void getSetDishDetailData(){
        GetDishDetailVolly getDishDetailVolly=new GetDishDetailVolly(activity,tv_dish_name,iv_dish_image,tv_dish_depict,tv_dish_material,tv_dish_step,iv_dish_media);
        getDishDetailVolly.loadOptionalData(DishStatic.dish_type,DishStatic.dish_id,DishStatic.dish_type_str);
    }

    public void init(){
        tv_dish_name= (TextView) activity.findViewById(R.id.tv_dish_name);
        tv_dish_depict= (TextView) activity.findViewById(R.id.iv_dish_depict);
        tv_dish_material= (TextView) activity.findViewById(R.id.tv_dish_material);
        tv_dish_step= (TextView) activity.findViewById(R.id.tv_dish_step);
        iv_dish_image= (ImageView) activity.findViewById(R.id.iv_dish_image);
        iv_dish_media= (ImageView) activity.findViewById(R.id.iv_dish_media);

        iv_dish_media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Messenger service_Messenger= MyResource.getService_Messenger();
                Intent intent=new Intent(activity, MediaPlayActivity.class);
                intent.putExtra("uri",iv_dish_media.getTag().toString());
                intent.putExtra("service_Messenger",service_Messenger);
                activity.startActivity(intent);
            }
        });
    }

    private TextView tv_dish_name,tv_dish_depict,tv_dish_material,tv_dish_step;
    private ImageView iv_dish_image,iv_dish_media;
    private Activity activity;
}

