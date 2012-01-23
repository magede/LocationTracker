package se.edevag.locationtracker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TrackerEntry {
	
	double latitude;
	double longitude;
	float accuracy;
	long time;
	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm:ss");
	
	public String getDisplayTime() {
		return sdf.format(new Date(time));
	}
}
