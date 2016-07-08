package com.example.administrator.babygrowth01.babyparadise.songs;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.tandong.swichlayout.SwitchLayout;
import com.yalantis.phoenix.PullToRefreshView;
import com.example.administrator.babygrowth01.Common.Handler.AsyncHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/21.
 */
public class SongsListFragment extends Fragment  {

    /** some views to show the content */
    private View songs_list_holder;
    private RecyclerView songs_list_recycler_view;
    private SongListRecycleAdapter songListRecycleAdapter;
    private PullToRefreshView pullToRefreshView;
    private LinearLayoutManager linearLayoutManager;

    /** async handler message and messenger basic variable */
    private AsyncHandler asyncHandler;
    private Messenger client_Messenger;
    private Messenger service_Messenger;
    private Message client_message;

    /** ready to add more data */
    private RequestQueue requestQueue;

    /** async transmit content */
    private ArrayList<SongsJson> arraylist=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        songs_list_holder=inflater.inflate(R.layout.songs_list_holder, container, false);
        MyResource.setFragment_show("SongsListFragment");
        return songs_list_holder;
    }


    @Override
    public void onStart() {
        super.onStart();
        /** set the entering animation */
        SwitchLayout.getFadingIn(this.getActivity());

        /** find the holder preparing for the refresh action */
        pullToRefreshView= (PullToRefreshView) songs_list_holder.findViewById(R.id.pull_to_refresh);

        /** to initialize the client and service messengers */
        client_Messenger=new Messenger(new HandlerActivity());
        service_Messenger=MyResource.getService_Messenger();

        /** send the request to the handler for loading the default BabyPaFragment.songs list */
        client_message=new Message();
        client_message.arg1=1;  //arg1=1 for the download default BabyPaFragment.songs list
        client_message.obj=arraylist;  //arraylist for loading the content
        sendToService(client_message);
        client_message=null;  //release the resource
    }

    public void init(ArrayList<SongsJson> arraylist){

        /** refresh for more registration */
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {

                /** ask for more data*/
                seekMoreData();
                pullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshView.setRefreshing(false);
                    }
                },1000);
            }
        });

        /** initiation about requesting for data */
        requestQueue=Volley.newRequestQueue(songs_list_holder.getContext());

        /** do the preparation to use the recyclerview*/
        songs_list_recycler_view= (RecyclerView) songs_list_holder.findViewById(R.id.songs_list_recycler_view);
        /** set the showing style and here are two style for use to use*/
        linearLayoutManager=new LinearLayoutManager(this.getActivity());
        linearLayoutManager.scrollToPosition(0);  //set the top to show
        songs_list_recycler_view.setLayoutManager(linearLayoutManager);
        //rv.setLayoutManager(new GridLayoutManager(this,3));
        /** set the adapter to load the content to the view */
        songListRecycleAdapter =new SongListRecycleAdapter(arraylist,this.getActivity());
        songs_list_recycler_view.setAdapter(songListRecycleAdapter);
        /** set the animation*/
        songs_list_recycler_view.setItemAnimator(new DefaultItemAnimator());
        /** set the item onclick listener */
        songs_list_recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(this.getActivity(), songs_list_recycler_view,service_Messenger));
        /** add item divide line*/
        //songs_list_recycler_view.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));

    }

    public class HandlerActivity extends Handler {
        @Override
        public void handleMessage(Message msgService) {
            super.handleMessage(msgService);

            switch (msgService.arg1){

                /** 1 means has finished downloading the data and should do the initialization */
                case 1:{
                    init((ArrayList<SongsJson>) msgService.obj);
                    System.out.println("In client Receive 3. ID=" + Thread.currentThread().getId() + "  Name=" + Thread.currentThread().getName());
                    break;
                }

                /** 2 for refreshing for more downloaded data finished and to add to the BabyPaFragment.songs list fragment*/
                /*case 2:{
                    addData((ArrayList<SongsJson>) msgService.obj);

                    break;
                }*/
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

    public void addData(ArrayList<SongsJson> arrayList){
        int position = ((LinearLayoutManager) songs_list_recycler_view.getLayoutManager()).
                findFirstVisibleItemPosition();
        songListRecycleAdapter.refreshAddItem(position,arrayList);
        linearLayoutManager.scrollToPosition(0);
    }


    /** when the user like to seek more data from the internet */
    public void seekMoreData(){

        String url_post="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/Songs/addMoreSongs"; //error occurs always result from the url
        StringRequest request_more=new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                String thumbnail_uri_base_root="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/Songs/version01_thumbnail/";
                String http_uri_base_root="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/Songs/version01/";

                List<SongsJson> results= JSON.parseArray(s, SongsJson.class);
                SQLiteDatabase dbWrite=MyResource.getSqLite().getWritableDatabase();
                ArrayList<SongsJson> arrayList=new ArrayList<>();
                arrayList.addAll(results);

                /**
                 * insert into to the SQLite database
                 */
                for (SongsJson songs_list :
                        results) {

                    ContentValues cv=new ContentValues();
                    cv.put("name", songs_list.getName());
                    cv.put("status", songs_list.getStatus());
                    cv.put("play_times", songs_list.getPlay_times());
                    cv.put("thumbnail_uri", thumbnail_uri_base_root+songs_list.getThumbnail_uri());
                    cv.put("http_uri", http_uri_base_root+songs_list.getHttp_uri());
                    cv.put("local_uri", songs_list.getLocal_uri());
                    dbWrite.insert("songs_list", null, cv);
                }

                for (SongsJson songs :
                        arrayList) {
                    songs.setThumbnail_uri(thumbnail_uri_base_root+songs.getThumbnail_uri());
                    songs.setHttp_uri(http_uri_base_root+songs.getHttp_uri());
                }

                addData(arrayList);
                requestQueue.cancelAll("seek_more_data");
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
                map.put("songs_id","2");
                return map;
            }
        };
        request_more.setTag("seek_more_data");
        requestQueue.add(request_more);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
