package com.example.administrator.babygrowth01.FirstTime;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.babyparadise.BabyPaFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/7.
 */
public class UserInfoFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View user_first_time=inflater.inflate(R.layout.first_time_user_register, container, false);
        MyResource.setFragment_show("UserInfoFragment");
        return user_first_time;
    }

    @Override
    public void onStart() {
        super.onStart();
        final EditText et_user_name= (EditText) activity.findViewById(R.id.et_user_name);
        final EditText et_mail= (EditText) activity.findViewById(R.id.et_mail);
        final EditText et_phone= (EditText) activity.findViewById(R.id.et_phone);
        final EditText et_account= (EditText) activity.findViewById(R.id.et_account);
        final EditText et_password= (EditText) activity.findViewById(R.id.et_password);
        final EditText et_qq_link= (EditText) activity.findViewById(R.id.et_qq_link);
        Button bt_register= (Button) activity.findViewById(R.id.bt_register);

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save information to a file
                insertDBGetId(et_user_name.getText().toString(),
                        et_mail.getText().toString(),
                        et_phone.getText().toString(),
                        et_account.getText().toString(),
                        et_password.getText().toString(),
                        et_qq_link.getText().toString());

                //enter the main fragment
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .add(R.id.MainFragment, new BabyPaFragment())
                        .commit();
            }
        });
    }

    public void insertDBGetId(final String cus_name, final String cus_mail, final String cus_phone,final String cus_account,final String cus_password,final String cus_qq_link){
        final RequestQueue requestQueue= Volley.newRequestQueue(activity);
        String thinkphp_path="http://120.27.126.41:8080/Android/babygrowth/Application/index.php/BabyPaServer/Customer/insertCusInfoDB";
        StringRequest request_more=new StringRequest(Request.Method.POST, thinkphp_path, new Response.Listener<String>() {
            @Override
            public void onResponse(String cus_id) {
                //we need cus_id so we add SaveUserInfo here
                new SaveUserInfo(activity).execute(cus_id,cus_name, cus_mail, cus_phone,cus_account,cus_password,cus_qq_link);
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
                HashMap<String,String> map=new HashMap<String,String>();
                map.put("cus_name",cus_name);
                map.put("cus_mail",cus_mail);
                map.put("cus_phone",cus_phone);
                map.put("cus_account",cus_account);
                map.put("cus_password",cus_password);
                map.put("cus_qq_link",cus_qq_link);
                return map;
            }
        };
        request_more.setTag("add_cus_info_db");
        requestQueue.add(request_more);
    }

    private Activity activity;
}
