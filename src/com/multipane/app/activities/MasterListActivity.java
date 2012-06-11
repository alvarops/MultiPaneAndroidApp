package com.multipane.app.activities;

import java.util.List;

import twitter4j.Tweet;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.mapquest.android.maps.MapFragmentActivity;
import com.multipane.app.R;
import com.multipane.app.fragments.DetailViewFragment;
import com.multipane.app.fragments.MasterListFragment.OnListItemSelectedListener;
import com.multipane.app.fragments.MasterListFragment.OnLocationChangedListener;
import com.multipane.app.tasks.TweetTask.OnTweetsReceivedListener;
import com.multipane.app.util.Extras;

public class MasterListActivity extends MapFragmentActivity implements
		OnListItemSelectedListener, OnLocationChangedListener, OnTweetsReceivedListener {

	Location defaultLocation;
	private List<Tweet> tweets;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the fragment as the content
		// A regular layout, and 600+ dp version exists
		setContentView(R.layout.master_list_fragment);
	}

	

	@Override
	public void onListItemSelected(Tweet tweet) {

		// Load the Fragment in an activity if its not present,
		// otherwise just update the fragment.
		DetailViewFragment detailView = (DetailViewFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_detailview);

		if (detailView == null || !detailView.isInLayout()) {
			Intent showContent = new Intent(getApplicationContext(),
					DetailViewActivity.class);
			showContent.putExtra(Extras.TWEET, tweet);
			showContent.putExtra(Extras.DEFAULT_LOCATION, defaultLocation);
			startActivity(showContent);
		} else {
			detailView.setDefaultLocation(defaultLocation);
			detailView.showPOI(tweet);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		this.defaultLocation = location;
	}

	@Override
	public void onTweetsReceived(List<Tweet> tweets) {
		this.tweets = tweets;
	}
	
}