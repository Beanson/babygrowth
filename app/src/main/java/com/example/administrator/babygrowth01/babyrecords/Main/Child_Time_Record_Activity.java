package com.example.administrator.babygrowth01.babyrecords.Main;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.babygrowth01.Common.ToolClass.CircleTransform;
import com.example.administrator.babygrowth01.MyResource;
import com.squareup.picasso.Picasso;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.administrator.babygrowth01.R;

public class Child_Time_Record_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.children_time_record);
        init();  // do some initialization with the variable to be used
        forRVPortait();
        forRVRecord();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /** do the refresh work and insert one more item of record*/
    public void pullToRefresh(){

        /* get current calendar */
        Calendar calendar=Calendar.getInstance();
        int monthOfYear=(calendar.get(Calendar.MONTH)+1);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
        String data=dayOfMonth+"/"+monthOfYear;

        /* first insert to the database with default record*/
        SQLiteDatabase dbWriter= MyResource.getSqLite().getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("child_id", portrait_id);
        values.put("pic_uri","null_pic");
        values.put("note","null_note");
        values.put("date_time",data);
        long id=dbWriter.insert(MyResource.getRecord_table(), null, values);
        dbWriter.close();

        /* add the new record to array_record for refreshing record recylerview*/
        RecordJson recordJson=new RecordJson();
        recordJson.setId((int) id);
        recordJson.setChild_id(portrait_id);
        recordJson.setPic_uri("null_pic");
        recordJson.setNote("null_note");
        recordJson.setDate_time(data);
        array_record.add(0, recordJson);
        recordRecyclerAdapter.refresh();
    }

    /** according to the back */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /* for camera*/
        if(requestCode==ADD_EDIT_RECORD_CAMERA){
            if(resultCode==RESULT_OK){
                System.out.println("pic uri"+addRecordHelper.getPic_path());
                addRecordHelper.editRecord(addRecordHelper.getPic_path(),portrait_id,getApplicationContext());
                recordRecyclerAdapter.refresh();
                return;
            }
        }

        /* for gallery*/
        if(resultCode==RESULT_OK && null != data){

            // add new child
            if(requestCode==ADD_CHILD){
                Bundle args=data.getExtras();
                PortraitJson portraitJson=new PortraitJson();
                portraitJson.setPortrait_uri(args.getString("new_portrait_uri"));
                portraitJson.setChild_id(++portrait_count);
                createBabyPortrait(portraitJson);
                addAndShowDefaultRecord(portrait_count);

            }else if(requestCode==ADD_EDIT_RECORD_CAMERA){
                System.out.println("backuri  " + data.getData());
                //addRecordHelper.editRecord(back_pic_uri, portrait_id);
                //recordRecyclerAdapter.refresh();
            }else{

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                /** select pictures by all columns */
                Cursor cursor =getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                /** action to the cursor */
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String back_pic_uri = cursor.getString(columnIndex);
                //System.out.println("back uri: "+back_pic_uri);
                cursor.close();

                /** the first time to assign image to this item  */
                if (requestCode == FIRST_RECORD_IMAGE ) {
                    array_record.get(recordRecyclerAdapter.getRecord_id()).setPic_uri(back_pic_uri);
                    int id=recordRecyclerAdapter.getImageView_reference_id();
                    updateRecordImage(id,back_pic_uri);
                    recordRecyclerAdapter.refresh();
                    /** save a copy to the local file image folder*/
                    /** we could do something later ,like upload to the server , save thumbnail to the local in version 2,3 etc*/
                    /*Bitmap b= BitmapFactory.decodeFile(portrait_path_original);
                    portrait_path=getFilesDir().getPath()+BabyInfo.BABY_INFO_FOLDER+"/portrait_image"+id+"jpg";
                    File file=new File(portrait_path);
                    try {
                        FileOutputStream fos=new FileOutputStream(file);
                        b.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }*/
                }else if(requestCode==CHANGE_PORTRAIT_IMAGE){
                    changePortraitImage(back_pic_uri);
                }else if(requestCode==CHANGE_RECORD_IMAGE){
                    int id=recordRecyclerAdapter.getImageView_reference_id();
                    array_record.get(recordRecyclerAdapter.getRecord_id()).setPic_uri(back_pic_uri);
                    updateRecordImage(id,back_pic_uri);
                    recordRecyclerAdapter.refresh();
                }else if(requestCode==CHANGE_RECORD_IMAGE_2){
                    int id=recordRecyclerAdapter.getImageView_reference_id();
                    recordRecyclerAdapter.getImageViewReferenceNote().setTag(R.id.rv_tag_2,back_pic_uri);
                    Picasso.with(this).load(new File(back_pic_uri)).into(recordRecyclerAdapter.getImageViewReferenceNote());
                    array_record.get(recordRecyclerAdapter.getRecord_id()).setPic_uri(back_pic_uri);
                    updateRecordImage(id, back_pic_uri);
                    setPic_path(back_pic_uri);
                    recordRecyclerAdapter.refresh();
                }else if(requestCode==ADD_EDIT_RECORD_GALLERY){
                    addRecordHelper.editRecord(back_pic_uri,portrait_id,getApplicationContext());
                    recordRecyclerAdapter.refresh();
                }
            }
        }
    }
