package com.example.administrator.babygrowth01.familydish;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.familydish.DishOrderSumbit.DishPaymentFragment;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/27.
 */
public class DishFragment extends Fragment implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyResource.setFragment_show_3("DishFragment");
        view=inflater.inflate(R.layout.family_dish_content, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init_click();
        init();
        if(DishStatic.dish_num>0){
            view.findViewById(R.id.dish_settlement).setVisibility(View.VISIBLE);
            ((TextView)view.findViewById(R.id.dish_chosen)).setText("已选择: "+(DishStatic.dish_num)+"个菜");
        }else if(DishStatic.dish_first_time) {

            loadOptionalData(dish_location, dish_type, dish_type_sum_begin[dish_type_id], SELECT_PREVIOUS_RECORD_MARK);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DishStatic.dish_first_time =true;
    }

    public void init(){

        /** refresh for more registration */
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /** ask for more data*/
                if(select_type==1){

                    loadOptionalData(dish_location,dish_type,dish_type_sum_begin[dish_type_id],SELECT_MORE_DATA);

                }else if(select_type==2){

                    loadFamilyData(dish_location,family_scare,people_type_sum_begin[dish_type_id],SELECT_MORE_DATA);

                }
                pullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshView.setRefreshing(false);
                    }
                },1000);
            }
        });

        /** initiation about requesting for data */
        requestQueue= Volley.newRequestQueue(activity);

        /** do the preparation to use the recyclerview*/
        dish_list_recycler_view= (RecyclerView) activity.findViewById(R.id.dish_list_recycler_view);
        /** set the showing style and here are two style for use to use*/
        linearLayoutManager=new LinearLayoutManager(activity);
        linearLayoutManager.scrollToPosition(0);  //set the top to show
        dish_list_recycler_view.setLayoutManager(linearLayoutManager);
        /** set the adapter to load the content to the view */
        dishListRecycleAdapter =new DishListRecycleAdapter(dishJson,activity);
        familyListRecycleAdapter=new FamilyListRecycleAdapter(dishFamilyJson,activity);
        dish_list_recycler_view.setAdapter(dishListRecycleAdapter);
        /** set the animation*/
        dish_list_recycler_view.setItemAnimator(new DefaultItemAnimator());
        /** set the item onclick listener */
        //dish_list_recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(this.getActivity(), dish_list_recycler_view,service_Messenger));
        /** add item divide line*/
        //dish_list_recycler_view.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        // if dish num above 0 means we should show the selected dish num

    }


    /** for loading data to the options dish arraylist*/
    public void addDish(List<DishJson> arrayList,int position){
        if(position==0){
            this.dishJson.addAll(0, arrayList);
            dishListRecycleAdapter.notifyDataSetChanged();
            linearLayoutManager.scrollToPosition(3);

        }else{
            this.dishJson.addAll(arrayList);
            dishListRecycleAdapter.notifyDataSetChanged();
        }
    }


    /** for loading data to the family dish arraylist*/
    public void addFamilyDish(List<DishFamilyJson> arrayList,int position){
        if(position==0){
            this.dishFamilyJson.addAll(0, arrayList);
            familyListRecycleAdapter.notifyDataSetChanged();
            linearLayoutManager.scrollToPosition(0);
        }else{
            this.dishFamilyJson.addAll(arrayList);
            familyListRecycleAdapter.notifyDataSetChanged();
        }
    }


    /** when the user like to seek more optional data from the internet */
    public void loadOptionalData(final int dish_location,final int dish_type,final int dish_begin,final int dish_length){
        //Toast.makeText(activity,"load",Toast.LENGTH_SHORT).show();
        String url_post="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/Dish/addOptionalDish"; //error occurs always result from the url
        StringRequest request_more=new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                String thumbnail_uri_base_root="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/Dish/options_dish/";
                String video_uri_base_root="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/Dish/family_dish/video/qing-dan-yu-tou-tang.mp4";

                List<DishJson> results= JSON.parseArray(s, DishJson.class);

                for (DishJson dishJson:
                        results) {
                    dishJson.setDish_thumbnail_uri(thumbnail_uri_base_root + DishStatic.dish_type_str+"thumbnail/"+dishJson.getDish_thumbnail_uri());
                    dishJson.setDish_video_uri(video_uri_base_root);
                }
                /* mark ,when true means add more data and should update the corresponding item ,when false means see the previous data */
                if(dish_length==3&&results.size()>0){
                    dish_type_sum_begin[dish_type_id]=results.get(results.size()-1).getId();
                    System.out.println("mark"+results.get(0).getId()+"   "+results.get(results.size()-1).getId());
                }


                if(refresh_from_top){
                    addDish(results,0);
                }else{
                    addDish(results,1);
                }

                requestQueue.cancelAll("seek_option_dish");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("error post");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<String,String>();
                map.put("dish_location",String.valueOf(dish_location));
                map.put("dish_type",String.valueOf(dish_type));
                map.put("dish_begin",String.valueOf(dish_begin));
                map.put("dish_length",String.valueOf(dish_length));
                return map;
            }
        };
        request_more.setTag("seek_option_dish");
        requestQueue.add(request_more);
    }


    /** when the user like to seek more family data from the internet */
    public void loadFamilyData(final int dish_location, final int family_scare,final int dish_begin,final int dish_length){

        String url_post="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/Dish/addFamilyDish"; //error occurs always result from the url
        StringRequest request_more=new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                String thumbnail_uri_base_root="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/Dish/family_dish/";
                String video_uri_base_root="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/Dish/family_dish/video/qing-dan-yu-tou-tang.mp4";

                List<DishFamilyJson> results= JSON.parseArray(s, DishFamilyJson.class);

                for (DishFamilyJson dishFamilyJson:
                        results) {
                    dishFamilyJson.setDish_thumbnail_uri(thumbnail_uri_base_root +"thumbnail/"+dishFamilyJson.getDish_thumbnail_uri());
                    dishFamilyJson.setDish_video_uri(video_uri_base_root);
                }
                /* mark ,when true means add more data and should update the corresponding item ,when false means see the previous data */
                if(dish_length==3&&results.size()>0){
                    people_type_sum_begin[people_type_id]=results.get(results.size()-1).getId();
                }


                if(refresh_from_top){
                    addFamilyDish(results, 0);
                }else{
                    addFamilyDish(results, 1);
                }

                requestQueue.cancelAll("seek_family_dish");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("error post");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<String,String>();
                map.put("dish_location",String.valueOf(dish_location));
                map.put("family_scale",String.valueOf(family_scare));
                map.put("dish_begin",String.valueOf(dish_begin));
                map.put("dish_length",String.valueOf(dish_length));
                return map;
            }
        };
        request_more.setTag("seek_family_dish");
        requestQueue.add(request_more);
    }

    public void init_click(){

        /** navigation bar*/
        rl_family_meal= (RelativeLayout) activity.findViewById(R.id.rl_family_meal);
        rl_options_dish= (RelativeLayout) activity.findViewById(R.id.rl_options_dish);
        rl_babysitter_plan= (RelativeLayout) activity.findViewById(R.id.rl_babysitter_plan);

        /** button to select dish type*/
        bt_fish= (Button) activity.findViewById(R.id.bt_fish);
        bt_meat= (Button) activity.findViewById(R.id.bt_meat);
        bt_vegetable= (Button) activity.findViewById(R.id.bt_vegetable);
        bt_cereal= (Button) activity.findViewById(R.id.bt_cereal);
        bt_soup= (Button) activity.findViewById(R.id.bt_soup);
        bt_dessert= (Button) activity.findViewById(R.id.bt_dessert);

        /** button to select family dish scale*/
        people_2_3= (Button) activity.findViewById(R.id.people_2_3);
        people_4_6= (Button) activity.findViewById(R.id.people_4_6);
        people_7_9= (Button) activity.findViewById(R.id.people_7_9);
        people_10_12= (Button) activity.findViewById(R.id.people_10_12);
        people_13_15= (Button) activity.findViewById(R.id.people_13_15);

        //payment button select
        bt_make_payment= (Button) activity.findViewById(R.id.bt_make_payment);

        /** refresh widget*/
        pullToRefreshView= (PullToRefreshView) activity.findViewById(R.id.pull_to_refresh);

        /** nar onclick listener*/
        rl_family_meal.setOnClickListener(this);
        rl_options_dish.setOnClickListener(this);
        rl_babysitter_plan.setOnClickListener(this);

        /** dish type onclick listener*/
        bt_fish.setOnClickListener(this);
        bt_meat.setOnClickListener(this);
        bt_vegetable.setOnClickListener(this);
        bt_cereal.setOnClickListener(this);
        bt_soup.setOnClickListener(this);
        bt_dessert.setOnClickListener(this);

        /** family scale onclick listener*/
        people_2_3.setOnClickListener(this);
        people_4_6.setOnClickListener(this);
        people_7_9.setOnClickListener(this);
        people_10_12.setOnClickListener(this);
        people_13_15.setOnClickListener(this);

        /* click payment button */
        bt_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dishOrder();
                /*for (DishStatic.DishSelectedTemp dish :
                        DishStatic.dishSelectedTemps) {
                    System.out.println(dish.getId()+" "+dish.getType()+" "+dish.getDish_name()+" "+dish.getThumbnail_path());
                }*/
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_babysitter_plan:{
                select_type=3;
                babysitterPlan();
                break;
            }
            case R.id.rl_family_meal:{
                activity.findViewById(R.id.family_scale).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.dish_type).setVisibility(View.GONE);
                select_type=2;
                break;
            }
            case R.id.rl_options_dish: {
                activity.findViewById(R.id.family_scale).setVisibility(View.GONE);
                activity.findViewById(R.id.dish_type).setVisibility(View.VISIBLE);
                select_type=1;
                break;
            }
            case R.id.bt_fish:{
                dish_type=1;
                DishStatic.dish_type_str="fish/";
                dish_type_id=0;
                dishJson.clear();
                break;
            }
            case R.id.bt_meat:{
                dish_type=2;
                DishStatic.dish_type_str="meat/";
                dish_type_id=1;
                dishJson.clear();
                break;
            }
            case R.id.bt_vegetable:{
                dish_type=3;
                DishStatic.dish_type_str="vegetable/";
                dish_type_id=2;
                dishJson.clear();
                break;
            }
            case R.id.bt_cereal:{
                dish_type=4;
                DishStatic.dish_type_str="cereal/";
                dish_type_id=3;
                dishJson.clear();
                break;
            }
            case R.id.bt_soup:{
                dish_type=5;
                DishStatic.dish_type_str="soup/";
                dish_type_id=4;
                dishJson.clear();
                break;
            }
            case R.id.bt_dessert:{
                dish_type=6;
                DishStatic.dish_type_str="dessert/";
                dish_type_id=5;
                dishJson.clear();
                break;
            }
            case R.id.people_2_3:{
                family_scare=1;
                people_type_id=0;
                dishFamilyJson.clear();
                break;
            }
            case R.id.people_4_6:{
                family_scare=2;
                people_type_id=1;
                dishFamilyJson.clear();
                break;
            }
            case R.id.people_7_9:{
                family_scare=3;
                people_type_id=2;
                dishFamilyJson.clear();
                break;
            }
            case R.id.people_10_12:{
                family_scare=4;
                people_type_id=3;
                dishFamilyJson.clear();
                break;
            }
            case R.id.people_13_15:{
                family_scare=5;
                people_type_id=4;
                dishFamilyJson.clear();
                break;
            }
        }

        if(select_type==1){
            /** optional data */
            dish_list_recycler_view.setAdapter(dishListRecycleAdapter);
            loadOptionalData(dish_location, dish_type, dish_type_sum_begin[dish_type_id], SELECT_PREVIOUS_RECORD_MARK);

        }else if(select_type==2){
            /** family data */
            dish_list_recycler_view.setAdapter(familyListRecycleAdapter);
            loadFamilyData(dish_location, family_scare, people_type_sum_begin[people_type_id], SELECT_PREVIOUS_RECORD_MARK);
        }else if(select_type==3){

        }

    }
    public void babysitterPlan(){
        /*getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.ly_family_dish_holder, new BabysitterPlanFragment())
                .commit();*/
    }

    public void dishOrder(){
        DishStatic.dish_first_time =false;
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.ly_family_dish_holder, new DishPaymentFragment())
                .commit();
    }


    /** the widget init params */
    private RelativeLayout rl_family_meal,rl_options_dish,rl_babysitter_plan;
    private Button bt_fish,bt_meat,bt_vegetable,bt_cereal,bt_soup,bt_dessert;
    private Button people_2_3,people_4_6,people_7_9,people_10_12,people_13_15;
    private Button bt_make_payment;

    /** some views to show the content */
    private RecyclerView dish_list_recycler_view;
    private DishListRecycleAdapter dishListRecycleAdapter;
    private FamilyListRecycleAdapter familyListRecycleAdapter;
    private PullToRefreshView pullToRefreshView;
    private LinearLayoutManager linearLayoutManager;

    /** ready to add more data */
    private RequestQueue requestQueue;

    /** holder ready to load data */
    private ArrayList<DishJson> dishJson =new ArrayList<>();
    private ArrayList<DishFamilyJson> dishFamilyJson =new ArrayList<>();

    /** to identify which type to select and initiate some default type*/
    public int select_type=1; // 1 for options dish; 2 for family dish
    private int dish_location=1;// 8 style and 1 for guangdong
    private int family_scare=2; //1 for 2-3 , 2 for 4-6 , 3 for 7-9 , 4 for 10-12 , 5 for 13-15
    private int dish_type=1; //1 for fish,2 for meal,3 for vegetable,4 for cereal,5 for soup,6 for dessert
    //public String dish_type_str="fish/";
    private boolean refresh_from_top=true; // when refresh whether to add to top or bottom

    /** ready to request for the data from server and do some initiation*/
    private int dish_type_id=0; // by default choose fish
    private int people_type_id=0; //by default choose guangdong dish
    private int[] dish_type_sum_begin ={5,17,24,31,10,38}; // the original place sit in the database
    private int[] people_type_sum_begin ={3,3,3,3,3}; //should be edit later when use family scale
    private final int SELECT_PREVIOUS_RECORD_MARK=0;
    private final int SELECT_MORE_DATA=3;

    private Activity activity;
    private View view;
}

