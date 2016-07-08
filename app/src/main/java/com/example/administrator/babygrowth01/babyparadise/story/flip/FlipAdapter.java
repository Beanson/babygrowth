package com.example.administrator.babygrowth01.babyparadise.story.flip;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.babygrowth01.R;
import com.example.administrator.babygrowth01.babyrecords.Main.RecordJson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class FlipAdapter extends BaseAdapter implements OnClickListener {


	public interface Callback{
		public void onPageRequested(int page);
	}

	private Activity activity;
	private LayoutInflater inflater;
	private Callback callback;
	private ArrayList<RecordJson> arrayList;

	public FlipAdapter(Activity activity,ArrayList<RecordJson> arrayList) {
		this.activity=activity;
		inflater = LayoutInflater.from(activity);
		/*for(int i = 0 ; i<10 ; i++){
			items.add(new Item());
		}*/
		this.arrayList=arrayList;
	}


	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return arrayList.get(position).getId();
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.tell_story_page, parent, false);
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageShow);
			holder.progressBar= (ProgressBar) convertView.findViewById(R.id.picasso_bar);
			holder.textView = (TextView) convertView.findViewById(R.id.textShow);
			holder.tv_data= (TextView) convertView.findViewById(R.id.tv_data);

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		//TODO set a text with the id as well
		//holder.text.setText(items.get(position).getId()+":"+position);
		holder.textView.setText(arrayList.get(position).getNote());
		holder.tv_data.setText(arrayList.get(position).getDate_time());

		Picasso.with(activity).load(new File(arrayList.get(position).getPic_uri()))
				.into(holder.imageView, new com.squareup.picasso.Callback() {
					@Override
					public void onSuccess() {
						holder.progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onError() {

					}
				});
		return convertView;
	}

	static class ViewHolder{
		ImageView imageView;
		ProgressBar progressBar;
		TextView textView,tv_data;
	}

	@Override
	public void onClick(View v) {
		/*switch(v.getId()){
		case R.id.first_page:
			if(callback != null){
				callback.onPageRequested(0);
			}
			break;
		case R.id.last_page:
			if(callback != null){
				callback.onPageRequested(getCount()-1);
			}
			break;
		}*/
	}

	/*public void addItems(int amount) {
		for(int i = 0 ; i<amount ; i++){
			items.add(new Item());
		}
		notifyDataSetChanged();
	}

	public void addItemsBefore(int amount) {
		for(int i = 0 ; i<amount ; i++){
			items.add(0, new Item());
		}
		notifyDataSetChanged();
	}*/

}