/****************************************************************************************************/
/** For portraits */

    public void forRVPortait(){

        /** send the request to the handler for loading portrait data */
        client_message=new Message();
        client_message.arg1=5;  //arg1=5 for the download portrait data
        client_message.obj=array_portrait;  //arraylist for loading the content
        sendToService(client_message);
        client_message=null;  //release the resource

        /** arraylist store some data of previous child data */
        //ArrayList<PortraitJson> arrayList=forRVPortraitData();

    }

    public void createBabyPortrait(PortraitJson ptjson){

        RelativeLayout view= (RelativeLayout) RelativeLayout.inflate(this,R.layout.child_portrait_each,null);
        String file_path=ptjson.getPortrait_uri();/* load the portrait uri*/
        view.setTag(R.id.rv_tag_1, ptjson.getChild_id());/* add id for marking which baby icon the user are clicking */

        final ImageView imageView= (ImageView) view.findViewById(R.id.iv_portrait); /* find the imageview*/
        Picasso.with(this).load(new File(file_path)).resize(80, 80).transform(new CircleTransform()).into(imageView);/* set child image to the round imageview*/

        TextView textView= (TextView) view.findViewById(R.id.tv_child_name);
        textView.setText(ptjson.getNick_name());
        linearLayout.addView(view,0);

        final RelativeLayout portrait_view = view;
        /** when click the portrait view ,should be judge long click or click*/
        view.setOnClickListener(new View.OnClickListener() {
            /** click means should show the record relative to the selected child*/
            @Override
            public void onClick(View v) {
                //array_record.removeAll(results);
                array_record.clear();
                // get portrait id for later change correspond portrait image
                portrait_id = (int) portrait_view.getTag(R.id.rv_tag_1);
                // prepare record data correspond to that portrait
                preRVData(portrait_id);
                //do refreshment to show the item on the screen
                recordRecyclerAdapter.refresh();
                //Toast.makeText(Child_Time_Record_Activity.this, finalView.getTag(R.id.rv_tag_1)+"",Toast.LENGTH_SHORT).show();
            }
        });

        /** on long click means should open gallery for portrait image selection*/
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                portrait_image_reference = imageView;
                portrait_id = (int) portrait_view.getTag(R.id.rv_tag_1);
                Toast.makeText(Child_Time_Record_Activity.this, portrait_id + "", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, CHANGE_PORTRAIT_IMAGE);
                return true;
            }
        });
    }

    public void changePortraitImage(String new_portrait_uri){
        /** change the content of array portrait and reset the imageview */
        array_portrait.get((portrait_id - 1)).setPortrait_uri(new_portrait_uri);
        Picasso.with(this).load(new File(new_portrait_uri)).transform(new CircleTransform()).into(portrait_image_reference);

        /** update the database */
        SQLiteDatabase dbWriter=MyResource.getSqLite().getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("portrait_uri", new_portrait_uri);
        dbWriter.update(MyResource.getChildren_info_table(), values, "child_id=" + portrait_id, null);
        dbWriter.close();
    }

    public void addAndShowDefaultRecord(int child_id){
        portrait_id=child_id;// this id of child is selected */
        array_record.clear(); // clear the record for upload new data show to recycler view
        preRVData(child_id);// upload new data especially id
        recordRecyclerAdapter.refresh();
    }

