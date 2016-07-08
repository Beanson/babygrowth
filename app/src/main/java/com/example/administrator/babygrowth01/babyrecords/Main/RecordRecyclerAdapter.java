package com.example.administrator.babygrowth01.babyrecords.Main;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.babyrecords.AliOss.OssClass;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/29.
 */
public class RecordRecyclerAdapter extends RecyclerView.Adapter {

    public RecordRecyclerAdapter(ArrayList<RecordJson> arrayList, Child_Time_Record_Activity context,Context applicationContext) {
        this.arrayList = arrayList;
        this.context = context;
        this.applicationContext=applicationContext;
        inti();
    }

    public void inti(){
        options.inJustDecodeBounds=true;
        requestQueue= Volley.newRequestQueue(context);
        ossClass=new OssClass(applicationContext);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.child_record_each,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder= (MyViewHolder) holder;
        System.out.println(arrayList.get(position).getPic_uri());
        /* add tag to imageview for later use*/ myViewHolder.getIv_pic().setTag(R.id.rv_tag_1, arrayList.get(position).getId()); //  local database id not the cloud id
        /* add tag to imageview for later use*/ myViewHolder.getIv_pic().setTag(R.id.rv_tag_2, arrayList.get(position).getPic_uri());
        /* add tag to imageview for later use*/ myViewHolder.getIv_pic().setTag(R.id.rv_tag_3, position);
        /* add tag to note for later use*/ myViewHolder.getTv_note().setTag(R.id.rv_tag_1, arrayList.get(position).getId());
        /* add tag to note for later use*/ myViewHolder.getTv_note().setTag(R.id.rv_tag_2, arrayList.get(position).getNote());
        /* add tag to note for later use*/ myViewHolder.getTv_note().setTag(R.id.rv_tag_3, position);

        String pic_uri=arrayList.get(position).getPic_uri();
        if(!pic_uri.equals("null_pic")){
            Picasso.with(context).load(new File(arrayList.get(position).getPic_uri())).resize(120,120).into(myViewHolder.getIv_pic());
        }else {
            Picasso.with(context).load(R.drawable.twins).resize(120,120).into(myViewHolder.getIv_pic());
        }
        myViewHolder.getTv_note().setText(arrayList.get(position).getNote());
        myViewHolder.getTv_month().setText(arrayList.get(position).getDate_time());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  {

        private ImageView iv_pic;
        private TextView tv_note,tv_month;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_pic= (ImageView) itemView.findViewById(R.id.iv_pic);
            tv_note= (TextView) itemView.findViewById(R.id.tv_note);
            tv_month= (TextView) itemView.findViewById(R.id.tv_month);


            iv_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    imageView_reference_id = Integer.parseInt(iv_pic.getTag(R.id.rv_tag_1).toString());
                    record_id = Integer.parseInt(iv_pic.getTag(R.id.rv_tag_3).toString());
                    //Toast.makeText(context, imageView_reference_id +"  and   "+record_id , Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, iv_pic.getTag(R.id.rv_tag_2).toString(), Toast.LENGTH_SHORT).show();
                    /** if R.id.rv_tag_2 equals null means the first time this item to pick photo should open gallery directly while not null show the picture first */
                    if (iv_pic.getTag(R.id.rv_tag_2).toString().equals("null_pic")) {
                        /** open gallery to pick photos */
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        context.startActivityForResult(i, FIRST_RECORD_IMAGE);
                    } else {
                        /** to see the image in full */
                        final ImageView imageView = (ImageView) context.findViewById(R.id.ImageFull);
                        final ProgressBar picasso_bar_2= (ProgressBar) context.findViewById(R.id.picasso_bar_2);
                        final RelativeLayout relativelayout= (RelativeLayout) context.findViewById(R.id.ImageFullLayout);
                        relativelayout.setVisibility(View.VISIBLE);

                        BitmapFactory.decodeFile(iv_pic.getTag(R.id.rv_tag_2).toString(),options);
                        int width=options.outWidth;
                        int height=options.outHeight;
                        if(width>height){
                            Picasso.with(context).load(new File(iv_pic.getTag(R.id.rv_tag_2).toString())).rotate(90)
                                    .into(imageView, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            picasso_bar_2.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }else {
                            Picasso.with(context).load(new File(iv_pic.getTag(R.id.rv_tag_2).toString()))
                                    .into(imageView, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            picasso_bar_2.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }

                        relativelayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                relativelayout.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            });

            /** long press for change the image */
            iv_pic.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    imageView_reference_id = Integer.parseInt(iv_pic.getTag(R.id.rv_tag_1).toString());
                    record_id = Integer.parseInt(iv_pic.getTag(R.id.rv_tag_3).toString());
                    //Toast.makeText(context, imageView_reference_id + "", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, record_id+"", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    context.startActivityForResult(i, CHANGE_RECORD_IMAGE);
                    return true;
                }
            });

            /* note click */
            tv_note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /** begin to edit record */
                    //Toast.makeText(context, imageView_reference_id + "", Toast.LENGTH_SHORT).show();

