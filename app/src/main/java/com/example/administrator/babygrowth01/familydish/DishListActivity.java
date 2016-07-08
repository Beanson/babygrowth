package com.example.administrator.babygrowth01.familydish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.R;

/**
 * Created by Administrator on 2016/2/21.
 */
public class DishListActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_dish_holder);

        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.ly_family_dish_holder, new DishFragment())
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            switch (MyResource.getFragment_show_3()){

                case "DishFragment":{
                    return super.onKeyDown(keyCode, event);
                }
                case "DishDetailFragment":{
                    getFragmentManager().popBackStack();
                    MyResource.setFragment_show_3("");
                    break;
                }
                case "DishPaymentFragment":{
                    DishStatic.dishSelectedTemps.clear();
                    DishStatic.selected_dish.clear();
                    DishStatic.selected_family_dish.clear();
                    DishStatic.dish_num=0;
                    DishStatic.dish_id=1;
                    DishStatic.dish_type=1;
                    DishStatic.dish_first_time=false;
                    DishStatic.dish_type_str="fish/";
                    getFragmentManager().popBackStack();
                    MyResource.setFragment_show_3("");
                    break;
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        DishStatic.dishSelectedTemps.clear();
        DishStatic.selected_dish.clear();
        DishStatic.selected_family_dish.clear();
        DishStatic.dish_num=0;
        DishStatic.dish_id=1;
        DishStatic.dish_type=1;
        DishStatic.dish_type_str="fish/";
        super.onDestroy();
    }
}