/*********************************************************************************************************/
    /**  For records */
    public void forRVRecord(){

        /** do the preparation to use the recyclerview*/
        rv_record= (RecyclerView) findViewById(R.id.rv_record_overall);
        /** set the showing style and here are two style for use to use*/
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.scrollToPosition(0);  //set the top to show
        rv_record.setLayoutManager(linearLayoutManager);
        //rv.setLayoutManager(new GridLayoutManager(this,3));

        /** prepare the data and set the adapter to load the content to the view */
        array_record=new ArrayList<>();
        //Toast.makeText(this,array_portrait.get(0).getRecord_uri()+"",Toast.LENGTH_SHORT ).show();
        preRVData(1); // the screen show the first child record information by default
        //Toast.makeText(this,array_record.toString(),Toast.LENGTH_SHORT ).show();
        recordRecyclerAdapter =new RecordRecyclerAdapter(array_record,this,getApplicationContext());
        rv_record.setAdapter(recordRecyclerAdapter);
        /** set the animation*/
        rv_record.setItemAnimator(new DefaultItemAnimator());

        /** set the item onclick listener */
        //rv_record.addOnItemTouchListener(new RecyclerItemClickListener(this,rv_record));
        /** add item divide line*/
        //songs_list_recycler_view.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    /** for loading child record portrait */
    public void preRVData(int child_id){
        /** send the request to the handler for loading record data */
        client_message=new Message();
        client_message.arg1=55;  //arg1=5 for the download record data
        client_message.arg2=child_id;
        client_message.obj=array_record;  //arraylist for loading the content
        sendToService(client_message);
        client_message=null;  //release the resource
    }

    public void updateRecordImage(int id, String new_reocrd_uri){

        /* update the database */
        SQLiteDatabase dbWriter=MyResource.getSqLite().getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("pic_uri",new_reocrd_uri);

        dbWriter.update(MyResource.getRecord_table(), values, "id=" + id, null);
        dbWriter.close();
    }
    /*************************************************************************************************/
    public void init(){
        linearLayout= (LinearLayout) findViewById(R.id.ll_portrait); // initiate the widgets
        iv_add= (ImageView) findViewById(R.id.pic_add); // add image initiate
        iv_demo= (ImageView) findViewById(R.id.iv_demo);
        ly_camera= (LinearLayout) findViewById(R.id.ly_camera);
        ly_gallery= (LinearLayout) findViewById(R.id.ly_gallery);
        client_Messenger=new Messenger(new HandlerActivity());
        service_Messenger= MyResource.getService_Messenger();
        array_portrait=new ArrayList<>();// init array_record and array_portrait
        array_record=new ArrayList<>();
        addRecordHelper=new AddRecordHelper(this);

        /*  when click the add picture you could add another child information*/
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Child_Time_Record_Activity.this, AddBabyActivity.class);
                intent.putExtra("portrait_count", portrait_count);
                startActivityForResult(intent,ADD_CHILD);
            }
        });

        /** when click the demo picture it will turn to show you how to use this part*/
        iv_demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /* open the camera ready to take photos and wait for recording */
        ly_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecordHelper.openCamera();
            }
        });

        /* open the gallery and pick pictures for recording */
        ly_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecordHelper.openGallery();
            }
        });

        /*findViewById(R.id.buttontest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase dbReader=MyResource.getSqLite().getReadableDatabase();
                Cursor c=dbReader.query(MyResource.getRecord_table(),null,null,null,null,null,"id desc");

                if(c.getCount()>0){
                    c.moveToFirst();
                    do{
                       int id=(c.getInt(c.getColumnIndex("id")));
                        int child_id=(c.getInt(c.getColumnIndex("child_id")));
                        String pic_uri=(c.getString(c.getColumnIndex("pic_uri")));
                        String note=(c.getString(c.getColumnIndex("note")));
                        String datatime=(c.getString(c.getColumnIndex("date_time")));
                        int height=(c.getInt(c.getColumnIndex("height")));
                        System.out.println(id+" "+pic_uri+" "+note+" "+child_id+"　"+datatime+" "+height);
                    }while (c.moveToNext());
                }
                *//** release the resource *//*
                c.close();dbReader.close();
            }
        });*/

        final PullToRefreshView pullToRefreshView= (PullToRefreshView) findViewById(R.id.refresh_record);
        /** refreshment initiate*/
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /** ask for more data*/
                if (portrait_count > 0) {
                    pullToRefresh();
                } else {
                    AlertDialog.Builder builder=new AlertDialog.Builder(Child_Time_Record_Activity.this);
                    builder.setTitle("提示");
                    builder.setMessage("您好！请先按+号图片添加孩子记录");
                    builder.setPositiveButton("确认", null);
                    builder.show();
                }
                pullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    public class HandlerActivity extends Handler {
        @Override
        public void handleMessage(Message msgService) {
            super.handleMessage(msgService);
            switch (msgService.arg1){
                // 5 means has finished downloading the data and should do the initialization */
                case 5:{
                    portrait_count=array_portrait.size();
                    System.out.println("find out "+portrait_count);
                    if(portrait_count>0)portrait_id=1; //initiate the default first child record
                    for (PortraitJson ptjson :
                            array_portrait) {
                        createBabyPortrait(ptjson);
                    }
                    break;
                }
                case 55:{
                    /* when load image done, reload the image */
                    recordRecyclerAdapter.refresh();
                    break;
                }
                default:{
                    break;
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        String fragment_show=MyResource.getFragment_show();

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            switch (fragment_show){
                case "RecordEdit":{
                    this.findViewById(R.id.edit).setVisibility(View.GONE);
                    break;
                }default:{
                    return super.onKeyDown(keyCode, event);
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void sendToService(Message msg){
        // binding to the messenger and to the handler */
        msg.replyTo=client_Messenger;
        try {
            service_Messenger.send(msg);

        } catch (RemoteException e) {
            e.printStackTrace();

        }
    }

    public int getChildId(){
        return portrait_id;
    }

    /**
     * setting pic path for adapter invoke
     */
    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }

    public String getPic_path() {
        return pic_path;
    }

    public ArrayList<RecordJson> getArray_record() {
        return array_record;
    }

    private RecyclerView rv_record;
    private LinearLayout linearLayout;
    private LinearLayoutManager linearLayoutManager;
    private RecordRecyclerAdapter recordRecyclerAdapter;
    private ArrayList<PortraitJson> array_portrait;
    private ArrayList<RecordJson> array_record;

    // preparing for child portrait using */
    private int portrait_count;
    private int portrait_id;
    private ImageView portrait_image_reference;

    // as for demo and show how to use it */
    private ImageView iv_demo, iv_add;

    // result back mark*/
    static final private int ADD_CHILD=1;
    private int FIRST_RECORD_IMAGE = 2;
    private int CHANGE_RECORD_IMAGE=3;
    private int CHANGE_RECORD_IMAGE_2=32;
    private int CHANGE_PORTRAIT_IMAGE=4;
    private final int ADD_EDIT_RECORD_GALLERY=5;
    private final int ADD_EDIT_RECORD_CAMERA=6;

    // transform data */
    private Messenger client_Messenger;
    private Messenger service_Messenger;
    private Message client_message;

    // camera and gallery
    private AddRecordHelper addRecordHelper;
    private LinearLayout ly_camera,ly_gallery;

    // record the image path
    private String  pic_path;
}
