package com.example.administrator.babygrowth01.babyparadise.parentMedia;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.administrator.babygrowth01.Common.ToolClass.CircleTransform;
import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.Common.Handler.AsyncHandler;
import com.example.administrator.babygrowth01.babyrecords.Main.PortraitJson;
import com.squareup.picasso.Picasso;
import com.tandong.swichlayout.SwitchLayout;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Administrator on 2016/2/21.
 */
public class ParentMediaListFragment extends Fragment implements View.OnClickListener {

    /** type select */
    private ImageView select_clothes,select_food,select_living,select_life;
    private LinearLayout child_info_linearLayout;
    int child_id,type;

    /** some views to show the content */
    private View parent_media_list_holder;
    private RecyclerView parent_media_recycler_view;
    private ParentMediaRecycleAdapter parentMediaRecycleAdapter;
    private ArrayList<PortraitJson> array_portrait;
    private PullToRefreshView pullToRefreshView;
    private LinearLayoutManager linearLayoutManager;
    private final String LOAD_FIRST="first",LOAD_MORE="more";

    /** async handler message and messenger basic variable */
    private AsyncHandler asyncHandler;
    private Messenger client_Messenger;
    private Messenger service_Messenger;
    private Message client_message;

    /** ready to add more data */
    private RequestQueue requestQueue;
    private Calendar calendar;

