package com.example.administrator.babygrowth01.familydish;

/**
 * Created by Administrator on 2016/2/25.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Messenger;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.Common.MediaPlayer.MediaPlayActivity;


public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    //private OnItemClickListener mListener;
    private RecyclerView rv;
    private Context context;
    private Messenger service_Messenger;

    /*public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }*/

    GestureDetector mGestureDetector;

    public RecyclerView getrv(){
        return rv;
    }

    public RecyclerItemClickListener(final Context context, RecyclerView rv, Messenger service_Messenger) {//OnItemClickListener listener,
        //mListener = listener;
        this.rv=rv;
        this.context=context;
        this.service_Messenger=service_Messenger;


        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                //View view = getrv().findChildViewUnder(e.getX(), e.getY());
                //int position=getrv().getChildLayoutPosition(view);
                //Toast.makeText(context,"  onsingletapup : "+position,Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                View view = getrv().findChildViewUnder(e.getX(), e.getY());
                String uri=view.getTag(R.id.rv_tag_1).toString();
                System.out.println(uri);
                beginPlaySongs(uri);
                //Toast.makeText(context,"  onsingletapconfirmed : "+view.getTag(),Toast.LENGTH_SHORT).show();
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View view = getrv().findChildViewUnder(e.getX(), e.getY());
                int position=getrv().getChildLayoutPosition(view);

                Toast.makeText(context,"  longpress : "+position,Toast.LENGTH_SHORT).show();

                super.onLongPress(e);
            }
        });
    }

    @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        /*View childView = view.findChildViewUnder(e.getX(), e.getY());
        //Toast.makeText(view.getContext(), "  " + e.getX(), Toast.LENGTH_SHORT).show();
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildLayoutPosition(childView));
            Toast.makeText(view.getContext(), "childview  " + view.getChildLayoutPosition(childView), Toast.LENGTH_SHORT).show();
            return true;
        }else {
            Toast.makeText(view.getContext(), "  nothing", Toast.LENGTH_SHORT).show();

        }*/

        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

    @Override
    public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}

    public void beginPlaySongs(String uri){
        Intent intent=new Intent(context, MediaPlayActivity.class);
        intent.putExtra("uri",uri);
        intent.putExtra("service_Messenger",service_Messenger);
        context.startActivity(intent);
    }
}