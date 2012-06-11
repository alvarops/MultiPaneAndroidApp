package com.multipane.app.fragments;

import twitter4j.GeoLocation;
import twitter4j.Tweet;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapquest.android.maps.AnnotationView;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.MapFragment;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MyLocationOverlay;
import com.mapquest.android.maps.OverlayItem;
import com.multipane.app.R;
import com.multipane.app.util.LocationUtil;

public class DetailViewFragment extends MapFragment {

	private static final String TAG = DetailViewFragment.class.getName();

	private MapView map;
	private AnnotationView annotation;
	protected MyLocationOverlay myLocationOverlay;
	private GeoPoint defaultLocation;
	
	public DetailViewFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		View view = (View) inflater.inflate(R.layout.detail_view_content,
				container, true);

		// Inflate the layout for this fragment
		map = (MapView) view.findViewById(R.id.map);

		// initialize the annotation to be shown later
		annotation = new AnnotationView(map);

		// set the zoom level, center point and enable the default zoom controls
		map.getController().setZoom(9);
		
		if (defaultLocation != null)
			map.getController().setCenter(defaultLocation);
		else
			map.getController().setCenter(new GeoPoint(38.892155, -77.036195));
		
		map.setBuiltInZoomControls(true);

		setupMyLocation();

		return view;
	}

	public void showPOI(Tweet tweet) {
		// use a custom POI marker by referencing the bitmap file directly,
		// using the filename as the resource ID

		Drawable icon = getResources().getDrawable(R.drawable.location_marker);
		final DefaultItemizedOverlay poiOverlay = new DefaultItemizedOverlay(icon);
		GeoLocation loc = tweet.getGeoLocation();
		GeoPoint tweetPoint;
				
		Log.d(TAG, "This loc " + loc);
		
		if (loc == null) {
		
			tweetPoint = new GeoPoint(defaultLocation.getLatitude(), defaultLocation.getLongitude());
			tweetPoint = LocationUtil.getRandomLocation(tweetPoint, 2000);
		
		} else {
			tweetPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
		}
		Log.d(TAG, "This tweetpoint " + tweetPoint);
		
		// set GeoPoints and title/snippet to be used in the annotation view
		OverlayItem poi1 = new OverlayItem(tweetPoint, tweet.getFromUser(), tweet.getText());
		poiOverlay.addItem(poi1);
		
		// add a tap listener for the POI overlay
		poiOverlay.setTapListener(new ItemizedOverlay.OverlayTapListener() {
			@Override
			public void onTap(GeoPoint pt, MapView mapView) {
				// when tapped, show the annotation for the overlayItem
				int lastTouchedIndex = poiOverlay.getLastFocusedIndex();
				if (lastTouchedIndex > -1) {
					OverlayItem tapped = poiOverlay.getItem(lastTouchedIndex);
					annotation.showAnnotationView(tapped);
				}
			}
		});

		map.getOverlays().add(poiOverlay);
	}

	protected void setupMyLocation() {
		this.myLocationOverlay = new MyLocationOverlay(getActivity(), map);

		myLocationOverlay.enableMyLocation();
		myLocationOverlay.runOnFirstFix(new Runnable() {
			@Override
			public void run() {
				
				defaultLocation = myLocationOverlay.getMyLocation();
				Log.d(TAG, "This location overlay " + defaultLocation);
				
				map.getController().animateTo(defaultLocation);
				map.getController().setZoom(14);
				map.getOverlays().add(myLocationOverlay);
				myLocationOverlay.setFollowing(true);
			}
		});
	}

	public void setDefaultLocation(Location defaultLocation) {
		Log.d(TAG, "This location " + defaultLocation);
		this.defaultLocation = new GeoPoint(defaultLocation.getLatitude(), defaultLocation.getLongitude());
		map.getController().animateTo(this.defaultLocation);
		map.getController().setZoom(14);
	}

}
