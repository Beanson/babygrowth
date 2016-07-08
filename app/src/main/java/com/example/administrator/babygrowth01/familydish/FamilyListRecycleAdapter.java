package com.example.administrator.babygrowth01.familydish;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.babygrowth01.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/19.
 */


public class FamilyListRecycleAdapter extends RecyclerView.Adapter {

    private ArrayList<DishFamilyJson> arrayList;
    private Activity activity;

    public FamilyListRecycleAdapter(ArrayList<DishFamilyJson> arrayList, Activity activity) {
        this.arrayList=arrayList;
        this.activity=activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.family_dish_each_2,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder vh = (MyViewHolder) holder;
        /** binding the id as tag with the view */
        vh.getView().setTag(R.id.dish_id,arrayList.get(position).getId());
        vh.getView().setTag(R.id.dish_name,arrayList.get(position).getDish_name());
        vh.getView().setTag(R.id.dish_thumbnail_uri,arrayList.get(position).getDish_thumbnail_uri());
        vh.getView().setTag(R.id.dish_adapt,arrayList.get(position).getDish_adapt());
        vh.getView().setTag(R.id.sub_dish,arrayList.get(position).getSub_dish());
        vh.getView().setTag(R.id.dish_video_uri, arrayList.get(position).getDish_video_uri());
        vh.getView().setTag(R.id.dish_chosen_mark, false);
        Picasso.with(activity).load(arrayList.get(position).getDish_thumbnail_uri()).into(vh.getIv_dish_each());
        vh.getTv_dish_each().setText(arrayList.get(position).getDish_depict());

        if(DishStatic.selected_family_dish.containsValue(arrayList.get(position).getId())){
            vh.getMark_choose().setVisibility(View.VISIBLE);
            vh.getBt_dish_each_order().setText("取消");
            vh.getView().setTag(R.id.dish_chosen_mark, true);
        }

    }


    @Override
    public int getItemCount() {
        System.out.println("get item count");
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View view,mark_choose;
        private Button bt_dish_each_div,bt_dish_each_order;
        private ImageView iv_dish_each;
        private TextView tv_dish_each;

        public MyViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            bt_dish_each_div= (Button) itemView.findViewById(R.id.bt_dish_each_div);
            bt_dish_each_order= (Button) itemView.findViewById(R.id.bt_dish_each_order);
            mark_choose=itemView.findViewById(R.id.mark_choose);
            iv_dish_each= (ImageView) itemView.findViewById(R.id.iv_dish_each);
            tv_dish_each= (TextView) itemView.findViewById(R.id.tv_dish_each);

            bt_dish_each_order.setOnClickListener(this);
            bt_dish_each_div.setOnClickListener(this);
        }

        public View getView() {
            return view;
        }

        public ImageView getIv_dish_each() {
            return iv_dish_each;
        }

        public TextView getTv_dish_each() {
            return tv_dish_each;
        }

        public View getMark_choose() {
            return mark_choose;
        }

        public Button getBt_dish_each_order() {
            return bt_dish_each_order;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                /** click to add to the mail car */
                case R.id.bt_dish_each_order:{
                    if(!(boolean)view.getTag(R.id.dish_chosen_mark)){
                        mark_choose.setVisibility(View.VISIBLE);
                        view.setTag(R.id.dish_chosen_mark, true);
                        bt_dish_each_order.setText("取消");
                        //DishStatic.selected_family_dish.add(Integer.parseInt(view.getTag(R.id.dish_id).toString()));
                        DishStatic.selected_family_dish.put(view.getTag(R.id.dish_id).toString(), Integer.parseInt(view.getTag(R.id.dish_id).toString()));
                        activity.findViewById(R.id.dish_settlement).setVisibility(View.VISIBLE);
                        TextView textView=(TextView)activity.findViewById(R.id.dish_chosen);
                        textView.setText("已选择: " + (++DishStatic.dish_num) + "个菜");
                    }else {
                        DishStatic.selected_family_dish.remove(view.getTag(R.id.dish_id).toString());
                        bt_dish_each_order.setText("订购");
                        mark_choose.setVisibility(View.GONE);
                        view.setTag(R.id.dish_chosen_mark, false);
                        --DishStatic.dish_num;
                        if(DishStatic.dish_num==0){
                            activity.findViewById(R.id.dish_settlement).setVisibility(View.GONE);
                        }else {
                            TextView textView=(TextView)activity.findViewById(R.id.dish_chosen);
                            textView.setText("已选择: "+(DishStatic.dish_num)+"个菜");
                        }
                    }
                    break;
                    //System.out.println("mark"+DishStatic.dish_num);
                }
                /** enter the view that show how to make the food  */
                case R.id.bt_dish_each_div:{
                    /*DishStatic.dish_type_str=Integer.parseInt(view.getTag(R.id.dish_id).toString());
                    activity.getFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.ly_family_dish_holder, new DishDetailFragment())
                            .commit();*/
                }
            }
        }
    }
}
