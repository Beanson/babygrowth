package com.example.administrator.babygrowth01.familydish.DishOrderSumbit;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.familydish.DishAddressListRecycleAdapter;

/**
 * Created by Administrator on 2016/4/7.
 */
public class DishPaymentFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyResource.setFragment_show_3("DishPaymentFragment");
        return inflater.inflate(R.layout.family_dish_address,container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    /** */
    private void init(){
        dish_submit= (Button) activity.findViewById(R.id.dish_submit);
        buyer= (EditText) activity.findViewById(R.id.buyer);
        user_phone= (EditText) activity.findViewById(R.id.user_phone);
        cus_time= (EditText) activity.findViewById(R.id.tv_order_time);
        location= (EditText) activity.findViewById(R.id.dish_location);

        //get information from sharedPreferences and set to editext widgets
        SharedPreferences sharedPreferences=activity.getSharedPreferences("cus_info", Context.MODE_PRIVATE);
        buyer.setText(sharedPreferences.getString("cus_name",null));
        user_phone.setText(sharedPreferences.getString("cus_phone",null));
        cus_time.setText(sharedPreferences.getString("cus_time",null));
        location.setText(sharedPreferences.getString("location",null));
        cus_time.setText(sharedPreferences.getString("cus_time","11:30"));

        dish_submit.setOnClickListener(this);

        /** do the preparation to use the recyclerview*/
        dish_address_list_recycler_view = (RecyclerView) activity.findViewById(R.id.dish_address_list_recycler_view);
        /** set the showing style and here are two style for use to use*/
        linearLayoutManager=new LinearLayoutManager(activity);
        linearLayoutManager.scrollToPosition(0);  //set the top to show
        dish_address_list_recycler_view.setLayoutManager(linearLayoutManager);
        /** set the adapter to load the content to the view */
        dishAddressListRecycleAdapter =new DishAddressListRecycleAdapter(activity);
        dish_address_list_recycler_view.setAdapter(dishAddressListRecycleAdapter);
        /** set the animation*/
        dish_address_list_recycler_view.setItemAnimator(new DefaultItemAnimator());
        /** set the item onclick listener */
        //dish_address_list_recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(this.getActivity(), dish_address_list_recycler_view,service_Messenger));
        /** add item divide line*/
        //dish_address_list_recycler_view.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dish_submit:{
                new DishOrderSubmit(activity,buyer.getText().toString(),user_phone.getText().toString(),location.getText().toString(),cus_time.getText().toString()).execute();
                break;
            }
        }
    }


    private EditText buyer,user_phone,location,cus_time;
    private Activity activity;
    private Button dish_submit;
    private RecyclerView dish_address_list_recycler_view;
    private DishAddressListRecycleAdapter dishAddressListRecycleAdapter;
    private LinearLayoutManager linearLayoutManager;
}
