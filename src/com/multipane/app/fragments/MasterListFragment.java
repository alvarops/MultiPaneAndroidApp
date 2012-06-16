package com.multipane.app.fragments;

import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Tweet;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.multipane.app.R;
import com.multipane.app.model.TweetAdapter;
import com.multipane.app.tasks.TweetTask;
import com.multipane.app.tasks.TweetTask.OnTweetsReceivedListener;

public class MasterListFragment extends ListFragment implements
		LocationListener, OnTweetsReceivedListener {

	private OnListItemSelectedListener listItemSelectedListener;
	OnLocationChangedListener locationListener;

	GeoLocation location;
	TweetAdapter adapter;
	private OnTweetsReceivedListener tweetsListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new TweetAdapter(getActivity(), R.layout.tweet_list_item,
				R.id.key);

		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);

		setListAdapter(adapter);

		listItemSelectedListener = (OnListItemSelectedListener) getActivity();
		locationListener = (OnLocationChangedListener) getActivity();
		tweetsListener = (OnTweetsReceivedListener) getActivity();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Tweet tweet = adapter.getItem(position);
		
		listItemSelectedListener.onListItemSelected(tweet);
	}

	@Override
	public void onLocationChanged(Location location) {
		// Called when a new location is found by the network location provider.
		locationListener.onLocationChanged(location);
		new TweetTask(this, location).execute();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onTweetsReceived(List<Tweet> tweets) {
		adapter.clear();
		for (Tweet tweet: tweets) {
			adapter.add(tweet);
		}
		adapter.notifyDataSetChanged();
		tweetsListener.onTweetsReceived(tweets);
	}

	public interface OnListItemSelectedListener {
		public void onListItemSelected(Tweet tweet);
	}

	public interface OnLocationChangedListener {
		public void onLocationChanged(Location location);
	}

}
