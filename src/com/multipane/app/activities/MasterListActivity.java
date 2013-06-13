package com.multipane.app.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.mapquest.android.maps.MapFragmentActivity;
import com.multipane.app.R;
import com.multipane.app.fragments.DetailViewFragment;
import com.multipane.app.fragments.MasterListFragment;
import com.multipane.app.fragments.MasterListFragment.OnListItemSelectedListener;
import com.multipane.app.tasks.TweetTask;
import com.multipane.app.util.Extras;

import java.util.List;

import twitter4j.Status;

public class MasterListActivity extends MapFragmentActivity implements
		OnListItemSelectedListener,LocationListener, TweetTask.OnTweetsReceivedListener,
        GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MasterListActivity";
    Location mDefaultLocation;
    private LocationClient mLocationClient;
    private List<Status> mTweets;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        mLocationClient = new LocationClient(this, this, this);

        // Load the fragment as the content
		// A regular layout, and 600+ dp version exists
		setContentView(R.layout.master_list_fragment);
	}

    @Override
    public void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }

    @Override
	public void onListItemSelected(Status tweet) {

		// Load the Fragment in an activity if its not present,
		// otherwise just update the fragment.
		DetailViewFragment detailView = (DetailViewFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_detailview);

		if (detailView == null || !detailView.isInLayout()) {
			Intent showContent = new Intent(getApplicationContext(),
					DetailViewActivity.class);
			showContent.putExtra(Extras.TWEET, tweet);
			showContent.putExtra(Extras.DEFAULT_LOCATION, mDefaultLocation);
			startActivity(showContent);
		} else {
			detailView.setDefaultLocation(mDefaultLocation);
			detailView.showPOI(tweet);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

    public void showErrorDialog(final int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                this,
                CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {
            // Create a new DialogFragment for the error dialog
            ErrorDialogFragment errorFragment =
                    new ErrorDialogFragment();
            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);
            // Show the error dialog in the DialogFragment
            errorFragment.show(getSupportFragmentManager(), "Location Updates");
        }
    }

    @Override
    public void onTweetsReceived(final List<Status> tweets) {
        mTweets = tweets;
        MasterListFragment fragment = (MasterListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_masterlist);
        if (fragment != null) fragment.onTweetsReceived(tweets);
    }

    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mDefaultLocation = location;
        mLocationClient.disconnect();
        // Called when a new location is found by the network location provider.
        new TweetTask(this, location).execute();
    }

    @Override
    public void onConnected(final Bundle bundle) {
        Log.d(TAG, "onConnected");
        mDefaultLocation = mLocationClient.getLastLocation();

        if (mDefaultLocation != null)
            new TweetTask(this, mDefaultLocation).execute();

        mLocationClient.requestLocationUpdates(new LocationRequest(), this);
    }

    @Override
    public void onDisconnected() {
        Log.d(TAG, "onDisconnected");
    }

    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed" + connectionResult);
        /*
         * If no resolution is available, display a dialog to the
         * user with the error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
        }

    }



}