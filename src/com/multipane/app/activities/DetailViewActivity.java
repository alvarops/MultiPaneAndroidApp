package com.multipane.app.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.mapquest.android.maps.MapFragmentActivity;
import com.multipane.app.R;
import com.multipane.app.fragments.DetailViewFragment;
import com.multipane.app.util.Extras;

import twitter4j.Status;

public class DetailViewActivity extends MapFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_view_fragment);

		Intent launchingIntent = getIntent();
		Status tweet = (Status) launchingIntent.getSerializableExtra(Extras.TWEET);
		Location defaultLocation = launchingIntent.getParcelableExtra(Extras.DEFAULT_LOCATION);

		DetailViewFragment detailView = (DetailViewFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_detailview);
		
		detailView.setDefaultLocation(defaultLocation);
		detailView.showPOI(tweet);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
