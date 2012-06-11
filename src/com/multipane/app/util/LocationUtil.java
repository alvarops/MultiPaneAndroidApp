package com.multipane.app.util;

import java.util.Random;

import android.location.Location;

import com.mapquest.android.maps.GeoPoint;

public class LocationUtil {

	private static final int METERS_IN_DEGREES = 111200;

	public static Location getRandomLocation(Location location, int radius) {
		Point p = new Point((int)(location.getLongitude() * 1E6), (int)(location.getLatitude()  * 1E6));
		
		p = getRandomLocation(p, radius);
		
		// Set the adjusted location
	    location.setLatitude(p.lat/1E6);
		location.setLongitude(p.lon/1E6);
		
		return location;
	}
	
	public static GeoPoint getRandomLocation(GeoPoint location, int radius) {
		Point p = new Point(location.getLongitudeE6(), location.getLatitudeE6());
		
		p = getRandomLocation(p, radius);
		
		// Set the adjusted location
	    GeoPoint newLocation = new GeoPoint(p.lat, p.lon);
	    
	    return newLocation;
	}
	
	static Point getRandomLocation(Point pos, int radius) {
		Random random = new Random();
		
		int alpha = random.nextInt(360);
		double diam = random.nextInt(radius) * 1E6 / METERS_IN_DEGREES;
		
		int lat = (int)(pos.lat + Math.sin(alpha) * diam);
		int lon = (int)(pos.lon + Math.cos(alpha) * diam);
		
		return new Point(lon, lat);
	}

	static class Point {
		int lon;
		int lat;
		
		Point(int lon, int lat) {
			this.lon = lon;
			this.lat = lat;
		}
	}
}
