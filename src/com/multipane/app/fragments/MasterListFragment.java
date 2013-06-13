package com.multipane.app.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.location.LocationClient;
import com.multipane.app.R;
import com.multipane.app.model.TweetAdapter;
import com.multipane.app.tasks.TweetTask;

import java.util.List;

import twitter4j.Status;

public class MasterListFragment extends ListFragment implements TweetTask.OnTweetsReceivedListener {


    private static final String TAG = "MasterListFragment";
    TweetAdapter adapter;
	private OnListItemSelectedListener listItemSelectedListener;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new TweetAdapter(getActivity(), R.layout.tweet_list_item,	R.id.key);

        listItemSelectedListener = (OnListItemSelectedListener) getActivity();
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Status tweet = adapter.getItem(position);
		
		listItemSelectedListener.onListItemSelected(tweet);
	}

    @Override
    public void onTweetsReceived(List<Status> tweets) {
        adapter.clear();
        for (Status tweet: tweets) {
            adapter.add(tweet);
        }
        adapter.notifyDataSetChanged();
    }

	public interface OnListItemSelectedListener {
        void onListItemSelected(Status tweet);
    }
}
