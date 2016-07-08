package com.example.administrator.babygrowth01.familydish.DishOrderSumbit;

import com.example.administrator.babygrowth01.familydish.DishStatic;

import java.util.List;

/**
 * Created by Administrator on 2016/4/8.
 */
public class InsertToDB {
    private int cus_id;
    private String cus_name;
    private String cus_phone;
    private String cus_address;
    private String cus_order_time;
    private List<DishStatic.DishSelectedTemp> dish_order_json;

    public InsertToDB(){

    }
    public InsertToDB(int cus_id, String cus_name,  String cus_phone, String cus_address, String cus_order_time, List<DishStatic.DishSelectedTemp> dish_order_json) {
        this.cus_id = cus_id;
        this.cus_name = cus_name;
        this.cus_phone = cus_phone;
        this.cus_address = cus_address;
        this.cus_order_time = cus_order_time;
        this.dish_order_json = dish_order_json;
    }

    public int getCus_id() {
        return cus_id;
    }

    public void setCus_id(int cus_id) {
        this.cus_id = cus_id;
    }

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public String getCus_phone() {
        return cus_phone;
    }

    public void setCus_phone(String cus_phone) {
        this.cus_phone = cus_phone;
    }

    public String getCus_address() {
        return cus_address;
    }

    public void setCus_address(String cus_address) {
        this.cus_address = cus_address;
    }

    public String getCus_order_time() {
        return cus_order_time;
    }

    public void setCus_order_time(String cus_order_time) {
        this.cus_order_time = cus_order_time;
    }

    public List<DishStatic.DishSelectedTemp> getDish_order_json() {
        return dish_order_json;
    }

    public void setDish_order_json(List<DishStatic.DishSelectedTemp> dish_order_json) {
        this.dish_order_json = dish_order_json;
    }
}
