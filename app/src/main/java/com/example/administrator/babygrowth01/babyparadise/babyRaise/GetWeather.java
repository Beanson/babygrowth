package com.example.administrator.babygrowth01.babyparadise.babyRaise;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.babygrowth01.babyparadise.babyRaise.Volley.VolleyTool;
import com.example.administrator.babygrowth01.MyResource;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016/3/13.
 */
public class GetWeather {

    private Activity activity;
    private double latitude = 0.0;
    private double longitude = 0.0;

    public GetWeather(Activity activity) {
        this.activity = activity;
    }

    public String getCityName() {
        /*LocationManager locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "none";
        }*/

        try {
            /*List<String> str = locationManager.getAllProviders();
            for (String location :
                    str) {
                System.out.println("mark location" + location);
            }*/

            //Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
            Geocoder gcd = new Geocoder(activity.getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            //System.out.println(location.getLongitude()+" "+location.getLatitude());
            addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "out";
    }

    private ImageView iv_weather, iv_show_clothes, iv_show_food;
    private TextView tv_data, tv_weather;
    public void requestWhetherInfo(ImageView iv_weather, ImageView iv_show_clothes, ImageView iv_show_food,TextView tv_data, TextView tv_weather) {
        this.iv_weather=iv_weather;
        this.iv_show_clothes=iv_show_clothes;
        this.iv_show_food=iv_show_food;
        this.tv_data=tv_data;
        this.tv_weather=tv_weather;

        System.out.println("location enter");
        getLatitudeAndLongitude();

    }


    public void getLatitudeAndLongitude() {

        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        /*if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                System.out.println("get location 1"+latitude+"      "+longitude);
                //return;
            }else {
                System.out.println("location none1");
            }
        } else {*/
            LocationListener locationListener = new LocationListener() {

                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                // Provider被enable时触发此函数，比如GPS被打开
                @Override
                public void onProviderEnabled(String provider) {

                }

                // Provider被disable时触发此函数，比如GPS被关闭
                @Override
                public void onProviderDisabled(String provider) {

                }

                //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        Log.e("Map", "Location changed : Lat: "
                                + location.getLatitude() + " Lng: "
                                + location.getLongitude());
                    }
                }
            };
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        try{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                latitude = location.getLatitude(); //经度
                longitude = location.getLongitude(); //纬度
                System.out.println("get location 2"+latitude+"      "+longitude);
                getCityNameAndWeather();
            }else{
                System.out.println("location none2");
            }
        }catch (Exception e){

        }

        //}
    }

    public void getCityNameAndWeather(){
        String city_name = getCityName();
        VolleyTool volleyTool = new VolleyTool(activity, iv_weather, iv_show_clothes, iv_show_food, tv_data, tv_weather);
        volleyTool.actionToWeatherInfo(city_name);
    }

}
