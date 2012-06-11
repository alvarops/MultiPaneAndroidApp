package com.multipane.app.tasks;

import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.location.Location;
import android.os.AsyncTask;

public class TweetTask  extends AsyncTask<Void, Void, List<Tweet>> {

	private OnTweetsReceivedListener listener;
	private GeoLocation location;

	public TweetTask(OnTweetsReceivedListener listener, Location location) {
		this.listener = listener;
		this.location = new GeoLocation(location.getLatitude(), location.getLongitude());
		
	}
	
	@Override
	protected List<Tweet> doInBackground(Void... params) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setOAuthConsumerKey("USLG8ce1GgCOTzmxODYyg").setOAuthConsumerSecret("Wk3XdtBDyGRLq5dz20NMasrzQ4BfeRvbPSQ1iKaI");
        
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        try {
            QueryResult result = twitter.search(new Query().geoCode(location, 2, Query.KILOMETERS));
            List<Tweet> tweets = result.getTweets();
           /* for (Tweet tweet : tweets) {
                System.out.println("@" + tweet.getFromUser() + " - " + tweet.getText());
                if (tweet != null && tweet.getGeoLocation() != null)
                	System.out.println(" - Lat: Long" + tweet.getGeoLocation().getLatitude() + ":"  + tweet.getGeoLocation().getLongitude());
                else
                	System.out.println(tweet.toString());
            }
            System.exit(0);
            */
            return tweets;
            
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            return null;
        }
	}
	
	@Override
	protected void onPostExecute(List<Tweet> result) {
		listener.onTweetsReceived(result);
		
	}
	
	public interface OnTweetsReceivedListener {
	    public void onTweetsReceived(List<Tweet> tweets);
	}
	
}