    /** async transmit content */
    private ArrayList<ParentMediaJson> parent_media_arraylist=new ArrayList<>();
    private int parent_media_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent_media_list_holder=inflater.inflate(R.layout.parent_media_list_holder, container, false);
        return parent_media_list_holder;
    }

    @Override
    public void onStart() {
        super.onStart();
        /** set the entering animation */
        SwitchLayout.getFadingIn(this.getActivity());

        /** some basic init*/
        select_clothes= (ImageView) parent_media_list_holder.findViewById(R.id.select_clothes);
        select_food= (ImageView) parent_media_list_holder.findViewById(R.id.select_food);
        select_living= (ImageView) parent_media_list_holder.findViewById(R.id.select_living);
        select_life= (ImageView) parent_media_list_holder.findViewById(R.id.select_life);
        /** register the onclick event */
        select_clothes.setOnClickListener(this);
        select_food.setOnClickListener(this);
        select_living.setOnClickListener(this);
        select_life.setOnClickListener(this);
        child_id=1;type=1;

        /** to initialize the client and service messengers */
        client_Messenger=new Messenger(new HandlerActivity());
        asyncHandler=MyResource.getAsyncHandler();
        service_Messenger=MyResource.getService_Messenger();
        array_portrait=new ArrayList<>();

        /** load child portrait and to show or hide the view */
        HorizontalScrollView horizontalScrollView= (HorizontalScrollView) parent_media_list_holder.findViewById(R.id.child_info_scrollview);
        child_info_linearLayout= (LinearLayout) parent_media_list_holder.findViewById(R.id.layout_parent_media_child_portrait);

        /** select the first child to be show correspond media data to parent */
        calendar=Calendar.getInstance();
        int age_layer=calendar.get(Calendar.YEAR)-Integer.parseInt(array_portrait.get(0).getBirth().substring(0, 4));

        SQLiteDatabase dbReader=MyResource.getSqLite().getReadableDatabase();
        Cursor cursor=dbReader.query(MyResource.getChildren_info_table(), null, null, null, null, null, null);
        if(cursor!=null&&cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                PortraitJson portraitJson=new PortraitJson();
                portraitJson.setChild_id(cursor.getInt(cursor.getColumnIndex("child_id")));
                portraitJson.setPortrait_uri(cursor.getString(cursor.getColumnIndex("portrait_uri")));
                portraitJson.setNick_name(cursor.getString(cursor.getColumnIndex("nick_name")));
                portraitJson.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                portraitJson.setBirth(cursor.getString(cursor.getColumnIndex("birth")));
                array_portrait.add(0,portraitJson);
            }while (cursor.moveToNext());
            cursor.close();


            Log.i("age_layer",String.valueOf(age_layer));
            /** initiate the portrait */
            for (PortraitJson ptjson :
                    array_portrait) {
                createBabyPortrait(ptjson);
            }
            /** send the request to the handler for loading the default parent media list */
            preRVData(1,1,LOAD_FIRST);
        }else {
            horizontalScrollView.setVisibility(View.GONE);
            /** send the request to the handler for loading the default parent media list */
            preRVData(0,1,LOAD_FIRST);/** for the refresh use*/
        }
        dbReader.close();

        /** find the holder preparing for the refresh action */
        pullToRefreshView= (PullToRefreshView) parent_media_list_holder.findViewById(R.id.pull_to_refresh);
    }

    public void init(ArrayList<ParentMediaJson> arraylist){

        /** refresh for more registration */
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {

                /** ask for more data*/
                preRVData(child_id, type, LOAD_MORE);
                pullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        /** initiation about requesting for data */
        requestQueue=Volley.newRequestQueue(parent_media_list_holder.getContext());

        /** do the preparation to use the recyclerview*/
        parent_media_recycler_view= (RecyclerView) parent_media_list_holder.findViewById(R.id.parent_media_recycler_view);
        /** set the showing style and here are two style for use to use*/
        linearLayoutManager=new LinearLayoutManager(this.getActivity());
        linearLayoutManager.scrollToPosition(0);  //set the top to show
        parent_media_recycler_view.setLayoutManager(linearLayoutManager);
        //rv.setLayoutManager(new GridLayoutManager(this,3));
        /** set the adapter to load the content to the view */
        parentMediaRecycleAdapter =new ParentMediaRecycleAdapter(arraylist,this.getActivity());
        parent_media_recycler_view.setAdapter(parentMediaRecycleAdapter);
        /** set the animation*/
        parent_media_recycler_view.setItemAnimator(new DefaultItemAnimator());
        /** set the item onclick listener */
        parent_media_recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(this.getActivity(), parent_media_recycler_view, service_Messenger));
        /** add item divide line*/
        //parent_media_recycler_view.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    /** type 1 for selecting clothes ; 2 for selecting food ; 3 for living ;4 for life*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_clothes:{
                type=1;
                preRVData(child_id,type,LOAD_FIRST);
                parentMediaRecycleAdapter.notifyDataSetChanged();
                break;
            }
            case R.id.select_food:{
                type=2;
                preRVData(child_id,type,LOAD_FIRST);
                parentMediaRecycleAdapter.notifyDataSetChanged();
                break;
            }
            case R.id.select_living:{
                type=3;
                preRVData(child_id,type,LOAD_FIRST);
                parentMediaRecycleAdapter.notifyDataSetChanged();
                break;
            }
            case R.id.select_life:{
                type=4;
                preRVData(child_id,type,LOAD_FIRST);
                parentMediaRecycleAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    public class HandlerActivity extends Handler {
        @Override
        public void handleMessage(Message msgService) {
            super.handleMessage(msgService);

            switch (msgService.arg1){

                /** 2 means has finished downloading the data and should do the initialization */
                case 2:{
                    init((ArrayList<ParentMediaJson>) msgService.obj);
                    System.out.println("In client Receive 3. ID=" + Thread.currentThread().getId() + "  Name=" + Thread.currentThread().getName());
                    break;
                }
                default:{
                    break;
                }
            }
        }
    }

    public void sendToService(Message msg){
        /** binding to the messenger and to the handler */
        msg.replyTo=client_Messenger;
        try {
            service_Messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addData(ArrayList<ParentMediaJson> arrayList){
        int position = ((LinearLayoutManager) parent_media_recycler_view.getLayoutManager()).
                findFirstVisibleItemPosition();
        parentMediaRecycleAdapter.refreshAddItem(position,arrayList);
        linearLayoutManager.scrollToPosition(0);
    }

    public void createBabyPortrait(PortraitJson ptjson){

        RelativeLayout view= (RelativeLayout) RelativeLayout.inflate(this.getActivity(),R.layout.parent_media_child_portrait,null);

        /** add id for marking which baby icon the user are clicking */
        int current_year=calendar.get(Calendar.YEAR);
        int current_month=calendar.get(Calendar.MONTH);
        int age_year=current_year-Integer.parseInt(ptjson.getBirth().substring(0,4));
        int age_month=current_month-Integer.parseInt(ptjson.getBirth().substring(6,8));
        view.setTag(R.id.rv_tag_1, ptjson.getChild_id());

        /** set child image to the round imageview*/
        final ImageView imageView= (ImageView) view.findViewById(R.id.media_parent_child_portrait);
        Picasso.with(this.getActivity()).load(new File(ptjson.getPortrait_uri())).resize(60,60).transform(new CircleTransform()).into(imageView);
        TextView child_name= (TextView) view.findViewById(R.id.media_parent_child_name);
        TextView child_age= (TextView) view.findViewById(R.id.media_parent_child_age);
        child_name.setText(ptjson.getNick_name());
        if(age_year==0){
            child_age.setText(age_year + "岁 " + age_month+"个月");
        }else{
            child_age.setText(age_month+"个月");
        }

        child_info_linearLayout.addView(view, 0);

        final RelativeLayout portrait_view = view;
        /** when click the portrait view should load corresponding item information and load to it */
        view.setOnClickListener(new View.OnClickListener() {
            /** click means should show the record relative to the selected child*/
            @Override
            public void onClick(View v) {
                //array_record.removeAll(results);
                parent_media_arraylist.clear();
                child_id = (int) portrait_view.getTag(R.id.rv_tag_1);
                //Toast.makeText(Child_Time_Record_Activity.this, portrait_id + "", Toast.LENGTH_SHORT).show();
                preRVData(child_id,type,LOAD_FIRST);
                parentMediaRecycleAdapter.notifyDataSetChanged();
            }
        });
    }

    /** preparing data for recycler view data source */
    public void preRVData(int child_id,int type,String times){
        client_message=new Message();
        client_message.arg1=2;  // identify parent media request
        Bundle bundle=new Bundle();
        bundle.putInt("child_id",child_id); // for the first initiation , to select the first child calculation
        bundle.putInt("type",type); // for the first initiation , to select the first child calculation
        bundle.putString("times", times); // times is to identify whether to add to array or to refresh original once and substitute
        client_message.setData(bundle);
        client_message.obj=parent_media_arraylist;  //arraylist for loading the content
        sendToService(client_message);
        client_message=null;  //release the resource
    }

    @Override
    public void onDestroy() {
        asyncHandler.terminate();
        super.onDestroy();
    }
}
