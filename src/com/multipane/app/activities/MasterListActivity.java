package com.multipane.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.mapquest.android.maps.MapFragmentActivity;
import com.multipane.app.R;
import com.multipane.app.fragments.DetailViewFragment;
import com.multipane.app.fragments.MasterListFragment;


public class MasterListActivity extends MapFragmentActivity implements MasterListFragment.OnListItemSelectedListener {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the fragment as the content
        // A regular layout, and 600+ dp version exists
        setContentView(R.layout.master_list_fragment);        
    }
	

	@Override
	public void onListItemSelected(String comicID) {
		
		// Load the Fragment in an activity if its not present,
		// otherwise just update the fragment.
		DetailViewFragment detailView = (DetailViewFragment) getSupportFragmentManager()
			.findFragmentById(R.id.fragment_detailview);
		 
		if (detailView == null || !detailView.isInLayout()) {
			Intent showContent = new Intent(getApplicationContext(),
					DetailViewActivity.class);
			showContent.setData(Uri.parse(comicID));
			startActivity(showContent);
		} else {
			//detailView.updateComic(comicID);
		}
	}


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
}