package com.example.administrator.babygrowth01.babyparadise.babyRaise.Volley;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.babygrowth01.babyparadise.babyRaise.WeatherJsonParser.Forecast;
import com.example.administrator.babygrowth01.babyparadise.babyRaise.WeatherJsonParser.WeatherJson;
import com.example.administrator.babygrowth01.babyparadise.babyRaise.WeatherJsonParser.WeatherPic;
import com.example.administrator.babygrowth01.babyparadise.parentMedia.ParentMediaJson;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/24.
 */
public class VolleyTool {

    private RequestQueue requestQueue;
    private String TAG="baby";
    private Activity activity;
    private String city_name;


    /** to get the whether */
    private String city_key;

    /** imageView and textview show*/
    private ImageView iv_weather,iv_show_clothes,iv_show_food;
    private TextView tv_data,tv_weather;


    public VolleyTool(Activity activity,ImageView iv_weather,ImageView iv_show_clothes,ImageView iv_show_food,TextView tv_data,TextView tv_weather) {
        requestQueue=com.android.volley.toolbox.Volley.newRequestQueue(activity);

        this.iv_weather = iv_weather;
        this.iv_show_clothes = iv_show_clothes;
        this.iv_show_food = iv_show_food;
        this.tv_data=tv_data;
        this.tv_weather = tv_weather;
    }


    public void getCityWeather(String city_key){
        String whether_url = "http://wthrcdn.etouch.cn/weather_mini?citykey="+city_key;
        final StringRequest request_whether = new StringRequest(Request.Method.GET, whether_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    String whether_info = new String(s.getBytes("ISO-8859-1"),"UTF-8");

                    System.out.println("weather "+whether_info);
                    WeatherJson weatherJson= JSON.parseObject(whether_info, WeatherJson.class);

                    Forecast forecast=weatherJson.getData().getForecast().get(0);

                    Calendar calendar=Calendar.getInstance();
                    int year=calendar.get(Calendar.YEAR);
                    int monthOfYear=calendar.get(Calendar.MONTH);
                    int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);

                    String today_type=forecast.getType();
                    String today_high=forecast.getHigh().replace("高温", "");
                    String today_low=forecast.getLow().replace("低温","");

                    /** prepare to write on the weather textview */
                    String date="今天: "+year+"-"+monthOfYear+"-"+dayOfMonth+"   城市:"+city_name;
                    String weather="天气: "+today_type+" ; 温度: "+today_low+"~"+today_high;

                    tv_data.setText(date); tv_weather.setText(weather);

                    int high=Integer.parseInt(today_high.replace("℃","").trim());
                    int low=Integer.parseInt(today_low.replace("℃", "").trim());
                    int average_temp=(high+low)/2;
                    picToBabyRaise(average_temp);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                requestQueue.cancelAll("request_whether");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request_whether.setTag("request_whether");
        requestQueue.add(request_whether);
    }

    /** add pictures to the baby raise view */
    public void picToBabyRaise(final int average_temp){

        String url_post = "http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/BabyRaise/getPicUri"; //only post could do the get data
        StringRequest request_city_code = new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                WeatherPic weatherPic= JSON.parseObject(s, WeatherPic.class);
                Picasso.with(activity).load(weatherPic.getIv_weather()).into(iv_weather);
//                Picasso.with(activity).load(weatherPic.getIv_show_clothes()).into(iv_show_clothes);
//                Picasso.with(activity).load(weatherPic.getIv_show_food()).into(iv_show_food);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("error post");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("average_temp", String.valueOf(average_temp));
                return map;
            }
        };

        request_city_code.setTag("request_city_code");
        requestQueue.add(request_city_code);

    }

    /** 1.first to gain city code
     *  2.to get the city weather
     *  3.to load the corresponding data to the UI
     *  */
    public void actionToWeatherInfo(final String city_name){

        this.city_name=city_name;
        String url_post = "http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/BabyRaise/getCityCode"; //only post could do the get data
        StringRequest request_city_code = new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println("city code" + s);
                getCityWeather(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("error post");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("city_name", city_name);
                return map;
            }
        };

        request_city_code.setTag("request_city_code");
        requestQueue.add(request_city_code);
    }
























    public void getMediaParentJSONVolley(final ArrayList<ParentMediaJson> arrayList, final int type, final int age_layer, final int id, final String times) {
        String parent_media_uri="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/ParentMedia/addMoreParentMedia";
        StringRequest request_more=new StringRequest(Request.Method.POST, parent_media_uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String parent_media_thumbnail_base_path="http://android-bucket.oss-cn-shenzhen.aliyuncs.com/BabyGrowth/ParentMedia/thumbnail/";
                List<ParentMediaJson> results= JSON.parseArray(s, ParentMediaJson.class);
                for (ParentMediaJson parent_media :
                        results) {
                    parent_media.setThumbnail_uri(parent_media_thumbnail_base_path+parent_media.getThumbnail_uri());
                }
                if(times.equals("first")){
                    arrayList.clear();

                }
                arrayList.addAll(results);
                Log.i(TAG, "add more parent media success");
                requestQueue.cancelAll("seek_more_parent_media_data");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG, "add more parent media success");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("type",String.valueOf(type));
                map.put("age_layer",String.valueOf(age_layer));
                map.put("id",String.valueOf(id));
                return map;
            }
        };
        request_more.setTag("seek_more_parent_media_data");
        requestQueue.add(request_more);
    }
}
