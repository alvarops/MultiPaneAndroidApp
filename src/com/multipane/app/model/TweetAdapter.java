package com.multipane.app.model;

import twitter4j.Tweet;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.multipane.app.R;

public class TweetAdapter extends ArrayAdapter<Tweet> {
	private static final String TAG = TweetAdapter.class.getSimpleName();
	
	private LayoutInflater inflator;
	private int layout;

	public TweetAdapter(Context context, int resource, int textViewResourceId) {
		super(context, textViewResourceId);
		
		this.inflator = (LayoutInflater)context.getSystemService
	      (Context.LAYOUT_INFLATER_SERVICE);
		this.layout = resource;
		this.setNotifyOnChange(false);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Tweet tweet = getItem(position);
		ViewHolder holder;
		
		try {
			if (convertView == null) {
				convertView = this.inflator.inflate(layout, null);
				holder = new ViewHolder();
				holder.key = (TextView) convertView.findViewById(R.id.key);
				holder.value = (TextView) convertView.findViewById(R.id.value);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String key = tweet.getFromUser();
			String value = tweet.getText();

			holder.key.setText(key);
			holder.value.setText(value);
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
		return convertView;
	}
	
	static class ViewHolder {
		TextView key;
		TextView value;
	}

}
