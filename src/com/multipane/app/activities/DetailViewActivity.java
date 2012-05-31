package com.multipane.app.activities;

import android.content.Intent;
import android.os.Bundle;

import com.mapquest.android.maps.MapFragmentActivity;
import com.multipane.app.R;
import com.multipane.app.fragments.DetailViewFragment;


public class DetailViewActivity extends MapFragmentActivity {

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view_fragment);
        
        Intent launchingIntent = getIntent();
        String content = launchingIntent.getData().toString();
     
        DetailViewFragment detailView = (DetailViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_detailview);
     
        //detailView.updateComic(content);
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