                    final RelativeLayout edit_record = (RelativeLayout) context.findViewById(R.id.edit);
                    final ImageView edit_portrait= (ImageView) edit_record.findViewById(R.id.edit_portrait);
                    final EditText editText = (EditText) edit_record.findViewById(R.id.edit_text);
                    Picasso.with(context).load(new File(iv_pic.getTag(R.id.rv_tag_2).toString())).error(R.drawable.twins).into(edit_portrait);
                    if(tv_note.getTag(R.id.rv_tag_2).toString()!="null_note"){
                        editText.setText(tv_note.getTag(R.id.rv_tag_2).toString());
                    }
                    edit_record.setVisibility(View.VISIBLE);

                    /*edit portrait to add new portrait or to view all for show original picture */
                    edit_portrait.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            imageViewReferenceNote =edit_portrait;
                            imageView_reference_id = Integer.parseInt(iv_pic.getTag(R.id.rv_tag_1).toString());
                            record_id = Integer.parseInt(iv_pic.getTag(R.id.rv_tag_3).toString());
                            //Toast.makeText(context, imageView_reference_id + "", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context, iv_pic.getTag(R.id.rv_tag_2).toString(), Toast.LENGTH_SHORT).show();
                            /** if R.id.rv_tag_2 equals null means the first time this item to pick photo should open gallery directly while not null show the picture first */
                            if (iv_pic.getTag(R.id.rv_tag_2).toString().equals("null_pic")) {
                                /** open gallery to pick photos */
                                Intent i = new Intent(
                                        Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                context.startActivityForResult(i, CHANGE_RECORD_IMAGE_2);
                            } else {
                                /** to see the image in full */
                                imageViewReferenceNote =edit_portrait;
                                final ImageView imageView = (ImageView) context.findViewById(R.id.ImageFull);
                                final ProgressBar picasso_bar_2= (ProgressBar) context.findViewById(R.id.picasso_bar_2);
                                final RelativeLayout relativelayout= (RelativeLayout) context.findViewById(R.id.ImageFullLayout);
                                relativelayout.setVisibility(View.VISIBLE);

                                BitmapFactory.decodeFile(iv_pic.getTag(R.id.rv_tag_2).toString(), options);
                                int width = options.outWidth;
                                int height = options.outHeight;
                                if (width > height) {
                                    Picasso.with(context).load(new File(iv_pic.getTag(R.id.rv_tag_2).toString())).rotate(90)
                                            .into(imageView, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    picasso_bar_2.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onError() {
                                                    picasso_bar_2.setVisibility(View.GONE);
                                                }
                                            });
                                } else {
                                    Picasso.with(context).load(new File(iv_pic.getTag(R.id.rv_tag_2).toString()))
                                            .into(imageView, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    picasso_bar_2.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onError() {
                                                    picasso_bar_2.setVisibility(View.GONE);
                                                }
                                            });
                                }

