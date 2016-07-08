package com.example.administrator.babygrowth01.babyparadise.story;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.administrator.babygrowth01.MyResource;
import com.example.administrator.babygrowth01.babyparadise.story.flip.FlipAdapter;
import com.example.administrator.babygrowth01.babyparadise.story.flip.FlipView;
import com.example.administrator.babygrowth01.babyparadise.story.flip.OverFlipMode;
import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.babyrecords.Main.RecordJson;

import java.util.ArrayList;

public class TellStoryActivity extends Activity implements FlipAdapter.Callback, FlipView.OnFlipListener, FlipView.OnOverFlipListener {
	
	private FlipView mFlipView;
	private FlipAdapter mAdapter;

	/** async handler message and messenger basic variable */
	private Messenger client_Messenger;
	private Messenger service_Messenger;
	private Message client_message;
	private ArrayList<RecordJson> arrayList_record;
	private int arraylist_record_size=0;
	private int current_record_size=0;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tell_story_activity);
		init();
		mFlipView = (FlipView) findViewById(R.id.flip_view);
		/*mAdapter = new FlipAdapter(this);
		mAdapter.setCallback(this);
		mFlipView.setAdapter(mAdapter);
		mFlipView.setOnFlipListener(this);
		mFlipView.peakNext(false);
		mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
		mFlipView.setEmptyView(findViewById(R.id.empty_view));
		mFlipView.setOnOverFlipListener(this);*/


		/*flipView = new FlipViewController(this);
		//Use RGB_565 can reduce peak memory usage on large screen device, but it's up to you to choose the best bitmap format
		flipView.setAnimationBitmapFormat(Bitmap.Config.RGB_565);
		flipView.setAdapter(new TravelAdapter(this));
		setContentView(flipView);*/
	}

	public void init(){
		/** to initialize the client and service messengers */
		client_Messenger=new Messenger(new HandlerActivity());
		service_Messenger= MyResource.getService_Messenger();
		arrayList_record=new ArrayList<>();

		/** play the background music */
		//String background_music_uri="file:///android_asset/BabyPaFragment/background_music/background_music_01.mp3";

		/** send the request to the handler for loading the default BabyPaFragment.songs list */
		MyResource.flip_mark=true;
		client_message=new Message();
		client_message.arg1=3;  //arg1=1 for the download default BabyPaFragment.songs list
		client_message.obj=arrayList_record;  //arraylist for loading the content
		sendToService(client_message);
		client_message=null;  //release the resource
	}

	public void sendToService(Message msg){
		/** binding to the messenger and to the handler */
		msg.replyTo=client_Messenger;
		try {
			service_Messenger.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	public class HandlerActivity extends Handler {
		@Override
		public void handleMessage(Message msgService) {
			super.handleMessage(msgService);
			switch (msgService.arg1) {
				/** 1 means has finished downloading the data and should do the initialization */
				case 3:{
					System.out.println("array size" + arrayList_record.size());
					arraylist_record_size = arrayList_record.size();
					mAdapter = new FlipAdapter(TellStoryActivity.this, arrayList_record);
					mAdapter.setCallback(TellStoryActivity.this);
					mFlipView.setAdapter(mAdapter);
					mFlipView.setOnFlipListener(TellStoryActivity.this);
					mFlipView.peakNext(false);
					mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
					mFlipView.setEmptyView(findViewById(R.id.empty_view));
					mFlipView.setOnOverFlipListener(TellStoryActivity.this);
					mFlipView.smoothFlipTo(0);
					//pauseAsync();
					break;
				}
				case 33:{
					Toast.makeText(TellStoryActivity.this,arraylist_record_size+"  "+current_record_size,Toast.LENGTH_SHORT).show();
					if(++current_record_size<arraylist_record_size){
						mFlipView.smoothFlipBy(1);
						pauseAsync();
					}else{
						Toast.makeText(TellStoryActivity.this,"已经是最后一张了",Toast.LENGTH_SHORT).show();
					}
					break;
				}
				default:{
					break;
				}
			}
		}
	}

	public void pauseAsync(){
		client_message=new Message();
		client_message.arg1=33;  //arg1=1 for the download default BabyPaFragment.songs list
		sendToService(client_message);
		client_message=null;  //release the resource
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.prepend:
			//mAdapter.addItemsBefore(5);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPageRequested(int page) {
		mFlipView.smoothFlipTo(page);
	}

	@Override
	public void onFlippedToPage(FlipView v, int position, long id) {
		Log.i("pageflip", "Page: "+position);
		if(position > mFlipView.getPageCount()-3 && mFlipView.getPageCount()<30){
			//mAdapter.addItems(5);
		}
	}

	@Override
	public void onOverFlip(FlipView v, OverFlipMode mode,
			boolean overFlippingPrevious, float overFlipDistance,
			float flipDistancePerPage) {
		Log.i("overflip", "overFlipDistance = "+overFlipDistance);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	protected void onStop() {
		super.onStop();
		MyResource.flip_mark=false;
		client_message=new Message();
		client_message.arg1=333;  //arg1=1 for the download default BabyPaFragment.songs list
		sendToService(client_message);
		client_message=null;  //release the resource
	}
}
