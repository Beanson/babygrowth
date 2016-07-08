package com.example.administrator.babygrowth01.babyparadise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.babygrowth01.Common.ToolClass.CircleTransform;
import com.example.administrator.babygrowth01.MainActivity;
import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.babyparadise.babyRaise.BabyRaiseFragment;
import com.example.administrator.babygrowth01.babyparadise.babyRecipe.BabyRecipeActivity;
import com.example.administrator.babygrowth01.babyparadise.baby_health.BabyHealthActivity;
import com.example.administrator.babygrowth01.babyparadise.clothesMall.ClothesListFragment;
import com.example.administrator.babygrowth01.babyparadise.course.CourseListFragment;
import com.example.administrator.babygrowth01.babyparadise.fun_play.FunPlayListFragment;
import com.example.administrator.babygrowth01.babyparadise.make3D.Make3DActivity;
import com.example.administrator.babygrowth01.babyparadise.playing_game.FoxBar.PlayGameActivity01;
import com.example.administrator.babygrowth01.babyparadise.playing_game.HitMouse.GameMouseHit;
import com.example.administrator.babygrowth01.babyparadise.songs.SongsListFragment;
import com.example.administrator.babygrowth01.babyparadise.story.TellStoryActivity;
import com.example.administrator.babygrowth01.babyparadise.toyMall.ToysListFragment;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class BabyPaFragment extends Fragment implements View.OnClickListener {

    private ImageView course,songs,raiseBaby,story,doll,clothes,fun,health,game;
    private View baby_pa_main;
    private MainActivity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity= (MainActivity) getActivity();
        baby_pa_main=inflater.inflate(R.layout.fragment_baby_pa_main, container, false);
        return baby_pa_main;
    }

    @Override
    public void onStart() {
        super.onStart();
        beforeInit();
    }

    public void beforeInit(){
        final LinearLayout register= (LinearLayout) baby_pa_main.findViewById(R.id.register);
        final LinearLayout contain_baby_pa= (LinearLayout) baby_pa_main.findViewById(R.id.contain_baby_pa);

        SharedPreferences sharedPreferences=activity.getSharedPreferences("cus_info", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();

        if(sharedPreferences.getString("cus_account",null)==null){
            // if cus_account is null means the first time to register account
            contain_baby_pa.setVisibility(View.GONE);
            final LinearLayout bottom_menu= (LinearLayout) activity.findViewById(R.id.bottom_menu);
            bottom_menu.setVisibility(View.GONE);

            Button bt_register= (Button) baby_pa_main.findViewById(R.id.bt_register);
            final TextView cus_account= (TextView) baby_pa_main.findViewById(R.id.et_account);
            final TextView cus_password= (TextView) baby_pa_main.findViewById(R.id.et_password);
            bt_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertDBGetId(cus_account.getText().toString(),cus_password.getText().toString(),editor);
                    register.setVisibility(View.GONE);
                    contain_baby_pa.setVisibility(View.VISIBLE);
                    bottom_menu.setVisibility(View.VISIBLE);
                    init();
                }
            });

        }else{
            register.setVisibility(View.GONE);
            init();
        }
    }

    public void init(){
        MyResource.setFragment_show("BabyPaFragment");
        songs= (ImageView) baby_pa_main.findViewById(R.id.songs);
        raiseBaby= (ImageView) baby_pa_main.findViewById(R.id.raiseBaby);
        story= (ImageView) baby_pa_main.findViewById(R.id.story);
        doll= (ImageView) baby_pa_main.findViewById(R.id.doll);
        clothes= (ImageView) baby_pa_main.findViewById(R.id.clothes);
        course= (ImageView) baby_pa_main.findViewById(R.id.course);
        fun= (ImageView) baby_pa_main.findViewById(R.id.fun);
        health= (ImageView) baby_pa_main.findViewById(R.id.health);
        game= (ImageView) baby_pa_main.findViewById(R.id.game);


        //Picasso.with(this.getActivity()).load("file:///android_asset/BabyPaFragment/course.png").into(course);
        Picasso.with(this.getActivity()).load("file:///android_asset/BabyPaFragment/music.png").transform(new CircleTransform()).into(songs);
        Picasso.with(this.getActivity()).load("file:///android_asset/BabyPaFragment/book2.png").transform(new CircleTransform()).into(raiseBaby);
        Picasso.with(this.getActivity()).load("file:///android_asset/BabyPaFragment/story.png").transform(new CircleTransform()).into(story);
        Picasso.with(this.getActivity()).load("file:///android_asset/BabyPaFragment/play.png").transform(new CircleTransform()).into(fun);
        Picasso.with(this.getActivity()).load("file:///android_asset/BabyPaFragment/health.png").transform(new CircleTransform()).into(health);
        Picasso.with(this.getActivity()).load("file:///android_asset/BabyPaFragment/game.png").transform(new CircleTransform()).into(game);
        /*Picasso.with(this.getActivity()).load("file:///android_asset/BabyPaFragment/toy.png").into(doll);
        Picasso.with(this.getActivity()).load("file:///android_asset/BabyPaFragment/clothes.png").into(clothes);*/

        songs.setOnClickListener(BabyPaFragment.this);
        raiseBaby.setOnClickListener(BabyPaFragment.this);
        story.setOnClickListener(BabyPaFragment.this);
        fun.setOnClickListener(BabyPaFragment.this);
        health.setOnClickListener(BabyPaFragment.this);
        game.setOnClickListener(BabyPaFragment.this);
        doll.setOnClickListener(BabyPaFragment.this);
        clothes.setOnClickListener(BabyPaFragment.this);
        course.setOnClickListener(BabyPaFragment.this);
    }

    /**
     * insert customer information to database by http protocol and save to sharedPreferences
     * @param cus_account customer account
     * @param cus_password customer password
     * @param editor editor
     */
    public void insertDBGetId(final String cus_account,final String cus_password,final SharedPreferences.Editor editor){
        final RequestQueue requestQueue= Volley.newRequestQueue(activity);
        String thinkphp_path="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/Customer/insertCusInfoDB";
        StringRequest request_more=new StringRequest(Request.Method.POST, thinkphp_path, new Response.Listener<String>() {
            @Override
            public void onResponse(String cus_id) {
                //we need cus_id so we add SaveUserInfo here
                editor.putString("cus_account",cus_account);
                editor.putString("cus_password",cus_password);
                editor.putInt("cus_id",Integer.parseInt(cus_id));
                editor.commit();
                requestQueue.cancelAll("add_cus_info_db");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("error post");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("cus_account",cus_account);
                map.put("cus_password",cus_password);
                return map;
            }
        };
        request_more.setTag("add_cus_info_db");
        requestQueue.add(request_more);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.songs:{
                songsOnClick();
                break;
            }
            case R.id.raiseBaby:{
                raiseBabyOnClick();
                break;
            }
            case R.id.story:{
                storyOnClick();
                break;
            }
            case R.id.fun:{
                funOnClick();
                break;
            }
            case R.id.health:{
                healthOnClick();
                break;
            }
            case R.id.game:{
                gameOnClick();
                break;
            }
            case R.id.doll:{
                toysOnClick();
                break;
            }
            case R.id.clothes:{
                clothesOnClick();
                break;
            }
            case R.id.course:{
                courseOnClick();
                break;
            }
        }
    }

    public void songsOnClick(){
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.MainFragment, new SongsListFragment())
                .commit();
    }

    public void raiseBabyOnClick(){
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.MainFragment, new BabyRaiseFragment())
                .commit();
    }

    public void storyOnClick(){
        Intent intent=new Intent(this.getActivity(), TellStoryActivity.class);
        startActivity(intent);
    }

    public void toysOnClick(){
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.MainFragment, new ToysListFragment())
                .commit();
    }

    public void clothesOnClick(){
//        getFragmentManager().beginTransaction()
//                .addToBackStack(null)
//                .replace(R.id.MainFragment, new ClothesListFragment())
//                .commit();
        Intent intent=new Intent(this.getActivity(), Make3DActivity.class);
        startActivity(intent);
    }

    public void courseOnClick(){
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.MainFragment, new CourseListFragment())
                .commit();
    }
    public void funOnClick(){
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.MainFragment, new FunPlayListFragment())
                .commit();
    }
    public void healthOnClick(){

//        Intent intent=new Intent(activity,BabyHealthActivity.class);
//        activity.startActivity(intent);

          Intent intent=new Intent(activity,BabyRecipeActivity.class);
          activity.startActivity(intent);

//        String url =""; // web address
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse(url));
//        activity.startActivity(intent);
    }
    public void gameOnClick(){
//        getFragmentManager().beginTransaction()
//                .addToBackStack(null)
//                .replace(R.id.MainFragment, new GamesListFragment())
//                .commit();
        //
//add some others //
        Intent intent=new Intent(getActivity(), GameMouseHit.class);
        getActivity().startActivity(intent);
    }
}