                                relativelayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        relativelayout.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    });

                    edit_portrait.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            imageView_reference_id = Integer.parseInt(iv_pic.getTag(R.id.rv_tag_1).toString());
                            record_id = Integer.parseInt(iv_pic.getTag(R.id.rv_tag_3).toString());

                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            context.startActivityForResult(i, CHANGE_RECORD_IMAGE_2);
                            return true;
                        }
                    });


                    /* when click save button just to submit the content to database */
                    edit_record.findViewById(R.id.edit_submit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String edit_str = editText.getText().toString();
                            String path_str = context.getPic_path();
                            int id = Integer.parseInt(tv_note.getTag(R.id.rv_tag_1).toString());
                            int show_id = Integer.parseInt(tv_note.getTag(R.id.rv_tag_3).toString());
                            addToDb(edit_str, path_str, id);

                            //add to cloud database
                            SharedPreferences preferences=context.getSharedPreferences("cus_info",Context.MODE_PRIVATE);
                            preferences.getInt("cus_id",0);
                            preferences.getInt("cus_id",0);

                            addToCloudDb(edit_str, path_str);
                            ossClass.putObjectToOss(path_str);
                            tv_note.setText(edit_str);
                            tv_note.setTag(R.id.rv_tag_2, edit_str);
                            arrayList.get(show_id).setNote(edit_str);
                            edit_record.setVisibility(View.GONE);
                        }
                    });
                }
            });

            /**
             * long click event to modify the items
             */
            tv_note.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("选择项")
                            .setItems(new String[]{"删除"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        new AlertDialog.Builder(context)
                                                .setTitle("确认")
                                                .setMessage("确定删除该记录？")
                                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //arraylist delete such record
                                                        arrayList.remove(Integer.parseInt(tv_note.getTag(R.id.rv_tag_3).toString()));
                                                        refresh();

                                                        // aliyun oss delete such record
                                                        ossClass.delete(iv_pic.getTag(R.id.rv_tag_2).toString());

                                                        //local database delete such record
                                                        deleteOssPicLocal(tv_note.getTag(R.id.rv_tag_1).toString());

                                                        //aliyun database delete such record
                                                        SharedPreferences preferences=context.getSharedPreferences("cus_info",Context.MODE_PRIVATE);
                                                        int cus_id=preferences.getInt("cus_id",0);
                                                        deleteOssPicYun(String.valueOf(cus_id), iv_pic.getTag(R.id.rv_tag_2).toString());

                                                    }
                                                })
                                                .setNegativeButton("否", null)
                                                .show();
                                    }
                                }
                            })
                            .show();
                    return true;
                }
            });
        }

        public ImageView getIv_pic() {
            return iv_pic;
        }

        public void setIv_pic(ImageView iv_pic) {
            this.iv_pic = iv_pic;
        }

        public TextView getTv_note() {
            return tv_note;
        }

        public void setTv_note(TextView tv_note) {
            this.tv_note = tv_note;
        }

        public TextView getTv_month() {
            return tv_month;
        }

    }

    public void refresh(){
        notifyDataSetChanged();
    }

    public void addToDb(String edit_str,String path_str,int id){
        SQLiteDatabase dbWriter= MyResource.getSqLite().getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("pic_uri",path_str);
        values.put("note", edit_str);
        dbWriter.update("children_records", values, "id=" + id, null);
        dbWriter.close();
    }

    /**
     * add to cloud database
     */
    public void addToCloudDb(String edit_str, String path_str){
        SharedPreferences preferences=context.getSharedPreferences("cus_info",Context.MODE_PRIVATE);
        final int user_id=preferences.getInt("cus_id",0);//user_id
        final int type=1;//type

        String []temp=path_str.split("\\/");
        final String uri=temp[temp.length-1];

        //child_id
        final int child_id=context.getChildId();

        //note
        final String note=edit_str;

        //date
        Date currentTime=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        final String datastr=format.format(currentTime);

        // insert record
        String url_post="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/Customer/addPicUriToDb"; //error occurs always result from the url
        StringRequest request_more=new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                requestQueue.cancelAll("add_pic_cloud");
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
                map.put("user_id",String.valueOf(user_id));
                map.put("type",String.valueOf(type));
                map.put("uri",uri);
                map.put("child_id",String.valueOf(child_id));
                map.put("note",note);
                map.put("date",datastr);
                return map;
            }
        };
        request_more.setTag("add_pic_cloud");
        requestQueue.add(request_more);

    }


    public int getImageView_reference_id() {
        return imageView_reference_id;
    }

    public ImageView getImageViewReferenceNote() {
        return imageViewReferenceNote;
    }

    public int getRecord_id() {
        return record_id;
    }

    /**
     * delete record from oss
     * @param user_id user_id mark the user record in aliyun database
     * @param uri uri specify the record
     */
    public void deleteOssPicYun(final String user_id,String uri){
        //get the last string divided by "/";
        String []temp=uri.split("\\/");
        final String path_uri=temp[temp.length-1];
        //System.out.println("user_id："+user_id+"　"+"uri:"+path_uri);

        String url_post="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/Customer/deletePicOss"; //error occurs always result from the url
        StringRequest request_more=new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                requestQueue.cancelAll("delete_pic_oss");
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
                map.put("user_id",user_id);
                map.put("uri",path_uri);
                return map;
            }
        };
        request_more.setTag("delete_pic_oss");
        requestQueue.add(request_more);
    }

    /**
     * delete such child record in local database
     * @param id  item id or the records
     */
    public void deleteOssPicLocal(String id){

        System.out.println("count id"+id);
        SQLiteDatabase dbWriter= MyResource.getSqLite().getWritableDatabase();

//        Cursor cursor = dbWriter.rawQuery("select count(*)from children_records",null);
//        //游标移到第一条记录准备获取数据
//        cursor.moveToFirst();
//        // 获取数据中的LONG类型数据
//        Long count = cursor.getLong(0);
//        cursor.close();
//        System.out.println("count  before"+count);

        dbWriter.delete("children_records","id=?", new String[]{id});

//        Cursor cursor2 = dbWriter.rawQuery("select count(*)from children_records",null);
//        //游标移到第一条记录准备获取数据
//        cursor2.moveToFirst();
//        // 获取数据中的LONG类型数据
//        Long count2 = cursor2.getLong(0);
//        cursor2.close();
//        System.out.println("count  after" + count2);

        dbWriter.close();
        System.out.println("complete");
    }

    private Child_Time_Record_Activity context;
    private Context applicationContext;
    private ArrayList<RecordJson> arrayList;
    private BitmapFactory.Options options=new BitmapFactory.Options();
    private ImageView imageViewReferenceNote;
    private int FIRST_RECORD_IMAGE = 2;
    private int CHANGE_RECORD_IMAGE=3;
    private int CHANGE_RECORD_IMAGE_2=32;
    private int imageView_reference_id;
    private int record_id;
    private RequestQueue requestQueue;
    private OssClass ossClass;
}
