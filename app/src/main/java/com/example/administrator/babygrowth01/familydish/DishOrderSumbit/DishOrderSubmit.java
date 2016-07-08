package com.example.administrator.babygrowth01.familydish.DishOrderSumbit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.babygrowth01.FirstTime.Customer;
import com.example.administrator.babygrowth01.babyrecords.Main.BabyInfo;
import com.example.administrator.babygrowth01.familydish.DishStatic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/7.
 */
public class DishOrderSubmit extends AsyncTask<Void,Void,Void>{

    //private String serverIp="120.27.126.41";
    private String serverIp="120.27.126.41";
    private int serverPort=3000;
    private int serverDishPort=3001;
    private int thinkphp_id;
    private Activity activity;
    private String cus_name,cus_phone,location,cus_time;

    public DishOrderSubmit(Activity activity,String cus_name,String cus_phone,String location,String cus_time) {
        this.activity = activity;
        this.cus_name=cus_name;
        this.cus_phone=cus_phone;
        this.location=location;
        this.cus_time=cus_time;
    }

    /** enter this function could place the order*/
    public void submitDishOrder(){
        insertToDB(queryThinkServer());
        //System.out.println(queryThinkServer());
    }

    /** send tcp socket to server and get the location and load num of the thinkphp server */
    public String queryThinkServer(){
        String thinkPhP_path_id_json="";
        try {
            Socket client=new Socket(serverIp,serverPort);
            InputStream inFromServer=client.getInputStream();
            DataInputStream in=new DataInputStream(inFromServer);
            thinkPhP_path_id_json=in.readUTF();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thinkPhP_path_id_json;
    }

    /** insert to database */
    public void insertToDB(String thinkphp_path_id_json){
        //  request initial
        final RequestQueue requestQueue= Volley.newRequestQueue(activity);
        ThinkphpPathId thinkphp_path_id=JSON.parseObject(thinkphp_path_id_json, ThinkphpPathId.class);
        String thinkphp_path=thinkphp_path_id.getThinkphp_uri();
        thinkphp_id=thinkphp_path_id.getThinkphp_id();
        // volley framework to get the new order item id and store ip
        StringRequest request_more=new StringRequest(Request.Method.POST, thinkphp_path, new Response.Listener<String>() {
            @Override
            public void onResponse(String back_data) {
                // back_data contains 'store_id'(int),'order_id'(int [])
                //System.out.println("mark+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                //System.out.println(back_data);
                requestQueue.cancelAll("add_order_item_db");
                new DishOrder().execute(back_data);
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
                map.put("zid_code","511400");
                map.put("order_items",prepareHasMap());
                return map;
            }
        };
        request_more.setTag("add_order_item_db");
        requestQueue.add(request_more);

    }


    /** DishOrder class contains function socket to the server */
    class DishOrder extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            return dishOrder(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("ok")) {
                AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                builder.setTitle("订单成功提示");
                builder.setMessage("您好！您的订单已提交成功，我们将最快速度为你配送！");
                builder.setPositiveButton("确认", null);
                builder.show();
            }
        }

        /** dish order that send to server with TCP protocol */
        public String dishOrder(String back_data){
            String mark="";
            try {
                Socket client=new Socket(serverIp,serverDishPort);
                // send the order request to server
                OutputStream outToServer=client.getOutputStream();
                DataOutputStream out=new DataOutputStream(outToServer);
                String to_server=back_data+","+order_item_final_json+";"+thinkphp_id;
                //System.out.println(to_server);
                out.writeUTF(to_server);

                // receive message from servers
                InputStream inFromServer=client.getInputStream();
                DataInputStream in=new DataInputStream(inFromServer);
                mark=in.readUTF();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mark;
        }
    }

    /*public String prepareStr(String store_id_ids){
        File mFile=new File(activity.getFilesDir().getPath()+ BabyInfo.USER_INFO_FOLDER+BabyInfo.USER_INFO);
        FileInputStream fis= null;
        String back_str="";
        try {

            fis = new FileInputStream(mFile);
            InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
            char []input=new char[fis.available()];
            isr.read(input); String inString=new String(input);
            Customer customer=JSON.parseObject(inString, Customer.class);
            cus_name=customer.getMail();cus_mail=customer.getMail();cus_phone=customer.getPhone();
            String dish_order_item=JSON.toJSONString(DishStatic.dishSelectedTemps, true);
            back_str= thinkphp_id +","+store_id_ids+"|"+cus_name+"|"+cus_phone+"|"+location+"|"+dish_order_item+"|"+cus_time;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return back_str;
        }*/

    public String prepareHasMap(){

        // read file and get customer information
//        try {
//            File mFile=new File(activity.getFilesDir().getPath()+ BabyInfo.USER_INFO_FOLDER+BabyInfo.USER_INFO);
//            FileInputStream fis= new FileInputStream(mFile);
//            InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
//            char []input=new char[fis.available()];
//            isr.read(input); String inString=new String(input);
//            isr.close();fis.close();
//
//            // parse the string and get customer information in detail
//            Customer customer=JSON.parseObject(inString, Customer.class);
//            cus_id=customer.getCus_id();
//            cus_name=customer.getCus_name();
//            cus_mail=customer.getCus_mail();
//            cus_phone=customer.getCus_phone();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        SharedPreferences preferences=activity.getSharedPreferences("cus_info", Context.MODE_PRIVATE);
        cus_id=preferences.getInt("cus_id",0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("cus_name",cus_name);
        editor.putString("cus_phone",cus_phone);
        editor.putString("cus_time",cus_time);
        editor.putString("location",location);
        editor.apply();

        //String dish_order_json=JSON.toJSONString(DishStatic.dishSelectedTemps); //convert the ordered dish to json
        InsertToDB insertToDB=new InsertToDB(cus_id,cus_name,cus_phone,location,cus_time,DishStatic.dishSelectedTemps); //convert all the item to json
        order_item_final_json=JSON.toJSONString(insertToDB);
        //System.out.println(order_item_final_json);
        return order_item_final_json;
    }


    private String order_item_final_json;
    private int cus_id;


    @Override
    protected Void doInBackground(Void... params) {
        submitDishOrder();
        return null;
    }
}
