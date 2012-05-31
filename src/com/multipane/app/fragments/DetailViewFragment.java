package com.multipane.app.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapFragment;
import com.mapquest.android.maps.MapView;
import com.multipane.app.R;

public class DetailViewFragment extends MapFragment {

	private static final String TAG = DetailViewFragment.class.getName();
	
	private MapView map;
	
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

		// set the zoom level, center point and enable the default zoom controls
		map.getController().setZoom(9);
		map.getController().setCenter(new GeoPoint(38.892155, -77.036195));
		map.setBuiltInZoomControls(true);

		return view;
	}

}
