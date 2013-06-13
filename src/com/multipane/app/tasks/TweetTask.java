package com.multipane.app.tasks;

import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.location.Location;
import android.os.AsyncTask;

public class TweetTask  extends AsyncTask<Void, Void, List<twitter4j.Status>> {

	private OnTweetsReceivedListener listener;
	private GeoLocation location;

	public TweetTask(OnTweetsReceivedListener listener, Location location) {
		this.listener = listener;
		this.location = new GeoLocation(location.getLatitude(), location.getLongitude());
		
	}
	
	@Override
	protected List<twitter4j.Status> doInBackground(Void... params) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("52vQGKsjCMlUGLH2gAvLA")
                .setOAuthConsumerSecret("7NFQ9djvp3Qotqk3harO9e1OEOeCEHDhArNGUWGsT3E")
                .setOAuthAccessToken("16094756-fbEpfNrmKIHtrjGtHbW9mxTmgPop1F7SBptuwRaGb")
                .setOAuthAccessTokenSecret("gI8sjzJBaZQ4BhXlGcYLBGvDNHXb85Jlr4XQ33I6M");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        try {
            QueryResult result = twitter.search(new Query().geoCode(location, 2, Query.KILOMETERS));
            List<twitter4j.Status> tweets = result.getTweets();
            for (twitter4j.Status tweet : tweets) {
                System.out.println("@" + tweet.getUser() + " - " + tweet.getText());
                if (tweet != null && tweet.getGeoLocation() != null)
                	System.out.println(" - Lat: Long" + tweet.getGeoLocation().getLatitude() + ":"  + tweet.getGeoLocation().getLongitude());
                else
                	System.out.println(tweet.toString());
            }

            return tweets;
            
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            return null;
        }
	}
	
	@Override
	protected void onPostExecute(List<twitter4j.Status> result) {
		listener.onTweetsReceived(result);
		
	}
	
	public interface OnTweetsReceivedListener {
	    public void onTweetsReceived(List<twitter4j.Status> tweets);
	}
	
}