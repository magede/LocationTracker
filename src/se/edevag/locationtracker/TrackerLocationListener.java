package se.edevag.locationtracker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class TrackerLocationListener implements LocationListener {

	private TrackerService service;
	
	public TrackerLocationListener(TrackerService model) {
		this.service = model;
	}
	
	public void onLocationChanged(Location location) {
		service.perist(location);
	}

	public void onProviderDisabled(String provider) {
		//service.sendNotification("Tracker: GPS disabled");
	}

	public void onProviderEnabled(String provider) {
		//service.sendNotification("Tracker: GPS enabled");
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		//service.sendNotification("onStatusChanged");
	}

}
